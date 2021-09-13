package ru.job4j.cinema.persistence;

import org.apache.commons.dbcp2.BasicDataSource;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Properties;
import java.util.logging.Logger;

public class Store {
    private final static Logger LOGGER = Logger.getLogger(Store.class.getName());
    private final BasicDataSource pool = new BasicDataSource();

    private Store() {
        Properties cfg = new Properties();
        String fileName = "db.properties";
        try (BufferedReader io = new BufferedReader(new FileReader(fileName))) {
            cfg.load(io);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        try {
            Class.forName(cfg.getProperty("jdbc.driver"));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        pool.setDriverClassName(cfg.getProperty("jdbc.driver"));
        pool.setUrl(cfg.getProperty("jdbc.url"));
        pool.setUsername(cfg.getProperty("jdbc.username"));
        pool.setPassword(cfg.getProperty("jdbc.password"));
        pool.setMinIdle(5);
        pool.setMaxIdle(10);
        pool.setMaxOpenPreparedStatements(100);
    }

    private static final class Lazy {
        private static final Store INST = new Store();
    }

    public static Store instOf() {
        return Lazy.INST;
    }
}
