package com.example.backendversion.service;

import com.example.backendversion.dto.RequestDTO;
import com.example.backendversion.entity.RequestEntity;
import com.example.backendversion.entity.UserEntity;
import com.example.backendversion.repository.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class RequestService {
    @Autowired
    private RequestRepository requestRepository;
    @Autowired
    private ConnectionService connectionService;

    public ResponseEntity<String> createRequest(UserEntity sender, UserEntity receiver) throws RuntimeException {
        RequestEntity req = new RequestEntity();
        req.setSender(sender);
        req.setReceiver(receiver);
        req.setStatus(RequestEntity.Status.PENDING);
        req.setCreatedAt(LocalDateTime.now());
        try {
            requestRepository.save(req);
        }catch (Exception e){
            throw new RuntimeException("Einladung konnte nicht erstellt werden." + e.getMessage());
        }
        return ResponseEntity.ok().body("Einladung wurde an " + receiver.getUsername() + " versendet.");
    }

    public boolean requestWithSameSenderAndReceiverExists(UserEntity sender, UserEntity receiver) {
        if(requestRepository.findBySenderAndReceiver(sender, receiver).isEmpty()){
            return false;
        }else{
            return true;
        }
    }



    public List<RequestDTO> getPendingReceivedRequests(UserEntity receiver) {
        List<RequestEntity> receivedRequestEntities = requestRepository.findByReceiverAndStatus(receiver, RequestEntity.Status.PENDING);
        List<RequestDTO> receivedRequestDTOs = new ArrayList<>();
        for (RequestEntity requestEntity : receivedRequestEntities) {
            RequestDTO requestDTO = new RequestDTO(requestEntity);
            receivedRequestDTOs.add(requestDTO);
        }
        return receivedRequestDTOs;
    }

    public List<RequestDTO> getSentRequests(UserEntity sender) {
        List<RequestEntity> sentRequestEntities = requestRepository.findBySender(sender);
        List<RequestDTO> sentRequestDTOs = new ArrayList<>();
        for (RequestEntity requestEntity : sentRequestEntities) {
            RequestDTO requestDTO = new RequestDTO(requestEntity);
            sentRequestDTOs.add(requestDTO);
        }
        return sentRequestDTOs;
    }

    public List<RequestDTO> getAllRequests(UserEntity currentUser) {
        List<RequestDTO> sentRequests = getSentRequests(currentUser);
        List<RequestDTO> receivedRequests = getPendingReceivedRequests(currentUser);
        List<RequestDTO> requestDTOs = new ArrayList<>();
        requestDTOs.addAll(sentRequests);
        requestDTOs.addAll(receivedRequests);
        return requestDTOs;
    }




    public void acceptRequest(int requestId) {
        requestRepository.findById(requestId).ifPresent(req -> {
            req.setStatus(RequestEntity.Status.ACCEPTED);
            connectionService.createConnection(req);
            requestRepository.save(req);
        });
    }

    public void declineRequest(int requestId) {
        requestRepository.findById(requestId).ifPresent(req -> {
            req.setStatus(RequestEntity.Status.DECLINED);
            requestRepository.save(req);
        });
    }
}