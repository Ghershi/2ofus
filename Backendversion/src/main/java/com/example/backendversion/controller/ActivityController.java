package com.example.backendversion.controller;

import com.example.backendversion.dto.ActivityInviteDTO;
import com.example.backendversion.dto.ActivitySubmissionDTO;
import com.example.backendversion.dto.Activity_Status_DTO;
import com.example.backendversion.enums.Activity_Status;
import com.example.backendversion.service.ActivityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/activity")
public class ActivityController {

    private final ActivityService activityService;

    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    @PostMapping("/invite")
    public ResponseEntity<?> inviteToActivity(@RequestBody ActivityInviteDTO activityInviteDTO){
        return activityService.createActivityInvite(activityInviteDTO);
    }

    @GetMapping("/pending&active")
    public ResponseEntity<?> getPendingInvites(@RequestParam("connectionid") Long connectionid, @RequestParam("userid") int userid){
        Map<String, List<ActivityInviteDTO>> response = new HashMap<>();
        response.put("pendingActivityInvites", activityService.fetchPendingActivityInvites(connectionid, userid));
        response.put("activeActivityInvites", activityService.fetchActiveActivityInvites(connectionid));

        return ResponseEntity.ok(response);
    }

    @PostMapping("/response")
    public ResponseEntity<?> respondToInvite(
            @RequestParam int activityInviteId,
            @RequestBody Activity_Status_DTO dto) {
        if (dto.getActivityStatus() == Activity_Status.ACTIVE) {
            return activityService.acceptActivityInvite(activityInviteId);
        } else if (dto.getActivityStatus() == Activity_Status.DECLINED) {
            return activityService.declineActivityInvite(activityInviteId);
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("submission")
    public ResponseEntity<?> submitActivity(
            @RequestBody ActivitySubmissionDTO dto) {
        return activityService.createActivitySubmission(dto);
    }

    @GetMapping("/listcompleted")
    public ResponseEntity<?> getCompletedActivities(@RequestParam("connectionid") Long connectionid){
        return activityService.getCompletedActivities(connectionid);}


}
