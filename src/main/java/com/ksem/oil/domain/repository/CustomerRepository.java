package com.ksem.oil.domain.repository;

import com.ksem.oil.domain.entity.Azs;
import com.ksem.oil.domain.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Long> {

    Optional<Customer> findByGlobalId(UUID uuid);

    Optional<Customer> findByNameAndAzs(String name, Azs azs);

    Optional<Customer> findByGlobalIdAndAzs(UUID id, Azs azs);
}
