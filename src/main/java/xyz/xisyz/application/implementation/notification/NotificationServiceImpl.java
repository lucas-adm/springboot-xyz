package xyz.xisyz.application.implementation.notification;

import jakarta.annotation.Nullable;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import xyz.xisyz.application.dto.notification.MessageNotification;
import xyz.xisyz.domain.notification.Notification;
import xyz.xisyz.domain.notification.NotificationRepository;
import xyz.xisyz.domain.notification.NotificationService;
import xyz.xisyz.domain.user.User;

import java.util.Objects;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository repository;

    @Override
    public void notify(@Nullable User toUser, User user, MessageNotification message) {
        if (toUser == null) return;
        if (Objects.equals(toUser.getId(), user.getId())) return;
        repository.save(new Notification(toUser, user, message.info()));
    }

    @Override
    public void readNotification(UUID id) {
        Notification notification = repository.findById(id).orElseThrow(EntityNotFoundException::new);
        notification.setRead(true);
        repository.save(notification);
    }

    @Override
    public Page<Notification> getNotifications(Pageable pageable, UUID idFromToken) {
        Page<Notification> notifications = repository.findAllByUserId(pageable, idFromToken);
        Page<Notification> snapshot = new PageImpl<>(
                notifications.getContent().stream().map(Notification::new).toList(),
                pageable,
                notifications.getTotalElements()
        );
        notifications.forEach(notification -> readNotification(notification.getId()));
        return snapshot;
    }

}