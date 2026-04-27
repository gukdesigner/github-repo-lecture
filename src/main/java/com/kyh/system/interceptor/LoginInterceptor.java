package com.kyh.system.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;

import com.kyh.system.model.UserAuth;

// ログイン状態を確認するインターセプター。
// セッションにログインユーザー情報が存在しない場合はログイン画面へリダイレクトする。
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        UserAuth loginUser = (session != null) ? (UserAuth) session.getAttribute("loginUser") : null;
        if (loginUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }
        return true;
    }
}
