-- ============================================================
-- 全テーブル定義 + 初期データ投入スクリプト（設計書準拠）
-- 対象DB: PostgreSQL 17
-- 実行順: syain_main → tg_setting → user_auth → INSERT
-- ============================================================


-- ============================================================
-- 1. 社員メインテーブル
-- ============================================================
DROP TABLE IF EXISTS syain_main;

CREATE TABLE syain_main (
    syain_id                  SERIAL PRIMARY KEY,
    first_name_kanji          VARCHAR(30)  NOT NULL,
    last_name_kanji           VARCHAR(30)  NOT NULL,
    first_name_kana           VARCHAR(30)  NOT NULL,
    last_name_kana            VARCHAR(30)  NOT NULL,
    first_name_eigo           VARCHAR(30)  NOT NULL,
    last_name_eigo            VARCHAR(30)  NOT NULL,
    seibetu                   INTEGER,
    tanjyobi                  DATE,
    kokuseki                  INTEGER,
    syussinn                  VARCHAR(30),
    haigusya                  INTEGER,
    passport_num              VARCHAR(20),
    passport_end_date         DATE,
    visa_kikan                INTEGER,
    visa_end_date             DATE,
    zairyu_sikaku             INTEGER,
    kojin_num                 VARCHAR(20),
    zairyu_num                VARCHAR(20),
    syozoku_kaisya            INTEGER      NOT NULL,
    nyuusya_date              DATE,
    taisya_date               DATE,
    syokugyo_kind             INTEGER      NOT NULL,
    rainiti_date              DATE,
    bikou                     TEXT,
    yuubin                    CHAR(8),
    jyusyo_1                  VARCHAR(100),
    jyusyo_2                  VARCHAR(100),
    moyori_eki                VARCHAR(30),
    tel                       VARCHAR(15),
    email                     VARCHAR(50),
    wechat                    VARCHAR(30),
    line                      VARCHAR(30),
    bokoku_jyusyo             VARCHAR(255),
    bokoku_kinnkyuu_rennraku  VARCHAR(255),
    saisyuu_gakureki          INTEGER,
    gakkou_name               VARCHAR(100),
    sennmom_name              VARCHAR(100),
    sotugyo_date              TIMESTAMP,
    gyumu_nensu               REAL,
    initial_name              VARCHAR(30),
    it_os                     VARCHAR(100),
    it_gengo                  VARCHAR(100),
    it_db                     VARCHAR(100),
    it_web_server             VARCHAR(100),
    it_fw                     VARCHAR(100),
    it_other                  VARCHAR(100),
    it_bikou                  TEXT,
    employeecode              VARCHAR(30),
    comment                   TEXT,
    kinyukikan_code           VARCHAR(10),
    kinyukikan_name           VARCHAR(50),
    siten_code                VARCHAR(10),
    siten_name                VARCHAR(50),
    kouza_kind                INTEGER,
    kouza_num                 VARCHAR(10),
    meigi_name                VARCHAR(50),
    delete_flag               INTEGER      NOT NULL DEFAULT 0,
    tourokubi                 TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    kousinnbi                 TIMESTAMP    DEFAULT CURRENT_TIMESTAMP
);


-- ============================================================
-- 2. マスタテーブル
-- ============================================================
DROP TABLE IF EXISTS tg_setting;

CREATE TABLE tg_setting (
    category1  INTEGER       NOT NULL,
    category2  INTEGER       NOT NULL,
    category3  INTEGER       NOT NULL,
    value1     VARCHAR(100)  NOT NULL,
    value2     VARCHAR(100),
    value3     VARCHAR(100),
    value4     VARCHAR(100),
    value5     VARCHAR(100),
    CONSTRAINT pk_tg_setting PRIMARY KEY (category1, category2, category3)
);


