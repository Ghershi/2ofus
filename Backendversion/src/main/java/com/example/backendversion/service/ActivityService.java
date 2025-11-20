package com.example.backendversion.service;

import com.example.backendversion.dto.ActivityInviteDTO;
import com.example.backendversion.dto.ActivitySubmissionDTO;
import com.example.backendversion.dto.Activity_Status_DTO;
import com.example.backendversion.entity.ActivityInviteEntity;
import com.example.backendversion.entity.ActivitySubmissionEntity;
import com.example.backendversion.entity.ConnectionEntity;
import com.example.backendversion.entity.UserEntity;
import com.example.backendversion.enums.Activity_Status;
import com.example.backendversion.repository.ActivityInviteRepository;
import com.example.backendversion.repository.ActivitySubmissionRepository;
import com.example.backendversion.repository.ConnectionRepository;
import com.example.backendversion.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class ActivityService {

    private final ConnectionRepository connectionRepository;
    private final UserService userService;
    private final ActivityInviteRepository activityInviteRepository;
    private final UserRepository userRepository;
    private final ActivitySubmissionRepository activitySubmissionRepository;

    public ActivityService(ConnectionRepository connectionRepository, UserService userService, ActivityInviteRepository activityInviteRepository, UserRepository userRepository, ActivitySubmissionRepository activitySubmissionRepository) {
        this.connectionRepository = connectionRepository;
        this.userService = userService;
        this.activityInviteRepository = activityInviteRepository;
        this.userRepository = userRepository;
        this.activitySubmissionRepository = activitySubmissionRepository;
    }

    public ResponseEntity<?> createActivityInvite(ActivityInviteDTO activityInviteDTO) {
        Optional<ConnectionEntity> optionalConnection= connectionRepository.findById(activityInviteDTO.getConnection());
        ConnectionEntity connectionEntity = optionalConnection.get();

        UserEntity initiatedBy = userService.getCurrentUser();

        ActivityInviteEntity activityInviteEntity = new ActivityInviteEntity(activityInviteDTO.getActivityId(), connectionEntity, initiatedBy, activityInviteDTO.getComment());

        try {
            activityInviteRepository.save(activityInviteEntity);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Fehler beim Speichern in der Datenbank."));
        }
        return ResponseEntity.ok(Map.of("message", "Activity invite erfolgreich erstellt."));
    }

    public List<ActivityInviteDTO> fetchPendingActivityInvites(Long id, int connectedUserId) {
        ConnectionEntity connection = connectionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Connection not found with id: " + id));

        UserEntity initiatedBy = userRepository.findById(connectedUserId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + connectedUserId));

        List<ActivityInviteEntity> activityInviteEntities = activityInviteRepository.findPendingActivityInvitesByConnectionAndUser(connection, initiatedBy).orElse(new ArrayList<>());
        List<ActivityInviteDTO> activityInviteDTOS = new ArrayList<>();

        for (ActivityInviteEntity activityInviteEntity : activityInviteEntities) {
            ActivityInviteDTO activityInviteDTO = new ActivityInviteDTO(activityInviteEntity);
            activityInviteDTOS.add(activityInviteDTO);
        }

        return activityInviteDTOS;
    }

    public List<ActivityInviteDTO> fetchActiveActivityInvites(Long id) {
        ConnectionEntity connection = connectionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Connection not found with id: " + id));

        LocalDateTime now = LocalDateTime.now();

        List<ActivityInviteEntity> activityInviteEntities = activityInviteRepository.findActiveActivityInvitesByConnection(connection, now)
                .orElse(new ArrayList<>());

        List<ActivityInviteDTO> activityInviteDTOS = new ArrayList<>();

        UserEntity user = userService.getCurrentUser();

        for (ActivityInviteEntity entity : activityInviteEntities) {
            if (checkUserSubmission(user, entity)) {
                continue;
            }
            ActivityInviteDTO activityInviteDTO = new ActivityInviteDTO(entity.getActivityInviteId(), entity.getActivityId(), entity.getDeadline());
            activityInviteDTOS.add(activityInviteDTO);
        }

        return activityInviteDTOS;
    }
    
    private boolean checkUserSubmission(UserEntity user, ActivityInviteEntity activityInvite){
       List<ActivitySubmissionEntity> submissionEntities = activitySubmissionRepository.findByActivityInvite(activityInvite).orElse(new ArrayList<>());
       
       for (ActivitySubmissionEntity submissionEntity : submissionEntities) {
           if (submissionEntity.getUserEntity() == user){
               return true;
           }
       }
       
       return false;
    }

    public ResponseEntity<?> acceptActivityInvite(int ActivityInviteId) {
        ActivityInviteEntity activityInvite = activityInviteRepository.findById(ActivityInviteId).orElseThrow(() -> new EntityNotFoundException("ActivityInvite not found with id: " + ActivityInviteId));
        activityInvite.setStatus(Activity_Status.ACTIVE);
        activityInvite.setStartTime(LocalDateTime.now());
        activityInviteRepository.save(activityInvite);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<?> declineActivityInvite(int ActivityInviteId) {
        ActivityInviteEntity activityInvite = activityInviteRepository.findById(ActivityInviteId).orElseThrow(() -> new EntityNotFoundException("ActivityInvite not found with id: " + ActivityInviteId));
        activityInvite.setStatus(Activity_Status.DECLINED);
        activityInviteRepository.save(activityInvite);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<?> createActivitySubmission(ActivitySubmissionDTO dto) {
        ActivityInviteEntity activityInvite = activityInviteRepository.findById(dto.getActivityInvite())
                .orElseThrow(() -> new EntityNotFoundException("ActivityInvite not found with id: " + dto.getActivityInvite()));
        UserEntity user = userService.getCurrentUser();

        ActivitySubmissionEntity activitySubmission = new ActivitySubmissionEntity(activityInvite, user, dto.getCaption(), dto.getPhotoUrl(), LocalDateTime.now());

            activitySubmissionRepository.save(activitySubmission);

            int submissionCount = activitySubmissionRepository.countByActivityInvite(activityInvite);

        Activity_Status_DTO status_dto = new Activity_Status_DTO(Activity_Status.ACTIVE);

            if(submissionCount == 2){
                activityInvite.setStatus(Activity_Status.COMPLETED);
                activityInviteRepository.save(activityInvite);
                status_dto.setActivityStatus(Activity_Status.COMPLETED);
            }

        return ResponseEntity.ok().body(Map.of("status", status_dto));
    }

    public ResponseEntity<?> getCompletedActivities(Long id) {
        ConnectionEntity connection = connectionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Connection not found with id: " + id));

        List<ActivityInviteEntity> activityInvites = activityInviteRepository.findCompletedActivityInvitesByConnection(connection).orElse(new ArrayList<>());

        List<ActivityInviteDTO> activityInviteDTOS = new ArrayList<>();

        List<ActivitySubmissionEntity> submissionEntityList = new ArrayList<>();

        List<ActivitySubmissionDTO> submissionDTOList = new ArrayList<>();

        for (ActivityInviteEntity activityInvite : activityInvites) {
            List<ActivitySubmissionEntity> entityList = activitySubmissionRepository.findByActivityInvite(activityInvite).orElse(new ArrayList<>());

            for (ActivitySubmissionEntity entity : entityList) {
                submissionEntityList.add(entity);
            }
        }

        for (ActivitySubmissionEntity entity : submissionEntityList) {
            ActivitySubmissionDTO dto = new ActivitySubmissionDTO(entity);
            submissionDTOList.add(dto);
        }

        for (ActivityInviteEntity entity : activityInvites) {
            ActivityInviteDTO activityInviteDTO = new ActivityInviteDTO(entity.getActivityInviteId(), entity.getActivityId());
            activityInviteDTOS.add(activityInviteDTO);
        }

        Map<String, Object> response = new HashMap<>();

        response.put("submissions", submissionDTOList);
        response.put("completedActivities", activityInviteDTOS);



        return ResponseEntity.ok().body(response);
    }
}
