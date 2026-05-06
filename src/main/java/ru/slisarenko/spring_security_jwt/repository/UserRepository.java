package ru.slisarenko.spring_security_jwt.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.slisarenko.spring_security_jwt.model.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);

    boolean existsByUsername(String username);

    void deleteByUsername(String username);

    int countAllByEnabled(boolean enabled);

    @Modifying
    @Transactional
    @Query("UPDATE UserEntity u SET u.failedAttempts = 0, u.isAccountNonLocked = true, u.lockTime = null WHERE u.username = :username")
    void unlockAccount(String username);
}
