package com.sky.service;

import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;

/**
 * @author xujj
 */
public interface UserService {
    User login(UserLoginDTO userLoginDTO);
}
