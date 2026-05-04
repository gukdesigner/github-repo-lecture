package com.kyh.system.dto;

import lombok.Getter;
import lombok.Setter;

// 社員検索画面の入力条件を保持する DTO
@Getter
@Setter
public class EmployeeSearchDto {

    // 絞り込む所属会社のコード。選択なしの場合は null となる。
    private Integer syozokuKaisya;

    // 社員名の部分一致検索キーワード。未入力の場合は null となる。
    private String employeeName;

    // 絞り込む職業種類のコード。選択なしの場合は null となる。
    private Integer syokugyoKind;

    // 在職チェックの状態。null を有効値として扱うため Boolean を使用する。
    // Spring MVC のバインドは Boolean 型では isWorking() でなく getWorking() を呼び出す。
    // Lombok は Boolean（ラッパー型）に getWorking() を生成するため問題なし。
    private Boolean working;

    // 非在職チェックの状態。null を有効値として扱うため Boolean を使用する。
    private Boolean notWorking;

    // 検索ボタンが押された場合に true となるフラグ。初回表示と検索実行を区別するために使用する。
    private boolean searched;
}
