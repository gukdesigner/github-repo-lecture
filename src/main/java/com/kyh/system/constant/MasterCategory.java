package com.kyh.system.constant;

import com.kyh.system.model.TgSetting;

// tg_setting テーブルのマスタ区分を category1〜3 の組み合わせで管理する。
// null は「絞り込みなし」を意味し、動的 WHERE で条件から除外される。
// 新しいマスタ区分を追加する場合はここに1行追加するだけでよい。
//
// 【tg_setting テーブル 全体の構成】
// ────────────────────────────────────────────────────────
// category1=1 ： 会社マスタ
//   ※ 1社につき複数行で情報を構成する（他のカテゴリとは構造が異なる）
//   category2 = 会社ID（1=ブライトスター, 2=トップクラウド, ...）
//   category3 = 情報の種類（行の種別）
//     1: 会社名（日本語 / 英語 / URL）
//     2: 担当者名（姓 / 名 / カナ）
//     3: 住所（郵便番号 / 番地 / 建物名）
//     4: 連絡先（電話 / FAX / メール / 担当者略名）
//     5: 電子印鑑ファイル名
//
// category1=2 ： フォルダ・ファイル管理（ファイル管理機能向け・画面未実装）
//   category2=1: 共有フォルダパスおよびサブフォルダ構成
//   category2=2: 帳票テンプレートファイル名一覧
//
// category1=3 ： 社員属性コードマスタ（ドロップダウン選択肢）
//   category2=1 : 国籍
//   category2=2 : ビザ期間（年数）
//   category2=3 : 在留資格
//   category2=4 : 職業種類
//   category2=5 : 学歴
//   category2=6 : IT スキル（OS・プラットフォーム）
//   category2=7 : IT スキル（プログラミング言語）
//   category2=8 : IT スキル（DB）
//   category2=9 : IT スキル（Web サーバー）　※画面未実装
//   category2=10: IT スキル（フレームワーク）
//   category2=11: 開発ツール・IDE　※画面未実装
//   category2=12: 開発業界　※画面未実装
//   category2=13: 開発役割（PM / SE / PG など）　※画面未実装
//
// category1=4 ： （テーブル定義書に記載あり・用途は別機能向け・画面未実装）
//
// category1=5 ： 契約・給与関連（給与管理機能向け・画面未実装）
//   category2=1: 請求率（円）
//   category2=2: 支払率（円）
//   category2=3: 契約形態（SES / 派遣 / 請負 など）
//   category2=4: 給与形態（月給 / 時給 / 月額固定）
//   category2=5: 残業計算単位（分）
//
// category1=7 ： 会社銀行口座マスタ（給与支払機能向け・画面未実装）
//   category2 = 会社ID（category1=1 の会社ID と対応）
// ────────────────────────────────────────────────────────
public enum MasterCategory {

    // ── 社員登録・更新画面で使用中 ──────────────────────────
    // 会社マスタ: category3=1 の行のみ取得（value1 に会社名が入っている行）。
    // 識別コードは category2（例: 1=ブライトスター, 2=トップクラウド）。
    // ※ 1社の情報は category3=1〜5 の複数行で構成されるが、
    //    ドロップダウンでは会社名行（category3=1）のみ使用する。
    COMPANY    (1, null,  1),
    NATIONALITY(3,    1, null),
    VISA_PERIOD(3,    2, null),
    RESIDENCE  (3,    3, null),
    JOB_TYPE   (3,    4, null),
    EDUCATION  (3,    5, null),
    IT_OS      (3,    6, null),
    IT_LANGUAGE(3,    7, null),
    IT_DB      (3,    8, null),
    IT_FW      (3,   10, null),

    // ── マスタデータは存在するが、現在は画面に表示していない ──
    WEB_SERVER  (3,    9, null),
    IDE_TOOL    (3,   11, null),
    DEV_INDUSTRY(3,   12, null),
    DEV_ROLE    (3,   13, null);

    public final int     category1;
    public final Integer category2;  // null = 絞り込みなし
    public final Integer category3;  // null = 絞り込みなし

    MasterCategory(int category1, Integer category2, Integer category3) {
        this.category1 = category1;
        this.category2 = category2;
        this.category3 = category3;
    }

    // TgSetting の各行から、このカテゴリにおける「項目コード」を取得する。
    // category2 が null（=COMPANY）の場合は各行の識別子が category2 に入っている。
    // category3 が null（=その他）の場合は各行の識別子が category3 に入っている。
    public Integer getItemCode(TgSetting setting) {
        return (category2 == null) ? setting.getCategory2() : setting.getCategory3();
    }
}
