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

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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

    // 社員一覧の1ページあたりの表示件数
    private static final int PAGE_SIZE = 10;

    @Autowired
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // 社員一覧画面を表示する。
    // 【処理フロー】
    //   ① tg_setting からドロップダウン用マスタ取得（所属会社・職業種類）
    //   ② セッションから UserRole を取得し canManage フラグを画面に渡す（S権限のみ削除可）
    //   ③ searched フラグがない初回表示のみ、在職中・ITエンジニア（code=4）を既定条件にセット
    //      searched=true は検索フォームの hidden から送信され、ユーザー操作による検索と区別する
    //   ④ 在職／非在職チェックの有効性検証
    //   ⑤ PageHelper でページング設定 → 検索条件で syain_main を SELECT → PageInfo として画面に渡す
    @GetMapping("/list")
    public String list(@ModelAttribute("searchDto") EmployeeSearchDto searchDto,
                       @RequestParam(defaultValue = "1") int pageNum,
                       HttpSession session, Model model) {
        try {
            List<TgSetting> jobTypeList = employeeService.getJobTypeList();

            UserRole role = getRole(session);
            model.addAttribute("companyList", employeeService.getCompanyList());
            model.addAttribute("jobTypeList", jobTypeList);
            model.addAttribute("canManage", role.canManageEmployee());

            employeeService.initializeDefaultSearchCondition(searchDto);

            if (!employeeService.isValidWorkingFilter(searchDto.getWorking(), searchDto.getNotWorking())) {
                model.addAttribute("errorMessage", "在籍と非在籍がいずれにしても、１つのチェックが必須です。");
                model.addAttribute("pageInfo", PageInfo.emptyPageInfo());
                return "employee/employeeList";
            }

            PageHelper.startPage(pageNum, PAGE_SIZE);
            PageInfo<com.kyh.system.dto.EmployeeListDto> pageInfo =
                new PageInfo<>(employeeService.getEmployeeSearchList(searchDto));
            model.addAttribute("pageInfo", pageInfo);
            return "employee/employeeList";
        } catch (Exception e) {
            logger.error("社員一覧の取得中にエラーが発生しました。", e);
            return "employee/systemError";
        }
    }

    // 社員登録フォーム画面を表示する。全ロールがアクセス可。エリア表示はロール権限で制御する。
    // 権限フラグ4種（canEditBasicInfo / canSeeSalary / canEditTechSkills / canSeeTechSkills）を
    // モデルにセットし、Thymeleaf の th:disabled・th:if で表示領域を制御する。
    // canEditTechSkills は th:inline で JS 変数として渡し、ITスキルのクリック編集可否を切り替える。
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

    // 社員情報を新規登録する。
    // 【処理フロー】
    //   ① canEditBasicInfo 権限確認（なければ一覧へリダイレクト）
    //   ② itOsValues（JS クリックで組み立てた "マスタID-レベル" 配列）をカンマ区切り文字列に結合
    //      例: ["2-1","4-3"] → "2-1,4-3"。@Validated より前に実行し検証対象に含める。
    //   ③ @Validated で EmployeeForm のアノテーション検証 → エラー時は登録画面に戻す
    //   ④ toSyainMain() で DTO → モデル変換 → insertEmployee() で DB 登録
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
    // toEmployeeForm() で SyainMain → EmployeeForm に変換する際、
    // itOs・給与情報は更新画面に表示しないため意図的に除外する（登録専用項目）。
    // 更新画面は登録画面と異なり canEditBasicInfo の1フラグのみ使用する。
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

    // 社員情報を更新する。
    // 【処理フロー】
    //   ① canEditBasicInfo 権限確認 / syainId の存在確認
    //   ② @Validated でフォーム検証 → エラー時は更新画面に戻す
    //   ③ updateEmployee() で更新前データを DB から再取得 → buildChanges() で差分検出
    //      変更なし → "変更項目なし" エラーで更新画面へ戻す
    //      変更あり → 差分 Map を MyBatis に渡して変更された列のみ UPDATE
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

        if (form.getSyainId() == null) {
            ra.addFlashAttribute("errorMessage", "社員IDが不正です。再度お試しください。");
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

    // セッションからログインユーザーのロールを取得する。
    // 未定義ロールは D（最小権限）として扱う。
    // 未ログインアクセスは LoginInterceptor で /login へリダイレクトされる前提。
    private UserRole getRole(HttpSession session) {
        UserAuth loginUser = (UserAuth) session.getAttribute("loginUser");
        UserRole role = UserRole.from(loginUser.getUserRole());
        return role != null ? role : UserRole.D;
    }

    // 登録画面エリア別権限フラグをモデルに追加する（設計書 2.9 参照）
    private void addRegisterPermissions(Model model, UserRole role) {
        model.addAttribute("canEditBasicInfo",  role.canEditBasicInfo());
        model.addAttribute("canSeeSalary",      role.canSeeSalary());
        model.addAttribute("canEditTechSkills", role.canEditTechSkills());
        model.addAttribute("canSeeTechSkills",  role.canSeeTechSkills());
    }

    // 登録・更新 両画面で共通して使うマスタ（所属会社・職業種類）をモデルに追加する。
    // 登録画面のみで使うマスタ（技術経験など）はこのメソッドに追加しないこと。
    private void loadMasterData(Model model) {
        model.addAttribute("companyList", employeeService.getCompanyList());
        model.addAttribute("jobTypeList", employeeService.getJobTypeList());
    }

    // 登録画面専用。IT OS リストを OS_CHUNK_SIZE 件ずつのチャンクに分割してモデルに追加する。
    // 技術経験の別カテゴリ（言語・DBなど）を登録画面に追加する場合は、
    // 同じパターンで addIt〇〇Chunks メソッドを追加し、showRegisterForm / prepareRegisterErrorView の両方から呼ぶこと。
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

    // EmployeeForm を SyainMain モデルに変換する（登録・更新 共通）。
    // 【フィールド追加時のルール】
    // ・登録・更新 両画面で使うフィールド → toSyainMain / toEmployeeForm / buildChanges / XML の insertEmployee と updateEmployee を更新
    // ・登録画面のみのフィールド（itOs・給与情報など） → toSyainMain / XML の insertEmployee のみ更新
    //   ※ toEmployeeForm・buildChanges・updateEmployee には追加しない（更新画面に表示しないため）
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

    // DB から取得した SyainMain を更新フォーム表示用の EmployeeForm に変換する。
    // 更新画面に表示する項目のみ設定する。登録専用フィールド（itOs・給与情報など）は意図的に除外している。
    // フィールド追加時のルールは toSyainMain のコメントを参照。
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
