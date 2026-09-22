package com.ys.hsm.complaint.controller;

import com.ys.hsm.complaint.dto.request.AssignComplaintRequest;
import com.ys.hsm.complaint.dto.request.ComplaintRequest;
import com.ys.hsm.complaint.dto.request.RejectComplaintRequest;
import com.ys.hsm.complaint.dto.response.ComplaintResponse;
import com.ys.hsm.complaint.entity.ComplaintHistory;
import com.ys.hsm.complaint.enums.ComplaintCategory;
import com.ys.hsm.complaint.enums.ComplaintStatus;
import com.ys.hsm.complaint.service.ComplaintService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/complaints")
@RequiredArgsConstructor
public class ComplaintController {

    private final ComplaintService complaintService;

    @PostMapping("/create")
    public ResponseEntity<ComplaintResponse> createComplaint(
            @Valid @RequestBody ComplaintRequest request,
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestHeader("X-Resident-Id") String residentId) {

        ComplaintResponse response =
                complaintService.createComplaint(
                        request,
                        residentId,
                        authorizationHeader
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{complaintNumber}")
    public ResponseEntity<ComplaintResponse> getComplaintByNumber(
            @PathVariable String complaintNumber) {

        ComplaintResponse response =
                complaintService.getComplaintByNumber(complaintNumber);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/resident/{residentId}")
    public ResponseEntity<List<ComplaintResponse>> getComplaintsByResident(
            @PathVariable String residentId) {

        List<ComplaintResponse> response =
                complaintService.getComplaintsByResident(residentId);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{complaintNumber}/assign")
    public ResponseEntity<ComplaintResponse> assignComplaint(
            @PathVariable String complaintNumber,
            @Valid @RequestBody AssignComplaintRequest request) {

        ComplaintResponse response =
                complaintService.assignComplaint(
                        complaintNumber,
                        request.getAssignedTo()
                );

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{complaintNumber}/start")
    public ResponseEntity<ComplaintResponse> startComplaint(
            @PathVariable String complaintNumber) {

        ComplaintResponse response =
                complaintService.startComplaint(complaintNumber);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{complaintNumber}/resolve")
    public ResponseEntity<ComplaintResponse> resolveComplaint(
            @PathVariable String complaintNumber) {

        ComplaintResponse response =
                complaintService.resolveComplaint(complaintNumber);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{complaintNumber}/close")
    public ResponseEntity<ComplaintResponse> closeComplaint(
            @PathVariable String complaintNumber) {

        ComplaintResponse response =
                complaintService.closeComplaint(complaintNumber);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{complaintNumber}/reject")
    public ResponseEntity<ComplaintResponse> rejectComplaint(
            @PathVariable String complaintNumber,
            @Valid @RequestBody RejectComplaintRequest request) {

        ComplaintResponse response =
                complaintService.rejectComplaint(
                        complaintNumber,
                        request.getReason()
                );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{complaintNumber}/history")
    public ResponseEntity<List<ComplaintHistory>> getComplaintHistory(
            @PathVariable String complaintNumber) {

        List<ComplaintHistory> history =
                complaintService.getComplaintHistory(complaintNumber);

        return ResponseEntity.ok(history);
    }

    @GetMapping
    public ResponseEntity<Page<ComplaintResponse>> getAllComplaints(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) ComplaintStatus status,
            @RequestParam(required = false) ComplaintCategory category,
            @RequestParam(required = false) String residentId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fromDate,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate toDate) {

        LocalDateTime fromDateTime =
                fromDate != null
                        ? fromDate.atStartOfDay()
                        : null;

        LocalDateTime toDateTime =
                toDate != null
                        ? toDate.plusDays(1).atStartOfDay().minusNanos(1)
                        : null;

        Page<ComplaintResponse> response =
                complaintService.getAllComplaints(
                        page,
                        size,
                        status,
                        category,
                        residentId,
                        fromDateTime,
                        toDateTime
                );

        return ResponseEntity.ok(response);
    }
}