package com.ksem.oil.domain.repository;

import com.ksem.oil.domain.entity.Azs;
import com.ksem.oil.domain.entity.MoneyTransaction;
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
public interface MoneyTransactionRepository extends JpaRepository<MoneyTransaction,Long> {

    Optional<MoneyTransaction> findByExtId(UUID uuid);

    List<MoneyTransaction> findAllByAzsAndDateBetween(Azs azs, LocalDateTime fromDate, LocalDateTime toDate);

    Optional<MoneyTransaction> findByExtIdAndRowNumber(UUID id,int maxCount);

    @Modifying
    @Query("delete from MoneyTransactions m where m.extId=:id")
    Integer deleteByExtId(@Param("id") UUID extId);

    List<MoneyTransaction> findAllByExtId(UUID extId);
}
