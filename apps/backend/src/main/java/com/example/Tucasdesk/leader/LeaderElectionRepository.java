package com.example.Tucasdesk.leader;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LeaderElectionRepository extends JpaRepository<LeaderElection, Long> {
    Optional<LeaderElection> findFirstByOrderByLastHeartbeatDesc();
}
