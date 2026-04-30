package com.kyh.system.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kyh.system.auth.UserRole;
import com.kyh.system.dto.EmployeeForm;
import com.kyh.system.dto.EmployeeSearchDto;
import com.kyh.system.model.SyainMain;
import com.kyh.system.model.TgSetting;
import com.kyh.system.model.UserAuth;
import com.kyh.system.service.EmployeeService;

// 社員情報の検索・登録・更新・削除を担当するコントローラー
@Controller
@RequestMapping("/employee")
public class EmployeeController {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    // OS 技術スタックを 1 行に表示する件数
    private static final int OS_CHUNK_SIZE = 5;

    // 登録フォームの性別初期値（1: 男性）
    private static final int DEFAULT_SEIBETU = 1;

    @Autowired
    private EmployeeService employeeService;

    // 社員一覧画面を表示する。初回アクセス時はデフォルト検索条件（在職中・ITエンジニア）を設定する。
    @GetMapping("/list")
    public String list(@ModelAttribute("searchDto") EmployeeSearchDto searchDto, HttpSession session, Model model) {
        try {
            List<TgSetting> jobTypeList = employeeService.getJobTypeList();

            UserRole role = getRole(session);
            model.addAttribute("companyList", employeeService.getCompanyList());
            model.addAttribute("jobTypeList", jobTypeList);
            model.addAttribute("canManage", role.canManageEmployee());

            employeeService.initializeDefaultSearchCondition(searchDto, jobTypeList);

            if (!employeeService.isValidWorkingFilter(searchDto.getWorking(), searchDto.getNotWorking())) {
                model.addAttribute("errorMessage", "在籍と非在籍がいずれにしても、１つのチェックが必須です。");
                model.addAttribute("employeeList", java.util.Collections.emptyList());
                return "employee/employeeList";
            }

            model.addAttribute("employeeList", employeeService.getEmployeeSearchList(searchDto));
            return "employee/employeeList";
        } catch (Exception e) {
            logger.error("社員一覧の取得中にエラーが発生しました。", e);
            return "employee/systemError";
        }
    }

    // 社員登録フォーム画面を表示する。全ロールがアクセス可。エリア表示はロール権限で制御する。
    @GetMapping("/register")
    public String showRegisterForm(HttpSession session, Model model) {
        try {
            UserRole role = getRole(session);

            EmployeeForm form = new EmployeeForm();
            form.setSeibetu(DEFAULT_SEIBETU);
            model.addAttribute("employee", form);
            addRegisterPermissions(model, role);
            loadMasterData(model);
            addItOsChunks(model);
            return "employee/employeeRegister";
        } catch (Exception e) {
            logger.error("社員登録フォームの表示中にエラーが発生しました。", e);
            return "employee/systemError";
        }
    }

    // 社員情報を新規登録する。基本情報編集権限（S・A・D）がない場合は一覧画面へリダイレクトする。
    @PostMapping("/register")
    public String register(
            @Validated
            @ModelAttribute("employee") EmployeeForm form,
            BindingResult result,
            @RequestParam(value = "itOsValues", required = false) List<String> itOsValues,
            HttpSession session,
            Model model,
            RedirectAttributes ra) {

        UserRole role = getRole(session);
        if (!role.canEditBasicInfo()) {
            ra.addFlashAttribute("errorMessage", "登録権限がありません。");
            return "redirect:/employee/list";
        }

        form.setItOs(joinValues(itOsValues));

        if (result.hasErrors()) {
            return prepareRegisterErrorView(model, result, role);
        }

        try {
            employeeService.insertEmployee(toSyainMain(form));
        } catch (Exception e) {
            logger.error("社員登録中にエラーが発生しました。employeecode={}", form.getEmployeecode(), e);
            return "employee/systemError";
        }
        ra.addFlashAttribute("successMessage", "社員情報を登録しました。");
        return "redirect:/employee/complete";
    }

    // 社員編集フォーム画面を表示する。指定された社員IDが存在しない場合は一覧画面へリダイレクトする。
    @GetMapping("/edit/{syainId}")
    public String showEditForm(@PathVariable("syainId") Integer syainId, HttpSession session, Model model, RedirectAttributes ra) {
        try {
            UserRole role = getRole(session);

            SyainMain employee = employeeService.getEmployeeBySyainId(syainId);
            if (employee == null) {
                ra.addFlashAttribute("errorMessage", "指定された社員情報が見つかりません。");
                return "redirect:/employee/list";
            }

            model.addAttribute("employee", toEmployeeForm(employee));
            model.addAttribute("canEditBasicInfo", role.canEditBasicInfo());
            loadMasterData(model);
            return "employee/employeeEdit";
        } catch (Exception e) {
            logger.error("社員編集フォームの表示中にエラーが発生しました。syainId={}", syainId, e);
            return "employee/systemError";
        }
    }

