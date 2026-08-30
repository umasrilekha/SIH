package com.govmesh.food.service;

import com.govmesh.food.entity.*;
import com.govmesh.food.repository.*;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
public class SeedDataService {

    private static final Logger log = LoggerFactory.getLogger(SeedDataService.class);

    private final UserRepository userRepository;
    private final RationRecordRepository rationRecordRepository;
    private final ApplicationRepository applicationRepository;
    private final AuditLogRepository auditLogRepository;
    private final NotificationRepository notificationRepository;
    private final ConsentRepository consentRepository;
    private final PasswordEncoder passwordEncoder;

    public SeedDataService(UserRepository userRepository,
                           RationRecordRepository rationRecordRepository,
                           ApplicationRepository applicationRepository,
                           AuditLogRepository auditLogRepository,
                           NotificationRepository notificationRepository,
                           ConsentRepository consentRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.rationRecordRepository = rationRecordRepository;
        this.applicationRepository = applicationRepository;
        this.auditLogRepository = auditLogRepository;
        this.notificationRepository = notificationRepository;
        this.consentRepository = consentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void seedInitialData() {
        if (userRepository.count() > 0) {
            log.info("Database already contains seed data. Skipping initial seeding.");
            return;
        }

        log.info("Seeding initial demo data for Food, Civil Supplies & Consumer Protection Department...");

        // 1. Seed Users
        User foodOfficer = User.builder()
                .username("food.officer")
                .passwordHash(passwordEncoder.encode("Food@123"))
                .fullName("Rajendra Sharma")
                .role("FOOD_SUPPLY_OFFICER")
                .department("Food & Civil Supplies")
                .employeeId("FOOD-EMP-001")
                .isActive(true)
                .build();

        User seniorOfficer = User.builder()
                .username("senior.officer")
                .passwordHash(passwordEncoder.encode("Senior@123"))
                .fullName("Sanjay Deshmukh")
                .role("SENIOR_OFFICER")
                .department("Food & Civil Supplies")
                .employeeId("FOOD-EMP-002")
                .isActive(true)
                .build();

        User admin = User.builder()
                .username("food.admin")
                .passwordHash(passwordEncoder.encode("Admin@123"))
                .fullName("Priya Kulkarni")
                .role("DEPARTMENT_ADMIN")
                .department("Food & Civil Supplies")
                .employeeId("FOOD-EMP-003")
                .isActive(true)
                .build();

        User auditor = User.builder()
                .username("auditor")
                .passwordHash(passwordEncoder.encode("Auditor@123"))
                .fullName("Vikramaditya Joshi")
                .role("AUDITOR")
                .department("State Audit Bureau")
                .employeeId("AUD-EMP-101")
                .isActive(true)
                .build();

        List<User> additionalOfficers = Arrays.asList(
                User.builder().username("sunita.patil").passwordHash(passwordEncoder.encode("Officer@123")).fullName("Sunita Patil").role("FOOD_SUPPLY_OFFICER").department("Food & Civil Supplies").employeeId("FOOD-EMP-005").isActive(true).build(),
                User.builder().username("anil.chavan").passwordHash(passwordEncoder.encode("Officer@123")).fullName("Anil Chavan").role("FOOD_SUPPLY_OFFICER").department("Food & Civil Supplies").employeeId("FOOD-EMP-006").isActive(true).build(),
                User.builder().username("meena.pawar").passwordHash(passwordEncoder.encode("Officer@123")).fullName("Meena Pawar").role("FOOD_SUPPLY_OFFICER").department("Food & Civil Supplies").employeeId("FOOD-EMP-007").isActive(true).build(),
                User.builder().username("ramesh.shinde").passwordHash(passwordEncoder.encode("Senior@123")).fullName("Ramesh Shinde").role("SENIOR_OFFICER").department("Food & Civil Supplies").employeeId("FOOD-EMP-008").isActive(true).build(),
                User.builder().username("deepak.more").passwordHash(passwordEncoder.encode("Officer@123")).fullName("Deepak More").role("FOOD_SUPPLY_OFFICER").department("Food & Civil Supplies").employeeId("FOOD-EMP-009").isActive(true).build(),
                User.builder().username("kavita.gaikwad").passwordHash(passwordEncoder.encode("Auditor@123")).fullName("Kavita Gaikwad").role("AUDITOR").department("State Audit Bureau").employeeId("AUD-EMP-102").isActive(true).build()
        );

        userRepository.saveAll(Arrays.asList(foodOfficer, seniorOfficer, admin, auditor));
        userRepository.saveAll(additionalOfficers);

        // 2. Seed Ration Records (20)
        List<RationRecord> rationRecords = Arrays.asList(
                RationRecord.builder().rationCardNo("MH12-2026-000124").holderName("Rajesh Kumar").houseAddress("12, M.G. Road, Shivajinagar, Pune").talukaCode("TAL-PUN-04").districtCode("DIST-PUN").verificationFlag(true).updateStatus("ACTIVE").build(),
                RationRecord.builder().rationCardNo("MH12-2026-000125").holderName("Aarti Suresh Patil").houseAddress("45, Lakshmi Chowk, Chinchwad, Pune").talukaCode("TAL-HAV-02").districtCode("DIST-PUN").verificationFlag(true).updateStatus("ACTIVE").build(),
                RationRecord.builder().rationCardNo("MH14-2026-000126").holderName("Ganesh Ramchandra Kulkarni").houseAddress("88, Station Road, Pimpri, Pune").talukaCode("TAL-HAV-02").districtCode("DIST-PUN").verificationFlag(true).updateStatus("ACTIVE").build(),
                RationRecord.builder().rationCardNo("MH12-2026-000127").holderName("Savita Vilas Shinde").houseAddress("101, Anand Nagar, Kothrud, Pune").talukaCode("TAL-PUN-04").districtCode("DIST-PUN").verificationFlag(false).updateStatus("PENDING_VERIFICATION").build(),
                RationRecord.builder().rationCardNo("MH15-2026-000128").holderName("Mahesh Dinkar Jadhav").houseAddress("14, College Road, Nashik").talukaCode("TAL-NSK-01").districtCode("DIST-NSK").verificationFlag(true).updateStatus("ACTIVE").build(),
                RationRecord.builder().rationCardNo("MH15-2026-000129").holderName("Pooja Nitin Bhosale").houseAddress("56, Panchavati, Nashik").talukaCode("TAL-NSK-01").districtCode("DIST-NSK").verificationFlag(true).updateStatus("ACTIVE").build(),
                RationRecord.builder().rationCardNo("MH04-2026-000130").holderName("Vijay Pandurang Thorat").houseAddress("202, Naupada, Thane").talukaCode("TAL-THN-01").districtCode("DIST-THN").verificationFlag(true).updateStatus("ACTIVE").build(),
                RationRecord.builder().rationCardNo("MH04-2026-000131").holderName("Smita Prabhakar Deshmukh").houseAddress("78, Ghodbunder Road, Thane").talukaCode("TAL-THN-01").districtCode("DIST-THN").verificationFlag(false).updateStatus("LOCKED").build(),
                RationRecord.builder().rationCardNo("MH09-2026-000132").holderName("Eknath Sadashiv More").houseAddress("33, Tarabai Park, Kolhapur").talukaCode("TAL-KLP-02").districtCode("DIST-KLP").verificationFlag(true).updateStatus("ACTIVE").build(),
                RationRecord.builder().rationCardNo("MH09-2026-000133").holderName("Sunanda Baburao Chavan").houseAddress("90, Shahupuri, Kolhapur").talukaCode("TAL-KLP-02").districtCode("DIST-KLP").verificationFlag(true).updateStatus("ACTIVE").build(),
                RationRecord.builder().rationCardNo("MH31-2026-000134").holderName("Ashok Wasudeo Kamble").houseAddress("15, Sitabuldi, Nagpur").talukaCode("TAL-NGP-01").districtCode("DIST-NGP").verificationFlag(true).updateStatus("ACTIVE").build(),
                RationRecord.builder().rationCardNo("MH31-2026-000135").holderName("Shilpa Arvind Wankhede").houseAddress("44, Dharampeth, Nagpur").talukaCode("TAL-NGP-01").districtCode("DIST-NGP").verificationFlag(true).updateStatus("ACTIVE").build(),
                RationRecord.builder().rationCardNo("MH20-2026-000136").holderName("Prakash Tarachand Jain").houseAddress("67, Kranti Chowk, Chhatrapati Sambhajinagar").talukaCode("TAL-CSN-01").districtCode("DIST-CSN").verificationFlag(true).updateStatus("ACTIVE").build(),
                RationRecord.builder().rationCardNo("MH20-2026-000137").holderName("Manjusha Madhav Rao").houseAddress("12, CIDCO Sector 3, Chhatrapati Sambhajinagar").talukaCode("TAL-CSN-01").districtCode("DIST-CSN").verificationFlag(true).updateStatus("ACTIVE").build(),
                RationRecord.builder().rationCardNo("MH13-2026-000138").holderName("Suryakant Bandu Solanke").houseAddress("89, Saat Rasta, Solapur").talukaCode("TAL-SLP-01").districtCode("DIST-SLP").verificationFlag(false).updateStatus("PENDING_VERIFICATION").build(),
                RationRecord.builder().rationCardNo("MH13-2026-000139").holderName("Rekha Govind Mote").houseAddress("23, Budhwar Peth, Solapur").talukaCode("TAL-SLP-01").districtCode("DIST-SLP").verificationFlag(true).updateStatus("ACTIVE").build(),
                RationRecord.builder().rationCardNo("MH11-2026-000140").holderName("Dattatray Narayan Pawar").houseAddress("55, Powai Naka, Satara").talukaCode("TAL-STR-01").districtCode("DIST-STR").verificationFlag(true).updateStatus("ACTIVE").build(),
                RationRecord.builder().rationCardNo("MH11-2026-000141").holderName("Geeta Hanmant Kadam").houseAddress("108, Rajwada, Satara").talukaCode("TAL-STR-01").districtCode("DIST-STR").verificationFlag(true).updateStatus("ACTIVE").build(),
                RationRecord.builder().rationCardNo("MH12-2026-000142").holderName("Nitin Shrikant Joshi").houseAddress("34, Hadapsar, Pune").talukaCode("TAL-PUN-04").districtCode("DIST-PUN").verificationFlag(true).updateStatus("ACTIVE").build(),
                RationRecord.builder().rationCardNo("MH12-2026-000143").holderName("Bhavna Sharad Mehta").houseAddress("76, Viman Nagar, Pune").talukaCode("TAL-HAV-02").districtCode("DIST-PUN").verificationFlag(true).updateStatus("ACTIVE").build()
        );
        rationRecordRepository.saveAll(rationRecords);

        // 3. Seed Applications (15) with realistic requestedAddress values
        List<Application> applications = Arrays.asList(
                Application.builder().applicationId("GM-2026-000124").citizenReference("CIT-MH-998811").rationCardNo("MH12-2026-000124").applicationType("ADDRESS_UPDATE").currentStatus("PENDING").sourceDepartment("REVENUE").requestedAddress("44 Example Road, Shivajinagar, Pune - 411005").build(),
                Application.builder().applicationId("GM-2026-000101").citizenReference("CIT-MH-998801").rationCardNo("MH12-2026-000125").applicationType("ADDRESS_UPDATE").currentStatus("UNDER_REVIEW").sourceDepartment("REVENUE").requestedAddress("99, Sector 21, Nigdi, Pimpri-Chinchwad, Pune - 411044").build(),
                Application.builder().applicationId("GM-2026-000102").citizenReference("CIT-MH-998802").rationCardNo("MH14-2026-000126").applicationType("MEMBER_ADDITION").currentStatus("APPROVED").sourceDepartment("REVENUE").requestedAddress("88, Station Road, Pimpri, Pune").build(),
                Application.builder().applicationId("GM-2026-000103").citizenReference("CIT-MH-998803").rationCardNo("MH12-2026-000127").applicationType("ADDRESS_UPDATE").currentStatus("PENDING").sourceDepartment("REVENUE").requestedAddress("205, Ideal Colony, Kothrud, Pune - 411038").build(),
                Application.builder().applicationId("GM-2026-000104").citizenReference("CIT-MH-998804").rationCardNo("MH15-2026-000128").applicationType("CARD_CATEGORY_CHANGE").currentStatus("APPROVED").sourceDepartment("REVENUE").requestedAddress("14, College Road, Nashik").build(),
                Application.builder().applicationId("GM-2026-000105").citizenReference("CIT-MH-998805").rationCardNo("MH15-2026-000129").applicationType("ADDRESS_UPDATE").currentStatus("REJECTED").sourceDepartment("REVENUE").requestedAddress("12, Gangapur Road, Nashik - 422005").officerComments("Incomplete address proof submitted.").build(),
                Application.builder().applicationId("GM-2026-000106").citizenReference("CIT-MH-998806").rationCardNo("MH04-2026-000130").applicationType("MEMBER_REMOVAL").currentStatus("APPROVED").sourceDepartment("REVENUE").requestedAddress("202, Naupada, Thane").build(),
                Application.builder().applicationId("GM-2026-000107").citizenReference("CIT-MH-998807").rationCardNo("MH04-2026-000131").applicationType("ADDRESS_UPDATE").currentStatus("INFORMATION_REQUIRED").sourceDepartment("REVENUE").requestedAddress("104, Majiwada, Thane West - 400601").officerComments("Please provide supporting Revenue 7/12 extract document.").build(),
                Application.builder().applicationId("GM-2026-000108").citizenReference("CIT-MH-998808").rationCardNo("MH09-2026-000132").applicationType("ADDRESS_UPDATE").currentStatus("UNDER_REVIEW").sourceDepartment("REVENUE").requestedAddress("77, Rajarampuri 5th Lane, Kolhapur - 416008").build(),
                Application.builder().applicationId("GM-2026-000109").citizenReference("CIT-MH-998809").rationCardNo("MH09-2026-000133").applicationType("MEMBER_ADDITION").currentStatus("PENDING").sourceDepartment("REVENUE").requestedAddress("90, Shahupuri, Kolhapur").build(),
                Application.builder().applicationId("GM-2026-000110").citizenReference("CIT-MH-998810").rationCardNo("MH31-2026-000134").applicationType("ADDRESS_UPDATE").currentStatus("APPROVED").sourceDepartment("REVENUE").requestedAddress("55, Civil Lines, Nagpur - 440001").build(),
                Application.builder().applicationId("GM-2026-000111").citizenReference("CIT-MH-998812").rationCardNo("MH31-2026-000135").applicationType("CARD_TRANSFER").currentStatus("PENDING").sourceDepartment("REVENUE").requestedAddress("44, Dharampeth, Nagpur").build(),
                Application.builder().applicationId("GM-2026-000112").citizenReference("CIT-MH-998813").rationCardNo("MH20-2026-000136").applicationType("ADDRESS_UPDATE").currentStatus("UNDER_REVIEW").sourceDepartment("REVENUE").requestedAddress("14, Jalna Road, Chhatrapati Sambhajinagar - 431001").build(),
                Application.builder().applicationId("GM-2026-000113").citizenReference("CIT-MH-998814").rationCardNo("MH20-2026-000137").applicationType("ADDRESS_UPDATE").currentStatus("APPROVED").sourceDepartment("REVENUE").requestedAddress("12, CIDCO Sector 3, Chhatrapati Sambhajinagar").build(),
                Application.builder().applicationId("GM-2026-000114").citizenReference("CIT-MH-998815").rationCardNo("MH13-2026-000138").applicationType("MEMBER_ADDITION").currentStatus("PENDING").sourceDepartment("REVENUE").requestedAddress("89, Saat Rasta, Solapur").build()
        );
        applicationRepository.saveAll(applications);

        // 4. Seed Consents (Demo scenarios)
        LocalDateTime now = LocalDateTime.now();
        List<Consent> consents = Arrays.asList(
                Consent.builder()
                        .consentId("CONSENT-00124")
                        .citizenReference("CIT-MH-998811")
                        .requestingDepartment("REVENUE")
                        .receivingDepartment("FOOD")
                        .purpose("RATION_ADDRESS_UPDATE")
                        .status("ACTIVE")
                        .issuedAt(now.minusDays(5))
                        .expiresAt(now.plusDays(25))
                        .build(),

                Consent.builder()
                        .consentId("CONSENT-EXPIRED-001")
                        .citizenReference("CIT-MH-998801")
                        .requestingDepartment("REVENUE")
                        .receivingDepartment("FOOD")
                        .purpose("RATION_ADDRESS_UPDATE")
                        .status("EXPIRED")
                        .issuedAt(now.minusDays(60))
                        .expiresAt(now.minusDays(30))
                        .build(),

                Consent.builder()
                        .consentId("CONSENT-REVOKED-001")
                        .citizenReference("CIT-MH-998802")
                        .requestingDepartment("REVENUE")
                        .receivingDepartment("FOOD")
                        .purpose("RATION_ADDRESS_UPDATE")
                        .status("REVOKED")
                        .issuedAt(now.minusDays(20))
                        .expiresAt(now.plusDays(10))
                        .revokedAt(now.minusDays(10))
                        .build(),

                Consent.builder()
                        .consentId("CONSENT-WRONG-PURPOSE-001")
                        .citizenReference("CIT-MH-998803")
                        .requestingDepartment("REVENUE")
                        .receivingDepartment("FOOD")
                        .purpose("BANK_ACCOUNT_UPDATE")
                        .status("ACTIVE")
                        .issuedAt(now.minusDays(2))
                        .expiresAt(now.plusDays(28))
                        .build(),

                Consent.builder()
                        .consentId("CONSENT-FIELD-VIOLATION-001")
                        .citizenReference("CIT-MH-998804")
                        .requestingDepartment("REVENUE")
                        .receivingDepartment("FOOD")
                        .purpose("RATION_ADDRESS_UPDATE")
                        .status("ACTIVE")
                        .issuedAt(now.minusDays(2))
                        .expiresAt(now.plusDays(28))
                        .build(),

                Consent.builder()
                        .consentId("CONSENT-PARTY-MISMATCH-001")
                        .citizenReference("CIT-MH-998805")
                        .requestingDepartment("PANCHAYAT")
                        .receivingDepartment("FOOD")
                        .purpose("RATION_ADDRESS_UPDATE")
                        .status("ACTIVE")
                        .issuedAt(now.minusDays(2))
                        .expiresAt(now.plusDays(28))
                        .build()
        );
        consentRepository.saveAll(consents);

        // 5. Seed Audit Logs (20)
        Long officer1Id = foodOfficer.getId();
        List<AuditLog> auditLogs = Arrays.asList(
                AuditLog.builder().timestamp(LocalDateTime.now().minusMinutes(2)).applicationId("GM-2026-000124").officerId(officer1Id).action("APPLICATION_VIEWED").result("SUCCESS").description("Officer viewed application details for address update").build(),
                AuditLog.builder().timestamp(LocalDateTime.now().minusMinutes(5)).applicationId(null).officerId(officer1Id).action("USER_LOGIN").result("SUCCESS").description("Officer food.officer authenticated successfully via JWT").build(),
                AuditLog.builder().timestamp(LocalDateTime.now().minusMinutes(15)).applicationId("GM-2026-000101").officerId(seniorOfficer.getId()).action("APPLICATION_REVIEW_STARTED").result("SUCCESS").description("Officer started review for application GM-2026-000101").build(),
                AuditLog.builder().timestamp(LocalDateTime.now().minusMinutes(30)).applicationId("GM-2026-000102").officerId(seniorOfficer.getId()).action("APPLICATION_APPROVED").result("SUCCESS").description("Address update approved and ration record updated").build(),
                AuditLog.builder().timestamp(LocalDateTime.now().minusMinutes(45)).applicationId("GM-2026-000104").officerId(admin.getId()).action("APPLICATION_APPROVED").result("SUCCESS").description("Admin approved card category update").build(),
                AuditLog.builder().timestamp(LocalDateTime.now().minusHours(1)).applicationId(null).officerId(auditor.getId()).action("AUDIT_EXPORT").result("SUCCESS").description("Auditor generated daily summary audit snapshot").build(),
                AuditLog.builder().timestamp(LocalDateTime.now().minusHours(2)).applicationId("GM-2026-000124").officerId(officer1Id).action("APPLICATION_INGESTED").result("SUCCESS").description("Application record created via simulated Revenue department payload").build(),
                AuditLog.builder().timestamp(LocalDateTime.now().minusHours(3)).applicationId(null).officerId(seniorOfficer.getId()).action("USER_LOGIN").result("SUCCESS").description("Senior officer senior.officer logged in").build(),
                AuditLog.builder().timestamp(LocalDateTime.now().minusHours(5)).applicationId("GM-2026-000101").officerId(officer1Id).action("RECORD_VIEWED").result("SUCCESS").description("Searched ration record MH12-2026-000125").build(),
                AuditLog.builder().timestamp(LocalDateTime.now().minusHours(6)).applicationId("GM-2026-000105").officerId(seniorOfficer.getId()).action("APPLICATION_REJECTED").result("SUCCESS").description("Officer rejected the address update request").build(),
                AuditLog.builder().timestamp(LocalDateTime.now().minusHours(12)).applicationId("GM-2026-000102").officerId(officer1Id).action("RATION_RECORD_UPDATED").result("SUCCESS").description("Ration record updated with new house address").build(),
                AuditLog.builder().timestamp(LocalDateTime.now().minusDays(1)).applicationId("GM-2026-000103").officerId(officer1Id).action("RECORD_VIEWED").result("SUCCESS").description("Searched ration record MH12-2026-000127").build(),
                AuditLog.builder().timestamp(LocalDateTime.now().minusDays(1).minusHours(2)).applicationId(null).officerId(admin.getId()).action("USER_LOGIN").result("SUCCESS").description("Admin food.admin logged into system management").build(),
                AuditLog.builder().timestamp(LocalDateTime.now().minusDays(1).minusHours(5)).applicationId("GM-2026-000106").officerId(seniorOfficer.getId()).action("APPLICATION_APPROVED").result("SUCCESS").description("Member removal verified and approved by senior officer").build(),
                AuditLog.builder().timestamp(LocalDateTime.now().minusDays(2)).applicationId("GM-2026-000107").officerId(officer1Id).action("INFORMATION_REQUESTED").result("SUCCESS").description("Requested supporting Revenue 7/12 extract document").build(),
                AuditLog.builder().timestamp(LocalDateTime.now().minusDays(2).minusHours(3)).applicationId(null).officerId(auditor.getId()).action("USER_LOGIN").result("SUCCESS").description("Auditor auditor logged in for compliance check").build(),
                AuditLog.builder().timestamp(LocalDateTime.now().minusDays(3)).applicationId("GM-2026-000108").officerId(officer1Id).action("APPLICATION_VIEWED").result("SUCCESS").description("Officer viewed application GM-2026-000108").build(),
                AuditLog.builder().timestamp(LocalDateTime.now().minusDays(4)).applicationId("GM-2026-000110").officerId(seniorOfficer.getId()).action("APPLICATION_APPROVED").result("SUCCESS").description("Application marked as APPROVED and ration record updated").build(),
                AuditLog.builder().timestamp(LocalDateTime.now().minusDays(5)).applicationId("GM-2026-000113").officerId(seniorOfficer.getId()).action("APPLICATION_APPROVED").result("SUCCESS").description("Application marked as APPROVED").build(),
                AuditLog.builder().timestamp(LocalDateTime.now().minusDays(6)).applicationId(null).officerId(officer1Id).action("SYSTEM_HEALTH_CHECK").result("SUCCESS").description("System health diagnostic scan executed successfully").build()
        );
        auditLogRepository.saveAll(auditLogs);

        // 6. Seed Notifications (10)
        List<Notification> notifications = Arrays.asList(
                Notification.builder().recipientUserId(officer1Id).title("New Address Update Request").message("Application GM-2026-000124 received from Revenue Department requires verification.").type("REQUEST").isRead(false).createdAt(LocalDateTime.now().minusMinutes(10)).build(),
                Notification.builder().recipientUserId(officer1Id).title("Urgent Review Required").message("Application GM-2026-000101 has been in UNDER_REVIEW state for over 4 hours.").type("ALERT").isRead(false).createdAt(LocalDateTime.now().minusHours(1)).build(),
                Notification.builder().recipientUserId(seniorOfficer.getId()).title("Pending Approval Queue").message("3 new applications are waiting for senior officer review in District Pune.").type("INFO").isRead(false).createdAt(LocalDateTime.now().minusHours(3)).build(),
                Notification.builder().recipientUserId(admin.getId()).title("System Audit Log Summary").message("Weekly security audit log generated for Department 2 (Food & Civil Supplies).").type("SYSTEM").isRead(true).createdAt(LocalDateTime.now().minusDays(1)).build(),
                Notification.builder().recipientUserId(officer1Id).title("Ration Record Status Updated").message("Ration card MH14-2026-000126 verification marked ACTIVE.").type("INFO").isRead(true).createdAt(LocalDateTime.now().minusDays(1)).build(),
                Notification.builder().recipientUserId(auditor.getId()).title("Audit Trail Export Ready").message("Compliance report for Q3 2026 is ready for inspection.").type("SYSTEM").isRead(false).createdAt(LocalDateTime.now().minusDays(2)).build(),
                Notification.builder().recipientUserId(officer1Id).title("Application Information Requested").message("Application GM-2026-000107 requested supporting documentation.").type("ALERT").isRead(true).createdAt(LocalDateTime.now().minusDays(3)).build(),
                Notification.builder().recipientUserId(seniorOfficer.getId()).title("Monthly Operational Report").message("Department operational report for August 2026 is available on the dashboard.").type("INFO").isRead(true).createdAt(LocalDateTime.now().minusDays(4)).build(),
                Notification.builder().recipientUserId(admin.getId()).title("Scheduled Maintenance Notice").message("Scheduled database indexing window on Saturday 02:00 AM IST.").type("SYSTEM").isRead(false).createdAt(LocalDateTime.now().minusDays(5)).build(),
                Notification.builder().recipientUserId(officer1Id).title("Welcome to GovMesh Portal").message("You are logged into Department 2 (Food, Civil Supplies & Consumer Protection).").type("INFO").isRead(true).createdAt(LocalDateTime.now().minusDays(7)).build()
        );
        notificationRepository.saveAll(notifications);

        log.info("Database successfully seeded with users, ration records, applications, consents, audit logs, and notifications!");
    }
}
