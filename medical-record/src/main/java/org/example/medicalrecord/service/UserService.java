package org.example.medicalrecord.service;

import org.example.medicalrecord.data.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    List<User> getUsers();

    User getUser(long id);

    User createUser(User user);

    User updateUser(User user, long id);

    void deleteUser(long id);
    
}
