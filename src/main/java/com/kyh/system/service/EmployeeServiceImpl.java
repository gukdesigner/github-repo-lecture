package com.kyh.system.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kyh.system.constant.MasterCategory;
import com.kyh.system.dto.EmployeeListDto;
import com.kyh.system.dto.EmployeeSearchDto;
import com.kyh.system.mapper.SyainMainMapper;
import com.kyh.system.mapper.TgSettingMapper;
import com.kyh.system.model.SyainMain;
import com.kyh.system.model.TgSetting;

// EmployeeService の実装クラス。
// 役割: 業務ルール判定とMapper呼び出しの調停（ControllerとSQLの橋渡し）。
@Service
public class EmployeeServiceImpl implements EmployeeService {

    // tg_setting（category1=3, category2=4）の職業種類マスタにおける「ITエンジニア」の category3 値。
    // 名称ではなくコードで指定することで、マスタの名称変更に影響されない。
    // 変更する場合はこの定数のみ修正すること。
    private static final int DEFAULT_JOB_TYPE_CODE = 4;

    @Autowired
    private TgSettingMapper tgSettingMapper;

    @Autowired
    private SyainMainMapper syainMainMapper;

    // 社員情報を新規登録する。delete_flag が未設定の場合は 0（有効）を初期値として設定する。
    @Override
    public void insertEmployee(SyainMain employee) {
        if (employee.getDeleteFlag() == null) {
            employee.setDeleteFlag(0);
        }
        syainMainMapper.insertEmployee(employee);
    }

    // 社員IDをもとに更新画面用の社員情報を取得する
    @Override
    public SyainMain getEmployeeBySyainId(Integer syainId) {
        return syainMainMapper.selectEmployeeForEdit(syainId);
    }

    // 変更されたフィールドのみ UPDATE する。変更がない場合は false を返す。
    // 更新前データを DB から再取得し buildChanges() で差分 Map を生成する。
    // null への変更も Map のキー存在チェックで正しく反映される（通常の null 比較とは異なる点に注意）。
    @Override
    public boolean updateEmployee(SyainMain employee) {
        SyainMain before = syainMainMapper.selectEmployeeForEdit(employee.getSyainId());
        Map<String, Object> changes = buildChanges(before, employee);
        if (changes.isEmpty()) {
            return false;
        }
        changes.put("syainId", employee.getSyainId());
        syainMainMapper.updateEmployee(changes);
        return true;
    }

    // 社員を論理削除する
    @Override
    public void logicalDeleteEmployee(Integer syainId) {
        syainMainMapper.logicalDeleteBySyainId(syainId);
    }

    // 指定したカテゴリのマスタ一覧を取得し、各行にビュー用の code を設定する
    private List<TgSetting> getSettings(MasterCategory category) {
        List<TgSetting> list = tgSettingMapper.selectByCategory(category.category1, category.category2, category.category3);
        list.forEach(s -> s.setCode(category.getItemCode(s)));
        return list;
    }

    // 所属会社マスタ一覧を取得する
    @Override
    public List<TgSetting> getCompanyList() {
        return getSettings(MasterCategory.COMPANY);
    }

    // 国籍マスタ一覧を取得する
    @Override
    public List<TgSetting> getNationalityList() {
        return getSettings(MasterCategory.NATIONALITY);
    }

    // ビザ期間マスタ一覧を取得する
    @Override
    public List<TgSetting> getVisaPeriodList() {
        return getSettings(MasterCategory.VISA_PERIOD);
    }

    // 在留資格マスタ一覧を取得する
    @Override
    public List<TgSetting> getResidenceStatusList() {
        return getSettings(MasterCategory.RESIDENCE);
    }

    // 職業種類マスタ一覧を取得する
    @Override
    public List<TgSetting> getJobTypeList() {
        return getSettings(MasterCategory.JOB_TYPE);
    }

    // 学歴マスタ一覧を取得する
    @Override
    public List<TgSetting> getEducationList() {
        return getSettings(MasterCategory.EDUCATION);
    }

