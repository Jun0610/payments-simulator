package org.pojo;

import org.DBConnection;
import org.jdbi.v3.core.Jdbi;

import java.util.LinkedList;
import java.util.List;

public class TransactionStats {
    private static TransactionStats stats;

    private static final Object object = new Object();
    private long transactionsPerSecond;

    private  final int MAX_SIZE = 10;

    private List<Transaction> tenMostRecentTransactions = new LinkedList<>();
    private int size = 0;

    public void addTransaction(Transaction t) {
        synchronized (object) {
            tenMostRecentTransactions.addFirst(t);
            if (size == MAX_SIZE) {
                tenMostRecentTransactions.removeLast();
            }
            else {
                size++;
            }
        }
    }

    public static synchronized TransactionStats getInstance() {
        if (stats == null) {
            stats = new TransactionStats(0);
        }
        return stats;
    }

    private TransactionStats(long transactionsPerSecond) {
        this.transactionsPerSecond = transactionsPerSecond;
    }

    private TransactionStats(){}

    public long getTransactionsPerSecond() {
        return transactionsPerSecond;
    }

    public void setTransactionsPerSecond(long transactionsPerSecond) {
        this.transactionsPerSecond = transactionsPerSecond;
    }

    public List<Transaction> getTenMostRecentTransactions() {
        List<Transaction> transactions;
        synchronized (object) {
            transactions = tenMostRecentTransactions.stream().toList();
        }
        setTransactionNames(transactions);
        return transactions;
    }

    private void setTransactionNames(List<Transaction> transactions) {
        Jdbi jdbi  = DBConnection.getJdbi();
        for (Transaction t : transactions) {
            String[] names = jdbi.withHandle(handle -> {
                String receiverName = handle.select("SELECT userName FROM users WHERE userId = ?", t.getReceiverId()).mapTo(String.class).one();
                String senderName = handle.select("SELECT userName FROM users WHERE userId = ?", t.getSenderId()).mapTo(String.class).one();
                return new String[]{receiverName, senderName};
            });
            t.setReceiverName(names[0]);
            t.setSenderName(names[1]);
        }
    }
}
