package com.example.backendversion.controller;


import com.example.backendversion.dto.UserDTO;
import com.example.backendversion.entity.UserEntity;
import com.example.backendversion.service.ConnectionService;
import com.example.backendversion.service.RequestService;
import com.example.backendversion.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private RequestService requestService;
    @Autowired
    private ConnectionService connectionService;

    @GetMapping("/user/list")
    public List<UserDTO> getUsers() {
        List<UserEntity> userEntityList = userService.getallUsers();

        List<UserDTO> userDTOS = new ArrayList<>();
        for (UserEntity userEntity : userEntityList) {
            UserDTO userDTO = new UserDTO();
            userDTO.setUserId(userEntity.getUserId());
            userDTO.setUsername(userEntity.getUsername());
            userDTO.setUserEmail(userEntity.getUserEmail());
            userDTO.setRole(userEntity.getRole());
            userDTOS.add(userDTO);

        }
        return userDTOS;
    }

}
