package com.kyh.system.dto;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;

// 社員登録・更新画面の入力値を受け取るフォーム専用 DTO
@Getter
@Setter
public class EmployeeForm {

    // テーブルの主キー。更新・削除時の識別に使用する。
    private Integer syainId;

    // 社員を一意に識別するコード。
    // 設計書 §2.7 No1: 必須 / 半角英数字 / 10文字以内
    @NotBlank(message = "社員コードを入力してください。")
    @Size(max = 10, message = "社員コードは10文字以下で入力してください。")
    @Pattern(regexp = "^[A-Za-z0-9]+$", message = "社員コードは半角英数字で入力してください。")
    private String employeecode;

    // 社員名（漢字）の姓。
    // 設計書 §2.7 No2: 必須 / 全角 / 15文字以内
    @NotBlank(message = "社員名（漢字）姓を入力してください。")
    @Size(max = 15, message = "社員名（漢字）姓は15文字以下で入力してください。")
    @Pattern(regexp = "^[^\\u0000-\\u007F]+$", message = "社員名（漢字）姓は全角で入力してください。")
    private String lastNameKanji;

    // 社員名（漢字）の名。
    // 設計書 §2.7 No3: 必須 / 全角 / 15文字以内
    @NotBlank(message = "社員名（漢字）名を入力してください。")
    @Size(max = 15, message = "社員名（漢字）名は15文字以下で入力してください。")
    @Pattern(regexp = "^[^\\u0000-\\u007F]+$", message = "社員名（漢字）名は全角で入力してください。")
    private String firstNameKanji;

    // 社員名（カタカナ）のセイ。
    // 設計書 §2.7 No4: 必須 / 全角カタカナ / 15文字以内
    @NotBlank(message = "社員名（カタカナ）セイを入力してください。")
    @Size(max = 15, message = "社員名（カタカナ）セイは15文字以下で入力してください。")
    @Pattern(regexp = "^[\u30A0-\u30FF\u30FC\u3000]+$", message = "社員名（カタカナ）セイは全角カタカナで入力してください。")
    private String lastNameKana;

    // 社員名（カタカナ）のメイ。
    // 設計書 §2.7 No5: 必須 / 全角カタカナ / 15文字以内
    @NotBlank(message = "社員名（カタカナ）メイを入力してください。")
    @Size(max = 15, message = "社員名（カタカナ）メイは15文字以下で入力してください。")
    @Pattern(regexp = "^[\u30A0-\u30FF\u30FC\u3000]+$", message = "社員名（カタカナ）メイは全角カタカナで入力してください。")
    private String firstNameKana;

    // 社員名（英語）のファーストネーム。
    // 設計書 §2.7 No7: 必須 / 英字 / 30文字以内
    @NotBlank(message = "社員名（英語）first nameを入力してください。")
    @Size(max = 30, message = "社員名（英語）first nameは30文字以下で入力してください。")
    @Pattern(regexp = "^[A-Za-z]+$", message = "社員名（英語）first nameは英字で入力してください。")
    private String firstNameEigo;

    // 社員名（英語）のラストネーム。
    // 設計書 §2.7 No6: 必須 / 英字 / 30文字以内
    @NotBlank(message = "社員名（英語）last nameを入力してください。")
    @Size(max = 30, message = "社員名（英語）last nameは30文字以下で入力してください。")
    @Pattern(regexp = "^[A-Za-z]+$", message = "社員名（英語）last nameは英字で入力してください。")
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
    // 設計書 §2.7 給与口座 No1: 数字のみ / 10文字以内
    @Size(max = 10, message = "金融機関コードは10文字以下で入力してください。")
    @Pattern(regexp = "^[0-9]*$", message = "金融機関コードは数字のみで入力してください。")
    private String kinyukikanCode;

    // 金融機関名。
    // 設計書 §2.7 給与口座 No2: 全角 / 50文字以内
    @Size(max = 50, message = "金融機関名は50文字以下で入力してください。")
    @Pattern(regexp = "^[^\\u0000-\\u007F]*$", message = "金融機関名は全角で入力してください。")
    private String kinyukikanName;

    // 支店コード。
    // 設計書 §2.7 給与口座 No3: 数字のみ / 10文字以内
    @Size(max = 10, message = "支店コードは10文字以下で入力してください。")
    @Pattern(regexp = "^[0-9]*$", message = "支店コードは数字のみで入力してください。")
    private String sitenCode;

    // 支店名。
    // 設計書 §2.7 給与口座 No4: 全角 / 50文字以内
    @Size(max = 50, message = "支店名は50文字以下で入力してください。")
    @Pattern(regexp = "^[^\\u0000-\\u007F]*$", message = "支店名は全角で入力してください。")
    private String sitenName;

    // 口座種類。1 が普通、2 が当座、3 が貯蓄。
    private Integer kouzaKind;

    // 口座番号。
    // 設計書 §2.7 給与口座 No6: 数字のみ / 10文字以内
    @Size(max = 10, message = "口座番号は10文字以下で入力してください。")
    @Pattern(regexp = "^[0-9]*$", message = "口座番号は数字のみで入力してください。")
    private String kouzaNum;

    // 口座名義人名。
    // 設計書 §2.7 給与口座 No7: 50文字以内
    @Size(max = 50, message = "名義人名は50文字以下で入力してください。")
    private String meigiName;
}
