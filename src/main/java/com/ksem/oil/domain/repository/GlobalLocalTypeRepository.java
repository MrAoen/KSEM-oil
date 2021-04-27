package com.ksem.oil.domain.repository;

import com.ksem.oil.domain.entity.Azs;
import com.ksem.oil.domain.entity.Global2LocalSalesType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GlobalLocalTypeRepository extends JpaRepository<Global2LocalSalesType,Long> {

    Optional<Global2LocalSalesType> findByAzsAndLocalType(Azs azs,Long localValue);
    Optional<Global2LocalSalesType> findByAzsAndGlobalType(Azs azs,Long globalValue);

}
