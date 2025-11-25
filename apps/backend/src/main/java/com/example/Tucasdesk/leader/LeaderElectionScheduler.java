package com.example.Tucasdesk.leader;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class LeaderElectionScheduler {

    private final LeaderElectionService leaderElectionService;
    private final LeaderElectionRepository leaderElectionRepository;

    public LeaderElectionScheduler(LeaderElectionService leaderElectionService, LeaderElectionRepository leaderElectionRepository) {
        this.leaderElectionService = leaderElectionService;
        this.leaderElectionRepository = leaderElectionRepository;
    }

    @Scheduled(fixedDelay = 10000) // 10 seconds
    public void runElection() {
        if (!leaderElectionService.isLeader()) {
            leaderElectionRepository.findFirstByOrderByLastHeartbeatDesc().ifPresentOrElse(leader -> {
                if (leader.getLastHeartbeat().isBefore(java.time.LocalDateTime.now().minusSeconds(30))) {
                    leaderElectionService.startElection();
                }
            }, () -> leaderElectionService.startElection());
        }
    }

    @Scheduled(fixedDelay = 5000) // 5 seconds
    public void sendHeartbeat() {
        leaderElectionService.sendHeartbeat();
    }
}
