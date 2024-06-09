package com.example.localguidebe.repository;

import com.example.localguidebe.entity.User;
import com.example.localguidebe.enums.RolesEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByEmail(String email);

    Page<User> findAll(Specification<Object> specification, Pageable pageable);

    List<User> findByRoles_Name(RolesEnum roles_name);

    Page<User> findByRoles_Name(RolesEnum roles_name, Pageable pageable);

}
