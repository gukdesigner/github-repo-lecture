package com.kyh.system.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kyh.system.auth.UserRole;
import com.kyh.system.mapper.UserAuthMapper;
import com.kyh.system.model.UserAuth;

// ログイン・ログアウト処理を担当するコントローラー
@Controller
public class LoginController {

    // 有効なアカウントを示す is_youkou の値
    private static final int IS_VALID_ACCOUNT = 1;

    @Autowired
    private UserAuthMapper userAuthMapper;

    // ルートアクセス時はログイン画面へリダイレクトする
    @GetMapping({"/", "/index", "/employee"})
    public String root() {
        return "redirect:/login";
    }

    // ログイン画面を表示する
    @GetMapping("/login")
    public String loginPage() {
        return "employee/employeeLogin";
    }

    // ログイン処理を行う。ID・パスワードの照合、権限コードの確認、アカウント有効状態の確認を順に行う。
    @PostMapping("/login")
    public String login(@RequestParam String userCode,
                        @RequestParam String password,
                        HttpServletRequest request,
                        HttpSession session,
                        Model model) {

        UserAuth user = userAuthMapper.loginCheck(userCode, password);

        if (user == null) {
            model.addAttribute("error", "ユーザーIDまたはパスワードが正しくありません。");
            return "employee/employeeLogin";
        }

        // 許可されていない権限コードの場合はログイン不可
        if (!UserRole.isSupported(user.getUserRole())) {
            model.addAttribute("error", "対応していない権限コードです。管理者にお問い合わせください。");
            return "employee/employeeLogin";
        }

        // アカウントが停止中の場合はログイン不可
        if (user.getIsYoukou() != IS_VALID_ACCOUNT) {
            model.addAttribute("error", "使用停止中のアカウントです。管理者にお問い合わせください。");
            return "employee/employeeLogin";
        }

        // セッション固定攻撃対策：認証成功後に新しいセッションIDを発行する
        session.invalidate();
        request.getSession(true).setAttribute("loginUser", user);
        return "redirect:/employee/list";
    }

    // ログアウト処理を行う。セッションを無効化してログイン画面へ戻る。
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
