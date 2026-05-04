package com.kyh.system.model;

import lombok.Getter;
import lombok.Setter;

// ログインユーザーの認証情報を保持するモデル。user_auth テーブルに対応する。
@Getter
@Setter
public class UserAuth {

    // テーブルの主キー。自動採番で付与される。
    private Integer userId;

    // ログイン時に使用するユーザーコード。
    private String userCode;

    // ユーザーの氏名。
    private String userName;

    // MD5 でハッシュ化されたパスワード。
    private String password;

    // ユーザーの権限コード。S・A・B・C・D のいずれかを保持する。CHAR(1) カラム。
    private String userRole;

    // アカウントの有効状態。1 が有効、0 が停止中。
    private Integer isYoukou;
}
