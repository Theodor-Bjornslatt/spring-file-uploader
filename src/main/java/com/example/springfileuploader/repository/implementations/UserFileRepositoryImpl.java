package com.example.springfileuploader.repository.implementations;

import com.example.springfileuploader.repository.UserFileRepository;
import com.example.springfileuploader.repository.models.FileDetail;
import com.example.springfileuploader.repository.models.User;
import com.mongodb.client.result.UpdateResult;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserFileRepositoryImpl implements UserFileRepository {
    private final MongoTemplate mongoTemplate;

    @Autowired
    public UserFileRepositoryImpl(MongoTemplate mongoTemplate){
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public boolean saveFileDetail(String userId, FileDetail fileDetailToSave) {
        Query userQuery = Query.query(Criteria.where("_id").is(new ObjectId(userId)));
        Update fileDetailsUpdate = new Update().push("fileDetails", fileDetailToSave);
        UpdateResult result = mongoTemplate.updateFirst(userQuery, fileDetailsUpdate, User.class);
        return result.wasAcknowledged() && result.getModifiedCount() == 1;
    }

    @Override
    public boolean deleteFileDetail(String userId, String fileId){
        Query userQuery = Query.query(Criteria.where("_id").is(new ObjectId(userId))
                .andOperator(Criteria.where("fileDetails").elemMatch(
                        Criteria.where("_id").is(new ObjectId(fileId))
                ))
        );
        Update fileDetailsUpdate = new Update().pull(
                "fileDetails",
                new Query(Criteria.where("_id").is(new ObjectId(fileId)))
        );
        UpdateResult result = mongoTemplate.updateFirst(userQuery, fileDetailsUpdate, User.class);
        return result.wasAcknowledged() && result.getModifiedCount() == 1;
    }

    @Override
    public Optional<FileDetail> findFileDetail(String userId, String fileId){
        AggregationOperation matchUser = Aggregation.match(Criteria.where("_id").is(new ObjectId(userId)));
        AggregationOperation unwind = Aggregation.unwind("fileDetails");
        AggregationOperation replaceRoot = Aggregation.replaceRoot("fileDetails");
        AggregationOperation matchFile = Aggregation.match(Criteria.where("_id").is(new ObjectId(fileId)));
        Aggregation aggregation = Aggregation.newAggregation(matchUser, unwind, replaceRoot, matchFile);
        FileDetail fileDetail = mongoTemplate.aggregate(
                aggregation,
                mongoTemplate.getCollectionName(User.class), FileDetail.class
                )
                .getUniqueMappedResult();
        return Optional.ofNullable(fileDetail);
    }

    @Override
    public List<FileDetail> getAllFileDetails(String userId){
        AggregationOperation matchUser = Aggregation.match(Criteria.where("_id").is(new ObjectId(userId)));
        AggregationOperation unwind = Aggregation.unwind("fileDetails");
        AggregationOperation replaceRoot = Aggregation.replaceRoot("fileDetails");
        Aggregation aggregation = Aggregation.newAggregation(matchUser, unwind, replaceRoot);
        return mongoTemplate.aggregate(
                        aggregation,
                        mongoTemplate.getCollectionName(User.class), FileDetail.class
                )
                .getMappedResults();
    }

}
