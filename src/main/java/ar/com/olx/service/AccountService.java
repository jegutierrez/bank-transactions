package ar.com.olx.service;

import ar.com.olx.repository.AccountRepository;
import ar.com.olx.entity.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Created by jegutierrez on 08/10/16.
 */

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public Collection<Account> findAll(){
        return accountRepository.findAll();
    }

    public Account findOne(long id){
        return accountRepository.findOne(id);
    }

    public Account create(Account account){
        return accountRepository.save(account);
    }

    public Account update(Account account){
        return accountRepository.save(account);
    }

    public Account delete(long id){
        Account account = accountRepository.findOne(id);
        accountRepository.delete(id);
        return account;
    }

}
