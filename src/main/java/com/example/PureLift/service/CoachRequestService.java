package com.example.PureLift.service;

import com.example.PureLift.dto.CoachRequestDTO;
import com.example.PureLift.entity.CoachRequest;
import com.example.PureLift.entity.User;
import com.example.PureLift.exception.UserNotFoundException;
import com.example.PureLift.repository.CoachRequestRepository;
import com.example.PureLift.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CoachRequestService {
    
    private final CoachRequestRepository coachRequestRepository;
    private final UserRepository userRepository;
    
    public CoachRequestService(CoachRequestRepository coachRequestRepository, UserRepository userRepository) {
        this.coachRequestRepository = coachRequestRepository;
        this.userRepository = userRepository;
    }
    
    @Transactional
    public CoachRequestDTO sendRequest(Long coachId, Long clientId, String message) {
        User coach = userRepository.findById(coachId)
                .orElseThrow(() -> new UserNotFoundException(coachId));
        User client = userRepository.findById(clientId)
                .orElseThrow(() -> new UserNotFoundException(clientId));
        
        // Sprawdź czy nie ma już aktywnego requestu
        coachRequestRepository.findByClientIdAndCoachIdAndStatus(clientId, coachId, CoachRequest.RequestStatus.PENDING)
                .ifPresent(request -> {
                    throw new IllegalArgumentException("Masz już wysłaną prośbę do tego trenera");
                });
        
        CoachRequest request = new CoachRequest();
        request.setCoach(coach);
        request.setClient(client);
        request.setMessage(message);
        request.setStatus(CoachRequest.RequestStatus.PENDING);
        
        CoachRequest saved = coachRequestRepository.save(request);
        return convertToDTO(saved);
    }
    
    public List<CoachRequestDTO> getCoachRequests(Long coachId, String status) {
        List<CoachRequest> requests;
        
        if (status != null) {
            CoachRequest.RequestStatus requestStatus = CoachRequest.RequestStatus.valueOf(status.toUpperCase());
            requests = coachRequestRepository.findByCoachIdAndStatus(coachId, requestStatus);
        } else {
            requests = coachRequestRepository.findByCoachId(coachId);
        }
        
        return requests.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public void acceptRequest(Long requestId, Long coachId) {
        CoachRequest request = coachRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request nie znaleziony"));
        
        if (!request.getCoach().getId().equals(coachId)) {
            throw new IllegalArgumentException("Nie masz uprawnień do tego requestu");
        }
        
        if (request.getStatus() != CoachRequest.RequestStatus.PENDING) {
            throw new IllegalArgumentException("Request już został przetworzony");
        }
        
        // Przypisz klienta do trenera
        User client = request.getClient();
        client.setCoach(request.getCoach());
        userRepository.save(client);
        
        // Zaktualizuj status requestu
        request.setStatus(CoachRequest.RequestStatus.ACCEPTED);
        coachRequestRepository.save(request);
    }
    
    @Transactional
    public void rejectRequest(Long requestId, Long coachId) {
        CoachRequest request = coachRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request nie znaleziony"));
        
        if (!request.getCoach().getId().equals(coachId)) {
            throw new IllegalArgumentException("Nie masz uprawnień do tego requestu");
        }
        
        if (request.getStatus() != CoachRequest.RequestStatus.PENDING) {
            throw new IllegalArgumentException("Request już został przetworzony");
        }
        
        request.setStatus(CoachRequest.RequestStatus.REJECTED);
        coachRequestRepository.save(request);
    }
    
    private CoachRequestDTO convertToDTO(CoachRequest request) {
        CoachRequestDTO dto = new CoachRequestDTO();
        dto.setId(request.getId());
        dto.setClientId(request.getClient().getId());
        dto.setClientName(request.getClient().getName());
        dto.setClientLastname(request.getClient().getLastname());
        dto.setClientEmail(request.getClient().getEmail());
        dto.setCoachId(request.getCoach().getId());
        dto.setCoachName(request.getCoach().getName());
        dto.setMessage(request.getMessage());
        dto.setStatus(request.getStatus().name());
        dto.setCreatedAt(request.getCreatedAt());
        return dto;
    }
}
