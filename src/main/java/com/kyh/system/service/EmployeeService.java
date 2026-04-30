package com.kyh.system.service;

import java.util.List;

import com.kyh.system.dto.EmployeeListDto;
import com.kyh.system.dto.EmployeeSearchDto;
import com.kyh.system.model.SyainMain;
import com.kyh.system.model.TgSetting;

// 社員情報に関するビジネスロジックを定義するサービスインターフェース
public interface EmployeeService {

    // 所属会社マスタ一覧を取得する
    List<TgSetting> getCompanyList();

    // 職業種類マスタ一覧を取得する
    List<TgSetting> getJobTypeList();

    // 国籍マスタ一覧を取得する
    List<TgSetting> getNationalityList();

    // ビザ期間マスタ一覧を取得する
    List<TgSetting> getVisaPeriodList();

    // 在留資格マスタ一覧を取得する
    List<TgSetting> getResidenceStatusList();

    // 学歴マスタ一覧を取得する
    List<TgSetting> getEducationList();

    // IT スキル（OS）マスタ一覧を取得する（技術経験表示用）
    List<TgSetting> getItOsList();

    // IT スキル（プログラミング言語）マスタ一覧を取得する
    List<TgSetting> getItLanguageList();

    // IT スキル（DB）マスタ一覧を取得する
    List<TgSetting> getItDbList();

    // IT スキル（フレームワーク）マスタ一覧を取得する
    List<TgSetting> getItFwList();

    // 初回表示時の社員一覧検索条件（在職中・既定職業）を設定する
    void initializeDefaultSearchCondition(EmployeeSearchDto searchDto, List<TgSetting> jobTypeList);

    // 在職／非在職チェック条件が検索可能かを判定する
    boolean isValidWorkingFilter(Boolean working, Boolean notWorking);

    // 検索条件に一致する社員一覧を取得する
    List<EmployeeListDto> getEmployeeSearchList(EmployeeSearchDto searchDto);

    // 社員情報を新規登録する
    void insertEmployee(SyainMain employee);

    // 社員IDをもとに更新画面用の社員情報を取得する。該当なし・削除済みの場合は null を返す。
    SyainMain getEmployeeBySyainId(Integer syainId);

    // 社員情報を更新する。変更がない場合は何もせず false を返す。
    boolean updateEmployee(SyainMain employee);

    // 社員を論理削除する（削除フラグを 1 に設定）
    void logicalDeleteEmployee(Integer syainId);
}