    // IT スキル（OS）マスタ一覧を取得する
    @Override
    public List<TgSetting> getItOsList() {
        return getSettings(MasterCategory.IT_OS);
    }

    // IT スキル（プログラミング言語）マスタ一覧を取得する
    @Override
    public List<TgSetting> getItLanguageList() {
        return getSettings(MasterCategory.IT_LANGUAGE);
    }

    // IT スキル（DB）マスタ一覧を取得する
    @Override
    public List<TgSetting> getItDbList() {
        return getSettings(MasterCategory.IT_DB);
    }

    // IT スキル（フレームワーク）マスタ一覧を取得する
    @Override
    public List<TgSetting> getItFwList() {
        return getSettings(MasterCategory.IT_FW);
    }

    // 初回表示時の社員一覧検索条件（在職中・既定職業）を設定する
    @Override
    public void initializeDefaultSearchCondition(EmployeeSearchDto searchDto) {
        if (searchDto.isSearched()) {
            return;
        }

        searchDto.setWorking(true);
        searchDto.setNotWorking(false);
        searchDto.setSyokugyoKind(DEFAULT_JOB_TYPE_CODE);
    }

    // 在職／非在職チェック条件が検索可能かを判定する
    @Override
    public boolean isValidWorkingFilter(Boolean working, Boolean notWorking) {
        return Boolean.TRUE.equals(working) || Boolean.TRUE.equals(notWorking);
    }

    // 検索条件に一致する社員一覧を取得する
    @Override
    public List<EmployeeListDto> getEmployeeSearchList(EmployeeSearchDto searchDto) {
        return syainMainMapper.selectEmployeeSearchList(searchDto);
    }

    // 更新前後を比較し、変更されたフィールドのみを Map に格納して返す。
    // Objects.equals を使用するのは、どちらかが null の場合でも NPE を回避するため。
    // この Map は MyBatis の updateEmployee(map) に渡され、containsKey で更新列を判定する。
    // フィールド追加時は本メソッドと mapper/SyainMainMapper.xml の updateEmployee を同時に更新すること。
    private Map<String, Object> buildChanges(SyainMain before, SyainMain after) {
        Map<String, Object> changes = new LinkedHashMap<>();
        if (!Objects.equals(before.getLastNameKanji(),  after.getLastNameKanji()))  changes.put("lastNameKanji",  after.getLastNameKanji());
        if (!Objects.equals(before.getFirstNameKanji(), after.getFirstNameKanji())) changes.put("firstNameKanji", after.getFirstNameKanji());
        if (!Objects.equals(before.getLastNameKana(),   after.getLastNameKana()))   changes.put("lastNameKana",   after.getLastNameKana());
        if (!Objects.equals(before.getFirstNameKana(),  after.getFirstNameKana()))  changes.put("firstNameKana",  after.getFirstNameKana());
        if (!Objects.equals(before.getFirstNameEigo(),  after.getFirstNameEigo()))  changes.put("firstNameEigo",  after.getFirstNameEigo());
        if (!Objects.equals(before.getLastNameEigo(),   after.getLastNameEigo()))   changes.put("lastNameEigo",   after.getLastNameEigo());
        if (!Objects.equals(before.getSeibetu(),        after.getSeibetu()))        changes.put("seibetu",        after.getSeibetu());
        if (!Objects.equals(before.getSyozokuKaisya(),  after.getSyozokuKaisya()))  changes.put("syozokuKaisya",  after.getSyozokuKaisya());
        if (!Objects.equals(before.getNyuusyaDate(),    after.getNyuusyaDate()))    changes.put("nyuusyaDate",    after.getNyuusyaDate());
        if (!Objects.equals(before.getTaisyaDate(),     after.getTaisyaDate()))     changes.put("taisyaDate",     after.getTaisyaDate());
        if (!Objects.equals(before.getSyokugyoKind(),   after.getSyokugyoKind()))   changes.put("syokugyoKind",   after.getSyokugyoKind());
        return changes;
    }
}
