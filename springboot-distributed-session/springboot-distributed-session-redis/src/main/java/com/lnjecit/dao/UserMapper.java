package com.lnjecit.dao;

import com.lnjecit.entity.User;

import java.util.List;

public interface UserMapper {
    
    List<User> selectAll(User user);
    
    User selectById(Integer userId);
    
    int deleteById(User record);
    
    
}
