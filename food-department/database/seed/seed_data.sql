-- ====================================================================
-- GovMesh - Food, Civil Supplies & Consumer Protection Department
-- Database Seed Data (Simulated Department System - Phase 1)
-- ====================================================================

-- 1. USERS (10 Officers & Admins)
-- Passwords BCrypt Encoded:
-- food.officer  -> Food@123
-- senior.officer -> Senior@123
-- food.admin   -> Admin@123
-- auditor      -> Auditor@123

INSERT INTO users (id, username, password_hash, full_name, role, department, employee_id, is_active, created_at, updated_at) VALUES
(1, 'food.officer', '$2a$10$r.7gYQxW1K/oPjS7s7g8u.L7Pz2Gq1H5w4U9e8d7c6b5a4m3n2o1p', 'Rajendra Sharma', 'FOOD_SUPPLY_OFFICER', 'Food & Civil Supplies', 'FOOD-EMP-001', true, NOW(), NOW()),
(2, 'senior.officer', '$2a$10$t.8hZRyX2L/pQkT8t8h9v.M8Qa3Hr2I6x5V0f9e8d7c6b5a4m3n2o1p', 'Sanjay Deshmukh', 'SENIOR_OFFICER', 'Food & Civil Supplies', 'FOOD-EMP-002', true, NOW(), NOW()),
(3, 'food.admin', '$2a$10$u.9iaSzY3M/qRlU9u9i0w.N9Rb4Is3J7y6W1g0f9e8d7c6b5a4m3n2o1p', 'Priya Kulkarni', 'DEPARTMENT_ADMIN', 'Food & Civil Supplies', 'FOOD-EMP-003', true, NOW(), NOW()),
(4, 'auditor', '$2a$10$v.0jbTzZ4N/rSmV0v0j1x.O0Sc5Jt4K8z7X2h1g0f9e8d7c6b5a4m3n2o1p', 'Vikramaditya Joshi', 'AUDITOR', 'State Audit Bureau', 'AUD-EMP-101', true, NOW(), NOW()),
(5, 'sunita.patil', '$2a$10$r.7gYQxW1K/oPjS7s7g8u.L7Pz2Gq1H5w4U9e8d7c6b5a4m3n2o1p', 'Sunita Patil', 'FOOD_SUPPLY_OFFICER', 'Food & Civil Supplies', 'FOOD-EMP-005', true, NOW(), NOW()),
(6, 'anil.chavan', '$2a$10$r.7gYQxW1K/oPjS7s7g8u.L7Pz2Gq1H5w4U9e8d7c6b5a4m3n2o1p', 'Anil Chavan', 'FOOD_SUPPLY_OFFICER', 'Food & Civil Supplies', 'FOOD-EMP-006', true, NOW(), NOW()),
(7, 'meena.pawar', '$2a$10$r.7gYQxW1K/oPjS7s7g8u.L7Pz2Gq1H5w4U9e8d7c6b5a4m3n2o1p', 'Meena Pawar', 'FOOD_SUPPLY_OFFICER', 'Food & Civil Supplies', 'FOOD-EMP-007', true, NOW(), NOW()),
(8, 'ramesh.shinde', '$2a$10$t.8hZRyX2L/pQkT8t8h9v.M8Qa3Hr2I6x5V0f9e8d7c6b5a4m3n2o1p', 'Ramesh Shinde', 'SENIOR_OFFICER', 'Food & Civil Supplies', 'FOOD-EMP-008', true, NOW(), NOW()),
(9, 'deepak.more', '$2a$10$r.7gYQxW1K/oPjS7s7g8u.L7Pz2Gq1H5w4U9e8d7c6b5a4m3n2o1p', 'Deepak More', 'FOOD_SUPPLY_OFFICER', 'Food & Civil Supplies', 'FOOD-EMP-009', true, NOW(), NOW()),
(10, 'kavita.gaikwad', '$2a$10$v.0jbTzZ4N/rSmV0v0j1x.O0Sc5Jt4K8z7X2h1g0f9e8d7c6b5a4m3n2o1p', 'Kavita Gaikwad', 'AUDITOR', 'State Audit Bureau', 'AUD-EMP-102', true, NOW(), NOW());

