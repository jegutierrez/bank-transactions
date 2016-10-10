package ar.com.olx.repository;

import ar.com.olx.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by jegutierrez on 08/10/16.
 */

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {}
