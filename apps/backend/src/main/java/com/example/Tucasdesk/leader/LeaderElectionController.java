package com.example.Tucasdesk.leader;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/leader")
public class LeaderElectionController {

    private final LeaderElectionService leaderElectionService;

    public LeaderElectionController(LeaderElectionService leaderElectionService) {
        this.leaderElectionService = leaderElectionService;
    }

    @GetMapping("/is-leader")
    public ResponseEntity<String> isLeader() {
        if (leaderElectionService.isLeader()) {
            return ResponseEntity.ok("I am the leader.");
        } else {
            return ResponseEntity.ok("I am a follower.");
        }
    }
}
