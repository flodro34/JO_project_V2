package fr.fdr.jo_app.ws;

import fr.fdr.jo_app.pojo.Ticket;
import fr.fdr.jo_app.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(ApiRegistration.API + ApiRegistration.REST_TICKET)
public class TicketController {
    @Autowired
    TicketService ticketService;

    @GetMapping
    public List<Ticket> getAllTickets() {
        return ticketService.getAllTickets();
    }

    @GetMapping("/{id}")
    public Ticket getTicket(@PathVariable Long id) {
        return ticketService.getTicketById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteTicket(@PathVariable Long id) {
        ticketService.deleteTicketById(id);
    }

    @PostMapping
    public Ticket createTicket(@RequestBody Ticket ticket) {
        return ticketService.createTicket(ticket);
    }

    @PutMapping("/{id}")
    public Optional<Ticket> updateTicket(@PathVariable Long id, @RequestBody Ticket ticket) {
        return ticketService.updateTicket(id,ticket);
    }

    @PostMapping("/buy")
    public Ticket buyTicket(@RequestBody Ticket ticket, @RequestParam Double amount) {
        return ticketService.buyTicket(ticket, amount);
    }

}