-- 2. RATION RECORDS (20 Records)
INSERT INTO ration_records (id, ration_card_no, holder_name, house_address, taluka_code, district_code, verification_flag, update_status, created_at, updated_at) VALUES
(1, 'MH12-2026-000124', 'Rajesh Kumar', '12, M.G. Road, Shivajinagar, Pune', 'TAL-PUN-04', 'DIST-PUN', true, 'ACTIVE', NOW() - INTERVAL '30 days', NOW()),
(2, 'MH12-2026-000125', 'Aarti Suresh Patil', '45, Lakshmi Chowk, Chinchwad, Pune', 'TAL-HAV-02', 'DIST-PUN', true, 'ACTIVE', NOW() - INTERVAL '29 days', NOW()),
(3, 'MH14-2026-000126', 'Ganesh Ramchandra Kulkarni', '88, Station Road, Pimpri, Pune', 'TAL-HAV-02', 'DIST-PUN', true, 'ACTIVE', NOW() - INTERVAL '28 days', NOW()),
(4, 'MH12-2026-000127', 'Savita Vilas Shinde', '101, Anand Nagar, Kothrud, Pune', 'TAL-PUN-04', 'DIST-PUN', false, 'PENDING_VERIFICATION', NOW() - INTERVAL '27 days', NOW()),
(5, 'MH15-2026-000128', 'Mahesh Dinkar Jadhav', '14, College Road, Nashik', 'TAL-NSK-01', 'DIST-NSK', true, 'ACTIVE', NOW() - INTERVAL '26 days', NOW()),
(6, 'MH15-2026-000129', 'Pooja Nitin Bhosale', '56, Panchavati, Nashik', 'TAL-NSK-01', 'DIST-NSK', true, 'ACTIVE', NOW() - INTERVAL '25 days', NOW()),
(7, 'MH04-2026-000130', 'Vijay Pandurang Thorat', '202, Naupada, Thane', 'TAL-THN-01', 'DIST-THN', true, 'ACTIVE', NOW() - INTERVAL '24 days', NOW()),
(8, 'MH04-2026-000131', 'Smita Prabhakar Deshmukh', '78, Ghodbunder Road, Thane', 'TAL-THN-01', 'DIST-THN', false, 'LOCKED', NOW() - INTERVAL '23 days', NOW()),
(9, 'MH09-2026-000132', 'Eknath Sadashiv More', '33, Tarabai Park, Kolhapur', 'TAL-KLP-02', 'DIST-KLP', true, 'ACTIVE', NOW() - INTERVAL '22 days', NOW()),
(10, 'MH09-2026-000133', 'Sunanda Baburao Chavan', '90, Shahupuri, Kolhapur', 'TAL-KLP-02', 'DIST-KLP', true, 'ACTIVE', NOW() - INTERVAL '21 days', NOW()),
(11, 'MH31-2026-000134', 'Ashok Wasudeo Kamble', '15, Sitabuldi, Nagpur', 'TAL-NGP-01', 'DIST-NGP', true, 'ACTIVE', NOW() - INTERVAL '20 days', NOW()),
(12, 'MH31-2026-000135', 'Shilpa Arvind Wankhede', '44, Dharampeth, Nagpur', 'TAL-NGP-01', 'DIST-NGP', true, 'ACTIVE', NOW() - INTERVAL '19 days', NOW()),
(13, 'MH20-2026-000136', 'Prakash Tarachand Jain', '67, Kranti Chowk, Chhatrapati Sambhajinagar', 'TAL-CSN-01', 'DIST-CSN', true, 'ACTIVE', NOW() - INTERVAL '18 days', NOW()),
(14, 'MH20-2026-000137', 'Manjusha Madhav Rao', '12, CIDCO Sector 3, Chhatrapati Sambhajinagar', 'TAL-CSN-01', 'DIST-CSN', true, 'ACTIVE', NOW() - INTERVAL '17 days', NOW()),
(15, 'MH13-2026-000138', 'Suryakant Bandu Solanke', '89, Saat Rasta, Solapur', 'TAL-SLP-01', 'DIST-SLP', false, 'PENDING_VERIFICATION', NOW() - INTERVAL '16 days', NOW()),
(16, 'MH13-2026-000139', 'Rekha Govind Mote', '23, Budhwar Peth, Solapur', 'TAL-SLP-01', 'DIST-SLP', true, 'ACTIVE', NOW() - INTERVAL '15 days', NOW()),
(17, 'MH11-2026-000140', 'Dattatray Narayan Pawar', '55, Powai Naka, Satara', 'TAL-STR-01', 'DIST-STR', true, 'ACTIVE', NOW() - INTERVAL '14 days', NOW()),
(18, 'MH11-2026-000141', 'Geeta Hanmant Kadam', '108, Rajwada, Satara', 'TAL-STR-01', 'DIST-STR', true, 'ACTIVE', NOW() - INTERVAL '13 days', NOW()),
(19, 'MH12-2026-000142', 'Nitin Shrikant Joshi', '34, Hadapsar, Pune', 'TAL-PUN-04', 'DIST-PUN', true, 'ACTIVE', NOW() - INTERVAL '12 days', NOW()),
(20, 'MH12-2026-000143', 'Bhavna Sharad Mehta', '76, Viman Nagar, Pune', 'TAL-HAV-02', 'DIST-PUN', true, 'ACTIVE', NOW() - INTERVAL '11 days', NOW());

