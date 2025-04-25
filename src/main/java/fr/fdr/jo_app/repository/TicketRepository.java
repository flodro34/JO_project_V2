package fr.fdr.jo_app.repository;

import fr.fdr.jo_app.pojo.Offer;
import fr.fdr.jo_app.pojo.Ticket;
import fr.fdr.jo_app.security.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    //List<Ticket> findAllByTokenUser(String tokenUser);

    List<Ticket> findByUser(User user);

    //List<Ticket> findByOffer(Offer offer);

    Ticket findByTokenTicket(String tokenTicket);
}
