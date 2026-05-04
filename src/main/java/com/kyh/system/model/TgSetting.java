package com.kyh.system.model;

import lombok.Getter;
import lombok.Setter;

// マスタ設定情報を保持するモデル。tg_setting テーブルに対応する。
// tg_setting は category1/2/3 の3階層キーで全マスタを1テーブルに集約した汎用マスタ。
// 区分の定義は MasterCategory.java を参照。各行の項目コードは code フィールド（ビュー用）に格納される。
@Getter
@Setter
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

    // ビュー用フィールド。MasterCategory.getItemCode() によってサービス層で設定される。
    // category2・category3 どちらが項目コードかの判断を呼び出し元が意識しなくて済む。
    private Integer code;

    // value1 の表示用エイリアス。ビュー層で item.name として統一的に参照できる。
    public String getName() { return value1; }
}
