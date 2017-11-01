package com.arcvideo.face.service.impl;

import com.arcvideo.face.model.User;
import com.arcvideo.face.repository.UserRepository;
import com.arcvideo.face.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created with idea
 * User: ben
 * Date:2017/10/31
 * Time:19:53
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User findOne(int id) {
        User user = userRepository.findOne(id);
        return user;
    }
}
