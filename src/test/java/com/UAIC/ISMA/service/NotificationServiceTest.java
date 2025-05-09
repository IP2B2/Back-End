    package com.UAIC.ISMA.service;

    import com.UAIC.ISMA.dao.Notification;
    import com.UAIC.ISMA.dto.NotificationDTO;
    import com.UAIC.ISMA.exception.NotificationNotFoundException;
    import com.UAIC.ISMA.repository.NotificationRepository;
    import com.UAIC.ISMA.repository.UserRepository;
    import org.junit.jupiter.api.BeforeEach;
    import org.junit.jupiter.api.Test;
    import org.mockito.InjectMocks;
    import org.mockito.Mock;
    import org.mockito.MockitoAnnotations;

    import java.util.List;
    import java.util.Optional;

    import static org.junit.jupiter.api.Assertions.*;
    import static org.mockito.Mockito.*;

    class NotificationServiceTest {

        @Mock
        private NotificationRepository notificationRepository;

        @Mock
        private UserRepository userRepository;

        @InjectMocks
        private NotificationService notificationService;

        private Notification notification;
        private NotificationDTO notificationDTO;

        @BeforeEach
        void setUp() {
            MockitoAnnotations.openMocks(this);

            notification = new Notification();
            notification.setId(1L);
            notification.setMessage("Test message");
            notification.setReadStatus(false);

            notificationDTO = new NotificationDTO();
            notificationDTO.setId(1L);
            notificationDTO.setMessage("Test message");
            notificationDTO.setReadStatus(false);
        }

        @Test
        void testFindById_Success() {
            when(notificationRepository.findById(1L)).thenReturn(Optional.of(notification));

            NotificationDTO result = notificationService.findById(1L);

            assertNotNull(result);
            assertEquals(notification.getId(), result.getId());
            assertEquals(notification.getMessage(), result.getMessage());
            assertEquals(notification.getReadStatus(), result.getReadStatus());
        }

        @Test
        void testFindById_NotificationNotFound() {
            when(notificationRepository.findById(1L)).thenReturn(Optional.empty());

            assertThrows(NotificationNotFoundException.class, () -> notificationService.findById(1L));
        }

        @Test
        void testCreate_Success() {
            when(notificationRepository.save(any(Notification.class))).thenReturn(notification);

            NotificationDTO result = notificationService.create(notificationDTO);

            assertNotNull(result);
            assertEquals(notification.getId(), result.getId());
            assertEquals(notification.getMessage(), result.getMessage());
            assertEquals(notification.getReadStatus(), result.getReadStatus());
        }

        @Test
        void testCreate_Fail_NullNotificationDTO() {
            assertThrows(NullPointerException.class, () -> notificationService.create(null));
        }

        @Test
        void testUpdate_Success() {
            when(notificationRepository.findById(1L)).thenReturn(Optional.of(notification));
            when(notificationRepository.save(any(Notification.class))).thenReturn(notification);

            notificationDTO.setMessage("Updated message");
            notificationDTO.setReadStatus(true);
            NotificationDTO result = notificationService.update(1L, notificationDTO);

            assertNotNull(result);
            assertEquals("Updated message", result.getMessage());
            assertTrue(result.getReadStatus());
        }

        @Test
        void testUpdate_Fail_NotificationNotFound() {
            when(notificationRepository.findById(1L)).thenReturn(Optional.empty());

            assertThrows(NotificationNotFoundException.class, () -> notificationService.update(1L, notificationDTO));
        }

        @Test
        void testUpdate_Fail_NullNotificationDTO() {
            when(notificationRepository.findById(1L)).thenReturn(Optional.of(notification));

            assertThrows(NullPointerException.class, () -> notificationService.update(1L, null));
        }

        @Test
        void testDelete_Success() {
            when(notificationRepository.findById(1L)).thenReturn(Optional.of(notification));

            notificationService.delete(1L);

            verify(notificationRepository, times(1)).delete(notification);
        }

        @Test
        void testDelete_Fail_NotificationNotFound() {
            when(notificationRepository.findById(1L)).thenReturn(Optional.empty());

            assertThrows(NotificationNotFoundException.class, () -> notificationService.delete(1L));
        }

        @Test
        void testFindAll_Success() {
            Notification notification2 = new Notification();
            notification2.setId(2L);
            notification2.setMessage("Message 2");
            notification2.setReadStatus(true);

            when(notificationRepository.findAll()).thenReturn(List.of(notification, notification2));

            List<NotificationDTO> result = notificationService.findAll(1L);

            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals(notification.getId(), result.get(0).getId());
            assertEquals(notification2.getId(), result.get(1).getId());
        }

        @Test
        void testFindAll_Fail_EmptyList() {
            when(notificationRepository.findAll()).thenReturn(List.of());

            List<NotificationDTO> result = notificationService.findAll(1L);

            assertNotNull(result);
            assertTrue(result.isEmpty());
        }
    }
