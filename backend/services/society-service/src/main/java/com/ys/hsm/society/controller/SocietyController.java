package com.ys.hsm.society.controller;

import com.ys.hsm.society.dto.request.SocietyRequest;
import com.ys.hsm.society.dto.response.SocietyResponse;
import com.ys.hsm.society.service.SocietyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/society")
public class SocietyController {

    private final SocietyService societyService;

    @GetMapping("/health")
    public String getHealth(){
        return "application is running";
    }

    @PostMapping("/create-society")
    public ResponseEntity<SocietyResponse> createSociety(@Valid @RequestBody
                                                         SocietyRequest societyRequest){

        SocietyResponse societyResponse = societyService.createSociety(societyRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(societyResponse);
    }

    @PatchMapping("/{societyId}/registration/approve")
    public ResponseEntity<SocietyResponse> approveSociety(
            @PathVariable String societyId) {

        SocietyResponse response =
                societyService.approveSociety(societyId);

        return ResponseEntity.ok(response);
    }


    @PatchMapping("/{societyId}/registration/reject")
    public ResponseEntity<SocietyResponse> rejectSociety(
            @PathVariable String societyId) {

        SocietyResponse response =
                societyService.rejectSociety(societyId);

        return ResponseEntity.ok(response);
    }
}
