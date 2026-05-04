package com.kyh.system.model;

import java.time.LocalDate;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

// 社員の全情報を保持するモデル。syain_main テーブルに対応する。
@Getter
@Setter
public class SyainMain {

    // テーブルの主キー。自動採番で付与される。
    private Integer syainId;

    // 社員を一意に識別するコード。DB 上はユニーク制約あり（唯一不同）。
    private String employeecode;

    // ---- 基本情報 ----

    // 社員名（漢字）の名。
    private String firstNameKanji;

    // 社員名（漢字）の姓。
    private String lastNameKanji;

    // 社員名（カタカナ）のメイ。
    private String firstNameKana;

    // 社員名（カタカナ）のセイ。
    private String lastNameKana;

    // 社員名（英語）のファーストネーム。
    private String firstNameEigo;

    // 社員名（英語）のラストネーム。
    private String lastNameEigo;

    // イニシャル名。SyainMainMapper の resultMap に未登録のため DB からは取得されない。将来の利用を想定して保持。
    private String initialName;

    // 性別。実装では 1=男性, 2=女性 を使用する。
    // ※ テーブル定義書には 0=女 / 1=男 と記載があるが、画面・ロジック共に 1=男 / 2=女 で実装している。
    private Integer seibetu;

    // 生年月日。
    private LocalDate tanjyobi;

    // 国籍コード。tg_setting(category1=3, category2=1) の category3 値を格納する。
    // 例: 1=中国, 2=日本, 3=韓国
    private Integer kokuseki;

    // 出身地。中国の場合は省レベルまで記入する。
    private String syussinn;

    // 配偶者の有無。0=なし, 1=あり。
    private Integer haigusya;

    // ---- パスポート・ビザ・在留情報 ----

    // パスポート番号。
    private String passportNum;

    // パスポートの有効期限。
    private LocalDate passportEndDate;

    // ビザ期間コード。tg_setting(category1=3, category2=2) の category3 値を格納する。
    // 例: 1=1年, 2=3年, 3=5年
    private Integer visaKikan;

    // ビザの有効期限。
    private LocalDate visaEndDate;

    // 在留資格コード。tg_setting(category1=3, category2=3) の category3 値を格納する。
    // 例: 1=日本国籍, 2=永住者, 3=技術・人文知識・国際業務, 4=企業内転勤
    private Integer zairyuSikaku;

    // 個人番号（マイナンバー）。
    private String kojinNum;

    // 在留カード番号。
    private String zairyuNum;

    // 来日日。
    private LocalDate rainitiDate;

    // ---- 会社・就業情報 ----

    // 所属会社コード。tg_setting(category1=1, category3=1) の category2 値を格納する。
    // 例: 1=株式会社ブライトスター, 2=株式会社トップクラウド
    private Integer syozokuKaisya;

    // 入社日。
    private LocalDate nyuusyaDate;

    // 退社日。在職中の場合は null となる。
    private LocalDate taisyaDate;

    // 職業種類コード。tg_setting(category1=3, category2=4) の category3 値を格納する。
    // 例: 1=役員, 2=総務, 3=IT営業, 4=ITエンジニア, 5=不動産スタッフ
    private Integer syokugyoKind;

    // 業務経験年数。
    private Float gyumuNensu;

    // ---- 住所・連絡先 ----

    // 郵便番号。CHAR 8桁（例: 169-0075）。
    private String yuubin;

    // 住所（都道府県・市区町村）。
    private String jyusyo1;

    // 住所（番地・マンション名など）。
    private String jyusyo2;

    // 最寄り駅。
    private String moyoriEki;

    // 電話番号。
    private String tel;

    // メールアドレス。
    private String email;

    // WeChat のアカウント ID。
    private String wechat;

    // LINE のアカウント ID。
    private String line;

    // 母国の住所。
    private String bokokuJyusyo;

    // 母国の緊急連絡先。
    private String bokokuKinnkyuuRennraku;

    // ---- 学歴情報 ----

    // 最終学歴コード。tg_setting(category1=3, category2=5) の category3 値を格納する。
    // 例: 1=大学院（博士）, 2=大学院（修士）, 3=大学, 4=短期大学, 5=専門学校
    private Integer saisyuuGakureki;

    // 学校名。
    private String gakkouName;

    // 専攻名。
    private String sennmomName;

    // 卒業日。
    private LocalDate sotugyoDate;

    // ---- IT スキル情報 ----
    // 【共通フォーマット】カンマ「,」区切りで複数スキルを保持する。
    // 各スキルは「マスタID-経験レベル」形式で記録する。
    //   経験レベル: 1=◎（実務1年以上）, 2=○（実務経験あり）, 3=△（知識あり）
    //   例: "2-1,4-3,6-2" → ID2を◎, ID4を△, ID6を○ で習得
    // マスタID は tg_setting の該当カテゴリの category3 値に対応する。

    // OS スキル。tg_setting(category1=3, category2=6) を参照。
    // 例: 1=DOS, 2=Windows, 3=Unix, 4=Linux, 7=AWS
    private String itOs;

    // プログラミング言語スキル。tg_setting(category1=3, category2=7) を参照。
    // 例: 1=C, 5=C#, 11=Java, 16=JavaScript, 21=Python
    private String itGengo;

    // DB スキル。tg_setting(category1=3, category2=8) を参照。
    // 例: 1=Oracle, 2=SQLServer, 4=MySql, 5=PostgreSQL
    private String itDb;

    // Web サーバースキル。tg_setting(category1=3, category2=9) を参照。画面未実装。
    // 例: 1=WebLogic, 2=Apache, 3=IIS, 8=Nginx
    private String itWebServer;

    // フレームワークスキル。tg_setting(category1=3, category2=10) を参照。
    // 例: 1=Struts, 7=Spring, 9=Spring Boot, 11=Thymeleaf
    private String itFw;

    // その他スキル（開発ツール・IDE）。tg_setting(category1=3, category2=11) を参照。
    // 例: 1=Eclipse, 2=IDEA, 3=Visual Studio, 11=GIT
    private String itOther;

    // IT スキルに関する備考・自己紹介文。
    private String itBikou;

    // ---- 給与・口座情報 ----

    // 金融機関コード。
    private String kinyukikanCode;

    // 金融機関名。
    private String kinyukikanName;

    // 支店コード。
    private String sitenCode;

    // 支店名。
    private String sitenName;

    // 口座種類。1=普通, 2=当座, 3=貯蓄。テーブル定義書では 1=普通（固定）と記載。
    private Integer kouzaKind;

    // 口座番号。
    private String kouzaNum;

    // 口座名義人名。
    private String meigiName;

    // ---- その他 ----

    // 備考。
    private String bikou;

    // コメント。
    private String comment;

    // 論理削除フラグ。0=有効, 1=削除済み。INSERT 時の初期値は 0。
    private Integer deleteFlag;

    // レコードの登録日時。DB が自動設定する。
    private Date tourokubi;

    // レコードの最終更新日時。DB が自動設定する。
    private Date kousinnbi;
}
