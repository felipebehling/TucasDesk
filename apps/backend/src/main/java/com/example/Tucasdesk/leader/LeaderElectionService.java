package com.example.Tucasdesk.leader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class LeaderElectionService {

    private static final Logger logger = LoggerFactory.getLogger(LeaderElectionService.class);

    private final LeaderElectionRepository leaderElectionRepository;
    private final String instanceId;
    private final int instanceIdInt;
    private boolean isLeader = false;
    private RestTemplate restTemplate;

    @Value("${app.cluster.size}")
    private int clusterSize;

    public LeaderElectionService(LeaderElectionRepository leaderElectionRepository, @Value("${INSTANCE_ID}") String instanceId) {
        this.leaderElectionRepository = leaderElectionRepository;
        this.instanceId = instanceId;
        this.instanceIdInt = Integer.parseInt(instanceId);
        this.restTemplate = new RestTemplate();
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Transactional
    public void startElection() {
        Optional<LeaderElection> currentLeader = leaderElectionRepository.findFirstByOrderByLastHeartbeatDesc();
        if (currentLeader.isPresent() && currentLeader.get().getLastHeartbeat().isAfter(LocalDateTime.now().minusSeconds(30))) {
            logger.info("Election stopped. Active leader is still present.");
            isLeader = false;
            return;
        }

        logger.info("Instance {} is starting an election.", instanceId);
        boolean higherInstanceExists = false;
        for (int i = instanceIdInt + 1; i <= clusterSize; i++) {
            try {
                String url = "http://backend-" + i + ":8080/leader/is-leader";
                restTemplate.getForObject(url, String.class);
                higherInstanceExists = true;
                break;
            } catch (Exception e) {
                // Instance is down
            }
        }

        if (!higherInstanceExists) {
            becomeLeader();
        } else {
            isLeader = false;
        }
    }

    @Transactional
    public void sendHeartbeat() {
        if (isLeader) {
            Optional<LeaderElection> currentLeader = leaderElectionRepository.findFirstByOrderByLastHeartbeatDesc();
            if (currentLeader.isPresent() && currentLeader.get().getServiceId().equals(instanceId)) {
                currentLeader.get().setLastHeartbeat(LocalDateTime.now());
                leaderElectionRepository.save(currentLeader.get());
                logger.info("Instance {} sent a heartbeat.", instanceId);
            } else {
                isLeader = false;
                startElection();
            }
        }
    }

    @Transactional
    public void becomeLeader() {
        try {
            leaderElectionRepository.deleteAllInBatch();
            LeaderElection newLeader = new LeaderElection();
            newLeader.setServiceId(instanceId);
            newLeader.setLastHeartbeat(LocalDateTime.now());
            leaderElectionRepository.save(newLeader);
            isLeader = true;
            logger.info("Instance {} is the new leader.", instanceId);
        } catch (Exception e) {
            logger.error("Error while becoming leader", e);
        }
    }

    public boolean isLeader() {
        return isLeader;
    }
}
