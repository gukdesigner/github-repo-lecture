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

    // 1. 正常なログイン
    @Test
    public void testLogin_successful() {

        // Arrange: ログイン成功を返すモックと、新セッションを準備する
        UserAuth user = new UserAuth();
        when(loginService.login("testuser", "password123"))
                .thenReturn(LoginResult.success(user));
        when(request.getSession(true)).thenReturn(newSession);

        // Act: ログインメソッドを実行する
        String result = controller.login("testuser", "password123", request, oldSession, model);

        // Assert: 社員一覧へリダイレクトされ、セッションが正しく再発行されることを確認する
        assertEquals("redirect:/employee/list", result);
        verify(oldSession).invalidate();
        verify(newSession).setAttribute("loginUser", user);
    }

    // 2. パスワード未入力
    @Test
    public void testLogin_emptyPassword() {

        // Arrange: パスワード空文字でサービスが失敗を返すよう設定する
        when(loginService.login("testuser", ""))
                .thenReturn(LoginResult.failure("ユーザーIDまたはパスワードが違います"));

        // Act: 空パスワードでログインを試みる
        String result = controller.login("testuser", "", request, oldSession, model);

        // Assert: ログイン画面に戻り、エラーメッセージがセットされ、セッションが無効化されないことを確認する
        assertEquals("employee/employeeLogin", result);
        assertEquals("ユーザーIDまたはパスワードが違います", model.getAttribute("error"));
        verify(oldSession, never()).invalidate();
    }

    // 3. パスワードnull
    @Test
    public void testLogin_nullPassword() {

        // Arrange
        when(loginService.login("testuser", null))
                .thenReturn(LoginResult.failure("ユーザーIDまたはパスワードが違います"));

        // Act
        String result = controller.login("testuser", null, request, oldSession, model);

        // Assert
        assertEquals("employee/employeeLogin", result);
    }

    // 4. ユーザーID未入力
    @Test
    public void testLogin_emptyUserId() {

        // Arrange
        when(loginService.login("", "password123"))
                .thenReturn(LoginResult.failure("ユーザーIDまたはパスワードが違います"));

        // Act
        String result = controller.login("", "password123", request, oldSession, model);

        // Assert
        assertEquals("employee/employeeLogin", result);
    }

    // 5. ユーザーIDnull
    @Test
    public void testLogin_nullUserId() {

        // Arrange
        when(loginService.login(null, "password123"))
                .thenReturn(LoginResult.failure("ユーザーIDまたはパスワードが違います"));

        // Act
        String result = controller.login(null, "password123", request, oldSession, model);

        // Assert
        assertEquals("employee/employeeLogin", result);
    }

    // 6. ユーザーIDが数字のみ
    @Test
    public void testLogin_userIdNumericOnly() {

        // Arrange
        when(loginService.login("123456", "password123"))
                .thenReturn(LoginResult.failure("ユーザーIDまたはパスワードが違います"));

        // Act
        String result = controller.login("123456", "password123", request, oldSession, model);

        // Assert
        assertEquals("employee/employeeLogin", result);
    }

    // 7. ユーザーIDにアンダースコア
    @Test
    public void testLogin_userIdWithUnderscore() {

        // Arrange
        when(loginService.login("admin_user", "password123"))
                .thenReturn(LoginResult.failure("ユーザーIDまたはパスワードが違います"));

        // Act
        String result = controller.login("admin_user", "password123", request, oldSession, model);

        // Assert
        assertEquals("employee/employeeLogin", result);
    }

    // 8. ユーザーIDが日本語
    @Test
    public void testLogin_userIdInJapanese() {

        // Arrange
        when(loginService.login("テスト", "password123"))
                .thenReturn(LoginResult.failure("ユーザーIDまたはパスワードが違います"));

        // Act
        String result = controller.login("テスト", "password123", request, oldSession, model);

        // Assert
        assertEquals("employee/employeeLogin", result);
    }

    // 9. パスワード間違い
    @Test
    public void testLogin_wrongPassword() {

        // Arrange
        when(loginService.login("testuser", "wrongpass"))
                .thenReturn(LoginResult.failure("ユーザーIDまたはパスワードが違います"));

        // Act
        String result = controller.login("testuser", "wrongpass", request, oldSession, model);

        // Assert
        assertEquals("employee/employeeLogin", result);
    }

    // 10. 存在しないユーザーID
    @Test
    public void testLogin_userNotFound() {

        // Arrange
        when(loginService.login("nonexistent", "anything"))
                .thenReturn(LoginResult.failure("ユーザーIDまたはパスワードが違います"));

        // Act
        String result = controller.login("nonexistent", "anything", request, oldSession, model);

        // Assert
        assertEquals("employee/employeeLogin", result);
    }
}