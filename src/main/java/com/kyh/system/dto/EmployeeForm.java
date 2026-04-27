package com.kyh.system.dto;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

// 社員登録・更新画面の入力値を受け取るフォーム専用 DTO
public class EmployeeForm {

    // テーブルの主キー。更新・削除時の識別に使用する。
    private Integer syainId;

    // 社員を一意に識別するコード。
    @NotBlank(message = "社員コードは必須です。")
    private String employeecode;

    // 社員名（漢字）の姓。
    @NotBlank(message = "社員名（漢字）姓は必須です。")
    private String lastNameKanji;

    // 社員名（漢字）の名。
    @NotBlank(message = "社員名（漢字）名は必須です。")
    private String firstNameKanji;

    // 社員名（カタカナ）のセイ。
    @NotBlank(message = "社員名（カタカナ）セイは必須です。")
    private String lastNameKana;

    // 社員名（カタカナ）のメイ。
    @NotBlank(message = "社員名（カタカナ）メイは必須です。")
    private String firstNameKana;

    // 社員名（英語）のファーストネーム。
    @NotBlank(message = "社員名（英語）first nameは必須です。")
    private String firstNameEigo;

    // 社員名（英語）のラストネーム。
    @NotBlank(message = "社員名（英語）last nameは必須です。")
    private String lastNameEigo;

    // 性別。1 が男性、2 が女性。
    private Integer seibetu;

    // 所属会社コード。tg_setting の会社マスタに対応する。
    @NotNull(message = "所属会社は必須です。")
    private Integer syozokuKaisya;

    // 入社日。yyyy-MM-dd 形式で受け取り LocalDate に変換する。
    @NotNull(message = "入社日は必須です。")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate nyuusyaDate;

	// 退社日。在職中の場合は null を設定する。yyyy-MM-dd 形式で受け取る。
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate taisyaDate;

    // 職業種類コード。tg_setting の職業種類マスタに対応する。
    @NotNull(message = "職業種類は必須です。")
    private Integer syokugyoKind;

    // 習得済み OS スキル。選択値をカンマ区切りで結合して保持する。
    private String itOs;

    // ---- 給与関連情報（登録画面のみ入力） ----

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

    // ---- getter / setter ----

    public Integer getSyainId() { return syainId; }
    public void setSyainId(Integer syainId) { this.syainId = syainId; }

    public String getEmployeecode() { return employeecode; }
    public void setEmployeecode(String employeecode) { this.employeecode = employeecode; }

    public String getLastNameKanji() { return lastNameKanji; }
    public void setLastNameKanji(String lastNameKanji) { this.lastNameKanji = lastNameKanji; }

    public String getFirstNameKanji() { return firstNameKanji; }
    public void setFirstNameKanji(String firstNameKanji) { this.firstNameKanji = firstNameKanji; }

    public String getLastNameKana() { return lastNameKana; }
    public void setLastNameKana(String lastNameKana) { this.lastNameKana = lastNameKana; }

    public String getFirstNameKana() { return firstNameKana; }
    public void setFirstNameKana(String firstNameKana) { this.firstNameKana = firstNameKana; }

    public String getFirstNameEigo() { return firstNameEigo; }
    public void setFirstNameEigo(String firstNameEigo) { this.firstNameEigo = firstNameEigo; }

    public String getLastNameEigo() { return lastNameEigo; }
    public void setLastNameEigo(String lastNameEigo) { this.lastNameEigo = lastNameEigo; }

    public Integer getSeibetu() { return seibetu; }
    public void setSeibetu(Integer seibetu) { this.seibetu = seibetu; }

    public Integer getSyozokuKaisya() { return syozokuKaisya; }
    public void setSyozokuKaisya(Integer syozokuKaisya) { this.syozokuKaisya = syozokuKaisya; }

    public LocalDate getNyuusyaDate() { return nyuusyaDate; }
    public void setNyuusyaDate(LocalDate nyuusyaDate) { this.nyuusyaDate = nyuusyaDate; }

    public LocalDate getTaisyaDate() { return taisyaDate; }
    public void setTaisyaDate(LocalDate taisyaDate) { this.taisyaDate = taisyaDate; }

    public Integer getSyokugyoKind() { return syokugyoKind; }
    public void setSyokugyoKind(Integer syokugyoKind) { this.syokugyoKind = syokugyoKind; }

    public String getItOs() { return itOs; }
    public void setItOs(String itOs) { this.itOs = itOs; }

    public String getKinyukikanCode() { return kinyukikanCode; }
    public void setKinyukikanCode(String kinyukikanCode) { this.kinyukikanCode = kinyukikanCode; }

    public String getKinyukikanName() { return kinyukikanName; }
    public void setKinyukikanName(String kinyukikanName) { this.kinyukikanName = kinyukikanName; }

    public String getSitenCode() { return sitenCode; }
    public void setSitenCode(String sitenCode) { this.sitenCode = sitenCode; }

    public String getSitenName() { return sitenName; }
    public void setSitenName(String sitenName) { this.sitenName = sitenName; }

    public Integer getKouzaKind() { return kouzaKind; }
    public void setKouzaKind(Integer kouzaKind) { this.kouzaKind = kouzaKind; }

    public String getKouzaNum() { return kouzaNum; }
    public void setKouzaNum(String kouzaNum) { this.kouzaNum = kouzaNum; }

    public String getMeigiName() { return meigiName; }
    public void setMeigiName(String meigiName) { this.meigiName = meigiName; }
}
