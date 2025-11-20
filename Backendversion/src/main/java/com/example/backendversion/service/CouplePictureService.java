package com.example.backendversion.service;

import com.example.backendversion.dto.CouplePictureDTO;
import com.example.backendversion.entity.CouplePictureEntity;
import com.example.backendversion.entity.UserEntity;
import com.example.backendversion.repository.CouplePictureRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
public class CouplePictureService {

    private final UserService userService;
    private final CouplePictureRepository couplePictureRepository;

    public CouplePictureService(UserService userService, CouplePictureRepository couplePictureRepository) {
        this.userService = userService;
        this.couplePictureRepository = couplePictureRepository;
    }

    public ResponseEntity<?> saveCouplePicture(@RequestBody CouplePictureDTO couplePictureDTO) {
        UserEntity currentUser = userService.getCurrentUser();
        LocalDateTime createdDate = LocalDateTime.now();

        CouplePictureEntity couplePictureEntity = new CouplePictureEntity(couplePictureDTO.getPhotoUrl(), createdDate, currentUser);

        try {
            couplePictureRepository.save(couplePictureEntity);
            return ResponseEntity.ok().body("Picture saved successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Problem saving couple picture.");
        }
    }

    public ResponseEntity<?> getCouplePictureByUserId () {
        UserEntity currentUser = userService.getCurrentUser();

        Optional<CouplePictureEntity> entity = couplePictureRepository.findFirstByUserOrderByCreatedDateDesc(currentUser);
        if (entity.isPresent()) {
            String photoUrl = entity.get().getPhotoUrl();
            return ResponseEntity.ok().body(Map.of("photoUrl", photoUrl));
        }

        return ResponseEntity.notFound().build();
    }

}
