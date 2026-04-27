package com.kyh.system.model;

import java.time.LocalDate;
import java.util.Date;

// 社員の全情報を保持するモデル。syain_main テーブルに対応する。
public class SyainMain {

    // テーブルの主キー。自動採番で付与される。
    private Integer syainId;

    // 社員を一意に識別するコード。
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

    // イニシャル名。
    private String initialName;

    // 性別。1 が男性、2 が女性。
    private Integer seibetu;

    // 生年月日。
    private LocalDate tanjyobi;

    // 国籍コード。tg_setting の国籍マスタに対応する。
    private Integer kokuseki;

    // 出身地。
    private String syussinn;

    // 配偶者の有無。1 が有り、0 が無し。
    private Integer haigusya;

    // ---- パスポート・ビザ・在留情報 ----

    // パスポート番号。
    private String passportNum;

    // パスポートの有効期限。
    private LocalDate passportEndDate;

    // ビザ期間コード。tg_setting のビザ期間マスタに対応する。
    private Integer visaKikan;

    // ビザの有効期限。
    private LocalDate visaEndDate;

    // 在留資格コード。tg_setting の在留資格マスタに対応する。
    private Integer zairyuSikaku;

    // 個人番号（マイナンバー）。
    private String kojinNum;

    // 在留カード番号。
    private String zairyuNum;

    // 来日日。
    private LocalDate rainitiDate;

    // ---- 会社・就業情報 ----

    // 所属会社コード。tg_setting の会社マスタに対応する。
    private Integer syozokuKaisya;

    // 入社日。
    private LocalDate nyuusyaDate;

    // 退社日。在職中の場合は null となる。
    private LocalDate taisyaDate;

    // 職業種類コード。tg_setting の職業種類マスタに対応する。
    private Integer syokugyoKind;

    // 業務経験年数。
    private Float gyumuNensu;

    // ---- 住所・連絡先 ----

    // 郵便番号。
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

    // 最終学歴コード。tg_setting の学歴マスタに対応する。
    private Integer saisyuuGakureki;

    // 学校名。
    private String gakkouName;

    // 専攻名。
    private String sennmomName;

    // 卒業日。
    private LocalDate sotugyoDate;

    // ---- IT スキル情報 ----

    // 習得済み OS スキル。選択値をカンマ区切りで保持する。
    private String itOs;

    // 習得済みプログラミング言語スキル。選択値をカンマ区切りで保持する。
    private String itGengo;

    // 習得済み DB スキル。選択値をカンマ区切りで保持する。
    private String itDb;

    // 習得済み Web サーバースキル。選択値をカンマ区切りで保持する。
    private String itWebServer;

    // 習得済みフレームワークスキル。選択値をカンマ区切りで保持する。
    private String itFw;

    // その他の IT スキル。選択値をカンマ区切りで保持する。
    private String itOther;

    // IT スキルに関する備考。
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

    // 口座種類。1 が普通、2 が当座、3 が貯蓄。
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

    // 論理削除フラグ。1 が削除済み、0 が有効。
    private Integer deleteFlag;

    // レコードの登録日時。
    private Date tourokubi;

    // レコードの最終更新日時。
    private Date kousinnbi;

    // ---- getter / setter ----

    public Integer getSyainId() { return syainId; }
    public void setSyainId(Integer syainId) { this.syainId = syainId; }

    public String getEmployeecode() { return employeecode; }
    public void setEmployeecode(String employeecode) { this.employeecode = employeecode == null ? null : employeecode.trim(); }

    public String getFirstNameKanji() { return firstNameKanji; }
    public void setFirstNameKanji(String firstNameKanji) { this.firstNameKanji = firstNameKanji == null ? null : firstNameKanji.trim(); }

    public String getLastNameKanji() { return lastNameKanji; }
    public void setLastNameKanji(String lastNameKanji) { this.lastNameKanji = lastNameKanji == null ? null : lastNameKanji.trim(); }

    public String getFirstNameKana() { return firstNameKana; }
    public void setFirstNameKana(String firstNameKana) { this.firstNameKana = firstNameKana == null ? null : firstNameKana.trim(); }

    public String getLastNameKana() { return lastNameKana; }
    public void setLastNameKana(String lastNameKana) { this.lastNameKana = lastNameKana == null ? null : lastNameKana.trim(); }

    public String getFirstNameEigo() { return firstNameEigo; }
    public void setFirstNameEigo(String firstNameEigo) { this.firstNameEigo = firstNameEigo == null ? null : firstNameEigo.trim(); }

