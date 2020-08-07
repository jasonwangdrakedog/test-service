package com.teg.analysis.mapper;

import com.teg.analysis.model.DO.UserDO;
import com.teg.analysis.sql.UserProvider;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author wangyuan
 * @date 2020/8/7 13:09
 */
@Mapper
public interface DemoMapper {


    @InsertProvider(type = UserProvider.class, method = "insertUser")
    void createUser(UserDO userREQ);


    @UpdateProvider(type = UserProvider.class, method = "updateUser")
    Integer updateUser(UserDO userREQ);

    @Insert("<script> INSERT INTO user "
            + "(birthday,user_name,user_address,sex,height,weight,province) "
            + "VALUES "
            + "<foreach collection = 'list' item='record' separator=',' > "
            + " (#{record.birthday}, "
            + " #{record.userName},"
            + " #{record.userAddress},"
            + " #{record.sex},"
            + " #{record.height}, "
            + " #{record.weight}, "
            + " #{record.province}) "
            + "</foreach> "
            + "</script>")
    void batchCreateUser(@Param("list") List<UserDO> userREQList);

    // @Select("select * from user ")
    @SelectProvider(type = UserProvider.class, method = "listUser")
    List<UserDO> listUser(UserDO userREQ);
}
