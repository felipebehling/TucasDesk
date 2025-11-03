package com.example.Tucasdesk.controller;

import org.springframework.web.bind.annotation.*;

import com.example.Tucasdesk.model.Status;
import com.example.Tucasdesk.repository.StatusRepository;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

/**
 * Controller for handling status-related requests.
 */
@CrossOrigin(origins = "${app.cors.allowed-origins}")
@RestController
@RequestMapping("/api/status")
public class StatusController {

    @Autowired
    private StatusRepository StatusRepository;

    /**
     * Retrieves a list of all statuses.
     *
     * @return A list of all {@link Status} objects.
     */
    @GetMapping
    public List<Status> listarTodos() {
        return StatusRepository.findAll();
    }

    /**
     * Creates a new status.
     *
     * @param Status The {@link Status} object to create, based on the request body.
     * @return The created {@link Status} object.
     */
    @PostMapping
    public Status criar(@RequestBody Status Status) {
        return StatusRepository.save(Status);
    }
}
