package ar.com.olx;

import ar.com.olx.repository.AccountRepository;
import ar.com.olx.entity.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * Created by jegutierrez on 09/10/16.
 */
@Component
public class DataLoader implements ApplicationRunner {

    private AccountRepository accountRepository;

    @Autowired
    public DataLoader(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public void run(ApplicationArguments args) {
        accountRepository.save(new Account("HSBC", "Argentina", 250000));
        accountRepository.save(new Account("Macro", "Argentina", 14500.50));
        accountRepository.save(new Account("HSBC", "Argentina", 33900));
        accountRepository.save(new Account("Banesco", "Panama", 15800250));
        accountRepository.save(new Account("Mercantil", "Venezuela", 8450.40));
    }
}