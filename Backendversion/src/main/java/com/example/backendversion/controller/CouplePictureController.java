package com.example.backendversion.controller;

import com.example.backendversion.dto.CouplePictureDTO;
import com.example.backendversion.service.CouplePictureService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/couplepic")
public class CouplePictureController {

    private final CouplePictureService couplePictureService;

    public CouplePictureController(CouplePictureService couplePictureService) {
        this.couplePictureService = couplePictureService;
    }

    @PostMapping("/save")
    public ResponseEntity<?> createCouplePicture(@RequestBody CouplePictureDTO couplePictureDTO) {
        return couplePictureService.saveCouplePicture(couplePictureDTO);
    }

    @GetMapping("/get")
    public ResponseEntity<?> getCouplePicture () {
        return couplePictureService.getCouplePictureByUserId();
    }

}
