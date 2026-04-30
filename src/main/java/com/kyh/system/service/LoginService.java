package com.kyh.system.service;

import com.kyh.system.model.UserAuth;

// ログイン認証に関するビジネスロジックを定義するサービスインターフェース
public interface LoginService {

    // ログイン検証を行い、結果を LoginResult で返す
    LoginResult login(String userCode, String password);

    // ログイン処理の結果を保持する値オブジェクト
    class LoginResult {
        private final boolean success;
        private final UserAuth user;
        private final String errorMessage;

        private LoginResult(boolean success, UserAuth user, String errorMessage) {
            this.success = success;
            this.user = user;
            this.errorMessage = errorMessage;
        }

        public static LoginResult success(UserAuth user) {
            return new LoginResult(true, user, null);
        }

        public static LoginResult failure(String errorMessage) {
            return new LoginResult(false, null, errorMessage);
        }

        public boolean isSuccess() { return success; }
        public UserAuth getUser() { return user; }
        public String getErrorMessage() { return errorMessage; }
    }
}
