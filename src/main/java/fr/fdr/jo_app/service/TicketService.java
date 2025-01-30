package fr.fdr.jo_app.service;

import fr.fdr.jo_app.pojo.Offer;
import fr.fdr.jo_app.pojo.Ticket;
import fr.fdr.jo_app.pojo.Transaction;
import fr.fdr.jo_app.repository.OfferRepository;
import fr.fdr.jo_app.repository.TicketRepository;
import fr.fdr.jo_app.repository.TransactionRepository;
import fr.fdr.jo_app.security.models.User;
import fr.fdr.jo_app.security.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TicketService {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private OfferRepository offerRepository;

    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    public Ticket getTicketById(Long id) {
        return ticketRepository.findById(id).stream()
            .filter(ticket -> ticket.getIdTicket().equals(id))
            .findAny()
            .orElse(null);
    }

    public Ticket getTicketByTokenTicket(String tokenTicket) {
            return ticketRepository.findByTokenTicket(tokenTicket);
    }


    public Ticket createTicket(Ticket ticket) {
        if(ticket.getIdTicket() == null) {
            return ticketRepository.save(ticket);
        }else{
            return null;
        }
    }

    public void deleteTicketById(Long id) {
        ticketRepository.deleteById(id);
    }

    public List<Ticket> getTicketsByUser(Long idUser) {
        User user = userRepository.findById(idUser).orElse(null);
        return ticketRepository.findByUser(user);
    }

    public Optional<Ticket> updateTicket (Long id, Ticket ticket) {
//        Ticket ticketToUpdate = ticketRepository.findById(id).orElse(null);
//        if(ticketToUpdate != null) {
//            ticketToUpdate.setIdTicket(ticket.getIdTicket());
//            ticketToUpdate.setUser(ticket.getUser());
//            ticketToUpdate.setDate(ticket.getDate());
//            ticketToUpdate.setTokenTicket(ticket.getTokenTicket());
//            this.ticketRepository.save(ticketToUpdate);
//
//        }
//        return ticketToUpdate;

        return ticketRepository.findById(id)
                .map(oldTicket -> {
                    modelMapper.map(ticket, oldTicket);
                    return ticketRepository.save(oldTicket);
                });

    }

    public Ticket buyTicket(Ticket ticket, Double amount) {

        if (ticket.getOffer() != null && ticket.getOffer().getIdOffer() == null) {
            Offer savedOffer = offerRepository.save(ticket.getOffer());
            ticket.setOffer(savedOffer);
        } else if (ticket.getOffer() != null) {
            Offer existingOffer = offerRepository.findById(ticket.getOffer().getIdOffer())
                    .orElseThrow(() -> new IllegalArgumentException("Offer not found!"));
            ticket.setOffer(existingOffer);
        }

        // 1. Generate transaction
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setDateTransaction(new Date(System.currentTimeMillis()));
        transaction.setTokenTransaction(generateTransactionToken());
        transaction = transactionRepository.save(transaction);

        // 2. Associate ticket/transaction
        ticket.setTransaction(transaction);

        // 3. Save ticket
        return ticketRepository.save(ticket);
    }

    private String generateTransactionToken() {
        return "TRX-" + System.currentTimeMillis();
    }


}
