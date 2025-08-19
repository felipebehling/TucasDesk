package com.example.Tucasdesk.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Tucasdesk.model.Status;

@Repository
public interface StatusRepository extends JpaRepository<Status, Integer> {
    List<Status> findAll();

    Status save(Status Status);
}
