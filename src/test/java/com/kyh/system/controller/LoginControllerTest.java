package com.kyh.system.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import com.kyh.system.model.UserAuth;
import com.kyh.system.service.LoginService;
import com.kyh.system.service.LoginService.LoginResult;

// 【AAA パターンとは】
// 単体テストを3つのフェーズに分けて記述する手法
//   Arrange（準備） : テストに必要なオブジェクトやモックをセットアップする
//   Act    （実行） : テスト対象のメソッドを1度だけ呼び出す
//   Assert （検証） : 実行結果が期待値と一致するか確認する
// フェーズを明確に分けることで「何をテストしているか」が一目でわかる
public class LoginControllerTest {

    // このテストクラスの読み方（フローと分岐）：
    // 1) 成功分岐（正常ログインでセッション再発行）
    // 2) 失敗分岐（入力不備・認証失敗でログイン画面へ戻す）
    // 先頭の成功ケースを基準に、以降の失敗ケース差分を見ると理解しやすい。

    // Given-When-Then 命名を使う理由：
    // - Given(前提) / When(操作) / Then(期待結果) がテスト名だけで読める
    // - 失敗ログから「どの条件で何が期待されたか」を直感的に把握できる
    // - AAA（Arrange/Act/Assert）と意味が対応し、レビュー時の認知負荷を下げられる

    private static final String LOGIN_ERROR_MESSAGE = "ユーザーIDまたはパスワードが違います";

    private LoginController controller;
    private LoginService loginService;

    private HttpServletRequest request;
    private HttpSession oldSession;
    private HttpSession newSession;
    private Model model;

    // 【Arrange の共通部分】
    // 全テストケースで必要なモックとコントローラーを事前生成する
    // 個別の Arrange は各テストメソッド内で追加する
    @BeforeEach
    public void setUp() {
        loginService = mock(LoginService.class);

        // テスト用モックをコンストラクタ経由で注入する
        // フィールドへの直接アクセスが不要になり、private final を維持できる
        controller = new LoginController(loginService);

        request = mock(HttpServletRequest.class);
        oldSession = mock(HttpSession.class);
        newSession = mock(HttpSession.class);
        model = new ConcurrentModel();
    }

    // ログイン失敗ケース用の Arrange を共通化するヘルパー
    private void arrangeFailedLogin(String userId, String password) {
        when(loginService.login(userId, password)).thenReturn(LoginResult.failure(LOGIN_ERROR_MESSAGE));
    }

    @Test
    // テスト観点: 正常な認証情報なら社員一覧へ遷移し、セッション再発行されること
    public void givenValidCredentials_whenLogin_thenRedirectToEmployeeList() {

        // 前提: このケースで使う入力値とモックの戻り値を準備する
        // Arrange
        UserAuth user = new UserAuth();
        when(loginService.login("testuser", "password123")).thenReturn(LoginResult.success(user));
        when(request.getSession(true)).thenReturn(newSession);

        // 操作: 準備した条件で対象メソッドを1回だけ実行する
        // Act
        String result = controller.login("testuser", "password123", request, oldSession, model);

        // 期待結果: 画面遷移・メッセージ・副作用が想定どおりか確認する
        // Assert
        assertEquals("redirect:/employee/list", result);
        verify(oldSession).invalidate();
        verify(newSession).setAttribute("loginUser", user);
    }

    @Test
    // テスト観点: パスワード空文字ならログイン画面に戻りエラー表示されること
    public void givenEmptyPassword_whenLogin_thenReturnLoginPageWithError() {

        // 前提: このケースで使う入力値とモックの戻り値を準備する
        // Arrange
        arrangeFailedLogin("testuser", "");

        // 操作: 準備した条件で対象メソッドを1回だけ実行する
        // Act
        String result = controller.login("testuser", "", request, oldSession, model);

        // 期待結果: 画面遷移・メッセージ・副作用が想定どおりか確認する
        // Assert
        assertEquals("employee/employeeLogin", result);
        assertEquals(LOGIN_ERROR_MESSAGE, model.getAttribute("error"));
        verify(oldSession, never()).invalidate();
    }

    @Test
    // テスト観点: パスワードnullならログイン失敗画面になること
    public void givenNullPassword_whenLogin_thenReturnLoginPage() {

        // 前提: このケースで使う入力値とモックの戻り値を準備する
        // Arrange
        arrangeFailedLogin("testuser", null);

        // 操作: 準備した条件で対象メソッドを1回だけ実行する
        // Act
        String result = controller.login("testuser", null, request, oldSession, model);

        // 期待結果: 画面遷移・メッセージ・副作用が想定どおりか確認する
        // Assert
        assertEquals("employee/employeeLogin", result);
    }

