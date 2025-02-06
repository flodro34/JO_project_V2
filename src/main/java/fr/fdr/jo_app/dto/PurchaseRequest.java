package fr.fdr.jo_app.dto;

import lombok.Data;

import java.util.List;

@Data
public class PurchaseRequest {

    // tickets to buy
    private List<Long> ticketIds;

    private Double totalAmount;


    public PurchaseRequest() {}
}
