package com.example.backendversion.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/images")
public class ImageUploadController {

    private static final String UPLOAD_DIR = "images";

    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile file) throws IOException {
        // Eindeutigen Dateinamen generieren (z.B. mit Zeitstempel)
        String originalFilename = file.getOriginalFilename();
        String filename = System.currentTimeMillis() + "_" + originalFilename;
        Path filePath = Paths.get(UPLOAD_DIR, filename);

        // Datei speichern
        Files.copy(file.getInputStream(), filePath);

        // Rückgabe: URL, unter der das Bild erreichbar ist
        String imageUrl = "http://localhost:8080/images/" + filename;
        return ResponseEntity.ok().body(Map.of("url", imageUrl));
    }

}