-- ============================================================
-- 3. ユーザー認証テーブル
-- 注意: 設計書の PASSWORD は VARCHAR(30) だが、
--       MD5 ハッシュ（32文字）を格納するため VARCHAR(100) を推奨
-- ============================================================
DROP TABLE IF EXISTS user_auth;

CREATE TABLE user_auth (
    user_id    INTEGER      PRIMARY KEY,
    user_code  VARCHAR(20)  NOT NULL UNIQUE,
    user_name  VARCHAR(30)  NOT NULL,
    password   VARCHAR(100) NOT NULL,
    user_role  CHAR(1)      NOT NULL,
    is_youkou  INTEGER      NOT NULL
);


-- ============================================================
-- user_auth 初期データ
-- password は MD5 ハッシュ値（UPPER 統一済み）
-- ============================================================
INSERT INTO user_auth VALUES (1, 'Hoshikawa2021', '星川 一輝', '3486BD910D3E3989C8630B071A19A4FA', 'S', 1);
INSERT INTO user_auth VALUES (2, 'Goen2021',       '呉 艶',    '00EAE9D5CF7FB0248A48F0ED97EB24EB', 'A', 1);
INSERT INTO user_auth VALUES (3, 'Douhina2021',    '道 日那',   'C0F3DCE007CA8F55B99854AA39CE17D9', 'B', 1);
INSERT INTO user_auth VALUES (4, 'Boureisyu2021',  'ボウ 玲珠', '47E233F98BEE73E132EFF2CD6E416E73', 'B', 1);
INSERT INTO user_auth VALUES (5, 'Egi2021',        'エギ',      '5C7B7288EBB69C722F489680E783FD9A', 'C', 1);
INSERT INTO user_auth VALUES (6, 'Kaanki2021',     '何 安琦',   'FBFE6A6D44EC68C43DB716C12EAF5F5A', 'D', 1);
INSERT INTO user_auth VALUES (7, 'Takutyou2021',   'タク 暢',   '790C644949EFA0FE8E5F5B3F32E7DA6C', 'C', 1);


-- ============================================================
-- tg_setting 初期データ
-- ============================================================

-- ------------------------------------------------------------
-- category1=1：会社マスタ
-- 1社につき category3=1〜5 の複数行で情報を構成する
--   category3=1：会社名（日本語 / 英語 / URL）
--   category3=2：担当者名（姓 / 名 / カナ姓 / カナ名）
--   category3=3：住所（郵便番号 / 番地 / 建物名）
--   category3=4：連絡先（電話 / FAX / メール / 担当者略名）
--   category3=5：電子印鑑ファイル名
-- ------------------------------------------------------------

-- 会社ID=1：株式会社ブライトスター
INSERT INTO tg_setting VALUES (1, 1, 1, '株式会社ブライトスター', 'BRIGHT STAR CO., LTD.', 'http://www.brightstar.co.jp', '', '');
INSERT INTO tg_setting VALUES (1, 1, 2, '星川', '一輝', 'ホシカワ', 'カズキ', '');
INSERT INTO tg_setting VALUES (1, 1, 3, '116-0014', '東京都荒川区東日暮里5-52-6', '折原ビル8F', '', '');
INSERT INTO tg_setting VALUES (1, 1, 4, '03-6806-8833', '03-6806-8838', 'soumu@brightstar.co.jp', 'タク', '暢');
INSERT INTO tg_setting VALUES (1, 1, 5, '電子印鑑1.png', '', '', '', '');

-- 会社ID=2：株式会社トップクラウド
INSERT INTO tg_setting VALUES (1, 2, 1, '株式会社トップクラウド', 'TOP CLOUD CO., LTD.', 'http://www.geocities.jp/kk_topcloud/', '', '');
INSERT INTO tg_setting VALUES (1, 2, 2, '星川', '一輝', 'ホシカワ', 'カズキ', '');
INSERT INTO tg_setting VALUES (1, 2, 3, '116-0014', '東京都荒川区東日暮里5-52-6', '折原ビル8F', '', '');
INSERT INTO tg_setting VALUES (1, 2, 4, '03-6806-8833', '03-6806-8838', 'soumu@brightstar.co.jp', 'タク', '暢');
INSERT INTO tg_setting VALUES (1, 2, 5, '電子印鑑2.png', '', '', '', '');

