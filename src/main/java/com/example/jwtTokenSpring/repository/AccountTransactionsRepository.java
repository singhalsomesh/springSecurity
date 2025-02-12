package com.example.jwtTokenSpring.repository;

import java.util.List;

import com.example.jwtTokenSpring.entity.AccountTransactions;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountTransactionsRepository extends CrudRepository<AccountTransactions, String> {

    List<AccountTransactions> findByCustomerIdOrderByTransactionDtDesc(long customerId);

}