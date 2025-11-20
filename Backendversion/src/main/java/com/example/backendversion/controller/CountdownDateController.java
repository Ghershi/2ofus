package com.example.backendversion.controller;

import com.example.backendversion.dto.CountdownDateDTO;
import com.example.backendversion.entity.CountdownDateEntity;
import com.example.backendversion.repository.CountdownDateRepository;
import com.example.backendversion.service.CountdownService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("api/countdown")
public class CountdownDateController {

    @Autowired
private CountdownService countdownService;
    @Autowired
    private CountdownDateRepository countdownDateRepository;

    @PostMapping("/save")
    public void saveCountdownDate(@RequestBody CountdownDateDTO dto) {
        if (dto.getCountdownDate() != null) {
            countdownService.saveCountdownDate(dto.getCountdownDate());
        }
    }

    @GetMapping("/get")
    public CountdownDateDTO getCountdownDate() {
        LocalDate date = countdownService.getCountdownDate();
        CountdownDateDTO dto = new CountdownDateDTO();
        dto.setCountdownDate(date);  // Kann null sein
        return dto;
    }
    }