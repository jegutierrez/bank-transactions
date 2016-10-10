package ar.com.olx.repository;

import ar.com.olx.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by jegutierrez on 09/10/16.
 */

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {}