package byurens.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import byurens.entities.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    
}
