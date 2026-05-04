package com.kyh.system.dto;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

// 社員検索結果の一覧表示に必要なフィールドを保持する DTO。
@Getter
@Setter
public class EmployeeListDto {

    // 社員テーブルの主キー。自動採番で付与される。
    private Integer syainId;

    // 社員を一意に識別するコード。
    private String employeecode;

    // 所属会社のコード。
    private Integer syozokuKaisya;

    // 社員名（漢字）の名。
    private String firstNameKanji;

    // 社員名（漢字）の姓。
    private String lastNameKanji;

    // 性別。1 が男性、2 が女性。
    private Integer seibetu;

    // 職業種類のコード。
    private Integer syokugyoKind;

    // 入社日。
    private LocalDate nyuusyaDate;

    // 退社日。在職中の場合は null となる。
    private LocalDate taisyaDate;

    // tg_setting から JOIN して取得した所属会社の名称。
    private String companyName;

    // tg_setting から JOIN して取得した職業種類の名称。
    private String jobTypeName;
}
