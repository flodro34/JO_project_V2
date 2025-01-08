package fr.fdr.jo_app;

import fr.fdr.jo_app.pojo.Ticket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TicketTest {
    private Ticket ticket1;
    private Ticket ticket2;

    @BeforeEach
    public void setUp() {
        ticket1 = new Ticket();
        ticket2 = new Ticket();
    }

    @Test
    public void testGetTicket() {

    }
}
