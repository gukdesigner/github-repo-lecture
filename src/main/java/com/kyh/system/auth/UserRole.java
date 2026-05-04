package com.kyh.system.auth;

// 設計書 2.9 登録画面エリア別権限（○=編集可, △=参照のみ, ×=非表示）
//                      S    A    B    C    D
// 基本情報・会社関連    ○    ○    △    △    ○
// 給与関連情報          ○    ○    ×    ×    ×
// 技術経験              ○    ○    ○    ×    △
public enum UserRole {
    S, A, B, C, D;

    // 社員の削除操作権限（S のみ）
    public boolean canManageEmployee() {
        return this == S;
    }

    // 基本情報・会社関連情報 編集権限（○: S・A・D）
    public boolean canEditBasicInfo() {
        return this == S || this == A || this == D;
    }

    // 給与関連情報 表示権限（○: S・A のみ）
    public boolean canSeeSalary() {
        return this == S || this == A;
    }

    // 技術経験 編集権限（○: S・A・B）
    public boolean canEditTechSkills() {
        return this == S || this == A || this == B;
    }

    // 技術経験 表示権限（△以上: S・A・B・D / C のみ非表示）
    public boolean canSeeTechSkills() {
        return this != C;
    }

    public static boolean isSupported(String roleCode) {
        return from(roleCode) != null;
    }

    public static UserRole from(String roleCode) {
        try {
            return roleCode != null ? UserRole.valueOf(roleCode) : null;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
