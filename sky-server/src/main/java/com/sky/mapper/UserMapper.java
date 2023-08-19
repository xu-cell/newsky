package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

import java.util.Map;

/**
 * @author xujj
 */
@Mapper
public interface UserMapper {
    User getUser(String openid);

    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertUser(User user);

    User getUserById(Long userId);
    /**
     * 根据动态条件统计用户数量
     * @param map
     * @return
     */
    Integer countByMap(Map map);
}
