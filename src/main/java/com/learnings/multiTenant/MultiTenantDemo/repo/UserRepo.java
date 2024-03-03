package com.learnings.multiTenant.MultiTenantDemo.repo;

import com.learnings.multiTenant.MultiTenantDemo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User,Long> {
}