    // 社員情報を更新する。基本情報編集権限（S・A・D）がない場合は一覧画面へリダイレクトする。
    @PostMapping("/edit")
    public String edit(
            @Validated
            @ModelAttribute("employee") EmployeeForm form,
            BindingResult result,
            HttpSession session,
            Model model,
            RedirectAttributes ra) {

        UserRole role = getRole(session);
        if (!role.canEditBasicInfo()) {
            ra.addFlashAttribute("errorMessage", "更新権限がありません。");
            return "redirect:/employee/list";
        }

        if (result.hasErrors()) {
            return prepareEditErrorView(model, result);
        }

        try {
            boolean updated = employeeService.updateEmployee(toSyainMain(form));
            if (!updated) {
                ra.addFlashAttribute("errorMessage", "社員変更項目がありません。");
                return "redirect:/employee/edit/" + form.getSyainId();
            }
        } catch (Exception e) {
            logger.error("社員更新中にエラーが発生しました。employeecode={}", form.getEmployeecode(), e);
            return "employee/systemError";
        }
        ra.addFlashAttribute("successMessage", "社員情報を更新しました。");
        return "redirect:/employee/complete";
    }


    // 登録処理でバリデーションエラーが発生した場合の画面再表示データを準備する
    private String prepareRegisterErrorView(Model model, BindingResult result, UserRole role) {
        addRegisterPermissions(model, role);
        loadMasterData(model);
        addItOsChunks(model);
        addValidationErrors(model, result);
        return "employee/employeeRegister";
    }

    // 更新処理でバリデーションエラーが発生した場合の画面再表示データを準備する
    private String prepareEditErrorView(Model model, BindingResult result) {
        model.addAttribute("canEditBasicInfo", true);
        loadMasterData(model);
        addValidationErrors(model, result);
        return "employee/employeeEdit";
    }

    // 作業完了画面を表示する
    @GetMapping("/complete")
    public String complete() {
        return "employee/employeeComplete";
    }

    // 社員を論理削除する。管理権限がない場合は一覧画面へリダイレクトする。
    // 削除後は削除前の検索条件を引き継いで再検索する。
    @PostMapping("/delete/{syainId}")
    public String delete(
            @PathVariable("syainId") Integer syainId,
            @RequestParam(required = false) Integer syozokuKaisya,
            @RequestParam(required = false) String employeeName,
            @RequestParam(required = false) Integer syokugyoKind,
            @RequestParam(defaultValue = "false") Boolean working,
            @RequestParam(defaultValue = "false") Boolean notWorking,
            HttpSession session, RedirectAttributes ra) {

        if (!getRole(session).canManageEmployee()) {
            ra.addFlashAttribute("errorMessage", "削除権限がありません。");
            return "redirect:/employee/list";
        }

        try {
            employeeService.logicalDeleteEmployee(syainId);
        } catch (Exception e) {
            logger.error("社員削除中にエラーが発生しました。syainId={}", syainId, e);
            return "employee/systemError";
        }

        ra.addAttribute("searched", true);
        if (syozokuKaisya != null) ra.addAttribute("syozokuKaisya", syozokuKaisya);
        if (employeeName != null && !employeeName.trim().isEmpty()) ra.addAttribute("employeeName", employeeName);
        if (syokugyoKind != null) ra.addAttribute("syokugyoKind", syokugyoKind);
        ra.addAttribute("working", working);
        ra.addAttribute("notWorking", notWorking);
        return "redirect:/employee/list";
    }

    // セッションからログインユーザー情報を取得する
    private UserAuth getLoginUser(HttpSession session) {
        return (UserAuth) session.getAttribute("loginUser");
    }

    // セッションからログインユーザーのロールを取得する。未定義ロールは D（最小権限）として扱う
    private UserRole getRole(HttpSession session) {
        UserRole role = UserRole.from(getLoginUser(session).getUserRole());
        return role != null ? role : UserRole.D;
    }

