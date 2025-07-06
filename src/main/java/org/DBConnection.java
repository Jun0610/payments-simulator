package org;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.transaction.SerializableTransactionRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DBConnection {

    private static final Logger log = LoggerFactory.getLogger(DBConnection.class.getName());

    private static Jdbi jdbi;

    public static synchronized Jdbi getJdbi() {
        if (jdbi == null) {
            DBDataSource.initDataSource();
            jdbi = Jdbi.create(DBDataSource.getDataSource());
            jdbi.setTransactionHandler(new SerializableTransactionRunner());
            log.info("Connected to database");
        }
        return jdbi;
    }

}