    public String getLastNameEigo() { return lastNameEigo; }
    public void setLastNameEigo(String lastNameEigo) { this.lastNameEigo = lastNameEigo == null ? null : lastNameEigo.trim(); }

    public String getInitialName() { return initialName; }
    public void setInitialName(String initialName) { this.initialName = initialName == null ? null : initialName.trim(); }

    public Integer getSeibetu() { return seibetu; }
    public void setSeibetu(Integer seibetu) { this.seibetu = seibetu; }

    public LocalDate getTanjyobi() { return tanjyobi; }
    public void setTanjyobi(LocalDate tanjyobi) { this.tanjyobi = tanjyobi; }

    public Integer getKokuseki() { return kokuseki; }
    public void setKokuseki(Integer kokuseki) { this.kokuseki = kokuseki; }

    public String getSyussinn() { return syussinn; }
    public void setSyussinn(String syussinn) { this.syussinn = syussinn == null ? null : syussinn.trim(); }

    public Integer getHaigusya() { return haigusya; }
    public void setHaigusya(Integer haigusya) { this.haigusya = haigusya; }

    public String getPassportNum() { return passportNum; }
    public void setPassportNum(String passportNum) { this.passportNum = passportNum == null ? null : passportNum.trim(); }

    public LocalDate getPassportEndDate() { return passportEndDate; }
    public void setPassportEndDate(LocalDate passportEndDate) { this.passportEndDate = passportEndDate; }

    public Integer getVisaKikan() { return visaKikan; }
    public void setVisaKikan(Integer visaKikan) { this.visaKikan = visaKikan; }

    public LocalDate getVisaEndDate() { return visaEndDate; }
    public void setVisaEndDate(LocalDate visaEndDate) { this.visaEndDate = visaEndDate; }

    public Integer getZairyuSikaku() { return zairyuSikaku; }
    public void setZairyuSikaku(Integer zairyuSikaku) { this.zairyuSikaku = zairyuSikaku; }

    public String getKojinNum() { return kojinNum; }
    public void setKojinNum(String kojinNum) { this.kojinNum = kojinNum == null ? null : kojinNum.trim(); }

    public String getZairyuNum() { return zairyuNum; }
    public void setZairyuNum(String zairyuNum) { this.zairyuNum = zairyuNum == null ? null : zairyuNum.trim(); }

    public LocalDate getRainitiDate() { return rainitiDate; }
    public void setRainitiDate(LocalDate rainitiDate) { this.rainitiDate = rainitiDate; }

    public Integer getSyozokuKaisya() { return syozokuKaisya; }
    public void setSyozokuKaisya(Integer syozokuKaisya) { this.syozokuKaisya = syozokuKaisya; }

    public LocalDate getNyuusyaDate() { return nyuusyaDate; }
    public void setNyuusyaDate(LocalDate nyuusyaDate) { this.nyuusyaDate = nyuusyaDate; }

    public LocalDate getTaisyaDate() { return taisyaDate; }
    public void setTaisyaDate(LocalDate taisyaDate) { this.taisyaDate = taisyaDate; }

    public Integer getSyokugyoKind() { return syokugyoKind; }
    public void setSyokugyoKind(Integer syokugyoKind) { this.syokugyoKind = syokugyoKind; }

    public Float getGyumuNensu() { return gyumuNensu; }
    public void setGyumuNensu(Float gyumuNensu) { this.gyumuNensu = gyumuNensu; }

    public String getYuubin() { return yuubin; }
    public void setYuubin(String yuubin) { this.yuubin = yuubin == null ? null : yuubin.trim(); }

    public String getJyusyo1() { return jyusyo1; }
    public void setJyusyo1(String jyusyo1) { this.jyusyo1 = jyusyo1 == null ? null : jyusyo1.trim(); }

    public String getJyusyo2() { return jyusyo2; }
    public void setJyusyo2(String jyusyo2) { this.jyusyo2 = jyusyo2 == null ? null : jyusyo2.trim(); }

    public String getMoyoriEki() { return moyoriEki; }
    public void setMoyoriEki(String moyoriEki) { this.moyoriEki = moyoriEki == null ? null : moyoriEki.trim(); }

