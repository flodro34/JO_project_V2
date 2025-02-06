package fr.fdr.jo_app.ws;

import fr.fdr.jo_app.dto.PurchaseRequest;
import fr.fdr.jo_app.pojo.Transaction;
import fr.fdr.jo_app.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiRegistration.API + ApiRegistration.REST_TRANSACTION)
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping
    public ResponseEntity<Transaction> buyTickets(@RequestBody PurchaseRequest request) {
        Transaction transaction = transactionService.processPurchase(request.getTicketIds(), request.getTotalAmount());
        return ResponseEntity.ok(transaction);
    }

}
