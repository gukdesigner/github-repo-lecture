package com.kyh.system.model;

// マスタ設定情報を保持するモデル。tg_setting テーブルに対応する。
// tg_setting は category1/2/3 の3階層キーで全マスタを1テーブルに集約した汎用マスタ。
// 区分の定義は MasterCategory.java を参照。各行の項目コードは code フィールド（ビュー用）に格納される。
public class TgSetting {

    // マスタ区分の第1レベル。テーブルの大分類を示す。
    private Integer category1;

    // マスタ区分の第2レベル。会社マスタでは各項目の識別コードとして使用する。
    private Integer category2;

    // マスタ区分の第3レベル。会社マスタ以外では各項目の識別コードとして使用する。
    private Integer category3;

    // マスタ項目の主表示名。
    private String value1;

    // マスタ項目の補足値2。
    private String value2;

    // マスタ項目の補足値3。
    private String value3;

    // マスタ項目の補足値4。
    private String value4;

    // マスタ項目の補足値5。
    private String value5;

    // ---- getter / setter ----

    public Integer getCategory1() { return category1; }
    public void setCategory1(Integer category1) { this.category1 = category1; }

    public Integer getCategory2() { return category2; }
    public void setCategory2(Integer category2) { this.category2 = category2; }

    public Integer getCategory3() { return category3; }
    public void setCategory3(Integer category3) { this.category3 = category3; }

    public String getValue1() { return value1; }
    public void setValue1(String value1) { this.value1 = value1 == null ? null : value1.trim(); }

    public String getValue2() { return value2; }
    public void setValue2(String value2) { this.value2 = value2 == null ? null : value2.trim(); }

    public String getValue3() { return value3; }
    public void setValue3(String value3) { this.value3 = value3 == null ? null : value3.trim(); }

    public String getValue4() { return value4; }
    public void setValue4(String value4) { this.value4 = value4 == null ? null : value4.trim(); }

    public String getValue5() { return value5; }
    public void setValue5(String value5) { this.value5 = value5 == null ? null : value5.trim(); }

    // ビュー用フィールド。MasterCategory.getItemCode() によってサービス層で設定される。
    // category2・category3 どちらが項目コードかの判断を呼び出し元が意識しなくて済む。
    private Integer code;

    public Integer getCode() { return code; }
    public void setCode(Integer code) { this.code = code; }

    // value1 の表示用エイリアス。ビュー層で item.name として統一的に参照できる。
    public String getName() { return value1; }
}
