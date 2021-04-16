package com.ksem.oil.domain.repository;

import com.ksem.oil.domain.entity.TopicEntryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TopicEntryEntityRepository extends JpaRepository<TopicEntryEntity, Long> {

    Optional<TopicEntryEntity> findByName(String name);

}