    // 登録画面エリア別権限フラグをモデルに追加する（設計書 2.9 参照）
    private void addRegisterPermissions(Model model, UserRole role) {
        model.addAttribute("canEditBasicInfo",  role.canEditBasicInfo());
        model.addAttribute("canSeeSalary",      role.canSeeSalary());
        model.addAttribute("canEditTechSkills", role.canEditTechSkills());
        model.addAttribute("canSeeTechSkills",  role.canSeeTechSkills());
    }

    // マスタデータ（所属会社・職業種類）をモデルに追加する
    private void loadMasterData(Model model) {
        model.addAttribute("companyList", employeeService.getCompanyList());
        model.addAttribute("jobTypeList", employeeService.getJobTypeList());
    }

    // IT OS リストを OS_CHUNK_SIZE 件ずつのチャンクに分割してモデルに追加する
    private void addItOsChunks(Model model) {
        List<List<TgSetting>> chunks = new ArrayList<List<TgSetting>>();
        List<TgSetting> all = employeeService.getItOsList();
        for (int i = 0; i < all.size(); i += OS_CHUNK_SIZE) {
            chunks.add(all.subList(i, Math.min(i + OS_CHUNK_SIZE, all.size())));
        }
        model.addAttribute("itOsChunks", chunks);
    }

    // バリデーションエラーメッセージをモデルに追加する
    private void addValidationErrors(Model model, BindingResult result) {
        List<String> validationErrors = new ArrayList<String>();
        result.getAllErrors().forEach(error -> validationErrors.add(error.getDefaultMessage()));
        model.addAttribute("validationErrors", validationErrors);
    }

    // リストの各要素をカンマ区切りで結合する。リストが null または空の場合は null を返す。
    private String joinValues(List<String> values) {
        return (values == null || values.isEmpty()) ? null : String.join(",", values);
    }

    // EmployeeForm を SyainMain モデルに変換する。
    // フィールドを追加した場合は toEmployeeForm・hasChanges・selectEmployeeForEdit も同時に更新すること。
    private SyainMain toSyainMain(EmployeeForm form) {
        SyainMain emp = new SyainMain();
        emp.setSyainId(form.getSyainId());
        emp.setEmployeecode(form.getEmployeecode());
        emp.setLastNameKanji(form.getLastNameKanji());
        emp.setFirstNameKanji(form.getFirstNameKanji());
        emp.setLastNameKana(form.getLastNameKana());
        emp.setFirstNameKana(form.getFirstNameKana());
        emp.setFirstNameEigo(form.getFirstNameEigo());
        emp.setLastNameEigo(form.getLastNameEigo());
        emp.setSeibetu(form.getSeibetu());
        emp.setSyozokuKaisya(form.getSyozokuKaisya());
        emp.setNyuusyaDate(form.getNyuusyaDate());
        emp.setTaisyaDate(form.getTaisyaDate());
        emp.setSyokugyoKind(form.getSyokugyoKind());
        emp.setItOs(form.getItOs());
        emp.setKinyukikanCode(form.getKinyukikanCode());
        emp.setKinyukikanName(form.getKinyukikanName());
        emp.setSitenCode(form.getSitenCode());
        emp.setSitenName(form.getSitenName());
        emp.setKouzaKind(form.getKouzaKind());
        emp.setKouzaNum(form.getKouzaNum());
        emp.setMeigiName(form.getMeigiName());
        return emp;
    }

    // DB から取得した SyainMain を編集フォーム表示用の EmployeeForm に変換する。
    // フィールドを追加した場合は toSyainMain・hasChanges・selectEmployeeForEdit も同時に更新すること。
    private EmployeeForm toEmployeeForm(SyainMain emp) {
        EmployeeForm form = new EmployeeForm();
        form.setSyainId(emp.getSyainId());
        form.setEmployeecode(emp.getEmployeecode());
        form.setLastNameKanji(emp.getLastNameKanji());
        form.setFirstNameKanji(emp.getFirstNameKanji());
        form.setLastNameKana(emp.getLastNameKana());
        form.setFirstNameKana(emp.getFirstNameKana());
        form.setFirstNameEigo(emp.getFirstNameEigo());
        form.setLastNameEigo(emp.getLastNameEigo());
        form.setSeibetu(emp.getSeibetu());
        form.setSyozokuKaisya(emp.getSyozokuKaisya());
        form.setNyuusyaDate(emp.getNyuusyaDate());
        form.setTaisyaDate(emp.getTaisyaDate());
        form.setSyokugyoKind(emp.getSyokugyoKind());
        return form;
    }
}
