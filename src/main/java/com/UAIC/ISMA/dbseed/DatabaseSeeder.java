package com.UAIC.ISMA.dbseed;

import com.UAIC.ISMA.entity.*;
import com.UAIC.ISMA.entity.enums.*;
import com.UAIC.ISMA.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    private final LaboratoryRepository labRepo;
    private final EquipmentRepository equipmentRepo;
    private final AccessRequestRepository accessRequestRepo;
    private final RequestApprovalRepository approvalRepo;
    private final RequestDocumentRepository docRepo;
    private final VirtualAccessRepository virtualAccessRepo;
    private final AuditLogRepository auditRepo;
    private final NotificationRepository notificationRepo;
    private final LabDocumentRepository labDocRepo;

    public DatabaseSeeder(UserRepository userRepository,
                          RoleRepository roleRepository,
                          PasswordEncoder passwordEncoder,
                          LaboratoryRepository labRepo,
                          EquipmentRepository equipmentRepo,
                          AccessRequestRepository accessRequestRepo,
                          RequestApprovalRepository approvalRepo,
                          RequestDocumentRepository docRepo,
                          VirtualAccessRepository virtualAccessRepo,
                          AuditLogRepository auditRepo,
                          NotificationRepository notificationRepo,
                          LabDocumentRepository labDocRepo) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.labRepo = labRepo;
        this.equipmentRepo = equipmentRepo;
        this.accessRequestRepo = accessRequestRepo;
        this.approvalRepo = approvalRepo;
        this.docRepo = docRepo;
        this.virtualAccessRepo = virtualAccessRepo;
        this.auditRepo = auditRepo;
        this.notificationRepo = notificationRepo;
        this.labDocRepo = labDocRepo;
    }

    @Override
    public void run(String... args) {
        for (RoleName roleName : RoleName.values()) {
            seedRoleIfNotExists(roleName);
        }

        seedUserIfNotExists("admin@student.uaic.ro", "Admin User", "admin123", RoleName.ADMIN);
        seedUserIfNotExists("coordonator@student.uaic.ro", "Coordonator User", "coordonator123", RoleName.COORDONATOR);
        seedUserIfNotExists("researcher@student.uaic.ro", "Researcher User", "researcher123", RoleName.RESEARCHER);
        seedUserIfNotExists("student@student.uaic.ro", "Student User", "student123", RoleName.STUDENT);

        if (labRepo.count() == 0) {
            Laboratory lab1 = labRepo.save(new Laboratory("Lab A", "Microscopie", "Etaj 1"));
            Laboratory lab2 = labRepo.save(new Laboratory("Lab B", "Chimie", "Etaj 2"));

            Equipment eq1 = equipmentRepo.save(new Equipment("Microscop X", "INV123", LocalDate.now().minusMonths(2), AvailabilityStatus.AVAILABLE, "Badge123", lab1));
            Equipment eq2 = equipmentRepo.save(new Equipment("Spectrometru Y", "INV456", LocalDate.now().minusMonths(1), AvailabilityStatus.IN_USE, "Badge456", lab2));

            User student = userRepository.findByEmail("student@student.uaic.ro");
            if (student != null) {
                AccessRequest request = AccessRequest.builder()
                        .user(student)
                        .equipment(eq1)
                        .status(RequestStatus.PENDING)
                        .requestType(RequestType.PHYSICAL)
                        .requestDate(LocalDateTime.now())
                        .build();

                request = accessRequestRepo.save(request);


                approvalRepo.save(new RequestApproval(ApprovalStatus.PENDING, request, student, "Initial verification"));
                docRepo.save(new RequestDocument("Cerere Microscop", "Cerere pentru acces microscop", "/files/microscop.pdf", request, student));
                virtualAccessRepo.save(new VirtualAccess("virt_student", passwordEncoder.encode("virtpass"), request));

                auditRepo.save(new AuditLog("CREATE_REQUEST", "Studentul a creat o cerere de acces", student));
                notificationRepo.save(new Notification("Cererea ta a fost înregistrată.", student));
            }

            labDocRepo.save(LabDocument.builder()
                    .filename("Regulament Lab A")
                    .fileType("application/pdf")
                    .version("v1.0")
                    .filePath("/docs/lab-a.pdf")
                    .lab(lab1)
                    .build());

        }
    }

    private void seedRoleIfNotExists(RoleName roleName) {
        if (roleRepository.findByRoleName(roleName) == null) {
            Role role = new Role();
            role.setRoleName(roleName);
            roleRepository.save(role);
        }
    }

    private void seedUserIfNotExists(String email, String username, String rawPassword, RoleName roleName) {
        if (!userRepository.existsByEmail(email)) {
            Role role = roleRepository.findByRoleName(roleName);
            User user = new User();
            user.setEmail(email);
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(rawPassword));
            user.setRole(role);
            user.setStatus("active");
            userRepository.save(user);
        }
    }
}
