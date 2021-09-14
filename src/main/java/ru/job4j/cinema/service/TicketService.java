package ru.job4j.cinema.service;

import ru.job4j.cinema.model.Account;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.persistence.Store;

import java.util.Objects;

public class TicketService {
    private static final Store store = Store.instOf();

    public Object buyTicket(String phone, String name, int row, int cell) {
        Account account = store.findAccountByPhoneNumber(phone);
        if (Objects.isNull(account) || !Objects.equals(account.getUsername(), name)) {
            return "Wrong username or phone";
        }
        Ticket ticket = store.save(new Ticket(0, account.getId(), row, cell));
        if (ticket.getId() == 0) {
            return "Try to buy another ticket";
        }
        return ticket;
    }
}
