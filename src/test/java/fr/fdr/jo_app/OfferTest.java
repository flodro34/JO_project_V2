package fr.fdr.jo_app;

import fr.fdr.jo_app.pojo.Offer;
import fr.fdr.jo_app.pojo.OfferType;
import fr.fdr.jo_app.repository.OfferRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class OfferTest {

    @Autowired
    private OfferRepository offerRepository;

    private Offer offer;

    @BeforeEach
    public void setup() {
        offerRepository.deleteAll(); // Nettoie la base avant chaque test

        // Création et sauvegarde d'une offre
        offer = new Offer(49.99, OfferType.SOLO);
        offer = offerRepository.save(offer);
    }

    @Test
    void testCreateAndRetrieveOffer() {
        // Act
        Optional<Offer> retrievedOffer = offerRepository.findById(offer.getIdOffer());

        // Assert
        assertTrue(retrievedOffer.isPresent(), "L'offre devrait exister !");
        assertEquals(49.99, retrievedOffer.get().getPrice(), "Le prix de l'offre est incorrect !");
        assertEquals(OfferType.SOLO, retrievedOffer.get().getType(), "Le type de l'offre est incorrect !");
    }

    @Test
    void testUpdateOffer() {
        // Arrange
        offer.setPrice(59.99);

        // Act
        offer = offerRepository.save(offer);
        Offer updatedOffer = offerRepository.findById(offer.getIdOffer()).orElse(null);

        // Assert
        assertNotNull(updatedOffer, "L'offre mise à jour ne devrait pas être null !");
        assertEquals(59.99, updatedOffer.getPrice(), "Le prix de l'offre n'a pas été mis à jour !");
    }

    @Test
    void testDeleteOffer() {
        // Arrange
        Long offerId = offer.getIdOffer();

        // Act
        offerRepository.delete(offer);
        Optional<Offer> deletedOffer = offerRepository.findById(offerId);

        // Assert
        assertTrue(deletedOffer.isEmpty(), "L'offre devrait être supprimée !");
    }
}
