package com.example.backendversion.service;

import com.example.backendversion.repository.ConnectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmotionService {

    @Autowired
    ConnectionRepository connectionRepository;


}
