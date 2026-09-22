package com.ys.hsm.complaint.service.impl;

import com.ys.hsm.complaint.entity.Counter;
import com.ys.hsm.complaint.service.ComplaintNumberGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.time.Year;

@Service
@RequiredArgsConstructor
public class ComplaintNumberGeneratorImpl implements ComplaintNumberGenerator {

    private final MongoTemplate mongoTemplate;

    @Override
    public String generateComplaintNumber() {

        Query query = new Query(
                Criteria.where("_id").is("complaint")
        );

        Update update = new Update()
                .inc("sequence", 1);

        FindAndModifyOptions options = FindAndModifyOptions.options()
                .returnNew(true)
                .upsert(true);

        Counter counter = mongoTemplate.findAndModify(
                query,
                update,
                options,
                Counter.class
        );

        if (counter == null) {
            throw new IllegalStateException(
                    "Unable to generate complaint number"
            );
        }

        return String.format(
                "CMP-%d-%06d",
                Year.now().getValue(),
                counter.getSequence()
        );
    }
}