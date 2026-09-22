package com.ys.hsm.complaint.service;

import com.ys.hsm.complaint.dto.request.ComplaintRequest;
import com.ys.hsm.complaint.dto.response.ComplaintResponse;
import com.ys.hsm.complaint.entity.ComplaintHistory;
import com.ys.hsm.complaint.enums.ComplaintCategory;
import com.ys.hsm.complaint.enums.ComplaintStatus;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

public interface ComplaintService {
    ComplaintResponse createComplaint(
            ComplaintRequest request,
            String residentId,
            String authorizationHeader
    );

    ComplaintResponse getComplaintByNumber(String complaintNumber);

    List<ComplaintResponse> getComplaintsByResident(String residentId);

    ComplaintResponse assignComplaint(
            String complaintNumber,
            String assignedTo
    );

    ComplaintResponse startComplaint(String complaintNumber);

    ComplaintResponse resolveComplaint(String complaintNumber);

    ComplaintResponse closeComplaint(String complaintNumber);

    ComplaintResponse rejectComplaint(
            String complaintNumber,
            String reason
    );

    List<ComplaintHistory> getComplaintHistory(String complaintNumber);

    Page<ComplaintResponse> getAllComplaints(
            int page,
            int size,
            ComplaintStatus status,
            ComplaintCategory category,
            String residentId,
            LocalDateTime fromDate,
            LocalDateTime toDate
    );
}
