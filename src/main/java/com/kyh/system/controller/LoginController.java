package com.kyh.system.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kyh.system.service.LoginService;
import com.kyh.system.service.LoginService.LoginResult;

// ログイン・ログアウト処理を担当するコントローラー
@Controller
public class LoginController {

    @Autowired
    private LoginService loginService;

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

    // ログイン処理を行う。検証は LoginService に委譲し、成功時のみセッションを発行する。
    @PostMapping("/login")
    public String login(@RequestParam String userCode,
                        @RequestParam String password,
                        HttpServletRequest request,
                        HttpSession session,
                        Model model) {

        LoginResult result = loginService.login(userCode, password);

        if (!result.isSuccess()) {
            model.addAttribute("error", result.getErrorMessage());
            return "employee/employeeLogin";
        }

        // セッション固定攻撃対策：認証成功後に新しいセッションIDを発行する
        session.invalidate();
        request.getSession(true).setAttribute("loginUser", result.getUser());
        return "redirect:/employee/list";
    }

    // ログアウト処理を行う。セッションを無効化してログイン画面へ戻る。
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
