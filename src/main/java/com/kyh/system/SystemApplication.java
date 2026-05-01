package com.kyh.system;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// ============================================================
// 社内統合管理システム（社員情報管理）
// ============================================================
// 技術構成: Spring Boot + MyBatis + Thymeleaf（サーバーサイドレンダリング）
// 層構造  : Controller → Service → Mapper の3層アーキテクチャ
//           画面表示は Thymeleaf テンプレート、SQL は MyBatis XML で管理
//
// 【初めてコードを読む場合の推奨順序】
//   ① application.properties        DB接続・ポート・MyBatis設定
//   ② model/SyainMain.java          社員データの全フィールド定義（61項目）
//      model/TgSetting.java         category1/2/3 キーのマスタ構造
//      constant/MasterCategory.java tg_setting テーブルの区分インデックス（Enum）
//   ③ LoginController               ログイン認証フロー
//      LoginInterceptor             /employee/** へのアクセスを認証ガード
//   ④ EmployeeController#list       一覧画面（検索・権限制御）
//   ⑤ EmployeeController#register   登録画面（バリデーション・INSERT）
//   ⑥ EmployeeController#edit       更新画面（差分 UPDATE）
// ============================================================
@SpringBootApplication
@MapperScan("com.kyh.system.mapper")
public class SystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(SystemApplication.class, args);
    }
}
