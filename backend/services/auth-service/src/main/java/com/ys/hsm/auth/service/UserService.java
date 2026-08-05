package com.ys.hsm.auth.service;

import com.ys.hsm.auth.dto.request.RegisterRequest;
import com.ys.hsm.auth.entity.User;

public interface UserService {

    User createUser(RegisterRequest registerRequest);

    User findByEmail(String email);

    boolean existsByEmail(String email);

    User updateUser(User user);
}