-- ------------------------------------------------------------
-- category1=3, category2=4：職業種類マスタ
-- syain_main.syokugyo_kind に対応する
-- ------------------------------------------------------------
INSERT INTO tg_setting VALUES (3, 4, 1, '役員',         '', '', '', '');
INSERT INTO tg_setting VALUES (3, 4, 2, '総務',         '', '', '', '');
INSERT INTO tg_setting VALUES (3, 4, 3, 'IT営業',       '', '', '', '');
INSERT INTO tg_setting VALUES (3, 4, 4, 'ITエンジニア', '', '', '', '');
INSERT INTO tg_setting VALUES (3, 4, 5, '不動産スタッフ', '', '', '', '');
INSERT INTO tg_setting VALUES (3, 4, 6, '個人事業主',   '', '', '', '');
INSERT INTO tg_setting VALUES (3, 4, 7, '人事',         '', '', '', '');

-- ------------------------------------------------------------
-- category1=3, category2=6：IT スキル（OS・プラットフォーム）
-- syain_main.it_os に対応する
-- ------------------------------------------------------------
INSERT INTO tg_setting VALUES (3, 6,  1, 'DOS',        '', '', '', '');
INSERT INTO tg_setting VALUES (3, 6,  2, 'Windows',    '', '', '', '');
INSERT INTO tg_setting VALUES (3, 6,  3, 'Unix',       '', '', '', '');
INSERT INTO tg_setting VALUES (3, 6,  4, 'Linux',      '', '', '', '');
INSERT INTO tg_setting VALUES (3, 6,  5, 'Android',    '', '', '', '');
INSERT INTO tg_setting VALUES (3, 6,  6, 'iOS',        '', '', '', '');
INSERT INTO tg_setting VALUES (3, 6,  7, 'AWS',        '', '', '', '');
INSERT INTO tg_setting VALUES (3, 6,  8, 'SAP',        '', '', '', '');
INSERT INTO tg_setting VALUES (3, 6,  9, 'Salesforce', '', '', '', '');
INSERT INTO tg_setting VALUES (3, 6, 10, 'Cosminexus', '', '', '', '');
INSERT INTO tg_setting VALUES (3, 6, 11, 'Docker',     '', '', '', '');
INSERT INTO tg_setting VALUES (3, 6, 12, 'Kubernetes', '', '', '', '');
INSERT INTO tg_setting VALUES (3, 6, 13, 'Containerd', '', '', '', '');

