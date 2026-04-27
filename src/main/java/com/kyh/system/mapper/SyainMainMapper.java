package com.kyh.system.mapper;

import com.kyh.system.model.SyainMain;
import com.kyh.system.dto.EmployeeSearchDto;
import com.kyh.system.dto.EmployeeListDto;
import java.util.List;
import java.util.Map;

// syain_main テーブルに対するデータアクセス操作を定義する MyBatis マッパーインターフェース
public interface SyainMainMapper {

    // 社員IDを指定して更新画面用の社員情報を取得する。削除済みの場合は null を返す。
    SyainMain selectEmployeeForEdit(Integer syainId);

    // 社員登録画面の入力内容を INSERT する。登録後の自動採番 ID は employee.syainId に設定される。
    int insertEmployee(SyainMain employee);

    // 社員更新画面で変更されたフィールドのみ UPDATE する。変更キーの存在有無で更新対象を判定する。
    int updateEmployee(Map<String, Object> changes);

    // 社員IDを指定して論理削除する（delete_flag を 1 に設定）
    int logicalDeleteBySyainId(Integer syainId);

    // 検索条件に一致する社員情報を一覧で取得する
    List<EmployeeListDto> selectEmployeeSearchList(EmployeeSearchDto searchDto);
}
