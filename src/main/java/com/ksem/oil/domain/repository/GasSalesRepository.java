package com.ksem.oil.domain.repository;

import com.ksem.oil.domain.entity.Azs;
import com.ksem.oil.domain.entity.GasSales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GasSalesRepository extends JpaRepository<GasSales, Long> {

    Optional<GasSales> findByExtId(UUID extId);

    Optional<GasSales> findByCheckNumberAndAzs(Long checkNumber, Azs azs);

    List<GasSales> findAllByAzsAndDateBetween(Azs azs, LocalDateTime start, LocalDateTime end);
}
