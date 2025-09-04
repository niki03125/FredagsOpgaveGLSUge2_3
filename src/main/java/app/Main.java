package app;

import app.DAO.ParcelDAO;
import app.HibernateConfig.HibernateConfig;
import app.entities.Parcel;
import app.exceptions.ApiException;
import jakarta.persistence.EntityManagerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class Main {
    private static final EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
    private static final ParcelDAO parcelDAO = ParcelDAO.getInstance(emf);

    public static void main(String[] args) {
        try{
            Parcel parcel1 = Parcel.builder()
                        .trackingNumber("nyttracking")
                        .senderName("mortenSender")
                        .receiverName("mortenModtager")
                        .deliveryStatus(DeliveryStatus.PENDING)
                        .lastUpdated(LocalDateTime.now())
                        .build();
            parcel1 = parcelDAO.create(parcel1);
            System.out.println(parcel1);
            runMethodes();
        }catch (ApiException e){
            System.out.println(e.getMessage());
        }finally {
            shutDown();
        }
    }

    private static void shutDown(){
        if(emf !=null && emf.isOpen()){
            emf.close();
            System.out.println("EntityManagerFactory is closed");
        }
    }

    private static void runMethodes(){

        //opgave 1 : create
        Parcel p1 = Parcel.builder()
                .trackingNumber("456DEF")
                .senderName("mortenSender")
                .receiverName("mortenModtager")
                .deliveryStatus(DeliveryStatus.PENDING)
                .lastUpdated(LocalDateTime.now())
                .build();
        p1 = parcelDAO.create(p1);
        System.out.println("\n1. created Parcel " + p1);

        Parcel p2 = Parcel.builder()
                .trackingNumber("123ABC")
                .senderName("nikiSender")
                .receiverName("nikiModtager")
                .deliveryStatus(DeliveryStatus.PENDING)
                .lastUpdated(LocalDateTime.now())
                .build();
        p2 = parcelDAO.create(p2);
        System.out.println("1. created another Parcel " + p2);

        // opgave 2: findByTrackingNumber
        Optional<Parcel> findByTrackingNumber = parcelDAO.findByTrackingNumber("123ABC");
        System.out.println("this is the parcel info for the given trackingNumber " + findByTrackingNumber.toString());

        // opgave 3: findAll
        List<Parcel> all =parcelDAO.findAll();
        System.out.println("Dette er alle de ansatte: ");
        all.forEach(System.out::println);

        //opgave 4: updateStatus
        Parcel UpdateStatus = parcelDAO.updateStatus("123ABC", DeliveryStatus.IN_TRANSIT);
        System.out.println("\nUpdated delivery status to: " + UpdateStatus);

        //opgave 5: deleteByTrackingNumber
        boolean deleted = parcelDAO.deleteByTrackingNumber("456DEF");
        if (deleted){
            System.out.println("The parcel trackingNumber that has been deleted is: " + deleted);
        }else {
            System.out.println("no student has that id: " + deleted);
        }

        //opgave 6: findByStatus
        List<Parcel> found = parcelDAO.findByStatus(DeliveryStatus.IN_TRANSIT);
        System.out.println("The parcels with this delivery status: ");
        found.forEach(System.out::println);
    }

}