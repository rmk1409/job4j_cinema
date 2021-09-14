package ru.job4j.cinema.model;

public class Ticket {
    private int id;
    private final int accountId;
    private final int row;
    private final int cell;

    public Ticket(int id, int accountId, int row, int cell) {
        this.id = id;
        this.accountId = accountId;
        this.row = row;
        this.cell = cell;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getAccountId() {
        return accountId;
    }

    public int getRow() {
        return row;
    }

    public int getCell() {
        return cell;
    }
}
