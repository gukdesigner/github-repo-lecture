package com.kyh.system.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Collections;

import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kyh.system.dto.EmployeeForm;
import com.kyh.system.model.UserAuth;
import com.kyh.system.service.EmployeeService;

// 【AAA パターンとは】
// 単体テストを3つのフェーズに分けて記述する手法
//   Arrange（準備） : テストに必要なオブジェクトやモックをセットアップする
//   Act    （実行） : テスト対象のメソッドを1度だけ呼び出す
//   Assert （検証） : 実行結果が期待値と一致するか確認する
// フェーズを明確に分けることで「何をテストしているか」が一目でわかる
public class EmployeeControllerEditTest {

    private EmployeeController controller;
    private EmployeeService employeeService;

    private HttpSession session;
    private Model model;
    private RedirectAttributes ra;
    private BindingResult bindingResult;

    // 【Arrange の共通部分】
    // 全テストケースで必要なモックとコントローラーを事前生成する
    // 個別の Arrange は各テストメソッド内で追加する
    @BeforeEach
    public void setUp() {
        employeeService = mock(EmployeeService.class);
        controller = new EmployeeController(employeeService);
        session = mock(HttpSession.class);
        model = new ConcurrentModel();
        ra = mock(RedirectAttributes.class);
        bindingResult = mock(BindingResult.class);
    }

    // セッションにロールをセットするヘルパー
    private void setRole(String roleCode) {
        UserAuth loginUser = new UserAuth();
        loginUser.setUserRole(roleCode);
        when(session.getAttribute("loginUser")).thenReturn(loginUser);
    }

    // 有効な EmployeeForm を生成するヘルパー
    private EmployeeForm validForm(int syainId) {
        EmployeeForm form = new EmployeeForm();
        form.setSyainId(syainId);
        form.setEmployeecode("EMP001");
        return form;
    }

    // 1. 権限なし
    @Test
    public void testEdit_noPermission() {

        // Arrange: 更新権限のないロール（C）をセットする
        setRole("C");
        EmployeeForm form = validForm(1);
        when(bindingResult.hasErrors()).thenReturn(false);

        // Act
        String result = controller.edit(form, bindingResult, session, model, ra);

        // Assert: 一覧画面へリダイレクトされ、エラーメッセージがセットされることを確認する
        assertEquals("redirect:/employee/list", result);
        verify(ra).addFlashAttribute("errorMessage", "更新権限がありません。");
    }

    // 2. syainId null
    @Test
    public void testEdit_nullSyainId() {

        // Arrange: 権限はあるが syainId が null のフォームを用意する
        setRole("S");
        EmployeeForm form = new EmployeeForm(); // syainId = null
        when(bindingResult.hasErrors()).thenReturn(false);

        // Act
        String result = controller.edit(form, bindingResult, session, model, ra);

        // Assert: 一覧画面へリダイレクトされ、不正エラーメッセージがセットされることを確認する
        assertEquals("redirect:/employee/list", result);
        verify(ra).addFlashAttribute("errorMessage", "社員IDが不正です。再度お試しください。");
    }

    // 3. バリデーションエラー
    @Test
    public void testEdit_validationError() {

        // Arrange: バリデーションエラーが発生する状態を準備する
        setRole("S");
        EmployeeForm form = validForm(1);
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getAllErrors()).thenReturn(Collections.emptyList());
        when(employeeService.getCompanyList()).thenReturn(Collections.emptyList());
        when(employeeService.getJobTypeList()).thenReturn(Collections.emptyList());

        // Act
        String result = controller.edit(form, bindingResult, session, model, ra);

        // Assert: 更新画面に戻ることを確認する
        assertEquals("employee/employeeEdit", result);
    }

    // 4. 変更なし
    @Test
    public void testEdit_noChanges() {

        // Arrange: 更新処理が「変更なし」を返す状態を準備する
        setRole("S");
        EmployeeForm form = validForm(1);
        when(bindingResult.hasErrors()).thenReturn(false);
        when(employeeService.updateEmployee(any())).thenReturn(false);

        // Act
        String result = controller.edit(form, bindingResult, session, model, ra);

        // Assert: 編集画面へリダイレクトされ、変更なしエラーがセットされることを確認する
        assertEquals("redirect:/employee/edit/1", result);
        verify(ra).addFlashAttribute("errorMessage", "社員変更項目がありません。");
    }

    // 5. DB例外
    @Test
    public void testEdit_dbException() {

        // Arrange: DB更新時に例外が発生する状態を準備する
        setRole("S");
        EmployeeForm form = validForm(1);
        when(bindingResult.hasErrors()).thenReturn(false);
        when(employeeService.updateEmployee(any())).thenThrow(new RuntimeException("DB error"));

        // Act
        String result = controller.edit(form, bindingResult, session, model, ra);

        // Assert: システムエラー画面に遷移することを確認する
        assertEquals("employee/systemError", result);
    }

    // 6. 正常更新成功
    @Test
    public void testEdit_success() {

        // Arrange: 更新処理が成功する状態を準備する
        setRole("S");
        EmployeeForm form = validForm(1);
        when(bindingResult.hasErrors()).thenReturn(false);
        when(employeeService.updateEmployee(any())).thenReturn(true);

        // Act
        String result = controller.edit(form, bindingResult, session, model, ra);

        // Assert: 完了画面へリダイレクトされ、成功メッセージがセットされることを確認する
        assertEquals("redirect:/employee/complete", result);
        verify(ra).addFlashAttribute("successMessage", "社員情報を更新しました。");
    }
}