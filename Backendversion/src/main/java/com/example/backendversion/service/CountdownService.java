package com.example.backendversion.service;

import com.example.backendversion.entity.CountdownDateEntity;
import com.example.backendversion.entity.UserEntity;
import com.example.backendversion.repository.CountdownDateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
    public class CountdownService {

        @Autowired
        private CountdownDateRepository countdownDateRepository;

        @Autowired
        private UserService userService;
    public void saveCountdownDate(LocalDate date) {
        UserEntity currentUser = userService.getCurrentUser();
        int userId = currentUser.getUserId();

        Optional<CountdownDateEntity> optionalCountdown = countdownDateRepository.findTopByUserUserIdOrderByIdDesc(userId);

        CountdownDateEntity countdownDateEntity;
        if (optionalCountdown.isPresent()) {
            countdownDateEntity = optionalCountdown.get();
        } else {
            countdownDateEntity = new CountdownDateEntity();
            countdownDateEntity.setUser(currentUser);
        }

        countdownDateEntity.setCountdownDate(date);
        countdownDateRepository.save(countdownDateEntity);
    }

    public LocalDate getCountdownDate() {
        UserEntity currentUser = userService.getCurrentUser();
        int userId = currentUser.getUserId();

        return countdownDateRepository.findTopByUserUserIdOrderByIdDesc(userId)
                .map(CountdownDateEntity::getCountdownDate)
                .orElse(null);
    }
}



