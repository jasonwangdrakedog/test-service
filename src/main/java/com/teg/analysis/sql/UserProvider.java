package com.teg.analysis.sql;

import com.teg.analysis.model.DO.UserDO;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.util.ObjectUtils;

/**
 * @author wangyuan
 * @date 2020/8/7 13:51
 */
public class UserProvider extends SQL {

    private static final String TABLE_NAME = "user";

    public String updateUser(UserDO userDO) {
        return new SQL() {{
            UPDATE(TABLE_NAME);
            if (!ObjectUtils.isEmpty(userDO.getBirthday())) {
                SET("birthday = #{birthday}");
            }
            if (!ObjectUtils.isEmpty(userDO.getHeight())) {
                SET("height= #{height}");
            }
            if (!ObjectUtils.isEmpty(userDO.getProvince())) {
                SET("province= #{province}");
            }
            if (!ObjectUtils.isEmpty(userDO.getWeight())) {
                SET("weight= #{weight}");
            }
            if (!ObjectUtils.isEmpty(userDO.getSex())) {
                SET("sex= #{sex}");
            }
            if (!ObjectUtils.isEmpty(userDO.getUserAddress())) {
                SET("user_address= #{userAddress}");
            }
            if (!ObjectUtils.isEmpty(userDO.getUserName())) {
                SET("user_name= #{userName}");
            }
            WHERE("user_id = #{userId}");
        }}.toString();
    }


    public String insertUser(UserDO userDO) {
        return new SQL() {{
            INSERT_INTO(TABLE_NAME);
            if (!ObjectUtils.isEmpty(userDO.getBirthday())) {
                VALUES("birthday", "'" + userDO.getBirthday() + "'");
            }
            if (!ObjectUtils.isEmpty(userDO.getUserName())) {
                VALUES("user_name", "'" + userDO.getUserName() + "'");
            }
            if (!ObjectUtils.isEmpty(userDO.getUserAddress())) {
                VALUES("user_address", "'" + userDO.getUserAddress() + "'");
            }
            if (!ObjectUtils.isEmpty(userDO.getSex())) {
                VALUES("sex", String.valueOf(userDO.getSex()));
            }
            if (!ObjectUtils.isEmpty(userDO.getHeight())) {
                VALUES("height", String.valueOf(userDO.getHeight()));
            }
            if (!ObjectUtils.isEmpty(userDO.getWeight())) {
                VALUES("weight", String.valueOf(userDO.getWeight()));
            }
            if (!ObjectUtils.isEmpty(userDO.getProvince())) {
                VALUES("province", "'" + userDO.getProvince() + "'");
            }
        }}.toString();
    }


    public String listUser(UserDO userDO) {
        return new SQL() {{
            SELECT("*");
            FROM(TABLE_NAME);
            if (!ObjectUtils.isEmpty(userDO.getUserId())) {
                WHERE("user_id=" + userDO.getUserId());
            }
            if (!ObjectUtils.isEmpty(userDO.getUserName())) {
                WHERE("user_name=" + userDO.getUserName());
            }
        }}.toString();
    }

}
