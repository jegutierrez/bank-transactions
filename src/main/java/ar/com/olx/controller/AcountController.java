package ar.com.olx.controller;

import ar.com.olx.entity.Account;
import ar.com.olx.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.NoResultException;
import java.util.Collection;

/**
 * Created by jegutierrez on 08/10/16.
 */

@RestController
@RequestMapping("/api/account")
public class AcountController extends BaseController{

    @Autowired
    private AccountService accountService;

    /**
     * Endpoint para obtener lista de cuentas registradas
     *
     * Si las encuentra retorna un HTTP status 200.
     *
     * Si no encuentra resultado retorna 404
     *
     * @param
     * @return Un ResponseEntity con la lista de cuentas en formato json.
     */
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<Account>> getAllAccounts(){
        Collection<Account> accounts = accountService.findAll();
        if (accounts.isEmpty()){
            throw new NoResultException("Not accounts found");
        }
        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }

    /**
     * Endpoint para obtener el detalle de una cuenta registrada
     *
     * Si las encuentra retorna un HTTP status 200.
     *
     * Si no encuentra resultado retorna 404
     *
     * @param id de la cuenta a consultar
     * @return Un ResponseEntity con el detalle de la cuenta en formato json.
     */
    @RequestMapping(value="/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Account> getAccount(@PathVariable("id") long id){
        Account account = accountService.findOne(id);
        if (account == null){
            throw new NoResultException("Account " + id + " not found");
        }
        return new ResponseEntity<>(account, HttpStatus.OK);
    }

    /**
     * Endpoint para registrar una cuenta
     *
     * Si la registra con exito retorna un HTTP status 201.
     *
     * Si ocurre algun error retorna 500
     *
     * @param account en formato json, que contenga el banco, el pais, y el saldo
     *                inicial de la cuenta
     * @return Un ResponseEntity con el detalle de la cuenta registrada en formato json.
     */
    @RequestMapping(method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Account> insert(@RequestBody Account account){
        Account ac = accountService.create(account);
        if (ac == null){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(ac, HttpStatus.CREATED);
    }

    /**
     * Endpoint para editar una cuenta
     *
     * Si la edita con exito retorna un HTTP status 200.
     *
     * Si la cuenta no existe retorna 404
     *
     * Si ocurre algun error retorna 500
     *
     * @param account en formato json, que contenga el banco, el pais, y el saldo
     *                de la cuenta
     * @return Un ResponseEntity con el detalle de la cuenta modificada en formato json.
     */
    @RequestMapping(method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Account> update(@RequestBody Account account){
        Account a = accountService.findOne(account.getId());
        if (a == null){
            throw new NoResultException("Account " + account.getId() + " not found");
        }
        Account ac = accountService.update(account);
        if (ac == null){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(ac, HttpStatus.OK);
    }

    /**
     * Endpoint para eliminar una cuenta
     *
     * Si la elimina con exito retorna un HTTP status 204.
     *
     * Si la cuenta no existe retorna 404
     *
     * @param id en de la cuenta que desea eliminar
     * @return
     */
    @RequestMapping(value="/{id}", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Account> delete(@PathVariable("id") long id){
        Account a = accountService.findOne(id);
        if (a == null){
            throw new NoResultException("Account " + id + " not found");
        }
        accountService.delete(id);
        return new ResponseEntity<>(null, null, HttpStatus.NO_CONTENT);
    }
}
