package com.kyh.system.mapper;

import org.apache.ibatis.annotations.Param;
import com.kyh.system.model.TgSetting;
import java.util.List;

// tg_setting テーブルに対するデータアクセス操作を定義する MyBatis マッパーインターフェース
public interface TgSettingMapper {

    // category1 を基準に category2・category3 で絞り込んでマスタ一覧を取得する。
    // null を渡した場合はその条件を適用しない。
    List<TgSetting> selectByCategory(
            @Param("category1") int category1,
            @Param("category2") Integer category2,
            @Param("category3") Integer category3);
}
