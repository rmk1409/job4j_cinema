package ru.job4j.cinema.persistence;

import org.apache.commons.dbcp2.BasicDataSource;
import org.postgresql.util.PSQLException;
import ru.job4j.cinema.model.Account;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.service.TicketService;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
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

    public Account findAccountByPhoneNumber(String phoneNumber) {
        Account account = null;
        String sql = "SELECT * FROM account WHERE phone = ?";
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)
        ) {
            ps.setString(1, phoneNumber);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    int id = it.getInt("id");
                    String username = it.getString("username");
                    String email = it.getString("email");
                    String phone = it.getString("phone");
                    account = new Account(id, username, email, phone);
                }

            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An exception was thrown", e);
        }
        return account;
    }

    public Collection<Ticket> findAllTickets() {
        List<Ticket> tickets = new ArrayList<>();
        String sql = "SELECT * FROM ticket";
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    int id = it.getInt("id");
                    int accountId = it.getInt("account_id");
                    int row = it.getInt("row");
                    int cell = it.getInt("cell");
                    tickets.add(new Ticket(id, accountId, row, cell));
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An exception was thrown", e);
        }
        return tickets;
    }

    public Ticket save(Ticket ticket) {
        String sql = "INSERT INTO ticket(row, cell, account_id) VALUES (?, ?, ?)";
        int keys = PreparedStatement.RETURN_GENERATED_KEYS;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql, keys)
        ) {
            ps.setInt(1, ticket.getRow());
            ps.setInt(2, ticket.getCell());
            ps.setInt(3, ticket.getAccountId());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    ticket.setId(id.getInt(1));
                }
            }
        } catch (PSQLException e) {
            throw new IllegalArgumentException(TicketService.ANOTHER_TICKET, e);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An exception was thrown", e);
        }
        return ticket;
    }
}
