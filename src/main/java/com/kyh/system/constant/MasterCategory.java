package com.kyh.system.constant;

import com.kyh.system.model.TgSetting;

// tg_setting テーブルのマスタ区分を category1〜3 の組み合わせで管理する。
// null は「絞り込みなし」を意味し、動的 WHERE で条件から除外される。
// 新しいマスタ区分を追加する場合はここに1行追加するだけでよい。
public enum MasterCategory {

    COMPANY    (1, null,  1),
    NATIONALITY(3,    1, null),
    VISA_PERIOD(3,    2, null),
    RESIDENCE  (3,    3, null),
    JOB_TYPE   (3,    4, null),
    EDUCATION  (3,    5, null),
    IT_OS      (3,    6, null),
    IT_LANGUAGE(3,    7, null),
    IT_DB      (3,    8, null),
    IT_FW      (3,   10, null),

    // 以下、今後画面追加時に使用予定
    WEB_SERVER  (3,    9, null),
    IDE_TOOL    (3,   11, null),
    DEV_INDUSTRY(3,   12, null),
    DEV_ROLE    (3,   13, null);

    public final int     category1;
    public final Integer category2;  // null = 絞り込みなし
    public final Integer category3;  // null = 絞り込みなし

    MasterCategory(int category1, Integer category2, Integer category3) {
        this.category1 = category1;
        this.category2 = category2;
        this.category3 = category3;
    }

    // TgSetting の各行から、このカテゴリにおける「項目コード」を取得する。
    // category2 が null（=COMPANY）の場合は各行の識別子が category2 に入っている。
    // category3 が null（=その他）の場合は各行の識別子が category3 に入っている。
    public Integer getItemCode(TgSetting setting) {
        return (category2 == null) ? setting.getCategory2() : setting.getCategory3();
    }
}
