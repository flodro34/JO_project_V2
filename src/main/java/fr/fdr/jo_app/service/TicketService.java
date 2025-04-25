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
import java.util.UUID;

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

        //Check Offer
        Offer offer = offerRepository.findById(ticket.getOffer().getIdOffer())
                .orElseThrow(() -> new RuntimeException("Offer not found !"));

        // Check User
        User user = userRepository.findById(ticket.getUser().getIdUser())
                .orElseThrow(() -> new RuntimeException("User not found !"));

        // Update token
        ticket.setUser(user);
        ticket.setOffer(offer);
        ticket.setTokenUser(user.getTokenUser()); // Met à jour le tokenUser
        ticket.setTokenTicket(ticket.generateTokenTicket());

        return ticketRepository.save(ticket);
    }

    public void deleteTicketById(Long id) {
        ticketRepository.deleteById(id);
    }

    public List<Ticket> getTicketsByUser(Long idUser) {
        User user = userRepository.findById(idUser).orElse(null);
        return ticketRepository.findByUser(user);
    }

    public Optional<Ticket> updateTicket (Long id, Ticket ticket) {

        return ticketRepository.findById(id)
                .map(oldTicket -> {
                    modelMapper.map(ticket, oldTicket);
                    return ticketRepository.save(oldTicket);
                });

    }



}
