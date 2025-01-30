package fr.fdr.jo_app;

import fr.fdr.jo_app.pojo.*;
import fr.fdr.jo_app.repository.*;
import fr.fdr.jo_app.security.config.WebSecurityConfig;
import fr.fdr.jo_app.security.models.User;
import fr.fdr.jo_app.security.repository.UserRepository;
import fr.fdr.jo_app.services.TicketServiceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Import(WebSecurityConfig.class)
public class TicketTest {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TicketServiceTest ticketServiceTest;

    private User user;
    private Offer offer;
    private Transaction transaction;


    @BeforeEach
    public void setup() {
        // Clean
        ticketRepository.deleteAll();
        offerRepository.deleteAll();
        transactionRepository.deleteAll();
        userRepository.deleteAll();

        // Create and save Offer
        offer = new Offer(20.0, OfferType.SOLO);
        offer = offerRepository.save(offer);

        // Create and save User
        user = new User();
        user.setUsername("testuser");
        user.setPassword("password123");
        user.setTokenUser("testUserToken");
        user = userRepository.save(user);

        // Create and save Transaction
        transaction = new Transaction();
        transaction = transactionRepository.save(transaction);
    }

    @Test
    void testCreateAndRetrieveTicket() {

        // Create ticket
        Ticket createdTicket = ticketServiceTest.createTicket(offer, new Date(), user, transaction);

        // Get ticket
        Ticket retrievedTicket = ticketRepository.findById(createdTicket.getIdTicket()).orElse(null);

        // Assert
        assertNotNull(retrievedTicket);
        assertEquals(createdTicket.getIdTicket(), retrievedTicket.getIdTicket());
        assertEquals(OfferType.SOLO, retrievedTicket.getOffer().getType());
        assertEquals("testuser", retrievedTicket.getUser().getUsername());
    }

    @Test
    void testCreateTicketWithoutTransaction() {

        // Create ticket
        Ticket createdTicket = ticketServiceTest.createTicket(offer, new Date(), user, null);

        // Assert
        assertNotNull(createdTicket);
        assertEquals("testUserToken-unknownTransaction", createdTicket.getTokenTicket()); // Vérifie le token généré
    }

    @Test
    void testDeleteTicket() {
        // Arrange
        Ticket ticket = new Ticket(offer, new Date(), user, transaction);
        ticket = ticketRepository.save(ticket);
        Long ticketId = ticket.getIdTicket();

        // Act
        ticketRepository.delete(ticket);
        Optional<Ticket> deletedTicket = ticketRepository.findById(ticketId);

        // Assert
        assertTrue(deletedTicket.isEmpty(), "Le ticket devrait être supprimé !");
    }

    @Test
    void testUpdateTicket() {
        // Arrange
        Ticket ticket = new Ticket(offer, new Date(), user, transaction);
        ticket = ticketRepository.save(ticket);

        // Update date
        Date newDate = new Date(System.currentTimeMillis() + 86400000); // Ajoute 1 jour
        ticket.setDate(newDate);

        // Act
        ticket = ticketRepository.save(ticket);
        Ticket updatedTicket = ticketRepository.findById(ticket.getIdTicket()).orElse(null);

        // Assert
        assertNotNull(updatedTicket, "Le ticket mis à jour ne devrait pas être null !");
        assertEquals(newDate, updatedTicket.getDate(), "La date du ticket n'a pas été mise à jour !");
    }

}
