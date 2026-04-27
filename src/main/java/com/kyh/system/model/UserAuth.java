package com.kyh.system.model;

// ログインユーザーの認証情報を保持するモデル。user_auth テーブルに対応する。
public class UserAuth {

    // テーブルの主キー。自動採番で付与される。
    private Integer userId;

    // ログイン時に使用するユーザーコード。
    private String userCode;

    // ユーザーの氏名。
    private String userName;

    // MD5 でハッシュ化されたパスワード。
    private String password;

    // ユーザーの権限コード。S・A・B・C・D のいずれかを保持する。
    private String userRole;

    // アカウントの有効状態。1 が有効、0 が停止中。
    private Integer isYoukou;

    // ---- getter / setter ----

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public String getUserCode() { return userCode; }
    public void setUserCode(String userCode) { this.userCode = userCode == null ? null : userCode.trim(); }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName == null ? null : userName.trim(); }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password == null ? null : password.trim(); }

    public String getUserRole() { return userRole; }
    public void setUserRole(String userRole) { this.userRole = userRole == null ? null : userRole.trim(); }

    public Integer getIsYoukou() { return isYoukou; }
    public void setIsYoukou(Integer isYoukou) { this.isYoukou = isYoukou; }
}
