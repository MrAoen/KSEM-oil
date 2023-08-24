package com.ksem.oil.domain.repository;

import com.ksem.oil.domain.entity.Azs;
import com.ksem.oil.domain.entity.GasTotal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface GasTotalsRepository extends JpaRepository<GasTotal,Long> {

    public Optional<GasTotal> getByAzsAndPeriodAndTank(Azs azs, LocalDateTime period, int tank);
}
