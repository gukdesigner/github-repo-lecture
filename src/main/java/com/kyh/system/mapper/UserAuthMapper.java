package com.kyh.system.mapper;

import org.apache.ibatis.annotations.Param;
import com.kyh.system.model.UserAuth;

// user_auth テーブルに対するデータアクセス操作を定義する MyBatis マッパーインターフェース
public interface UserAuthMapper {

    // ユーザーコードとパスワード(MD5)を照合してユーザー情報を取得する
    UserAuth loginCheck(@Param("userCode") String userCode, @Param("password") String password);
}
