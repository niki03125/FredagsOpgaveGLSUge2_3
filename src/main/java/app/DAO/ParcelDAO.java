package app.DAO;

import app.DeliveryStatus;
import app.entities.Parcel;
import app.exceptions.ApiException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;
import java.util.Optional;

public class ParcelDAO {
    private static EntityManagerFactory emf;
    private static ParcelDAO instance;

    public ParcelDAO(){
    }

    public static ParcelDAO getInstance(EntityManagerFactory _emf){
        if (emf == null) {
            emf = _emf;
            instance = new ParcelDAO();
        }
        return instance;
    }

    public static Parcel create(Parcel parcel){
        EntityManager em = emf.createEntityManager();
        try{
            em.getTransaction().begin();
            em.persist(parcel);
            em.getTransaction().commit();
            return parcel;
        } catch (ApiException e){
            em.getTransaction().rollback();
            throw new ApiException(401, e.getMessage());
        }finally{
            em.close();
        }
    }

    public Optional<Parcel> findByTrackingNumber(String trackingNumber){
        EntityManager em = emf.createEntityManager();
        try{
            em.getTransaction().begin();
            Parcel found = em.createQuery("SELECT p FROM Parcel p WHERE p.trackingNumber = :trackingNumber", Parcel.class)
                            .setParameter("trackingNumber", trackingNumber)
                                    .getSingleResult();
            return Optional.ofNullable(found);
        }catch (ApiException e){
            em.getTransaction().rollback();
            throw new ApiException(401, e.getMessage());
        }finally {
            em.close();
        }
    }

    public List<Parcel> findAll(){
        EntityManager em = emf.createEntityManager();
        try{
            em.getTransaction().begin();
            List<Parcel> allParcels = em.createQuery("SELECT p FROM Parcel p", Parcel.class)
                    .getResultList();
            return allParcels;
        }catch (ApiException e){
            em.getTransaction().rollback();
            throw new ApiException(401, e.getMessage());
        }finally {
            em.close();
        }
    }

    public Parcel updateStatus(String trackingNumber, DeliveryStatus deliveryStatus){
        EntityManager em = emf.createEntityManager();
        try{
            em.getTransaction().begin();
            int updated = em.createQuery("UPDATE Parcel p SET p.deliveryStatus =:deliveryStatus WHERE p.trackingNumber =:trackingNumber")
                    .setParameter("deliveryStatus", deliveryStatus)
                    .setParameter("trackingNumber",trackingNumber)
                    .executeUpdate();
            em.getTransaction().commit();
            if(updated == 0){
                throw new ApiException(401, "No Parcel found with trackingNumber: " + trackingNumber);
            }
            return em.createQuery("SELECT p FROM Parcel p WHERE p.trackingNumber =:trackingNumber", Parcel.class)
                    .setParameter("trackingNumber", trackingNumber)
                    .getSingleResult();
        }catch (ApiException e){
            em.getTransaction().rollback();
            throw new ApiException(401, e.getMessage());
        }finally {
            em.close();
        }
    }


    public boolean deleteByTrackingNumber(String trackingNumber){
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            int deleteParcel = em.createQuery("DELETE FROM Parcel p WHERE p.trackingNumber =:trackingNumber")
                    .setParameter("trackingNumber", trackingNumber)
                    .executeUpdate();
            em.getTransaction().commit();
            return deleteParcel > 0;
        }catch (ApiException e){
            em.getTransaction().rollback();
            throw new ApiException(401, e.getMessage());
        }finally {
            em.close();
        }
    }

    public List<Parcel> findByStatus(DeliveryStatus deliveryStatus){
        EntityManager em = emf.createEntityManager();
        try{
            em.getTransaction().begin();
            return em.createQuery("SELECT p FROM Parcel p WHERE p.deliveryStatus =:deliveryStatus", Parcel.class)
                    .setParameter("deliveryStatus", deliveryStatus)
                    .getResultList();
        }catch (ApiException e){
            em.getTransaction().rollback();
            throw new ApiException(401, e.getMessage());
        }finally {
         em.close();
         }
    }

}
