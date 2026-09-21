package com.ys.hsm.resident.controller;

import com.ys.hsm.resident.dto.request.ResidentRequest;
import com.ys.hsm.resident.entity.Resident;
import com.ys.hsm.resident.service.ResidentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/residents")
@RequiredArgsConstructor
public class ResidentController {
    private final ResidentService residentService;

    @PostMapping("/register")
    public ResponseEntity<Resident> registerResident(
            @Valid @RequestBody ResidentRequest request,
            @RequestHeader("Authorization") String authorizationHeader) {

        Resident resident = residentService.registerResident(request, authorizationHeader);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(resident);
    }
}
