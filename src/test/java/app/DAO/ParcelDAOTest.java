package app.DAO;

import app.DeliveryStatus;
import app.HibernateConfig.HibernateConfig;
import app.entities.Parcel;
import app.populators.ParcelPopulator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

class ParcelDAOTest {
    private static final EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryForTest();
    private static final ParcelDAO parcelDAO = ParcelDAO.getInstance(emf);
    private static Parcel p1;
    private static Parcel p2;

    @BeforeEach
    void setUp() {
        try(EntityManager em= emf.createEntityManager()){
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Parcel").executeUpdate();
            em.createNativeQuery("ALTER SEQUENCE parcel_id_seq RESTART WITH 1");
            em.getTransaction().commit();
            Parcel[] parcels = ParcelPopulator.populator(parcelDAO);
            p1 = parcels[0];
            p2 = parcels[1];
        }
    }

    @AfterEach
    void tearDown() {
        if(emf !=null && emf.isOpen()){
            emf.close();
            System.out.println("EntityManagerFactory is closed");
        }
    }

    @Test
    void getInstance() {
        assertNotNull(emf);
    }

    @Test
    void create() {
        Parcel p3 = Parcel.builder()
                .trackingNumber("testParcelCreate")
                .senderName("testSender")
                .receiverName("testModtager")
                .deliveryStatus(DeliveryStatus.PENDING)
                .lastUpdated(LocalDateTime.now())
                .build();
        p3 = parcelDAO.create(p3);
        assertNotNull(p3.getId()); // tjekker om id ikk er null
        List<Parcel> parcels = parcelDAO.findAll();
        assertEquals(3,parcels.size()); //tjekker om listen er blevet større
    }

    @Test
    void findByTrackingNumber() {
        Optional<Parcel> found = parcelDAO.findByTrackingNumber(p1.getTrackingNumber());//søger efter en parcel som findes
        assertTrue(found.isPresent()); //parcel findes
        assertEquals(p1.getTrackingNumber(), found.get().getTrackingNumber());
    }

    @Test
    void findAll() {
        List<Parcel> parcels =parcelDAO.findAll();
        assertEquals(2, parcels.size());
    }

    @Test
    void updateStatus() {
        Parcel updatedStatus = parcelDAO.updateStatus(p1.getTrackingNumber(), DeliveryStatus.IN_TRANSIT);
        assertNotNull(updatedStatus); //tjekker at vi får en parcel tilbage
        assertEquals(DeliveryStatus.IN_TRANSIT,updatedStatus.getDeliveryStatus());//tjekker om statusen er opdateret
    }

    @Test
    void deleteByTrackingNumber() {
        parcelDAO.deleteByTrackingNumber(p1.getTrackingNumber());
        List<Parcel> parcels = parcelDAO.findAll();
        assertEquals(1,parcels.size()); // tjekker der kun er  parcel tilbage i listen
        assertEquals(p2.getId(), parcels.get(0).getId()); // bekræfter p2 stadig er på liste, efter p1 er slettet
    }

    @Test
    void findByStatus() {
        List<Parcel> found = parcelDAO.findByStatus(p1.getDeliveryStatus());//
        assertEquals(p1.getDeliveryStatus(), found.get(0).getDeliveryStatus());
    }
}