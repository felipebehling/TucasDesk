package com.example.Tucasdesk.controller;

import org.springframework.web.bind.annotation.*;

import com.example.Tucasdesk.model.Status;
import com.example.Tucasdesk.repository.StatusRepository;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/status")
public class StatusController {

    @Autowired
    private StatusRepository StatusRepository;

    @GetMapping
    public List<Status> listarTodos() {
        return StatusRepository.findAll();
    }

    @PostMapping
    public Status criar(@RequestBody Status Status) {
        return StatusRepository.save(Status);
    }
}
