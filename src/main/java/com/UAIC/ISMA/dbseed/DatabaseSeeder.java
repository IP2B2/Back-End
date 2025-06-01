package com.UAIC.ISMA.dbseed;

import com.UAIC.ISMA.entity.*;
import com.UAIC.ISMA.entity.enums.*;
import com.UAIC.ISMA.repository.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

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
    public void run(String... args) throws JsonProcessingException {
        Faker faker = new Faker(new Locale("ro"));
        Random random = new Random();

        // Seed roles
        if (roleRepository.findByRoleName(RoleName.ADMIN) == null)
            roleRepository.save(new Role(RoleName.ADMIN, "Administrator"));
        if (roleRepository.findByRoleName(RoleName.STUDENT) == null)
            roleRepository.save(new Role(RoleName.STUDENT, "Student"));
        if (roleRepository.findByRoleName(RoleName.RESEARCHER) == null)
            roleRepository.save(new Role(RoleName.RESEARCHER, "Researcher"));
        if (roleRepository.findByRoleName(RoleName.COORDONATOR) == null)
            roleRepository.save(new Role(RoleName.COORDONATOR, "Coordonator"));

        List<Role> roles = roleRepository.findAll();

        List<Laboratory> laboratories = new ArrayList<>();
        for (int i = 0; i < 200; i++) {
            Laboratory lab = new Laboratory(
                    "Laborator " + faker.university().name() + " " + (i + 1),
                    faker.company().industry(),
                    "Etaj " + (random.nextInt(4) + 1)
            );
            laboratories.add(labRepo.save(lab));
        }

        List<Equipment> equipments = new ArrayList<>();
        for (int i = 0; i < 200; i++) {
            Laboratory lab = laboratories.get(random.nextInt(laboratories.size()));
            Equipment eq = new Equipment(
                    "Echipament " + faker.commerce().productName() + " " + (i + 1),
                    "INV" + (1000 + i),
                    LocalDateTime.now().minusDays(random.nextInt(700)),
                    AvailabilityStatus.values()[i % AvailabilityStatus.values().length],
                    "Badge" + (1000 + i),
                    lab
            );
            eq.setPhoto(new ObjectMapper().writeValueAsString(List.of("https://i.imgur.com/s2dxEyx.png")));
            eq.setUsage(faker.lorem().sentence(6));
            eq.setMaterial(faker.commerce().material());
            eq.setDescription(faker.lorem().paragraph(2));
            eq.setIsComplex(random.nextBoolean());
            equipments.add(equipmentRepo.save(eq));
        }

        seedUserIfNotExists("admin@student.uaic.ro", "Admin User", "admin123", RoleName.ADMIN,
                "Admin", "User", "Informatica", null, null, "ADM001");
        seedUserIfNotExists("coordonator@student.uaic.ro", "Coordonator User", "coordonator123", RoleName.COORDONATOR,
                "Coordonator", "User", "Informatica", null, null, "COORD001");
        seedUserIfNotExists("researcher@student.uaic.ro", "Researcher User", "researcher123", RoleName.RESEARCHER,
                "Researcher", "User", "Informatica", null, null, "RESEAR001");
        seedUserIfNotExists("student@student.uaic.ro", "Student User", "student123", RoleName.STUDENT,
                "Student", "User", "Informatica", "1", "D4", "STUD001");

        List<User> users = new ArrayList<>();
        for (int i = 0; i < 200; i++) {
            Role role = roles.get(i % roles.size());
            String firstName = "User";
            String lastName = "Name" + (i + 1);
            String email = (firstName + "." + lastName + "@uaic.ro").toLowerCase();
            String an = role.getRoleName() == RoleName.STUDENT ? String.valueOf(random.nextInt(4) + 1) : null;
            String grupa = role.getRoleName() == RoleName.STUDENT ? "G" + (random.nextInt(10) + 1) : null;
            String nrMarca = switch (role.getRoleName()) {
                case STUDENT -> "ST";
                case RESEARCHER -> "RE";
                case COORDONATOR -> "CO";
                default -> "AD";
            } + (10000 + i);
            String status = random.nextBoolean() ? "active" : "inactive";
            User user = new User(null, firstName + " " + lastName, email, passwordEncoder.encode("password"),
                    status, firstName, lastName, faker.university().name(), an, grupa, nrMarca, role,
                    null, null, null, null, null);
            users.add(userRepository.save(user));
        }

        // Ensure each equipment has at least 3 approved access requests
        for (Equipment eq : equipments) {
            for (int j = 0; j < 3; j++) {
                User user = users.get(random.nextInt(users.size()));
                LocalDateTime requestDate = LocalDateTime.now().minusDays(random.nextInt(30));
                AccessRequest ar = AccessRequest.builder()
                        .user(user)
                        .equipment(eq)
                        .requestDate(requestDate)
                        .expectedReturnDate(requestDate.plusDays(random.nextInt(5) + 1))
                        .status(RequestStatus.APPROVED)
                        .requestType(RequestType.values()[random.nextInt(RequestType.values().length)])
                        .proposalFile("cerere_init_" + eq.getId() + "_" + j + ".pdf")
                        .borrowerCNP(String.valueOf(random.nextInt(9) + 1) + String.valueOf(random.nextInt(899999999) + 100000000) + String.valueOf(random.nextInt(899) + 100))
                        .borrowerAddress(faker.address().fullAddress())
                        .build();
                ar = accessRequestRepo.save(ar);

                approvalRepo.save(new RequestApproval(ApprovalStatus.APPROVED, ar, user, faker.lorem().sentence(5)));
                docRepo.save(new RequestDocument("Document_INIT_" + j, faker.lorem().sentence(10), "/files/doc_INIT_" + j + ".pdf", ar, user));
                virtualAccessRepo.save(new VirtualAccess("virt_" + user.getUsername().toLowerCase().replace(" ", "") + "_init_" + j, "virtpass_init" + j, ar));
                auditRepo.save(new AuditLog("APPROVE_REQUEST", "Approved during seeding", user));
                notificationRepo.save(new Notification("Cererea ta a fost aprobată automat.", user));
            }
        }

        for (int i = 0; i < 200; i++) {
            User user = users.get(random.nextInt(users.size()));
            Equipment equipment = equipments.get(random.nextInt(equipments.size()));
            LocalDateTime requestDate = LocalDateTime.now().minusDays(random.nextInt(30));
            AccessRequest ar = AccessRequest.builder()
                    .user(user)
                    .equipment(equipment)
                    .requestDate(requestDate)
                    .expectedReturnDate(requestDate.plusDays(random.nextInt(5) + 1))
                    .status(RequestStatus.values()[i % RequestStatus.values().length])
                    .requestType(RequestType.values()[i % RequestType.values().length])
                    .proposalFile("cerere_" + i + ".pdf")
                    .borrowerCNP(String.valueOf(random.nextInt(9) + 1) + String.valueOf(random.nextInt(899999999) + 100000000) + String.valueOf(random.nextInt(899) + 100))
                    .borrowerAddress(faker.address().fullAddress())
                    .build();
            ar = accessRequestRepo.save(ar);

            approvalRepo.save(new RequestApproval(ApprovalStatus.values()[i % ApprovalStatus.values().length], ar, user, faker.lorem().sentence(5)));
            docRepo.save(new RequestDocument("Document " + i, faker.lorem().sentence(10), "/files/document" + i + ".pdf", ar, user));
            virtualAccessRepo.save(new VirtualAccess("virt_" + user.getUsername().toLowerCase().replace(" ", "") + "_" + i, "virtpass" + i, ar));
            auditRepo.save(new AuditLog(faker.hacker().verb().toUpperCase() + "_REQUEST", faker.lorem().sentence(10), user));
            notificationRepo.save(new Notification(faker.lorem().sentence(12), user));
        }

        for (int i = 0; i < 200; i++) {
            Laboratory lab = laboratories.get(random.nextInt(laboratories.size()));
            labDocRepo.save(LabDocument.builder()
                    .filename("Document_" + i + ".pdf")
                    .fileType("application/pdf")
                    .version("v" + (random.nextInt(3) + 1) + ".0")
                    .filePath("/docs/document" + i + ".pdf")
                    .lab(lab)
                    .build());
        }
    }

    private void seedUserIfNotExists(String email, String username, String rawPassword, RoleName roleName,
                                     String firstName, String lastName, String facultate, String an, String grupa, String nrMarca) {
        if (!userRepository.existsByEmail(email)) {
            Role role = roleRepository.findByRoleName(roleName);
            User user = new User(null, username, email, passwordEncoder.encode(rawPassword), "active", firstName, lastName,
                    facultate, an, grupa, nrMarca, role, null, null, null, null, null
            );
            userRepository.save(user);
        }
    }
}
