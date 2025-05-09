package com.UAIC.ISMA.service;

import com.UAIC.ISMA.dao.Notification;
import com.UAIC.ISMA.dto.NotificationDTO;
import com.UAIC.ISMA.exception.NotificationNotFoundException;
import com.UAIC.ISMA.mapper.NotificationMapper;
import com.UAIC.ISMA.repository.NotificationRepository;
import com.UAIC.ISMA.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository,
                               UserRepository userRepository) {
        this.notificationRepository = notificationRepository;
    }

    public List<NotificationDTO> findAll(Long userId) {
        return notificationRepository.findAll()
                .stream()
                .map(NotificationMapper::toDTO)
                .collect(Collectors.toList());
    }

    public NotificationDTO findById(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new NotificationNotFoundException(id));
        return NotificationMapper.toDTO(notification);
    }

    public NotificationDTO create(NotificationDTO dto) {
        Notification entity = NotificationMapper.toEntity(dto);
        Notification savedNotification = notificationRepository.save(entity);
        return NotificationMapper.toDTO(savedNotification);
    }

    public NotificationDTO update(Long id, NotificationDTO dto) {
        Notification existing = notificationRepository.findById(id)
                .orElseThrow(() -> new NotificationNotFoundException(id));

        existing.setMessage(dto.getMessage());
        existing.setReadStatus(dto.getReadStatus());

        Notification updatedNotification = notificationRepository.save(existing);
        return NotificationMapper.toDTO(updatedNotification);
    }

    public void delete(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new NotificationNotFoundException(id));
        notificationRepository.delete(notification);
    }
}
