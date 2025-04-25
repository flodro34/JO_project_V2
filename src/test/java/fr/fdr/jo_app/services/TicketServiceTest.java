package fr.fdr.jo_app.services;

import fr.fdr.jo_app.pojo.*;
import fr.fdr.jo_app.repository.TicketRepository;
import fr.fdr.jo_app.security.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TicketServiceTest {

    private final TicketRepository ticketRepository;

    @Autowired
    public TicketServiceTest(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

//    public Ticket createTicket(Offer offer, Date date, User user, Transaction transaction) {
//        Ticket ticket = new Ticket(offer, date, user, transaction);
//        return ticketRepository.save(ticket);
//    }

    public Ticket createTicket(Offer offer, Date date, User user, Transaction transaction) {
        Ticket ticket = new Ticket();
        ticket.setOffer(offer);
        ticket.setDate(date);
        ticket.setUser(user);
        ticket.setTransaction(transaction);
        if (user != null) {
            ticket.setTokenUser(user.getTokenUser()); // Assure que le tokenUser est défini
        }
        return ticketRepository.save(ticket);
    }

}
