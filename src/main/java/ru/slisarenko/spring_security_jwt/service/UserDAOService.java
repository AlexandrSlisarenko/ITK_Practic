package ru.slisarenko.spring_security_jwt.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.slisarenko.spring_security_jwt.model.User;
import ru.slisarenko.spring_security_jwt.repository.UserRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class UserDAOService {
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username) {
        return this.userRepository.findByUsername(username);
    }

    @Transactional(readOnly = true)
    public User loadUserByUsername(String username) {
        var userFromDB = this.findByUsername(username);
        if (userFromDB.isPresent()) {
            return userFromDB.get();
        } else {
            throw new UsernameNotFoundException(username);
        }
    }

    public void unlockAccount(String username){
        this.userRepository.unlockAccount(username);
    }

    public void saveUser(User user) {
        this.userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public boolean isUserAccountNonLocked(String username) {
        return userRepository.findByUsername(username)
                .map(User::isAccountNonLocked)
                .orElse(false);
    }
    @Transactional(readOnly = true)
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Transactional(readOnly = true)
    public Page<User> findAll(int page, int size) {
        var pageable = PageRequest.of(page, size);
        var context = userRepository.findAll(pageable);
        return new PageImpl<>(context.getContent(), pageable, context.getTotalElements());
    }

    public void deleteUser(String username) {
        if(this.userRepository.existsByUsername(username)){
            this.userRepository.deleteByUsername(username);
        } else {
            throw new UsernameNotFoundException(username);
        }
    }

    @Transactional(readOnly = true)
    public int countEnabledAccounts() {
        return this.userRepository.countAllByEnabled(true);
    }
}
