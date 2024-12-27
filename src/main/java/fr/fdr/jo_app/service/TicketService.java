package fr.fdr.jo_app.service;

import fr.fdr.jo_app.pojo.Ticket;
import fr.fdr.jo_app.repository.TicketRepository;
import fr.fdr.jo_app.security.models.User;
import fr.fdr.jo_app.security.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    public Ticket getTicketById(Long id) {
        return ticketRepository.findById(id).stream()
            .filter(ticket -> ticket.getIdTicket().equals(id))
            .findAny()
            .orElse(null);
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
}
