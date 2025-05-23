package com.mariuszilinskas.vsp.users.account.service;

import com.mariuszilinskas.vsp.users.account.dto.UserAdminResponse;
import com.mariuszilinskas.vsp.users.account.enums.UserAuthority;
import com.mariuszilinskas.vsp.users.account.enums.UserRole;
import com.mariuszilinskas.vsp.users.account.enums.UserStatus;
import com.mariuszilinskas.vsp.users.account.exception.ResourceNotFoundException;
import com.mariuszilinskas.vsp.users.account.model.User;
import com.mariuszilinskas.vsp.users.account.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Service implementation for managing user accounts, accessible only by system admins.
 * This service handles user roles, authorities, and suspension.
 *
 * @author Marius Zilinskas
 */
@Service
@RequiredArgsConstructor
public class UserAdminServiceImp implements UserAdminService {

    private static final Logger logger = LoggerFactory.getLogger(UserAdminServiceImp.class);
    private final UserRepository userRepository;

    @Override
    public List<UserAdminResponse> getUsers() {
        logger.info("Getting all platform Users");
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(UserAdminServiceImp::mapToUserAdminResponse)
                .toList();
    }

    private static UserAdminResponse mapToUserAdminResponse(User user) {
        return new UserAdminResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getCountry(),
                user.isEmailVerified(),
                user.getStatus().name(),
                user.getRoles(),
                user.getAuthorities(),
                user.getCreatedAt(),
                user.getLastActive()
        );
    }

    @Override
    @Transactional
    public void grantUserRole(UUID userId, UserRole userRole) {
        logger.info("Granting '{}' Role for User [id: '{}']", userRole, userId);

        User user = findUserById(userId);
        List<UserRole> roles = new ArrayList<>(user.getRoles());

        if (!roles.contains(userRole)) {
            roles.add(userRole);
            user.setRoles(roles);
            userRepository.save(user);
        }
    }

    @Override
    @Transactional
    public void removeUserRole(UUID userId, UserRole userRole) {
        logger.info("Removing '{}' Role for User [id: '{}']", userRole, userId);

        User user = findUserById(userId);
        List<UserRole> roles = user.getRoles();

        if (roles.contains(userRole)) {
            roles.remove(userRole);
            user.setRoles(roles);
            userRepository.save(user);
        }
    }

    @Override
    @Transactional
    public void grantUserAuthority(UUID userId, UserAuthority authority) {
        logger.info("Granting '{}' Authority for User [id: '{}']", authority, userId);

        User user = findUserById(userId);
        List<UserAuthority> authorities = new ArrayList<>(user.getAuthorities());

        if (!authorities.contains(authority)) {
            authorities.add(authority);
            user.setAuthorities(authorities);
            userRepository.save(user);
        }
    }

    @Override
    @Transactional
    public void removeUserAuthority(UUID userId, UserAuthority authority) {
        logger.info("Removing '{}' Authority for User [id: '{}']", authority, userId);

        User user = findUserById(userId);
        List<UserAuthority> authorities = new ArrayList<>(user.getAuthorities());

        if (authorities.contains(authority)) {
            authorities.remove(authority);
            user.setAuthorities(authorities);
            userRepository.save(user);
        }
    }

    @Override
    @Transactional
    public void updateUserStatus(UUID userId, UserStatus status) {
        logger.info("Setting status '{}' for User [id: '{}']", status, userId);
        User user = findUserById(userId);
        user.setStatus(status);
        userRepository.save(user);
    }

    private User findUserById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(User.class, "id", userId));
    }

}
