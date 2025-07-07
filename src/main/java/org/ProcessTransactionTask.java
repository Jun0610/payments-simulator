package org;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.transaction.TransactionIsolationLevel;
import org.pojo.Transaction;
import org.pojo.TransactionStats;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

public class ProcessTransactionTask implements Callable<Void> {

    private final Transaction transaction;
    private static final Logger log = LoggerFactory.getLogger(ProcessTransactionTask.class.getName());
    public ProcessTransactionTask(Transaction t) {
        transaction = t;
    }

    @Override
    public Void call() {

        try {
            Jdbi jdbi = DBConnection.getJdbi();
            int success = jdbi.inTransaction(TransactionIsolationLevel.SERIALIZABLE, handle -> {
                Integer moneyBalanceOfSender = handle.select("SELECT balance FROM moneyBalance WHERE userId = ? FOR UPDATE", transaction.getSenderId()).mapTo(Integer.class).one();
                if (moneyBalanceOfSender == null) {
                    log.error("Error in retrieving money balance of sender");
                    handle.rollback();
                    return 1;
                }
                if (moneyBalanceOfSender < transaction.getAmount()) {
                    log.warn("Insufficient balance for userId {}", transaction.getSenderId());
                    handle.rollback();
                    return 1;
                }
                Integer moneyBalanceOfReceiver = handle.select("SELECT balance FROM moneyBalance WHERE userId = ? FOR UPDATE", transaction.getReceiverId()).mapTo(Integer.class).one();
                if (moneyBalanceOfReceiver == null) {
                    log.error("Error in retrieving money balance of receiver");
                    handle.rollback();
                    return 1;
                }
                handle.execute("UPDATE moneyBalance SET balance = ? WHERE userID = ?", moneyBalanceOfSender - transaction.getAmount(), transaction.getSenderId());
                handle.execute("UPDATE moneyBalance SET balance = ? WHERE userID = ?", moneyBalanceOfReceiver + transaction.getAmount(), transaction.getReceiverId());
                return 0;
            });
            TransactionCount.incrementCount();
            if (success == 0) {
                TransactionStats.getInstance().addTransaction(transaction);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return null;

    }
}
