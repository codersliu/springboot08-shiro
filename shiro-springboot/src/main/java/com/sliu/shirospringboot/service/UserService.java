package com.sliu.shirospringboot.service;

import com.sliu.shirospringboot.pojo.User;

public interface UserService {
    public User queryUserByName(String name);
}
