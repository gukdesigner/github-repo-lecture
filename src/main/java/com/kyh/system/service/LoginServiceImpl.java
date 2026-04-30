package com.kyh.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kyh.system.auth.UserRole;
import com.kyh.system.mapper.UserAuthMapper;
import com.kyh.system.model.UserAuth;

// ログイン認証に関するビジネスロジックを実装するサービスクラス
@Service
public class LoginServiceImpl implements LoginService {

    private static final int IS_VALID_ACCOUNT = 1;

    @Autowired
    private UserAuthMapper userAuthMapper;

    @Override
    public LoginResult login(String userCode, String password) {
        UserAuth user = userAuthMapper.loginCheck(userCode, password);

        if (user == null) {
            return LoginResult.failure("ユーザーIDまたはパスワードが正しくありません。");
        }
        if (!UserRole.isSupported(user.getUserRole())) {
            return LoginResult.failure("対応していない権限コードです。管理者にお問い合わせください。");
        }
        if (user.getIsYoukou() != IS_VALID_ACCOUNT) {
            return LoginResult.failure("使用停止中のアカウントです。管理者にお問い合わせください。");
        }

        return LoginResult.success(user);
    }
}
