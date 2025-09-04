package app.entities;

import app.DeliveryStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
public class Parcel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "trackingNumber", unique = true, nullable = false, length = 100)
    private String trackingNumber;
    @Column(name = "senderName", nullable = false, length = 100)
    private String senderName;
    @Column(name = "receiverName", nullable = false, length = 100)
    private String receiverName;
    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;
    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    @PrePersist
    protected void onCreate(){
        if(lastUpdated == null){
            lastUpdated = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate(){
        this.lastUpdated = LocalDateTime.now();
    }
}
