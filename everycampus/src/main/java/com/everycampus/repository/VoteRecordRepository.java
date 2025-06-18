package com.everycampus.repository;

import com.everycampus.entity.VoteRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VoteRecordRepository extends JpaRepository<VoteRecord, Long> {
    List<VoteRecord> findByVoteId(Long voteId);
    boolean existsByUsernameAndVoteId(String username, Long voteId);
}
