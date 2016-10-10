package ar.com.olx.service;

import ar.com.olx.entity.Account;
import ar.com.olx.entity.TransactionStatus;
import ar.com.olx.entity.TransactionType;
import ar.com.olx.repository.AccountRepository;
import ar.com.olx.repository.TransactionRepository;
import ar.com.olx.entity.Transaction;
import ar.com.olx.util.TransactionLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

import static ar.com.olx.util.Constants.*;

/**
 * Created by jegutierrez on 08/10/16.
 */

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    public Collection<Transaction> findAll(){
        return transactionRepository.findAll();
    }

    public Transaction find(long id){
        return transactionRepository.findOne(id);
    }

    /**
     * Procesamiento de la transaccion
     *
     * Se calcula el tipo, el impuesto y el estado de la misma
     * Y se loguea en un archivo de texto con las transacciones
     * (independientemente del estado)
     *
     * @param transaction
     * @return transaction
     */
    public Transaction process(Transaction transaction) {

        // Seteo el tipo de transaccion
        TransactionType type = getType(transaction);
        transaction.setType(type);

        // Calculo el impuesto
        double tax = computeTax(transaction);
        transaction.setTax(tax);

        // Chequeo si se puede aprobar la transaccion
        TransactionStatus status = computeStatus(transaction);
        transaction.setStatus(status);

        if (status == TransactionStatus.SUCCESS){
            save(transaction);
        }
        TransactionLogger.log(transaction);
        return transaction;
    }

    /**
     * Se guarda y loguea de la transaccion,
     * Una vez que se hicieron las validaciones se procesa la transaccon
     * Se actualizan los saldos de las cuentas y se guarda la transaccion
     * @param transaction
     * @return transaction Reterorna la transaccion modificada
     */
    public Transaction save(Transaction transaction){
        Account source = accountRepository.findOne(transaction.getSource());
        Account target = accountRepository.findOne(transaction.getTarget());

        double totalAmount = transaction.getAmount() + transaction.getTax();
        double currentSourceBalance = source.getBalance();
        double currentTargetBalance = target.getBalance();

        // Actualizo los salgos de las cuentas de origen y destino
        source.setBalance(currentSourceBalance - totalAmount);
        target.setBalance(currentTargetBalance + totalAmount);
        accountRepository.save(source);
        accountRepository.save(target);

        return transactionRepository.save(transaction);
    }

    /**
     * Setea los diferentes estados posibles
     *
     * SUCCESS: se aprueba la transaccion
     * FAILURE_SAME_ACCOUNT: error porque la cuenta de origen y destino es la misma
     * FAILURE_ACCOUNT_NOT_FOUND: alguna de las cuentas no existe
     * FAILURE_INSUFFICIENT_BALANCE: la cuenta de origen no tiene saldo suficiente
     *
     * @param transaction
     * @return TransactionStatus
     */
    public TransactionStatus computeStatus(Transaction transaction) {
        // Denegado si los numero de cuenta son iguales
        if (transaction.getSource() == transaction.getTarget())
            return TransactionStatus.FAILURE_SAME_ACCOUNT;

        // Denegado si no existe la cuenta de origen
        Account source = accountRepository.findOne(transaction.getSource());
        if (source == null)
            return TransactionStatus.FAILURE_ACCOUNT_NOT_FOUND;

        // Denegado si no existe la cuenta de destino
        Account target = accountRepository.findOne(transaction.getTarget());
        if (target == null)
            return TransactionStatus.FAILURE_ACCOUNT_NOT_FOUND;

        // Denegado si no tiene fondos la cuenta de origen
        double resultSourceBalance = source.getBalance() - transaction.getAmount() - transaction.getTax();
        if (resultSourceBalance < 0)
           return TransactionStatus.FAILURE_INSUFFICIENT_BALANCE;

        // Se aprueba la transaccion si todo salio bien
        return TransactionStatus.SUCCESS;
    }

    /**
     * Calcula el impuesto dependiendo del tipo de transaccion
     *
     * Las transacciones internacionales se cobra como impuesto, el 5% del valor del monto
     * a transferir a la cuenta origen, en cambio en una transacción nacional, solo se
     * cobra el 1% en caso que las cuentas sean de diferentes bancos. En caso de
     * pertenecer al mismo banco, no se cobrara nada.
     *
     * @param transaction
     * @return double tax con monto del impuesto calculado
     */
    public double computeTax(Transaction transaction) {
        Account source = accountRepository.findOne(transaction.getSource());
        Account target = accountRepository.findOne(transaction.getTarget());

        // Calculo del impuesto dependiendo del tipo de transaccion
        if (transaction.getType() == TransactionType.INTERNATIONAL) {
            return transaction.getAmount() * INTERNATIONAL_TAX;
        } else if (source.getBank().equalsIgnoreCase(target.getBank())) {
            return transaction.getAmount() * SAME_BANK_TAX;
        } else {
            return transaction.getAmount() * NATIONAL_TAX;
        }
    }

    /**
     * Calcula tipo de transaccion
     * NATIONAL: transacciones en el mismo pais
     * INTERNATIONAL: transacciones en diferente pais
     * @param transaction
     * @return TransactionType
     */
    public TransactionType getType(Transaction transaction){

        Account source = accountRepository.findOne(transaction.getSource());
        Account target = accountRepository.findOne(transaction.getTarget());

        // Si nos nombres de los paices son diferentes se considera Internacional
        return source.getCountry().equalsIgnoreCase(target.getCountry()) ?
                TransactionType.NATIONAL : TransactionType.INTERNATIONAL;
    }

}
