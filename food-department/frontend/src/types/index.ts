export type Role = 'FOOD_SUPPLY_OFFICER' | 'SENIOR_OFFICER' | 'DEPARTMENT_ADMIN' | 'AUDITOR';

export interface User {
  id: number;
  username: string;
  fullName: string;
  role: Role;
  department: string;
  employeeId: string;
  isActive: boolean;
  createdAt: string;
}

export interface AuthResponse {
  token: string;
  tokenType: string;
  expiresInMs: number;
  user: User;
}

export interface ServiceStatusItem {
  name: string;
  status: 'OPERATIONAL' | 'NOT_CONFIGURED' | 'DEGRADED';
  isConnected: boolean;
  note: string;
}

export interface DashboardSummary {
  totalIncomingRequests: number;
  pendingCount: number;
  processingCount: number;
  completedCount: number;
  rejectedCount: number;
  failedCount: number;
  serviceStatus: ServiceStatusItem[];
}

export type ApplicationStatus = 'PENDING' | 'UNDER_REVIEW' | 'APPROVED' | 'REJECTED' | 'INFORMATION_REQUIRED' | 'PROCESSING' | 'COMPLETED' | 'FAILED';

export interface Application {
  id: number;
  applicationId: string;
  citizenReference: string;
  rationCardNo: string;
  applicationType: string;
  currentStatus: ApplicationStatus;
  sourceDepartment: string;
  requestedAddress?: string;
  officerComments?: string;
  reviewedByOfficer?: string;
  correlationId?: string;
  requestVersion?: string;
  canonicalRequestHash?: string;
  documentHash?: string;
  hashStatus?: string;
  documentId?: string;
  documentName?: string;
  documentType?: string;
  documentSize?: number;
  consentId?: string;
  acknowledgementId?: string;
  sentAt?: string;
  receivedAt?: string;
  validatedAt?: string;
  acceptedAt?: string;
  processingStartedAt?: string;
  completedAt?: string;
  rawSourceJson?: string;
  createdAt: string;
  updatedAt: string;
}

export interface RationRecord {
  id: number;
  rationCardNo: string;
  holderName: string;
  houseAddress: string;
  talukaCode: string;
  districtCode: string;
  verificationFlag: boolean;
  updateStatus: string;
  createdAt: string;
  updatedAt: string;
}

export interface ApplicationDetail {
  application: Application;
  currentRationRecord: RationRecord | null;
}

export interface RecentActivity {
  id: number;
  timeAgo: string;
  timestamp: string;
  description: string;
  action: string;
  result: string;
  officerName: string;
}

export interface AuditLog {
  id: number;
  timestamp: string;
  applicationId: string | null;
  officerId: number | null;
  officerName: string;
  action: string;
  result: string;
  description: string;
}

export interface NotificationItem {
  id: number;
  recipientUserId: number;
  title: string;
  message: string;
  type: 'REQUEST' | 'ALERT' | 'INFO' | 'SYSTEM';
  isRead: boolean;
  createdAt: string;
}

export interface ComponentHealth {
  name: string;
  category: string;
  status: 'OPERATIONAL' | 'NOT_CONFIGURED' | 'DOWN';
  isConfigured: boolean;
  message: string;
  version: string;
}

export interface SystemHealth {
  departmentName: string;
  environment: string;
  timestamp: string;
  components: ComponentHealth[];
}
