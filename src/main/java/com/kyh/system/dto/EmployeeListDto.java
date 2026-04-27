package com.kyh.system.dto;

import java.time.LocalDate;

// 社員検索結果の一覧表示に必要なフィールドを保持する DTO。
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

    public EmployeeListDto() {}

    public Integer getSyainId() { return syainId; }
    public void setSyainId(Integer syainId) { this.syainId = syainId; }

    public String getEmployeecode() { return employeecode; }
    public void setEmployeecode(String employeecode) { this.employeecode = employeecode; }

    public Integer getSyozokuKaisya() { return syozokuKaisya; }
    public void setSyozokuKaisya(Integer syozokuKaisya) { this.syozokuKaisya = syozokuKaisya; }

    public String getFirstNameKanji() { return firstNameKanji; }
    public void setFirstNameKanji(String firstNameKanji) { this.firstNameKanji = firstNameKanji; }

    public String getLastNameKanji() { return lastNameKanji; }
    public void setLastNameKanji(String lastNameKanji) { this.lastNameKanji = lastNameKanji; }

    public Integer getSeibetu() { return seibetu; }
    public void setSeibetu(Integer seibetu) { this.seibetu = seibetu; }

    public Integer getSyokugyoKind() { return syokugyoKind; }
    public void setSyokugyoKind(Integer syokugyoKind) { this.syokugyoKind = syokugyoKind; }

    public LocalDate getNyuusyaDate() { return nyuusyaDate; }
    public void setNyuusyaDate(LocalDate nyuusyaDate) { this.nyuusyaDate = nyuusyaDate; }

    public LocalDate getTaisyaDate() { return taisyaDate; }
    public void setTaisyaDate(LocalDate taisyaDate) { this.taisyaDate = taisyaDate; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getJobTypeName() { return jobTypeName; }
    public void setJobTypeName(String jobTypeName) { this.jobTypeName = jobTypeName; }
}
