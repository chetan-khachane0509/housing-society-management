package com.ys.hsm.complaint.service.impl;

import com.ys.hsm.complaint.client.ResidentClient;
import com.ys.hsm.complaint.dto.request.ComplaintRequest;
import com.ys.hsm.complaint.dto.resident.ResidentDetailsResponse;
import com.ys.hsm.complaint.dto.response.ComplaintResponse;
import com.ys.hsm.complaint.entity.Complaint;
import com.ys.hsm.complaint.entity.ComplaintHistory;
import com.ys.hsm.complaint.enums.ComplaintCategory;
import com.ys.hsm.complaint.enums.ComplaintStatus;
import com.ys.hsm.complaint.repository.ComplaintHistoryRepository;
import com.ys.hsm.complaint.repository.ComplaintRepository;
import com.ys.hsm.complaint.service.ComplaintNumberGenerator;
import com.ys.hsm.complaint.service.ComplaintService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ComplaintServiceImpl implements ComplaintService {

    private final ComplaintRepository complaintRepository;
    private final ComplaintNumberGenerator complaintNumberGenerator;
    private final ResidentClient residentClient;
    private final ComplaintHistoryRepository complaintHistoryRepository;

    @Override
    public ComplaintResponse createComplaint(
            ComplaintRequest request,
            String residentId,
            String authorizationHeader) {

        ResidentDetailsResponse resident =
                residentClient.getResidentById(
                        residentId,
                        authorizationHeader
                );

        String complaintNumber =
                complaintNumberGenerator.generateComplaintNumber();

        LocalDateTime now = LocalDateTime.now();

        String residentName =
                resident.getFirstName() + " " + resident.getLastName();

        Complaint complaint = Complaint.builder()
                .complaintNumber(complaintNumber)

                .residentId(resident.getId())
                .residentName(residentName)
                .residentEmail(resident.getEmail())
                .residentMobile(resident.getMobile())

                .societyId(resident.getSocietyId())
                .wingId(resident.getWingId())
                .flatId(resident.getFlatId())

                .category(request.getCategory())
                .title(request.getTitle())
                .description(request.getDescription())

                .status(ComplaintStatus.OPEN)

                .createdAt(now)
                .updatedAt(now)
                .build();

        Complaint savedComplaint =
                complaintRepository.save(complaint);

        return ComplaintResponse.builder()
                .complaintNumber(savedComplaint.getComplaintNumber())

                .residentName(savedComplaint.getResidentName())
                .residentEmail(savedComplaint.getResidentEmail())
                .residentMobile(savedComplaint.getResidentMobile())

                .societyId(savedComplaint.getSocietyId())
                .wingId(savedComplaint.getWingId())
                .flatId(savedComplaint.getFlatId())

                .category(savedComplaint.getCategory())
                .title(savedComplaint.getTitle())
                .description(savedComplaint.getDescription())

                .status(savedComplaint.getStatus())

                .createdAt(savedComplaint.getCreatedAt())
                .updatedAt(savedComplaint.getUpdatedAt())

                .build();
    }

    @Override
    public ComplaintResponse getComplaintByNumber(String complaintNumber) {

        Complaint complaint = complaintRepository
                .findByComplaintNumber(complaintNumber)
                .orElseThrow(() -> new RuntimeException(
                        "Complaint not found with complaint number: " + complaintNumber
                ));

        return ComplaintResponse.builder()
                .complaintNumber(complaint.getComplaintNumber())
                .residentName(complaint.getResidentName())
                .residentEmail(complaint.getResidentEmail())
                .residentMobile(complaint.getResidentMobile())
                .societyId(complaint.getSocietyId())
                .wingId(complaint.getWingId())
                .flatId(complaint.getFlatId())
                .category(complaint.getCategory())
                .title(complaint.getTitle())
                .description(complaint.getDescription())
                .status(complaint.getStatus())
                .createdAt(complaint.getCreatedAt())
                .updatedAt(complaint.getUpdatedAt())
                .build();
    }

    @Override
    public List<ComplaintResponse> getComplaintsByResident(String residentId) {

        List<Complaint> complaints =
                complaintRepository.findByResidentId(residentId);

        return complaints.stream()
                .map(complaint -> ComplaintResponse.builder()
                        .complaintNumber(complaint.getComplaintNumber())
                        .residentName(complaint.getResidentName())
                        .residentEmail(complaint.getResidentEmail())
                        .residentMobile(complaint.getResidentMobile())
                        .societyId(complaint.getSocietyId())
                        .wingId(complaint.getWingId())
                        .flatId(complaint.getFlatId())
                        .category(complaint.getCategory())
                        .title(complaint.getTitle())
                        .description(complaint.getDescription())
                        .status(complaint.getStatus())
                        .createdAt(complaint.getCreatedAt())
                        .updatedAt(complaint.getUpdatedAt())
                        .build())
                .toList();
    }

    @Override
    public ComplaintResponse assignComplaint(
            String complaintNumber,
            String assignedTo) {

        Complaint complaint = complaintRepository
                .findByComplaintNumber(complaintNumber)
                .orElseThrow(() -> new RuntimeException(
                        "Complaint not found with complaint number: "
                                + complaintNumber
                ));

        if (complaint.getStatus() != ComplaintStatus.OPEN) {
            throw new RuntimeException(
                    "Complaint can only be assigned when status is OPEN"
            );
        }

        ComplaintStatus previousStatus = complaint.getStatus();
        complaint.setAssignedTo(assignedTo);
        complaint.setAssignedAt(LocalDateTime.now());
        complaint.setStatus(ComplaintStatus.ASSIGNED);
        complaint.setUpdatedAt(LocalDateTime.now());

        saveComplaintHistory(
                complaint.getComplaintNumber(),
                previousStatus,
                ComplaintStatus.ASSIGNED,
                "ASSIGNED",
                "Complaint assigned to " + assignedTo
        );

        Complaint updatedComplaint =
                complaintRepository.save(complaint);

        return ComplaintResponse.builder()
                .complaintNumber(updatedComplaint.getComplaintNumber())
                .residentName(updatedComplaint.getResidentName())
                .residentEmail(updatedComplaint.getResidentEmail())
                .residentMobile(updatedComplaint.getResidentMobile())
                .societyId(updatedComplaint.getSocietyId())
                .wingId(updatedComplaint.getWingId())
                .flatId(updatedComplaint.getFlatId())
                .category(updatedComplaint.getCategory())
                .title(updatedComplaint.getTitle())
                .description(updatedComplaint.getDescription())
                .status(updatedComplaint.getStatus())
                .createdAt(updatedComplaint.getCreatedAt())
                .updatedAt(updatedComplaint.getUpdatedAt())
                .build();
    }

    @Override
    public ComplaintResponse startComplaint(String complaintNumber) {

        Complaint complaint = complaintRepository
                .findByComplaintNumber(complaintNumber)
                .orElseThrow(() -> new RuntimeException(
                        "Complaint not found with complaint number: "
                                + complaintNumber
                ));

        if (complaint.getStatus() != ComplaintStatus.ASSIGNED) {
            throw new RuntimeException(
                    "Complaint can only be started when status is ASSIGNED"
            );
        }

        ComplaintStatus previousStatus = complaint.getStatus();
        complaint.setStatus(ComplaintStatus.IN_PROGRESS);
        complaint.setUpdatedAt(LocalDateTime.now());

        saveComplaintHistory(
                complaint.getComplaintNumber(),
                previousStatus,
                ComplaintStatus.IN_PROGRESS,
                "STARTED",
                "Complaint work started"
        );

        Complaint updatedComplaint =
                complaintRepository.save(complaint);

        return ComplaintResponse.builder()
                .complaintNumber(updatedComplaint.getComplaintNumber())
                .residentName(updatedComplaint.getResidentName())
                .residentEmail(updatedComplaint.getResidentEmail())
                .residentMobile(updatedComplaint.getResidentMobile())
                .societyId(updatedComplaint.getSocietyId())
                .wingId(updatedComplaint.getWingId())
                .flatId(updatedComplaint.getFlatId())
                .category(updatedComplaint.getCategory())
                .title(updatedComplaint.getTitle())
                .description(updatedComplaint.getDescription())
                .status(updatedComplaint.getStatus())
                .createdAt(updatedComplaint.getCreatedAt())
                .updatedAt(updatedComplaint.getUpdatedAt())
                .build();
    }

    @Override
    public ComplaintResponse resolveComplaint(String complaintNumber) {

        Complaint complaint = complaintRepository
                .findByComplaintNumber(complaintNumber)
                .orElseThrow(() -> new RuntimeException(
                        "Complaint not found with complaint number: "
                                + complaintNumber
                ));

        if (complaint.getStatus() != ComplaintStatus.IN_PROGRESS) {
            throw new RuntimeException(
                    "Complaint can only be resolved when status is IN_PROGRESS"
            );
        }

        ComplaintStatus previousStatus = complaint.getStatus();

        complaint.setStatus(ComplaintStatus.RESOLVED);
        complaint.setResolvedAt(LocalDateTime.now());
        complaint.setUpdatedAt(LocalDateTime.now());

        saveComplaintHistory(
                complaint.getComplaintNumber(),
                previousStatus,
                ComplaintStatus.RESOLVED,
                "RESOLVED",
                "Complaint resolved"
        );

        Complaint updatedComplaint =
                complaintRepository.save(complaint);

        return ComplaintResponse.builder()
                .complaintNumber(updatedComplaint.getComplaintNumber())
                .residentName(updatedComplaint.getResidentName())
                .residentEmail(updatedComplaint.getResidentEmail())
                .residentMobile(updatedComplaint.getResidentMobile())
                .societyId(updatedComplaint.getSocietyId())
                .wingId(updatedComplaint.getWingId())
                .flatId(updatedComplaint.getFlatId())
                .category(updatedComplaint.getCategory())
                .title(updatedComplaint.getTitle())
                .description(updatedComplaint.getDescription())
                .status(updatedComplaint.getStatus())
                .createdAt(updatedComplaint.getCreatedAt())
                .updatedAt(updatedComplaint.getUpdatedAt())
                .build();
    }

    @Override
    public ComplaintResponse closeComplaint(String complaintNumber) {

        Complaint complaint = complaintRepository
                .findByComplaintNumber(complaintNumber)
                .orElseThrow(() -> new RuntimeException(
                        "Complaint not found with complaint number: "
                                + complaintNumber
                ));

        if (complaint.getStatus() != ComplaintStatus.RESOLVED) {
            throw new RuntimeException(
                    "Complaint can only be closed when status is RESOLVED"
            );
        }
        ComplaintStatus previousStatus = complaint.getStatus();
        complaint.setStatus(ComplaintStatus.CLOSED);
        complaint.setClosedAt(LocalDateTime.now());
        complaint.setUpdatedAt(LocalDateTime.now());

        saveComplaintHistory(
                complaint.getComplaintNumber(),
                previousStatus,
                ComplaintStatus.CLOSED,
                "CLOSED",
                "Complaint closed"
        );

        Complaint updatedComplaint =
                complaintRepository.save(complaint);

        return ComplaintResponse.builder()
                .complaintNumber(updatedComplaint.getComplaintNumber())
                .residentName(updatedComplaint.getResidentName())
                .residentEmail(updatedComplaint.getResidentEmail())
                .residentMobile(updatedComplaint.getResidentMobile())
                .societyId(updatedComplaint.getSocietyId())
                .wingId(updatedComplaint.getWingId())
                .flatId(updatedComplaint.getFlatId())
                .category(updatedComplaint.getCategory())
                .title(updatedComplaint.getTitle())
                .description(updatedComplaint.getDescription())
                .status(updatedComplaint.getStatus())
                .createdAt(updatedComplaint.getCreatedAt())
                .updatedAt(updatedComplaint.getUpdatedAt())
                .build();
    }

    @Override
    public ComplaintResponse rejectComplaint(
            String complaintNumber,
            String reason) {

        Complaint complaint = complaintRepository
                .findByComplaintNumber(complaintNumber)
                .orElseThrow(() -> new RuntimeException(
                        "Complaint not found with complaint number: "
                                + complaintNumber
                ));

        if (complaint.getStatus() != ComplaintStatus.OPEN) {
            throw new RuntimeException(
                    "Complaint can only be rejected when status is OPEN"
            );
        }

        ComplaintStatus previousStatus = complaint.getStatus();
        complaint.setStatus(ComplaintStatus.REJECTED);
        complaint.setRejectionReason(reason);
        complaint.setUpdatedAt(LocalDateTime.now());

        saveComplaintHistory(
                complaint.getComplaintNumber(),
                previousStatus,
                ComplaintStatus.REJECTED,
                "REJECTED",
                reason
        );

        Complaint updatedComplaint =
                complaintRepository.save(complaint);

        return ComplaintResponse.builder()
                .complaintNumber(updatedComplaint.getComplaintNumber())
                .residentName(updatedComplaint.getResidentName())
                .residentEmail(updatedComplaint.getResidentEmail())
                .residentMobile(updatedComplaint.getResidentMobile())
                .societyId(updatedComplaint.getSocietyId())
                .wingId(updatedComplaint.getWingId())
                .flatId(updatedComplaint.getFlatId())
                .category(updatedComplaint.getCategory())
                .title(updatedComplaint.getTitle())
                .description(updatedComplaint.getDescription())
                .status(updatedComplaint.getStatus())
                .createdAt(updatedComplaint.getCreatedAt())
                .updatedAt(updatedComplaint.getUpdatedAt())
                .build();
    }

    @Override
    public List<ComplaintHistory> getComplaintHistory(
            String complaintNumber) {

        complaintRepository.findByComplaintNumber(complaintNumber)
                .orElseThrow(() -> new RuntimeException(
                        "Complaint not found with complaint number: "
                                + complaintNumber
                ));

        return complaintHistoryRepository
                .findByComplaintNumberOrderByChangedAtAsc(
                        complaintNumber
                );
    }

    @Override
    public Page<ComplaintResponse> getAllComplaints(
            int page,
            int size,
            ComplaintStatus status,
            ComplaintCategory category,
            String residentId,
            LocalDateTime fromDate,
            LocalDateTime toDate) {

        Pageable pageable = PageRequest.of(page, size);

        Page<Complaint> complaints;

        if (fromDate != null && toDate != null) {

            complaints = complaintRepository.findByCreatedAtBetween(
                    fromDate,
                    toDate,
                    pageable
            );

        } else if (status != null && category != null && residentId != null) {

            complaints = complaintRepository
                    .findByStatusAndCategory(
                            status,
                            category,
                            pageable
                    );

        } else if (status != null && residentId != null) {

            complaints = complaintRepository
                    .findByStatusAndResidentId(
                            status,
                            residentId,
                            pageable
                    );

        } else if (category != null && residentId != null) {

            complaints = complaintRepository
                    .findByCategoryAndResidentId(
                            category,
                            residentId,
                            pageable
                    );

        } else if (status != null) {

            complaints = complaintRepository
                    .findByStatus(
                            status,
                            pageable
                    );

        } else if (category != null) {

            complaints = complaintRepository
                    .findByCategory(
                            category,
                            pageable
                    );

        } else if (residentId != null) {

            complaints = complaintRepository
                    .findByResidentId(
                            residentId,
                            pageable
                    );

        } else {

            complaints = complaintRepository.findAll(pageable);
        }

        return complaints.map(complaint ->
                ComplaintResponse.builder()
                        .complaintNumber(complaint.getComplaintNumber())
                        .residentName(complaint.getResidentName())
                        .residentEmail(complaint.getResidentEmail())
                        .residentMobile(complaint.getResidentMobile())
                        .societyId(complaint.getSocietyId())
                        .wingId(complaint.getWingId())
                        .flatId(complaint.getFlatId())
                        .category(complaint.getCategory())
                        .title(complaint.getTitle())
                        .description(complaint.getDescription())
                        .status(complaint.getStatus())
                        .createdAt(complaint.getCreatedAt())
                        .updatedAt(complaint.getUpdatedAt())
                        .build()
        );
    }


        private void saveComplaintHistory (
                String complaintNumber,
                ComplaintStatus previousStatus,
                ComplaintStatus newStatus,
                String action,
                String remarks){

            ComplaintHistory history = ComplaintHistory.builder()
                    .complaintNumber(complaintNumber)
                    .previousStatus(previousStatus)
                    .newStatus(newStatus)
                    .action(action)
                    .remarks(remarks)
                    .changedAt(LocalDateTime.now())
                    .build();

            complaintHistoryRepository.save(history);
        }
}