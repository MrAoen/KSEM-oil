package com.ksem.oil.domain.repository;

import com.ksem.oil.domain.entity.Azs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AzsRepository extends JpaRepository<Azs, Long> {

    Optional<Azs> findByName(String name);

    Optional<Azs> findByExtId(UUID uuid);
}
