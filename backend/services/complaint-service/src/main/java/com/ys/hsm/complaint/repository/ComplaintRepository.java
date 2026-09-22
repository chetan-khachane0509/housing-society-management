package com.ys.hsm.complaint.repository;

import com.ys.hsm.complaint.entity.Complaint;
import com.ys.hsm.complaint.enums.ComplaintCategory;
import com.ys.hsm.complaint.enums.ComplaintStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ComplaintRepository extends MongoRepository<Complaint, String> {

    Optional<Complaint> findByComplaintNumber(String complaintNumber);

    List<Complaint> findByResidentId(String residentId);

    boolean existsByComplaintNumber(String complaintNumber);

    Page<Complaint> findAll(Pageable pageable);

    Page<Complaint> findByStatus(
            ComplaintStatus status,
            Pageable pageable
    );

    Page<Complaint> findByCategory(
            ComplaintCategory category,
            Pageable pageable
    );

    Page<Complaint> findByStatusAndCategory(
            ComplaintStatus status,
            ComplaintCategory category,
            Pageable pageable
    );

    Page<Complaint> findByResidentId(
            String residentId,
            Pageable pageable
    );

    Page<Complaint> findByStatusAndResidentId(
            ComplaintStatus status,
            String residentId,
            Pageable pageable
    );

    Page<Complaint> findByCategoryAndResidentId(
            ComplaintCategory category,
            String residentId,
            Pageable pageable
    );

    Page<Complaint> findByStatusAndCategoryAndResidentId(
            ComplaintStatus status,
            ComplaintCategory category,
            String residentId,
            Pageable pageable
    );

    Page<Complaint> findByCreatedAtBetween(
            LocalDateTime fromDate,
            LocalDateTime toDate,
            Pageable pageable
    );
}
