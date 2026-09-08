package byurens.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import byurens.service.NotificationService;
import lombok.RequiredArgsConstructor;

@RestController 
@RequestMapping("/api/notifications")
@RequiredArgsConstructor 
public class NotificationController {
    private final NotificationService notificationService;

    @PatchMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable UUID id) {
        notificationService.markAsRead(id);
        return ResponseEntity.noContent().build();
    }
}
