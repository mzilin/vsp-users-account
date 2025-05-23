package com.mariuszilinskas.vsp.users.account.repository;

import com.mariuszilinskas.vsp.users.account.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for managing User entities. Supports standard CRUD operations.
 *
 * @author Marius Zilinskas
 */
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

}
