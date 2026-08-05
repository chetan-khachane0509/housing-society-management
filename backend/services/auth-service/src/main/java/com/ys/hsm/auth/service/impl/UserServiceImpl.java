package com.ys.hsm.auth.service.impl;

import com.ys.hsm.auth.dto.request.RegisterRequest;
import com.ys.hsm.auth.entity.User;
import com.ys.hsm.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    /**
     * @param registerRequest
     * @return
     */
    @Override
    public User createUser(RegisterRequest registerRequest) {
        return null;
    }

    /**
     * @param email
     * @return
     */
    @Override
    public User findByEmail(String email) {
        return null;
    }

    /**
     * @param email
     * @return
     */
    @Override
    public boolean existsByEmail(String email) {
        return false;
    }

    /**
     * @param user
     * @return
     */
    @Override
    public User updateUser(User user) {
        return null;
    }
}
