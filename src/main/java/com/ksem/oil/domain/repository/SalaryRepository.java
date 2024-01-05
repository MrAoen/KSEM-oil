package com.ksem.oil.domain.repository;

import com.ksem.oil.domain.entity.Azs;
import com.ksem.oil.domain.entity.Salary;
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
public interface SalaryRepository extends JpaRepository<Salary, Long> {
    Optional<Salary> findByExtId(UUID extId);

    List<Salary> findAllByAzsAndDateBetween(Azs azs, LocalDateTime fromDate, LocalDateTime toDate);

    @Modifying
    @Query("delete from Salary s where s.extId=:id")
    Integer deleteByExtId(@Param("id") UUID extId);

    List<Salary> findAllByExtId(UUID extId);
}
