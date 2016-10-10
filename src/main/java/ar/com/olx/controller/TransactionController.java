package ar.com.olx.controller;

import ar.com.olx.entity.Transaction;
import ar.com.olx.entity.TransactionStatus;
import ar.com.olx.service.AsyncTransaction;
import ar.com.olx.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.*;

import javax.persistence.NoResultException;
import java.util.Collection;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by jegutierrez on 08/10/16.
 */
@RestController
@RequestMapping("/api/transaction")
public class TransactionController extends BaseController{


    @Autowired
    private TransactionService transactionService;

    @Autowired
    private ThreadPoolTaskExecutor transactionPool;

    /**
     * Endpoint para obtener una lista de las transacciones
     *
     * Si las encuentra retorna un HTTP status 200
     *
     * @param
     * @return Un ResponseEntity con la lista de transacciones en formato Json.
     */
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<Transaction>> findAll(){
        Collection<Transaction> t = transactionService.findAll();
        if (t.isEmpty()){
            throw new NoResultException("Not transaction found");
        }
        return new ResponseEntity<>(t, HttpStatus.OK);
    }

    /**
     * Endpoint para obtener detalle de una transaccion por id
     *
     * Si las encuentra retorna un HTTP status 200.
     *
     * Si no encuentra resultado retorna 404
     *
     * @param id de la transaccion a consultar
     * @return Un ResponseEntity con la lista de transacciones en formato Json.
     */
    @RequestMapping(value="/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Transaction> find(@PathVariable("id") long id){
        Transaction t = transactionService.find(id);
        if (t == null){
            throw new NoResultException("Transaction " + id + " not found");
        }
        return new ResponseEntity<>(t, HttpStatus.OK);
    }

    /**
     * Endpoint para procesar una transaccion de manera asincrona
     *
     * Si la puede registrar retorna un HTTP status 201.
     *
     * Si no encuentra alguna de las cuentas de origen o destino retorna 404
     *
     * Si intenta transferir a la misma cuenta o la cuenta de origen
     * tiene saldo insuficiente retorna 422
     *
     * Si ocurre un error retorna HTTP status 500.
     *
     * @param transaction en formato json, que contenga la cuenta de origen, destino
     *                    y el monto a transferir
     * @return Un ResponseEntity con el detalle de la transaccion procesada, en el
     * campo 'status' se observa el estado de la transaccion, 'SUCCESS' indica que
     * se hizo la transferencia con exito, cualquiero otro estado indica que hubo
     * error
     */
    @RequestMapping(method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Transaction> insert(@RequestBody Transaction transaction){
        AsyncTransaction asyncTransaction;
        Future<Transaction> asyncResponse;
        try {
            asyncTransaction = new AsyncTransaction(transaction);
            asyncTransaction.setTransactionService(transactionService);
            asyncResponse = transactionPool.submit(asyncTransaction);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        try {
            if (asyncResponse.get().getStatus() == TransactionStatus.FAILURE_ACCOUNT_NOT_FOUND){
                return new ResponseEntity<>(asyncResponse.get(), HttpStatus.NOT_FOUND);
            } else if (asyncResponse.get().getStatus() == TransactionStatus.FAILURE_SAME_ACCOUNT){
                return new ResponseEntity<>(asyncResponse.get(), HttpStatus.UNPROCESSABLE_ENTITY);
            } else if (asyncResponse.get().getStatus() == TransactionStatus.FAILURE_INSUFFICIENT_BALANCE){
                return new ResponseEntity<>(asyncResponse.get(), HttpStatus.UNPROCESSABLE_ENTITY);
            } else {
                return new ResponseEntity<>(asyncResponse.get(), HttpStatus.CREATED);
            }

        } catch (InterruptedException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (ExecutionException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