-- ------------------------------------------------------------
-- category1=3, category2=7：IT スキル（プログラミング言語）
-- syain_main.it_gengo に対応する
-- ------------------------------------------------------------
INSERT INTO tg_setting VALUES (3, 7,  1, 'C',          '', '', '', '');
INSERT INTO tg_setting VALUES (3, 7,  2, 'ProC',       '', '', '', '');
INSERT INTO tg_setting VALUES (3, 7,  3, 'C#',         '', '', '', '');
INSERT INTO tg_setting VALUES (3, 7,  4, 'VC++',       '', '', '', '');
INSERT INTO tg_setting VALUES (3, 7,  5, 'C++',        '', '', '', '');
INSERT INTO tg_setting VALUES (3, 7,  6, 'Go',         '', '', '', '');
INSERT INTO tg_setting VALUES (3, 7,  7, 'VB',         '', '', '', '');
INSERT INTO tg_setting VALUES (3, 7,  8, 'VB.net',     '', '', '', '');
INSERT INTO tg_setting VALUES (3, 7,  9, 'ASP',        '', '', '', '');
INSERT INTO tg_setting VALUES (3, 7, 10, 'ASP.net',    '', '', '', '');
INSERT INTO tg_setting VALUES (3, 7, 11, 'Java',       '', '', '', '');
INSERT INTO tg_setting VALUES (3, 7, 12, 'JSP',        '', '', '', '');
INSERT INTO tg_setting VALUES (3, 7, 13, 'PHP',        '', '', '', '');
INSERT INTO tg_setting VALUES (3, 7, 14, 'HTML',       '', '', '', '');
INSERT INTO tg_setting VALUES (3, 7, 15, 'XML',        '', '', '', '');
INSERT INTO tg_setting VALUES (3, 7, 16, 'JavaScript', '', '', '', '');
INSERT INTO tg_setting VALUES (3, 7, 17, 'VBScript',   '', '', '', '');
INSERT INTO tg_setting VALUES (3, 7, 18, 'Scala',      '', '', '', '');
INSERT INTO tg_setting VALUES (3, 7, 19, 'ProjectC',   '', '', '', '');
INSERT INTO tg_setting VALUES (3, 7, 20, 'PLSQL',      '', '', '', '');
INSERT INTO tg_setting VALUES (3, 7, 21, 'Python',     '', '', '', '');
INSERT INTO tg_setting VALUES (3, 7, 22, 'Shell',      '', '', '', '');
INSERT INTO tg_setting VALUES (3, 7, 23, 'ABAP',       '', '', '', '');
INSERT INTO tg_setting VALUES (3, 7, 24, 'Swift',      '', '', '', '');
INSERT INTO tg_setting VALUES (3, 7, 25, 'TypeScript', '', '', '', '');

-- ------------------------------------------------------------
-- category1=3, category2=8：IT スキル（DB）
-- syain_main.it_db に対応する
-- ------------------------------------------------------------
INSERT INTO tg_setting VALUES (3, 8,  1, 'Oracle',        '', '', '', '');
INSERT INTO tg_setting VALUES (3, 8,  2, 'SQLServer',      '', '', '', '');
INSERT INTO tg_setting VALUES (3, 8,  3, 'DB2',            '', '', '', '');
INSERT INTO tg_setting VALUES (3, 8,  4, 'MySQL',          '', '', '', '');
INSERT INTO tg_setting VALUES (3, 8,  5, 'PostgreSQL',     '', '', '', '');
INSERT INTO tg_setting VALUES (3, 8,  6, 'SQLite',         '', '', '', '');
INSERT INTO tg_setting VALUES (3, 8,  7, 'Access',         '', '', '', '');
INSERT INTO tg_setting VALUES (3, 8,  8, 'NoSQL',          '', '', '', '');
INSERT INTO tg_setting VALUES (3, 8,  9, 'MongoDB',        '', '', '', '');
INSERT INTO tg_setting VALUES (3, 8, 10, 'Elasticsearch',  '', '', '', '');
INSERT INTO tg_setting VALUES (3, 8, 11, 'Solr',           '', '', '', '');
INSERT INTO tg_setting VALUES (3, 8, 12, 'InfluxDB',       '', '', '', '');
INSERT INTO tg_setting VALUES (3, 8, 13, 'Prometheus',     '', '', '', '');

-- ------------------------------------------------------------
-- category1=3, category2=9：IT スキル（Web サーバー）※画面未実装
-- syain_main.it_web_server に対応する
-- ------------------------------------------------------------
INSERT INTO tg_setting VALUES (3, 9, 1, 'WebLogic',   '', '', '', '');
INSERT INTO tg_setting VALUES (3, 9, 2, 'Apache',     '', '', '', '');
INSERT INTO tg_setting VALUES (3, 9, 3, 'IIS',        '', '', '', '');
INSERT INTO tg_setting VALUES (3, 9, 4, 'LotusNotes', '', '', '', '');
INSERT INTO tg_setting VALUES (3, 9, 5, 'Tomcat',     '', '', '', '');
INSERT INTO tg_setting VALUES (3, 9, 6, 'Undertow',   '', '', '', '');
INSERT INTO tg_setting VALUES (3, 9, 7, 'Kestrel',    '', '', '', '');
INSERT INTO tg_setting VALUES (3, 9, 8, 'Nginx',      '', '', '', '');
INSERT INTO tg_setting VALUES (3, 9, 9, 'Netty',      '', '', '', '');

