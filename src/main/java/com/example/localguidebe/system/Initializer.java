package com.example.localguidebe.system;

import com.example.localguidebe.entity.Role;
import com.example.localguidebe.entity.User;
import com.example.localguidebe.enums.RolesEnum;
import com.example.localguidebe.repository.RoleRepository;
import com.example.localguidebe.repository.UserRepository;
import com.example.localguidebe.service.RoleService;
import com.example.localguidebe.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class Initializer implements CommandLineRunner {

    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public Initializer(UserService userService, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        createRoleIfNotFound(RolesEnum.ADMIN);
        createRoleIfNotFound(RolesEnum.GUIDER);
        createRoleIfNotFound(RolesEnum.TRAVELER);
        createRootUser();
    }

    private void createRoleIfNotFound(RolesEnum roleName) {
        if (roleService.findByName(roleName) == null) {
            Role role = new Role(roleName);
            roleService.save(role);
        }
    }
    private void createRootUser() {
        User checkedUser = userService.findUserByEmail("admin12345@gmail.com");
        User checkedGuide = userService.findUserByEmail("guide12345@gmail.com");
        if(checkedUser != null){
            checkedUser.setPassword(passwordEncoder.encode("admin12345"));
            userService.saveUser(checkedUser);
        }
        else {
            User user = new User();
            user.setFullName("admin");
            user.setPassword(passwordEncoder.encode("admin12345"));
            user.setEmail("admin12345@gmail.com");
            Set<Role> roles = new HashSet<>();
            roles.add(roleService.findByName(RolesEnum.ADMIN));
            user.setRoles(roles);
            userService.saveUser(user);


        }

        if(checkedGuide != null){
            checkedGuide.setPassword(passwordEncoder.encode("guide12345"));
            userService.saveUser(checkedUser);
        }
        else {
            User user1 = new User();
            user1.setFullName("guide");
            user1.setPassword(passwordEncoder.encode("guide12345"));
            user1.setEmail("guide12345@gmail.com");
            Set<Role> roles1 = new HashSet<>();
            roles1.add(roleService.findByName(RolesEnum.GUIDER));
            user1.setRoles(roles1);
            userService.saveUser(user1);
        }
    }
}
