package com.ksem.oil.domain.repository;

import com.ksem.oil.domain.entity.CuponSales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CuponSalesRepository extends JpaRepository<CuponSales, Long> {

    @Modifying
    @Query("delete from Cupone g where g.extId=:id")
    Integer deleteByExtId(@Param("id") UUID extId);

    Optional<CuponSales> getByExtIdAndNumber(UUID extId,Integer number);

    List<CuponSales> getByExtIdOrderByNumber(UUID extId);

    List<CuponSales> getByExtId(UUID extId);
}