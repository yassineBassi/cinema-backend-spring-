package com.yassinebassii.cinema.dao;

import com.yassinebassii.cinema.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findRoleByRole(String role);
}
