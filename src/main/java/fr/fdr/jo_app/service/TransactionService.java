package fr.fdr.jo_app.service;

import fr.fdr.jo_app.pojo.Ticket;
import fr.fdr.jo_app.pojo.Transaction;
import fr.fdr.jo_app.repository.TicketRepository;
import fr.fdr.jo_app.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TicketRepository ticketRepository;

    public Transaction processPurchase(List<Long> ticketIds, Double totalAmount) {
        // buy tickets
        Transaction transaction = new Transaction();
        transaction.setAmount(totalAmount);
        transaction.setDateTransaction(new Date(System.currentTimeMillis()));
        transaction.setTokenTransaction(generateTransactionToken());
        transaction = transactionRepository.save(transaction);

        // Update tickets
        List<Ticket> tickets = ticketRepository.findAllById(ticketIds);
        for (Ticket ticket : tickets) {
            ticket.setTransaction(transaction);
            ticket.prepareData();
            ticketRepository.save(ticket);
        }

        return transaction;
    }

    private String generateTransactionToken() {
        return "TRX-" + System.currentTimeMillis();
    }
}

