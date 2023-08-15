package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

/**
 * @author xujj
 */
@Mapper
public interface UserMapper {
    User getUser(String openid);

    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertUser(User user);
}
