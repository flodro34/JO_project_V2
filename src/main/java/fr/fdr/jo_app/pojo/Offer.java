package fr.fdr.jo_app.pojo;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Offer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idOffer;

    private Double price;

    @Enumerated(EnumType.STRING)
    private OfferType type;

    public Offer() {
    }

    public Offer(Double price, OfferType type) {
        this.price = price;
        this.type = type;
    }

    @Override
    public String toString() {
        return "Offer{" +
                "idOffer=" + idOffer +
                ", price=" + price +
                ", type=" + type +
                '}';
    }
}