-- ------------------------------------------------------------
-- category1=3, category2=10：IT スキル（フレームワーク）
-- syain_main.it_fw に対応する
-- ------------------------------------------------------------
INSERT INTO tg_setting VALUES (3, 10,  1, 'Struts',             '', '', '', '');
INSERT INTO tg_setting VALUES (3, 10,  2, 'WebWork',            '', '', '', '');
INSERT INTO tg_setting VALUES (3, 10,  3, 'JavaServlet',        '', '', '', '');
INSERT INTO tg_setting VALUES (3, 10,  4, 'Hibernate',          '', '', '', '');
INSERT INTO tg_setting VALUES (3, 10,  5, 'Seasar2',            '', '', '', '');
INSERT INTO tg_setting VALUES (3, 10,  6, 'JUnit',              '', '', '', '');
INSERT INTO tg_setting VALUES (3, 10,  7, 'Spring',             '', '', '', '');
INSERT INTO tg_setting VALUES (3, 10,  8, 'Spring MVC',         '', '', '', '');
INSERT INTO tg_setting VALUES (3, 10,  9, 'Spring Boot',        '', '', '', '');
INSERT INTO tg_setting VALUES (3, 10, 10, 'Shiro',              '', '', '', '');
INSERT INTO tg_setting VALUES (3, 10, 11, 'Thymeleaf',          '', '', '', '');
INSERT INTO tg_setting VALUES (3, 10, 12, 'Velocity',           '', '', '', '');
INSERT INTO tg_setting VALUES (3, 10, 13, 'Freemarker',         '', '', '', '');
INSERT INTO tg_setting VALUES (3, 10, 14, '.Net Framework',     '', '', '', '');
INSERT INTO tg_setting VALUES (3, 10, 15, '.Net Core',          '', '', '', '');
INSERT INTO tg_setting VALUES (3, 10, 16, 'Winform',            '', '', '', '');
INSERT INTO tg_setting VALUES (3, 10, 17, 'WPF',                '', '', '', '');
INSERT INTO tg_setting VALUES (3, 10, 18, 'Asp.net Webform',    '', '', '', '');
INSERT INTO tg_setting VALUES (3, 10, 19, 'Asp.net MVC',        '', '', '', '');
INSERT INTO tg_setting VALUES (3, 10, 20, 'Asp.net Razor Page', '', '', '', '');
INSERT INTO tg_setting VALUES (3, 10, 21, 'Blazor',             '', '', '', '');
INSERT INTO tg_setting VALUES (3, 10, 22, 'Spring Cloud',       '', '', '', '');
INSERT INTO tg_setting VALUES (3, 10, 23, 'Spring Security',    '', '', '', '');
INSERT INTO tg_setting VALUES (3, 10, 24, 'tornado',            '', '', '', '');
INSERT INTO tg_setting VALUES (3, 10, 25, 'Django',             '', '', '', '');
INSERT INTO tg_setting VALUES (3, 10, 26, 'Flask',              '', '', '', '');
INSERT INTO tg_setting VALUES (3, 10, 27, 'Sanic',              '', '', '', '');

