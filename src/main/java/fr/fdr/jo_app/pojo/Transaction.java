package fr.fdr.jo_app.pojo;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTransaction;

    private Double amount;

    private Date dateTransaction;

    private String tokenTransaction;

    @OneToMany(mappedBy = "transaction", cascade = CascadeType.ALL)
    private List<Ticket> tickets = new ArrayList<>();

    public Transaction(Double amount, Date dateTransaction, String tokenTransaction) {
        this.amount = amount;
        this.dateTransaction = dateTransaction;
        this.tokenTransaction = tokenTransaction;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "idTransaction=" + idTransaction +
                ", amount=" + amount +
                ", dateTransaction=" + dateTransaction +
                ", tokenTransaction='" + tokenTransaction + '\'' +
                '}';
    }
}
