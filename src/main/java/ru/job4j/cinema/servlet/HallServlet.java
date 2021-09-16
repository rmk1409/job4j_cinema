package ru.job4j.cinema.servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.job4j.cinema.persistence.Store;
import ru.job4j.cinema.service.TicketService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class HallServlet extends HttpServlet {
    private static final Gson GSON = new GsonBuilder().create();
    private static final Store store = Store.instOf();
    private static final TicketService service = new TicketService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json; charset=utf-8");
        OutputStream output = resp.getOutputStream();
        String json = GSON.toJson(store.findAllTickets());
        output.write(json.getBytes(StandardCharsets.UTF_8));
        output.flush();
        output.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        OutputStream output = resp.getOutputStream();
        String phone = req.getParameter("phoneNumber");
        String name = req.getParameter("username");
        int row = Integer.parseInt(req.getParameter("row"));
        int cell = Integer.parseInt(req.getParameter("cell"));
        try {
            String json = GSON.toJson(service.buyTicket(phone, name, row, cell));
            output.write(json.getBytes(StandardCharsets.UTF_8));
            output.flush();
            output.close();
        } catch (IllegalArgumentException e) {
            int statusCode = Objects.equals(e.getMessage(), TicketService.WRONG_CREDENTIALS)
                    ? HttpServletResponse.SC_UNAUTHORIZED
                    : HttpServletResponse.SC_CONFLICT;
            resp.sendError(statusCode, e.getMessage());
        }
    }
}
