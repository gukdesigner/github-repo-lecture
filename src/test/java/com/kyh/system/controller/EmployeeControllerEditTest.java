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

    // このテストクラスの読み方（フローと分岐）：
    // 1) 権限チェック分岐（Cロールは更新不可）
    // 2) 入力チェック分岐（syainId null / BindingResult エラー）
    // 3) 更新結果分岐（変更なし / 例外 / 成功）
    // 上から順に読むと、edit処理の主要分岐を一通り追える構成になっている。

    // LoginControllerTest と同様に Given-When-Then 命名へ統一する理由：
    // - 権限・バリデーション・例外など分岐の意図を、テスト名だけで把握しやすくするため
    // - AAA の各フェーズとテスト名の意味を一致させ、保守時の読みやすさを上げるため


    private EmployeeController controller;
    private EmployeeService employeeService;

    private HttpSession session;
    private Model model;
    private RedirectAttributes redirectAttributes;
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
        redirectAttributes = mock(RedirectAttributes.class);
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

    // 1. 権限なし（最初の分岐）
    // role=C の場合は更新処理に進まず、一覧へ戻す
    @Test
    // テスト観点: 更新権限がない場合は一覧へ戻り権限エラーになること
    public void givenNoUpdatePermission_whenEdit_thenRedirectToListWithError() {

        // Arrange: 更新権限のないロール（C）をセットする
        setRole("C");
        EmployeeForm form = validForm(1);
        when(bindingResult.hasErrors()).thenReturn(false);

        // 操作: 準備した条件で対象メソッドを1回だけ実行する
        // Act
        String result = controller.edit(form, bindingResult, session, model, redirectAttributes);

        // Assert: 一覧画面へリダイレクトされ、エラーメッセージがセットされることを確認する
        assertEquals("redirect:/employee/list", result);
        verify(redirectAttributes).addFlashAttribute("errorMessage", "更新権限がありません。");
    }

    // 2. syainId null（入力不正分岐）
    // 権限OKでも主キー不正なら更新せずエラー返却
    @Test
    // テスト観点: 社員IDがnullなら更新せずエラーで一覧へ戻ること
    public void givenNullSyainId_whenEdit_thenRedirectToListWithError() {

        // Arrange: 権限はあるが syainId が null のフォームを用意する
        setRole("S");
        EmployeeForm form = new EmployeeForm(); // syainId = null
        when(bindingResult.hasErrors()).thenReturn(false);

        // 操作: 準備した条件で対象メソッドを1回だけ実行する
        // Act
        String result = controller.edit(form, bindingResult, session, model, redirectAttributes);

        // Assert: 一覧画面へリダイレクトされ、不正エラーメッセージがセットされることを確認する
        assertEquals("redirect:/employee/list", result);
        verify(redirectAttributes).addFlashAttribute("errorMessage", "社員IDが不正です。再度お試しください。");
    }

    // 3. バリデーションエラー（入力検証分岐）
    // 入力エラー時は更新を行わず編集画面を再表示
    @Test
    // テスト観点: バリデーションエラー時は編集画面を再表示すること
    public void givenValidationErrors_whenEdit_thenReturnEditPage() {

        // Arrange: バリデーションエラーが発生する状態を準備する
        setRole("S");
        EmployeeForm form = validForm(1);
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getAllErrors()).thenReturn(Collections.emptyList());
        when(employeeService.getCompanyList()).thenReturn(Collections.emptyList());
        when(employeeService.getJobTypeList()).thenReturn(Collections.emptyList());

        // 操作: 準備した条件で対象メソッドを1回だけ実行する
        // Act
        String result = controller.edit(form, bindingResult, session, model, redirectAttributes);

        // Assert: 更新画面に戻ることを確認する
        assertEquals("employee/employeeEdit", result);
    }

    // 4. 変更なし（更新結果分岐）
    // updateEmployee=false の場合は「変更項目なし」として編集画面へ戻す
    @Test
    // テスト観点: 変更項目なしなら編集画面へ戻しエラー表示すること
    public void givenNoChangedFields_whenEdit_thenRedirectBackToEditWithError() {

        // Arrange: 更新処理が「変更なし」を返す状態を準備する
        setRole("S");
        EmployeeForm form = validForm(1);
        when(bindingResult.hasErrors()).thenReturn(false);
        when(employeeService.updateEmployee(any())).thenReturn(false);

        // 操作: 準備した条件で対象メソッドを1回だけ実行する
        // Act
        String result = controller.edit(form, bindingResult, session, model, redirectAttributes);

        // Assert: 編集画面へリダイレクトされ、変更なしエラーがセットされることを確認する
        assertEquals("redirect:/employee/edit/1", result);
        verify(redirectAttributes).addFlashAttribute("errorMessage", "社員変更項目がありません。");
    }

    // 5. DB例外（例外分岐）
    // サービス例外発生時はシステムエラー画面へ遷移
    @Test
    // テスト観点: 更新中例外発生時はシステムエラー画面へ遷移すること
    public void givenDatabaseException_whenEdit_thenReturnSystemErrorPage() {

        // Arrange: DB更新時に例外が発生する状態を準備する
        setRole("S");
        EmployeeForm form = validForm(1);
        when(bindingResult.hasErrors()).thenReturn(false);
        when(employeeService.updateEmployee(any())).thenThrow(new RuntimeException("DB error"));

        // 操作: 準備した条件で対象メソッドを1回だけ実行する
        // Act
        String result = controller.edit(form, bindingResult, session, model, redirectAttributes);

        // Assert: システムエラー画面に遷移することを確認する
        assertEquals("employee/systemError", result);
    }

    // 6. 正常更新成功（成功分岐）
    // 更新成功時は完了画面へ遷移し、成功メッセージを設定
    @Test
    // テスト観点: 正常更新時は完了画面へ遷移し成功メッセージを設定すること
    public void givenValidInput_whenEdit_thenRedirectToCompleteWithSuccessMessage() {

        // Arrange: 更新処理が成功する状態を準備する
        setRole("S");
        EmployeeForm form = validForm(1);
        when(bindingResult.hasErrors()).thenReturn(false);
        when(employeeService.updateEmployee(any())).thenReturn(true);

        // 操作: 準備した条件で対象メソッドを1回だけ実行する
        // Act
        String result = controller.edit(form, bindingResult, session, model, redirectAttributes);

        // Assert: 完了画面へリダイレクトされ、成功メッセージがセットされることを確認する
        assertEquals("redirect:/employee/complete", result);
        verify(redirectAttributes).addFlashAttribute("successMessage", "社員情報を更新しました。");
    }
}