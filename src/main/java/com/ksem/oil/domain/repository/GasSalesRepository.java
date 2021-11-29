package com.ksem.oil.domain.repository;

import com.ksem.oil.domain.entity.Azs;
import com.ksem.oil.domain.entity.GasSales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GasSalesRepository extends JpaRepository<GasSales, Long> {

    Optional<GasSales> findByExtId(UUID extId);

    Optional<GasSales> findByExtIdAndRowNumber(UUID extId, int rowNumber);

    Optional<GasSales> findByCheckNumberAndAzs(Long checkNumber, Azs azs);

    List<GasSales> findAllByAzsAndDateBetween(Azs azs, LocalDateTime start, LocalDateTime end);

    @Modifying
    @Query("delete from GasSales g where g.extId=:id")
    Integer deleteByExtId(@Param("id") UUID extId);

    List<GasSales> findAllByExtId(UUID extId);

}
