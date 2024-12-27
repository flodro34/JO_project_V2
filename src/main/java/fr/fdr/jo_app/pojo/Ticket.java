package fr.fdr.jo_app.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

    private String tokenTransaction;

    @ManyToOne
    @JoinColumn(name = "idUser", referencedColumnName = "idUser")
    @JsonIgnore
    private User user;

    public Ticket() {

    }

    public Ticket(Offer offer,Date date) {
        this.offer = offer;
        this.date = date;
    }

    @PrePersist
    @PreUpdate
    private void prepareData() {
        if (user != null) {
            this.tokenUser = user.getTokenUser();
        }
        this.tokenTicket = generateTokenTicket();
    }

    private String generateTokenTicket() {
        return tokenUser + "-" + tokenTransaction;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "idTicket=" + idTicket +
                ", type of Offer='" + (offer != null ? offer.toString() : "null") +
                ", date=" + date +
                ", tokenTicket='" + tokenTicket + '\'' +
                ", tokenUser='" + tokenUser + '\'' +
                ", tokenTransaction='" + tokenTransaction + '\'' +
                ", user=" + (user != null ? user.toString() : "null") +
                '}';
    }
}
