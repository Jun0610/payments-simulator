package org;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DBDataSource {
    private static final HikariConfig config = new HikariConfig();
    private static HikariDataSource ds;

    public static void initDataSource() {
        config.setJdbcUrl( "jdbc:mysql://localhost:3306/payments" );
        config.setUsername( "root" );
        config.setPassword( "Admin_123" );
        config.setMaximumPoolSize(100);
        ds = new HikariDataSource( config );
    }

    public static HikariDataSource getDataSource() {
        return ds;
    }


    private DBDataSource() {}

}
