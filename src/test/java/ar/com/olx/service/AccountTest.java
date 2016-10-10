package ar.com.olx.service;

import ar.com.olx.entity.Account;
import ar.com.olx.ws.AbstractTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by jegutierrez on 09/10/16.
 */

@Transactional
public class AccountTest extends AbstractTest{

    @Autowired
    private AccountService accountService;

    @Test
    public void testFindOne() {

        long id = 100000;

        Account account = accountService.findOne(id);

        Assert.assertNotNull("failure - expected not null", account);
        Assert.assertEquals("failure - expected id attribute match", id, account.getId());

    }
}
