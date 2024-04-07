package com.favour.Horizon.Banking.App.repository;

import com.favour.Horizon.Banking.App.domain.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, String> {
}
