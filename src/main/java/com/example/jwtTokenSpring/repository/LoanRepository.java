package com.example.jwtTokenSpring.repository;

import java.util.List;

import com.example.jwtTokenSpring.entity.Loans;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanRepository extends CrudRepository<Loans, Long> {

    List<Loans> findByCustomerIdOrderByStartDtDesc(long customerId);

}
