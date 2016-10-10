package ar.com.olx.util;

import ar.com.olx.entity.Transaction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by jegutierrez on 09/10/16.
 */
public class TransactionLogger {

    private static final Logger logger = LogManager.getLogger("transaction");

    // Log de las transacciones procesadas
    public static void log(Transaction transaction){
        logger.info("Transaction id: {}, status: {}, Amount: {}, Source account: {}, " +
                        "Target Account: {}, Tax: {}, Type: {}.",
                transaction.getId(),
                transaction.getStatus(),
                transaction.getAmount(),
                transaction.getSource(),
                transaction.getTarget(),
                transaction.getTax(),
                transaction.getType());
    }
}
