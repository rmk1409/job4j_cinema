package ru.job4j.cinema.service;

import ru.job4j.cinema.model.Account;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.persistence.Store;

import java.util.Objects;

public class TicketService {
    private static final Store store = Store.instOf();
    public static final String WRONG_CREDENTIALS = "Wrong username or phone";
    public static final String ANOTHER_TICKET = "Try to buy another ticket";

    public Object buyTicket(String phone, String name, int row, int cell) {
        Account account = store.findAccountByPhoneNumber(phone);
        if (Objects.isNull(account) || !Objects.equals(account.getUsername(), name)) {
            throw new IllegalArgumentException(WRONG_CREDENTIALS);
        }
        return store.save(new Ticket(0, account.getId(), row, cell));
    }
}
