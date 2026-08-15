package byurens.service;

import java.time.LocalDateTime;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import byurens.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationCleanupService {
    private final NotificationRepository notificationRepository;

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void deleteOldNotification() {
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        long deletedCount = notificationRepository.deleteByCreatedAtBefore(thirtyDaysAgo);
        log.info("Notification cleanup trigger. Removed {} old notifications.", deletedCount);
    }
}