    public String getTel() { return tel; }
    public void setTel(String tel) { this.tel = tel == null ? null : tel.trim(); }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email == null ? null : email.trim(); }

    public String getWechat() { return wechat; }
    public void setWechat(String wechat) { this.wechat = wechat == null ? null : wechat.trim(); }

    public String getLine() { return line; }
    public void setLine(String line) { this.line = line == null ? null : line.trim(); }

    public String getBokokuJyusyo() { return bokokuJyusyo; }
    public void setBokokuJyusyo(String bokokuJyusyo) { this.bokokuJyusyo = bokokuJyusyo == null ? null : bokokuJyusyo.trim(); }

    public String getBokokuKinnkyuuRennraku() { return bokokuKinnkyuuRennraku; }
    public void setBokokuKinnkyuuRennraku(String bokokuKinnkyuuRennraku) { this.bokokuKinnkyuuRennraku = bokokuKinnkyuuRennraku == null ? null : bokokuKinnkyuuRennraku.trim(); }

    public Integer getSaisyuuGakureki() { return saisyuuGakureki; }
    public void setSaisyuuGakureki(Integer saisyuuGakureki) { this.saisyuuGakureki = saisyuuGakureki; }

    public String getGakkouName() { return gakkouName; }
    public void setGakkouName(String gakkouName) { this.gakkouName = gakkouName == null ? null : gakkouName.trim(); }

    public String getSennmomName() { return sennmomName; }
    public void setSennmomName(String sennmomName) { this.sennmomName = sennmomName == null ? null : sennmomName.trim(); }

    public LocalDate getSotugyoDate() { return sotugyoDate; }
    public void setSotugyoDate(LocalDate sotugyoDate) { this.sotugyoDate = sotugyoDate; }

    public String getItOs() { return itOs; }
    public void setItOs(String itOs) { this.itOs = itOs == null ? null : itOs.trim(); }

    public String getItGengo() { return itGengo; }
    public void setItGengo(String itGengo) { this.itGengo = itGengo == null ? null : itGengo.trim(); }

    public String getItDb() { return itDb; }
    public void setItDb(String itDb) { this.itDb = itDb == null ? null : itDb.trim(); }

    public String getItWebServer() { return itWebServer; }
    public void setItWebServer(String itWebServer) { this.itWebServer = itWebServer == null ? null : itWebServer.trim(); }

    public String getItFw() { return itFw; }
    public void setItFw(String itFw) { this.itFw = itFw == null ? null : itFw.trim(); }

    public String getItOther() { return itOther; }
    public void setItOther(String itOther) { this.itOther = itOther == null ? null : itOther.trim(); }

    public String getItBikou() { return itBikou; }
    public void setItBikou(String itBikou) { this.itBikou = itBikou == null ? null : itBikou.trim(); }

    public String getKinyukikanCode() { return kinyukikanCode; }
    public void setKinyukikanCode(String kinyukikanCode) { this.kinyukikanCode = kinyukikanCode == null ? null : kinyukikanCode.trim(); }

    public String getKinyukikanName() { return kinyukikanName; }
    public void setKinyukikanName(String kinyukikanName) { this.kinyukikanName = kinyukikanName == null ? null : kinyukikanName.trim(); }

    public String getSitenCode() { return sitenCode; }
    public void setSitenCode(String sitenCode) { this.sitenCode = sitenCode == null ? null : sitenCode.trim(); }

    public String getSitenName() { return sitenName; }
    public void setSitenName(String sitenName) { this.sitenName = sitenName == null ? null : sitenName.trim(); }

    public Integer getKouzaKind() { return kouzaKind; }
    public void setKouzaKind(Integer kouzaKind) { this.kouzaKind = kouzaKind; }

    public String getKouzaNum() { return kouzaNum; }
    public void setKouzaNum(String kouzaNum) { this.kouzaNum = kouzaNum == null ? null : kouzaNum.trim(); }

    public String getMeigiName() { return meigiName; }
    public void setMeigiName(String meigiName) { this.meigiName = meigiName == null ? null : meigiName.trim(); }

    public String getBikou() { return bikou; }
    public void setBikou(String bikou) { this.bikou = bikou == null ? null : bikou.trim(); }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public Integer getDeleteFlag() { return deleteFlag; }
    public void setDeleteFlag(Integer deleteFlag) { this.deleteFlag = deleteFlag; }

    public Date getTourokubi() { return tourokubi; }
    public void setTourokubi(Date tourokubi) { this.tourokubi = tourokubi; }

    public Date getKousinnbi() { return kousinnbi; }
    public void setKousinnbi(Date kousinnbi) { this.kousinnbi = kousinnbi; }
}
