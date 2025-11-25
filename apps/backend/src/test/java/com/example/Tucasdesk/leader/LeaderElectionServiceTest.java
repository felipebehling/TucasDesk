package com.example.Tucasdesk.leader;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LeaderElectionServiceTest {

    private LeaderElectionRepository leaderElectionRepository;
    private LeaderElectionService leaderElectionService1;
    private LeaderElectionService leaderElectionService2;
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        leaderElectionRepository = Mockito.mock(LeaderElectionRepository.class);
        restTemplate = Mockito.mock(RestTemplate.class);
        leaderElectionService1 = new LeaderElectionService(leaderElectionRepository, "1");
        leaderElectionService2 = new LeaderElectionService(leaderElectionRepository, "2");
        leaderElectionService1.setRestTemplate(restTemplate);
        leaderElectionService2.setRestTemplate(restTemplate);
        ReflectionTestUtils.setField(leaderElectionService1, "clusterSize", 2);
        ReflectionTestUtils.setField(leaderElectionService2, "clusterSize", 2);
    }

    @Test
    void testHighestInstanceIdBecomesLeader() {
        when(restTemplate.getForObject(eq("http://backend-2:8080/leader/is-leader"), eq(String.class))).thenReturn("I am the leader.");
        leaderElectionService1.startElection();
        assertFalse(leaderElectionService1.isLeader());
        when(restTemplate.getForObject(anyString(), eq(String.class))).thenThrow(new RuntimeException());
        leaderElectionService2.startElection();
        assertTrue(leaderElectionService2.isLeader());
    }

    @Test
    void testFollowerBecomesLeaderWhenLeaderFails() {
        LeaderElection leader = new LeaderElection();
        leader.setServiceId("2");
        leader.setLastHeartbeat(LocalDateTime.now().minusMinutes(1));
        when(leaderElectionRepository.findFirstByOrderByLastHeartbeatDesc()).thenReturn(Optional.of(leader));
        when(restTemplate.getForObject(anyString(), eq(String.class))).thenThrow(new RuntimeException());
        leaderElectionService1.startElection();
        assertTrue(leaderElectionService1.isLeader());
    }

    @Test
    void testHigherInstancePreventsElection() {
        // Instance 2 is the leader
        when(restTemplate.getForObject(anyString(), eq(String.class))).thenThrow(new RuntimeException());
        leaderElectionService2.startElection();
        assertTrue(leaderElectionService2.isLeader());

        // Instance 1 tries to become leader, but can't because instance 2 is up
        when(restTemplate.getForObject(eq("http://backend-2:8080/leader/is-leader"), eq(String.class))).thenReturn("I am the leader.");
        leaderElectionService1.startElection();
        assertFalse(leaderElectionService1.isLeader());
    }

    @Test
    void testFormerLeaderBecomesFollower() {
        // Instance 2 is the leader
        when(restTemplate.getForObject(anyString(), eq(String.class))).thenThrow(new RuntimeException());
        leaderElectionService2.startElection();
        assertTrue(leaderElectionService2.isLeader());

        // Instance 2 fails
        when(restTemplate.getForObject(eq("http://backend-2:8080/leader/is-leader"), eq(String.class))).thenThrow(new RuntimeException());
        LeaderElection leader = new LeaderElection();
        leader.setServiceId("2");
        leader.setLastHeartbeat(LocalDateTime.now().minusMinutes(1));
        when(leaderElectionRepository.findFirstByOrderByLastHeartbeatDesc()).thenReturn(Optional.of(leader));

        // Instance 1 becomes the leader
        leaderElectionService1.startElection();
        assertTrue(leaderElectionService1.isLeader());

        // Instance 2 comes back online
        leader.setServiceId("1");
        leader.setLastHeartbeat(LocalDateTime.now());
        when(leaderElectionRepository.findFirstByOrderByLastHeartbeatDesc()).thenReturn(Optional.of(leader));
        leaderElectionService2.startElection();
        assertFalse(leaderElectionService2.isLeader());
    }

    @Test
    void testHeartbeat() {
        leaderElectionService1.becomeLeader();
        assertTrue(leaderElectionService1.isLeader());
        LeaderElection leader = new LeaderElection();
        leader.setServiceId("1");
        leader.setLastHeartbeat(LocalDateTime.now());
        when(leaderElectionRepository.findFirstByOrderByLastHeartbeatDesc()).thenReturn(Optional.of(leader));
        leaderElectionService1.sendHeartbeat();
        verify(leaderElectionRepository, times(1)).save(leader);
    }
}
