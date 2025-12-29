package com.example.PureLift.controller;

import com.example.PureLift.dto.ClientDTO;
import com.example.PureLift.dto.CoachDTO;
import com.example.PureLift.dto.CoachRequestDTO;
import com.example.PureLift.dto.SendCoachRequestDTO;
import com.example.PureLift.entity.User;
import com.example.PureLift.service.CoachRequestService;
import com.example.PureLift.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/coaches")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class CoachController {
    
    private final UserService userService;
    private final CoachRequestService coachRequestService;

    public CoachController(UserService userService, CoachRequestService coachRequestService) {
        this.userService = userService;
        this.coachRequestService = coachRequestService;
    }

    @GetMapping
    public ResponseEntity<List<CoachDTO>> getAllCoaches() {
        List<CoachDTO> coaches = userService.getAllCoaches();
        return ResponseEntity.ok(coaches);
    }

    @GetMapping("/{coachId}/clients")
    public ResponseEntity<List<ClientDTO>> getCoachClients(@PathVariable Long coachId) {
        List<ClientDTO> clients = userService.getCoachClients(coachId);
        return ResponseEntity.ok(clients);
    }

    @PostMapping("/{coachId}/clients")
    public ResponseEntity<Void> assignClientToCoach(
            @PathVariable Long coachId,
            @RequestBody Map<String, Long> body) {
        Long clientId = body.get("clientId");
        userService.assignClientToCoach(coachId, clientId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{coachId}/clients/{clientId}")
    public ResponseEntity<Void> removeClientFromCoach(
            @PathVariable Long coachId,
            @PathVariable Long clientId) {
        userService.removeClientFromCoach(coachId, clientId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{coachId}/join")
    public ResponseEntity<Void> joinCoach(
            @PathVariable Long coachId,
            @AuthenticationPrincipal User currentUser) {
        userService.assignClientToCoach(coachId, currentUser.getId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{coachId}/request")
    public ResponseEntity<CoachRequestDTO> sendCoachRequest(
            @PathVariable Long coachId,
            @RequestBody SendCoachRequestDTO requestDTO,
            @AuthenticationPrincipal User currentUser) {
        CoachRequestDTO result = coachRequestService.sendRequest(coachId, currentUser.getId(), requestDTO.getMessage());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/requests")
    public ResponseEntity<List<CoachRequestDTO>> getMyRequests(
            @AuthenticationPrincipal User currentUser,
            @RequestParam(required = false) String status) {
        List<CoachRequestDTO> requests = coachRequestService.getCoachRequests(currentUser.getId(), status);
        return ResponseEntity.ok(requests);
    }

    @PostMapping("/requests/{requestId}/accept")
    public ResponseEntity<Void> acceptRequest(
            @PathVariable Long requestId,
            @AuthenticationPrincipal User currentUser) {
        coachRequestService.acceptRequest(requestId, currentUser.getId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/requests/{requestId}/reject")
    public ResponseEntity<Void> rejectRequest(
            @PathVariable Long requestId,
            @AuthenticationPrincipal User currentUser) {
        coachRequestService.rejectRequest(requestId, currentUser.getId());
        return ResponseEntity.ok().build();
    }
}
