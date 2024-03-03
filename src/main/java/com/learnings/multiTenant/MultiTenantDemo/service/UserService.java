package com.learnings.multiTenant.MultiTenantDemo.service;

import com.learnings.multiTenant.MultiTenantDemo.model.User;
import com.learnings.multiTenant.MultiTenantDemo.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepo userRepo;

    public User createUser(User user){
        return userRepo.save(user);
    }
}
