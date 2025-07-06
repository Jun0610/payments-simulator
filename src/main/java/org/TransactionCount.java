package org;

public class TransactionCount {
    private static long count = 0;


    public static synchronized void incrementCount() {
        count++;
    }

    public static synchronized long getCount() {
        return count;
    }

    public static synchronized void resetCount() {
        count = 0;
    }
}
