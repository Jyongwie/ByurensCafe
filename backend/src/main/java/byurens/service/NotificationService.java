package byurens.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import byurens.entities.InventoryItem;
import byurens.entities.Notification;
import byurens.entities.Staff;
import byurens.exception.ByurensCafeException;
import byurens.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;

    @Transactional
    public void createLowStockNotification(InventoryItem inventoryItem, List<Staff> recipients) {
        if (inventoryItem.isLowStock()) {
            String title = inventoryItem.getName() + " in Low Stock";
            String message = inventoryItem.getName() + " current stock: " + inventoryItem.getCurrentStock();

            List<Notification> notifications = new ArrayList<>();

            for (Staff staff : recipients) {
                Notification notification = Notification.builder()
                    .recipient(staff)
                    .title(title)
                    .message(message)
                    .build();
                notifications.add(notification);
            }

            notificationRepository.saveAll(notifications);
        }
    }

    @Transactional
    public void markAsRead(UUID id) {
        Notification notification = notificationRepository.findById(id)
            .orElseThrow(() -> new ByurensCafeException("Notification not found"));

        notification.setRead(true);
    }
}
