package com.example.PureLift.repository;

import com.example.PureLift.entity.CoachRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CoachRequestRepository extends JpaRepository<CoachRequest, Long> {
    List<CoachRequest> findByCoachIdAndStatus(Long coachId, CoachRequest.RequestStatus status);
    List<CoachRequest> findByCoachId(Long coachId);
    Optional<CoachRequest> findByClientIdAndCoachIdAndStatus(Long clientId, Long coachId, CoachRequest.RequestStatus status);
}