    @Test
    // テスト観点: ユーザーID空文字ならログイン失敗画面になること
    public void givenEmptyUserId_whenLogin_thenReturnLoginPage() {

        // 前提: このケースで使う入力値とモックの戻り値を準備する
        // Arrange
        arrangeFailedLogin("", "password123");

        // 操作: 準備した条件で対象メソッドを1回だけ実行する
        // Act
        String result = controller.login("", "password123", request, oldSession, model);

        // 期待結果: 画面遷移・メッセージ・副作用が想定どおりか確認する
        // Assert
        assertEquals("employee/employeeLogin", result);
    }

    @Test
    // テスト観点: ユーザーIDnullならログイン失敗画面になること
    public void givenNullUserId_whenLogin_thenReturnLoginPage() {

        // 前提: このケースで使う入力値とモックの戻り値を準備する
        // Arrange
        arrangeFailedLogin(null, "password123");

        // 操作: 準備した条件で対象メソッドを1回だけ実行する
        // Act
        String result = controller.login(null, "password123", request, oldSession, model);

        // 期待結果: 画面遷移・メッセージ・副作用が想定どおりか確認する
        // Assert
        assertEquals("employee/employeeLogin", result);
    }

    @Test
    // テスト観点: 数字のみIDは認証失敗として扱われること
    public void givenNumericOnlyUserId_whenLogin_thenReturnLoginPage() {

        // 前提: このケースで使う入力値とモックの戻り値を準備する
        // Arrange
        arrangeFailedLogin("123456", "password123");

        // 操作: 準備した条件で対象メソッドを1回だけ実行する
        // Act
        String result = controller.login("123456", "password123", request, oldSession, model);

        // 期待結果: 画面遷移・メッセージ・副作用が想定どおりか確認する
        // Assert
        assertEquals("employee/employeeLogin", result);
    }

    @Test
    // テスト観点: アンダースコアを含むIDは認証失敗として扱われること
    public void givenUserIdWithUnderscore_whenLogin_thenReturnLoginPage() {

        // 前提: このケースで使う入力値とモックの戻り値を準備する
        // Arrange
        arrangeFailedLogin("admin_user", "password123");

        // 操作: 準備した条件で対象メソッドを1回だけ実行する
        // Act
        String result = controller.login("admin_user", "password123", request, oldSession, model);

        // 期待結果: 画面遷移・メッセージ・副作用が想定どおりか確認する
        // Assert
        assertEquals("employee/employeeLogin", result);
    }

    @Test
    // テスト観点: 日本語IDは認証失敗として扱われること
    public void givenJapaneseUserId_whenLogin_thenReturnLoginPage() {

        // 前提: このケースで使う入力値とモックの戻り値を準備する
        // Arrange
        arrangeFailedLogin("テスト", "password123");

        // 操作: 準備した条件で対象メソッドを1回だけ実行する
        // Act
        String result = controller.login("テスト", "password123", request, oldSession, model);

        // 期待結果: 画面遷移・メッセージ・副作用が想定どおりか確認する
        // Assert
        assertEquals("employee/employeeLogin", result);
    }

    @Test
    // テスト観点: 誤ったパスワードではログインできないこと
    public void givenWrongPassword_whenLogin_thenReturnLoginPage() {

        // 前提: このケースで使う入力値とモックの戻り値を準備する
        // Arrange
        arrangeFailedLogin("testuser", "wrongpass");

        // 操作: 準備した条件で対象メソッドを1回だけ実行する
        // Act
        String result = controller.login("testuser", "wrongpass", request, oldSession, model);

        // 期待結果: 画面遷移・メッセージ・副作用が想定どおりか確認する
        // Assert
        assertEquals("employee/employeeLogin", result);
    }

    @Test
    // テスト観点: 存在しないユーザーIDではログインできないこと
    public void givenUnknownUserId_whenLogin_thenReturnLoginPage() {

        // 前提: このケースで使う入力値とモックの戻り値を準備する
        // Arrange
        arrangeFailedLogin("nonexistent", "anything");

        // 操作: 準備した条件で対象メソッドを1回だけ実行する
        // Act
        String result = controller.login("nonexistent", "anything", request, oldSession, model);

        // 期待結果: 画面遷移・メッセージ・副作用が想定どおりか確認する
        // Assert
        assertEquals("employee/employeeLogin", result);
    }
}
