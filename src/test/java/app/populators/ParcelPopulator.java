package app.populators;

import app.DAO.ParcelDAO;
import app.DeliveryStatus;
import app.entities.Parcel;

import java.time.LocalDateTime;

public class ParcelPopulator {

    public static Parcel[] populator(ParcelDAO parcelDAO){
        Parcel p1 = Parcel.builder()
            .trackingNumber("456DEF")
            .senderName("mortenSender")
            .receiverName("mortenModtager")
            .deliveryStatus(DeliveryStatus.PENDING)
            .lastUpdated(LocalDateTime.now())
            .build();
            p1 = parcelDAO.create(p1);

        Parcel p2 = Parcel.builder()
                .trackingNumber("123ABC")
                .senderName("nikiSender")
                .receiverName("nikiModtager")
                .deliveryStatus(DeliveryStatus.PENDING)
                .lastUpdated(LocalDateTime.now())
                .build();
        p2 = parcelDAO.create(p2);


    return new Parcel[]{p1,p2};
    }


}