-- 3. APPLICATIONS (15 Interoperability Update Requests)
INSERT INTO applications (id, application_id, citizen_reference, ration_card_no, application_type, current_status, source_department, created_at, updated_at) VALUES
(1, 'GM-2026-000124', 'CIT-MH-998811', 'MH12-2026-000124', 'ADDRESS_UPDATE', 'PENDING', 'REVENUE', NOW() - INTERVAL '2 hours', NOW() - INTERVAL '2 hours'),
(2, 'GM-2026-000101', 'CIT-MH-998801', 'MH12-2026-000125', 'ADDRESS_UPDATE', 'PROCESSING', 'REVENUE', NOW() - INTERVAL '5 hours', NOW() - INTERVAL '4 hours'),
(3, 'GM-2026-000102', 'CIT-MH-998802', 'MH14-2026-000126', 'MEMBER_ADDITION', 'COMPLETED', 'REVENUE', NOW() - INTERVAL '1 day', NOW() - INTERVAL '12 hours'),
(4, 'GM-2026-000103', 'CIT-MH-998803', 'MH12-2026-000127', 'ADDRESS_UPDATE', 'PENDING', 'REVENUE', NOW() - INTERVAL '1 day', NOW() - INTERVAL '1 day'),
(5, 'GM-2026-000104', 'CIT-MH-998804', 'MH15-2026-000128', 'CARD_CATEGORY_CHANGE', 'COMPLETED', 'REVENUE', NOW() - INTERVAL '2 days', NOW() - INTERVAL '1 day'),
(6, 'GM-2026-000105', 'CIT-MH-998805', 'MH15-2026-000129', 'ADDRESS_UPDATE', 'REJECTED', 'REVENUE', NOW() - INTERVAL '2 days', NOW() - INTERVAL '2 days'),
(7, 'GM-2026-000106', 'CIT-MH-998806', 'MH04-2026-000130', 'MEMBER_REMOVAL', 'COMPLETED', 'REVENUE', NOW() - INTERVAL '3 days', NOW() - INTERVAL '2 days'),
(8, 'GM-2026-000107', 'CIT-MH-998807', 'MH04-2026-000131', 'ADDRESS_UPDATE', 'FAILED', 'REVENUE', NOW() - INTERVAL '3 days', NOW() - INTERVAL '3 days'),
(9, 'GM-2026-000108', 'CIT-MH-998808', 'MH09-2026-000132', 'ADDRESS_UPDATE', 'PROCESSING', 'REVENUE', NOW() - INTERVAL '4 days', NOW() - INTERVAL '1 day'),
(10, 'GM-2026-000109', 'CIT-MH-998809', 'MH09-2026-000133', 'MEMBER_ADDITION', 'PENDING', 'REVENUE', NOW() - INTERVAL '4 days', NOW() - INTERVAL '4 days'),
(11, 'GM-2026-000110', 'CIT-MH-998810', 'MH31-2026-000134', 'ADDRESS_UPDATE', 'COMPLETED', 'REVENUE', NOW() - INTERVAL '5 days', NOW() - INTERVAL '3 days'),
(12, 'GM-2026-000111', 'CIT-MH-998812', 'MH31-2026-000135', 'CARD_TRANSFER', 'PENDING', 'REVENUE', NOW() - INTERVAL '5 days', NOW() - INTERVAL '5 days'),
(13, 'GM-2026-000112', 'CIT-MH-998813', 'MH20-2026-000136', 'ADDRESS_UPDATE', 'PROCESSING', 'REVENUE', NOW() - INTERVAL '6 days', NOW() - INTERVAL '2 days'),
(14, 'GM-2026-000113', 'CIT-MH-998814', 'MH20-2026-000137', 'ADDRESS_UPDATE', 'COMPLETED', 'REVENUE', NOW() - INTERVAL '6 days', NOW() - INTERVAL '4 days'),
(15, 'GM-2026-000114', 'CIT-MH-998815', 'MH13-2026-000138', 'MEMBER_ADDITION', 'PENDING', 'REVENUE', NOW() - INTERVAL '7 days', NOW() - INTERVAL '7 days');