-- ------------------------------------------------------------
-- category1=3, category2=11：開発ツール・IDE ※画面未実装
-- syain_main.it_other に対応する
-- ------------------------------------------------------------
INSERT INTO tg_setting VALUES (3, 11,  1, 'Eclipse',        '', '', '', '');
INSERT INTO tg_setting VALUES (3, 11,  2, 'IDEA',           '', '', '', '');
INSERT INTO tg_setting VALUES (3, 11,  3, 'Visual Studio',  '', '', '', '');
INSERT INTO tg_setting VALUES (3, 11,  4, 'Android Studio', '', '', '', '');
INSERT INTO tg_setting VALUES (3, 11,  5, 'Xcode',          '', '', '', '');
INSERT INTO tg_setting VALUES (3, 11,  6, 'Ultra Edit',     '', '', '', '');
INSERT INTO tg_setting VALUES (3, 11,  7, 'Object Browser', '', '', '', '');
INSERT INTO tg_setting VALUES (3, 11,  8, 'PLSQL Developer','', '', '', '');
INSERT INTO tg_setting VALUES (3, 11,  9, 'SQL Developer',  '', '', '', '');
INSERT INTO tg_setting VALUES (3, 11, 10, 'SVN',            '', '', '', '');
INSERT INTO tg_setting VALUES (3, 11, 11, 'GIT',            '', '', '', '');
INSERT INTO tg_setting VALUES (3, 11, 12, 'TFS',            '', '', '', '');
INSERT INTO tg_setting VALUES (3, 11, 13, 'VSS',            '', '', '', '');
INSERT INTO tg_setting VALUES (3, 11, 14, 'Vue.js',         '', '', '', '');
INSERT INTO tg_setting VALUES (3, 11, 15, 'React.js',       '', '', '', '');
INSERT INTO tg_setting VALUES (3, 11, 16, 'Angular.js',     '', '', '', '');
INSERT INTO tg_setting VALUES (3, 11, 17, 'jQuery',         '', '', '', '');
INSERT INTO tg_setting VALUES (3, 11, 18, 'Bootstrap',      '', '', '', '');

-- ------------------------------------------------------------
-- category1=3, category2=13：開発役割 ※画面未実装
-- ------------------------------------------------------------
INSERT INTO tg_setting VALUES (3, 13, 1, 'PM',     '', '', '', '');
INSERT INTO tg_setting VALUES (3, 13, 2, 'PMO',    '', '', '', '');
INSERT INTO tg_setting VALUES (3, 13, 3, 'TL',     '', '', '', '');
INSERT INTO tg_setting VALUES (3, 13, 4, 'PL',     '', '', '', '');
INSERT INTO tg_setting VALUES (3, 13, 5, 'SE',     '', '', '', '');
INSERT INTO tg_setting VALUES (3, 13, 6, 'PG',     '', '', '', '');
INSERT INTO tg_setting VALUES (3, 13, 7, 'TESTER', '', '', '', '');


