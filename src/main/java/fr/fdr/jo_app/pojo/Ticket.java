package fr.fdr.jo_app.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import fr.fdr.jo_app.security.models.User;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@Entity
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTicket;

    @ManyToOne
    @JoinColumn(name = "idOffer", referencedColumnName = "idOffer")
    private Offer offer;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date date;

    private String tokenTicket;

    private String tokenUser;

    @ManyToOne
    @JoinColumn(name = "idTransaction", referencedColumnName = "idTransaction")
    private Transaction transaction;

    @ManyToOne
    @JoinColumn(name = "idUser", referencedColumnName = "idUser")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private User user;

    public Ticket() {
    }

    public Ticket(Offer offer, Date date, User user, Transaction transaction) {
        this.offer = offer;
        this.date = date;
        this.user = user;
        this.transaction = transaction;
        if (user != null) {
            this.tokenUser = user.getTokenUser();
        }
        if (transaction != null) {
            this.tokenTicket = generateTokenTicket();
        }
    }

    @PrePersist
    @PreUpdate
    public void prepareData() {
        if (user != null) {
            this.tokenUser = user.getTokenUser();
        }
        this.tokenTicket = generateTokenTicket();
    }

    public String generateTokenTicket() {
        return (tokenUser != null ? tokenUser : "unknownUser") + "-" +
                (transaction != null && transaction.getTokenTransaction() != null ? transaction.getTokenTransaction() : "unknownTransaction");
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "idTicket=" + idTicket +
                ", type of Offer='" + (offer != null ? offer.toString() : "null") +
                ", date=" + date +
                ", tokenTicket='" + tokenTicket + '\'' +
                ", tokenUser='" + tokenUser + '\'' +
                ", transaction=" + (transaction != null ? transaction.toString() : "null") +
                ", user=" + (user != null ? user.toString() : "null") +
                '}';
    }
}