-- 4. AUDIT LOGS (20 Log Entries)
INSERT INTO audit_logs (id, timestamp, application_id, officer_id, action, result, description) VALUES
(1, NOW() - INTERVAL '2 minutes', 'GM-2026-000124', 1, 'VIEW_APPLICATION', 'SUCCESS', 'Officer viewed application details for address update'),
(2, NOW() - INTERVAL '5 minutes', NULL, 1, 'USER_LOGIN', 'SUCCESS', 'Officer food.officer authenticated successfully via JWT'),
(3, NOW() - INTERVAL '15 minutes', 'GM-2026-000101', 2, 'STATUS_UPDATE', 'SUCCESS', 'Application status changed from PENDING to PROCESSING'),
(4, NOW() - INTERVAL '30 minutes', 'GM-2026-000102', 2, 'RECORD_VERIFICATION', 'SUCCESS', 'Verified member details against district database'),
(5, NOW() - INTERVAL '45 minutes', 'GM-2026-000104', 3, 'ROLE_PERMISSION_CHECK', 'SUCCESS', 'Admin authorization check passed for card category update'),
(6, NOW() - INTERVAL '1 hour', NULL, 4, 'AUDIT_EXPORT', 'SUCCESS', 'Auditor generated daily summary audit snapshot'),
(7, NOW() - INTERVAL '2 hours', 'GM-2026-000124', 1, 'INGEST_SIMULATION', 'SUCCESS', 'Application record created via simulated Revenue department payload'),
(8, NOW() - INTERVAL '3 hours', NULL, 2, 'USER_LOGIN', 'SUCCESS', 'Senior officer senior.officer logged in'),
(9, NOW() - INTERVAL '5 hours', 'GM-2026-000101', 1, 'SEARCH_RECORD', 'SUCCESS', 'Searched ration record MH12-2026-000125'),
(10, NOW() - INTERVAL '6 hours', 'GM-2026-000105', 2, 'REJECT_APPLICATION', 'SUCCESS', 'Application rejected due to incomplete address verification proof'),
(11, NOW() - INTERVAL '12 hours', 'GM-2026-000102', 1, 'COMPLETED_APPLICATION', 'SUCCESS', 'Member addition processed successfully'),
(12, NOW() - INTERVAL '1 day', 'GM-2026-000103', 5, 'SEARCH_RECORD', 'SUCCESS', 'Searched ration record MH12-2026-000127'),
(13, NOW() - INTERVAL '1 day 2 hours', NULL, 3, 'USER_LOGIN', 'SUCCESS', 'Admin food.admin logged into system management'),
(14, NOW() - INTERVAL '1 day 5 hours', 'GM-2026-000106', 2, 'RECORD_VERIFICATION', 'SUCCESS', 'Member removal verified by senior officer'),
(15, NOW() - INTERVAL '2 days', 'GM-2026-000107', 1, 'SYSTEM_VALIDATION', 'FAILED', 'Validation failed: Taluka code mismatch with district database'),
(16, NOW() - INTERVAL '2 days 3 hours', NULL, 4, 'USER_LOGIN', 'SUCCESS', 'Auditor auditor logged in for compliance check'),
(17, NOW() - INTERVAL '3 days', 'GM-2026-000108', 6, 'VIEW_APPLICATION', 'SUCCESS', 'Officer viewed application GM-2026-000108'),
(18, NOW() - INTERVAL '4 days', 'GM-2026-000110', 8, 'STATUS_UPDATE', 'SUCCESS', 'Application marked as COMPLETED'),
(19, NOW() - INTERVAL '5 days', 'GM-2026-000113', 2, 'STATUS_UPDATE', 'SUCCESS', 'Application marked as COMPLETED'),
(20, NOW() - INTERVAL '6 days', NULL, 1, 'SYSTEM_HEALTH_CHECK', 'SUCCESS', 'System health diagnostic scan executed successfully');