-- ============================================================
-- syain_main サンプルデータ（user_auth の7名に対応）
-- ============================================================
INSERT INTO syain_main (
    first_name_kanji, last_name_kanji, first_name_kana, last_name_kana,
    first_name_eigo, last_name_eigo, seibetu, tanjyobi, kokuseki,
    syussinn, haigusya, passport_num, passport_end_date, visa_kikan,
    visa_end_date, zairyu_sikaku, kojin_num, zairyu_num, syozoku_kaisya,
    nyuusya_date, taisya_date, syokugyo_kind, rainiti_date, bikou,
    yuubin, jyusyo_1, jyusyo_2, moyori_eki, tel, email,
    wechat, line, bokoku_jyusyo, bokoku_kinnkyuu_rennraku,
    saisyuu_gakureki, gakkou_name, sennmom_name, sotugyo_date, gyumu_nensu,
    it_os, it_gengo, it_db, it_web_server, it_fw, it_other, it_bikou,
    employeecode, comment,
    kinyukikan_code, kinyukikan_name, siten_code, siten_name,
    kouza_kind, kouza_num, meigi_name,
    delete_flag, tourokubi, kousinnbi
) VALUES
(
    '星川', '一輝', 'ホシカワ', 'カズキ',
    'HOSHIKAWA', 'KAZUKI', 1, '1988-04-10', 2,
    '東京都', 1, 'JP1000001', '2031-04-09', 1,
    '2031-04-09', 1, '111122223333', 'JPZ000001', 1,
    '2017-01-01', NULL, 1, '1988-04-10', 'システム管理者アカウント対応社員',
    '116-0014', '東京都荒川区東日暮里5-52-6', '折原ビル8F', '日暮里駅',
    '090-1000-0001', 'hoshikawa@brightstar.co.jp',
    'hoshikawa_wechat', 'hoshikawa_line', '東京都荒川区', '家族:090-9000-0001',
    3, '早稲田大学', '経営情報学', '2010-03-20', 12.0,
    '2-3,4-3,7-2', '11-3,16-2', '1-2,5-3', '2-2,5-3', '7-3,8-3,23-2', '1-3,11-3',
    '管理・承認・社員管理全般対応',
    'EMP0001', 'システム管理者対応社員',
    '0001', '三菱UFJ銀行', '101', '日暮里支店', 1, '1000001', 'ホシカワ カズキ',
    0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
),
(
    '呉', '艶', 'ゴ', 'エン',
    'GO', 'EN', 0, '1991-07-08', 1,
    '上海市', 1, 'CN2000002', '2029-07-07', 2,
    '2028-07-07', 3, '222233334444', 'CNZ000002', 1,
    '2019-04-01', NULL, 2, '2016-03-15', '統合管理者アカウント対応社員',
    '116-0014', '東京都荒川区東日暮里5-52-6', '折原ビル8F', '西日暮里駅',
    '090-1000-0002', 'goen@brightstar.co.jp',
    'goen_wechat', 'goen_line', '中国上海市浦東新区', '家族:13800000002',
    3, '復旦大学', '国際経営', '2013-07-01', 8.5,
    '2-2,4-2', '11-2,16-2', '5-2', '2-2,5-2', '7-2,8-1', '1-2,11-2',
    '統合管理、総務、営業支援対応',
    'EMP0002', '統合管理者対応社員',
    '0002', 'みずほ銀行', '102', '上野支店', 1, '1000002', 'ゴ エン',
    0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
),
(
    '道', '日那', 'ミチ', 'ヒナ',
    'MICHI', 'HINA', 0, '1994-02-14', 2,
    '大阪府', 0, 'JP3000003', '2032-02-13', 1,
    '2032-02-13', 1, '333344445555', 'JPZ000003', 1,
    '2020-06-01', NULL, 3, '1994-02-14', '営業担当アカウント対応社員',
    '116-0014', '東京都荒川区東日暮里5-52-6', '折原ビル8F', '鶯谷駅',
    '090-1000-0003', 'douhina@brightstar.co.jp',
    'douhina_wechat', 'douhina_line', '大阪府大阪市', '家族:090-9000-0003',
    3, '関西大学', '商学', '2016-03-20', 5.0,
    '2-2,7-1', '11-2,16-1', '4-1,5-1', '2-2', '7-1,8-1', '11-2,17-1',
    '営業、顧客折衝、提案書作成対応',
    'EMP0003', '営業担当対応社員',
    '0003', 'りそな銀行', '103', '日暮里支店', 1, '1000003', 'ミチ ヒナ',
    0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
),
(
    'ボウ', '玲珠', 'ボウ', 'レイジュ',
    'BOU', 'REIJU', 0, '1995-11-03', 1,
    '北京市', 0, 'CN4000004', '2030-11-02', 3,
    '2030-11-02', 5, '444455556666', 'CNZ000004', 2,
    '2021-04-01', NULL, 4, '2019-05-20', '営業担当アカウント対応社員',
    '116-0014', '東京都荒川区東日暮里5-52-6', '折原ビル8F', '上野駅',
    '090-1000-0004', 'boureisyu@topcloud.co.jp',
    'boureisyu_wechat', 'boureisyu_line', '中国北京市海淀区', '家族:13800000004',
    5, '北京信息専門学校', 'ソフトウェア開発', '2017-07-01', 4.5,
    '2-3,4-2', '11-3,13-1,16-2', '4-2,5-2', '5-2,8-1', '8-3,9-2', '1-3,2-2,11-2',
    'Java開発、Web案件営業同行可能',
    'EMP0004', '営業担当対応社員',
    '0004', '三井住友銀行', '104', '上野支店', 1, '1000004', 'ボウ レイジュ',
    0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
),
(
    'エギ', '美咲', 'エギ', 'ミサキ',
    'EGI', 'MISAKI', 0, '1992-12-21', 2,
    '神奈川県', 1, 'JP5000005', '2031-12-20', 1,
    '2031-12-20', 1, '555566667777', 'JPZ000005', 1,
    '2018-10-01', NULL, 2, '1992-12-21', '経理担当アカウント対応社員',
    '116-0014', '東京都荒川区東日暮里5-52-6', '折原ビル8F', '田端駅',
    '090-1000-0005', 'egi@brightstar.co.jp',
    'egi_wechat', 'egi_line', '神奈川県横浜市', '家族:090-9000-0005',
    3, '明治大学', '会計学', '2015-03-20', 7.0,
    '2-2', '11-1,20-1', '7-2', '2-1', '6-1', '9-1,11-2',
    '経理、請求、帳票管理対応',
    'EMP0005', '経理担当対応社員',
    '0005', 'ゆうちょ銀行', '105', '本店', 1, '1000005', 'エギ ミサキ',
    0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
),
(
    '何', '安琦', 'ナン', 'アンキ',
    'NAN', 'ANKI', 0, '1993-08-18', 1,
    '広東省', 0, 'CN6000006', '2029-08-17', 2,
    '2027-08-17', 3, '666677778888', 'CNZ000006', 2,
    '2022-01-11', NULL, 2, '2020-09-01', '人事担当アカウント対応社員',
    '116-0014', '東京都荒川区東日暮里5-52-6', '折原ビル8F', '三河島駅',
    '090-1000-0006', 'kaanki@topcloud.co.jp',
    'kaanki_wechat', 'kaanki_line', '中国広東省深圳市', '家族:13800000006',
    2, '深圳大学大学院', '人材管理', '2018-06-30', 6.0,
    '2-2,4-1', '11-1,16-1', '5-1', '2-1', '7-1,8-1', '11-2',
    '採用、人事、社員情報更新対応',
    'EMP0006', '人事担当対応社員',
    '0006', 'みずほ銀行', '106', '西日暮里支店', 1, '1000006', 'ナン アンキ',
    0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
),
(
    'タク', '暢', 'タク', 'チョウ',
    'TAKU', 'CHO', 1, '1987-03-05', 1,
    '吉林省', 1, 'CN7000007', '2028-03-04', 2,
    '2028-03-04', 4, '777788889999', 'CNZ000007', 1,
    '2016-09-01', NULL, 2, '2010-04-10', '経理担当アカウント対応社員',
    '116-0014', '東京都荒川区東日暮里5-52-6', '折原ビル8F', '日暮里駅',
    '090-1000-0007', 'takutyou@brightstar.co.jp',
    'takutyou_wechat', 'takutyou_line', '中国吉林省長春市', '家族:13800000007',
    3, '吉林大学', '経済学', '2009-07-01', 10.0,
    '2-2,7-1', '11-1,20-1', '1-1,5-1', '2-1', '6-1', '10-1,11-2',
    '経費、入金、支払管理対応',
    'EMP0007', '経理担当対応社員',
    '0007', '三菱UFJ銀行', '107', '三河島支店', 1, '1000007', 'タク チョウ',
    0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
);
