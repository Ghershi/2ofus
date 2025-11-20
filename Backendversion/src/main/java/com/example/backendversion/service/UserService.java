package com.example.backendversion.service;

import com.example.backendversion.entity.UserEntity;
import com.example.backendversion.exceptions.EmailAlreadyExistsException;
import com.example.backendversion.exceptions.UserNameAlreadyExistsException;
import com.example.backendversion.repository.ConnectionRepository;
import com.example.backendversion.repository.RequestRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.backendversion.repository.UserRepository;

import java.util.List;
import java.util.Optional;




@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

public List<UserEntity> getallUsers() {
    return userRepository.findAll();

}

    public UserEntity getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    public Optional<UserEntity> findById(int id) {
        return userRepository.findById(id);
    }



    @Transactional
    public UserEntity register(String username, String password, String email, int age) {
        if (userRepository.existsByUsername(username)) {
            throw new UserNameAlreadyExistsException("Username existiert bereits");
        }

        if (userRepository.existsByUserEmail(email)) {
            throw new EmailAlreadyExistsException("Es ist bereits ein*e User*in mit dieser E-Mail registriert.");
        }

        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setUserPassword(passwordEncoder.encode(password));
        user.setUserEmail(email);
        user.setUserAge(age);
        user.setRole("user");
        return userRepository.save(user);
    }

    public Optional<UserEntity> authenticate(String username, String password) {
        return userRepository.findByUsername(username)
                .filter(user -> passwordEncoder.matches(password, user.getUserPassword()));
    }
    public UserEntity findByEmail(String email) {
        Optional<UserEntity> optionalUser = userRepository.findByUserEmail(email);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        } else {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
    }
}