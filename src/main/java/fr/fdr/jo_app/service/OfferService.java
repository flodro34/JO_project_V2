package fr.fdr.jo_app.service;

import fr.fdr.jo_app.pojo.Offer;
import fr.fdr.jo_app.repository.OfferRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OfferService {

    @Autowired
    OfferRepository offerRepository;

    @Autowired
    ModelMapper modelMapper;

    public Offer createOffer(Offer offer) {
        return offerRepository.save(offer);
    }

    public List<Offer> getAllOffers() {
        return offerRepository.findAll();
    }

    public Offer getOfferById(Long id) {
        return offerRepository.findById(id).stream()
                .filter(offer -> offer.getIdOffer().equals(id))
                        .findAny()
                        .orElse(null);
    }

    public Offer updateOffer(Offer offer) {
        Offer oldOffer = getOfferById(offer.getIdOffer());
        if(oldOffer != null) {
            oldOffer.setType(offer.getType());
            oldOffer.setPrice(offer.getPrice());
        }
        return oldOffer;
    }

    public void deleteOffer(Long id) {
        offerRepository.deleteById(id);
    }

}