-- 5. NOTIFICATIONS (10 System Notifications)
INSERT INTO notifications (id, recipient_user_id, title, message, type, is_read, created_at) VALUES
(1, 1, 'New Address Update Request', 'Application GM-2026-000124 received from Revenue Department requires verification.', 'REQUEST', false, NOW() - INTERVAL '10 minutes'),
(2, 1, 'Urgent Review Required', 'Application GM-2026-000101 has been in PROCESSING state for over 4 hours.', 'ALERT', false, NOW() - INTERVAL '1 hour'),
(3, 2, 'Pending Approval Queue', '3 new applications are waiting for senior officer review in District Pune.', 'INFO', false, NOW() - INTERVAL '3 hours'),
(4, 3, 'System Audit Log Summary', 'Weekly security audit log generated for Department 2 (Food & Civil Supplies).', 'SYSTEM', true, NOW() - INTERVAL '1 day'),
(5, 1, 'Ration Record Status Updated', 'Ration card MH14-2026-000126 verification marked ACTIVE.', 'INFO', true, NOW() - INTERVAL '1 day'),
(6, 4, 'Audit Trail Export Ready', 'Compliance report for Q3 2026 is ready for inspection.', 'SYSTEM', false, NOW() - INTERVAL '2 days'),
(7, 1, 'Application Validation Failure', 'Application GM-2026-000107 failed validation check due to location code error.', 'ALERT', true, NOW() - INTERVAL '3 days'),
(8, 2, 'Monthly Operational Report', 'Department operational report for August 2026 is available on the dashboard.', 'INFO', true, NOW() - INTERVAL '4 days'),
(9, 3, 'Scheduled Maintenance Notice', 'Scheduled database indexing window on Saturday 02:00 AM IST.', 'SYSTEM', false, NOW() - INTERVAL '5 days'),
(10, 1, 'Welcome to GovMesh Portal', 'You are logged into Department 2 (Food, Civil Supplies & Consumer Protection).', 'INFO', true, NOW() - INTERVAL '7 days');
