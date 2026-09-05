import { Application, ApplicationDetail, DashboardSummary, RationRecord, RecentActivity, AuditLog } from '../types';

export const FALLBACK_APPLICATIONS: Application[] = [
  {
    id: 1,
    applicationId: "GM-2026-000124",
    citizenReference: "CIT-MH-1001",
    rationCardNo: "MH12-2026-000124",
    applicationType: "ADDRESS_UPDATE",
    currentStatus: "PENDING",
    sourceDepartment: "REVENUE",
    requestedAddress: "Flat 402, Shivshankar Heights, Karve Road, Kothrud, Haveli, Pune - 411038",
    createdAt: "2026-09-05T04:30:05.000Z",
    updatedAt: "2026-09-05T04:30:05.000Z",
    canonicalRequestHash: "sha256:7f83b1657ff1fc53b92dc18148a1d65dfc2d4b1fa3d677284addd200126d9069",
    documentHash: "sha256:a591a6d40bf420404a011733cfb7b190d62c65bf0bcda32b57b277d9ad9f146e",
    hashStatus: "VERIFIED"
  },
  {
    id: 2,
    applicationId: "GM-2026-000101",
    citizenReference: "CIT-MH-998801",
    rationCardNo: "MH12-2026-000125",
    applicationType: "ADDRESS_UPDATE",
    currentStatus: "PROCESSING",
    sourceDepartment: "REVENUE",
    requestedAddress: "99, Sector 21, Nigdi, Pimpri-Chinchwad, Pune - 411044",
    createdAt: "2026-09-05T02:30:00.000Z",
    updatedAt: "2026-09-05T03:00:00.000Z"
  },
  {
    id: 3,
    applicationId: "GM-2026-000102",
    citizenReference: "CIT-MH-998802",
    rationCardNo: "MH14-2026-000126",
    applicationType: "MEMBER_ADDITION",
    currentStatus: "COMPLETED",
    sourceDepartment: "REVENUE",
    requestedAddress: "88, Station Road, Pimpri, Pune",
    createdAt: "2026-09-04T10:00:00.000Z",
    updatedAt: "2026-09-04T14:30:00.000Z"
  },
  {
    id: 4,
    applicationId: "GM-2026-000103",
    citizenReference: "CIT-MH-998803",
    rationCardNo: "MH12-2026-000127",
    applicationType: "ADDRESS_UPDATE",
    currentStatus: "PENDING",
    sourceDepartment: "REVENUE",
    requestedAddress: "205, Ideal Colony, Kothrud, Pune - 411038",
    createdAt: "2026-09-04T08:00:00.000Z",
    updatedAt: "2026-09-04T08:00:00.000Z"
  },
  {
    id: 5,
    applicationId: "GM-2026-000104",
    citizenReference: "CIT-MH-998804",
    rationCardNo: "MH15-2026-000128",
    applicationType: "CARD_CATEGORY_CHANGE",
    currentStatus: "COMPLETED",
    sourceDepartment: "REVENUE",
    requestedAddress: "14, College Road, Nashik",
    createdAt: "2026-09-03T11:00:00.000Z",
    updatedAt: "2026-09-03T15:00:00.000Z"
  },
  {
    id: 6,
    applicationId: "GM-2026-000105",
    citizenReference: "CIT-MH-998805",
    rationCardNo: "MH15-2026-000129",
    applicationType: "ADDRESS_UPDATE",
    currentStatus: "REJECTED",
    sourceDepartment: "REVENUE",
    requestedAddress: "12, Gangapur Road, Nashik - 422005",
    officerComments: "Incomplete address proof submitted.",
    createdAt: "2026-09-03T09:00:00.000Z",
    updatedAt: "2026-09-03T10:00:00.000Z"
  },
  {
    id: 7,
    applicationId: "GM-2026-000106",
    citizenReference: "CIT-MH-998806",
    rationCardNo: "MH04-2026-000130",
    applicationType: "MEMBER_REMOVAL",
    currentStatus: "COMPLETED",
    sourceDepartment: "REVENUE",
    requestedAddress: "202, Naupada, Thane",
    createdAt: "2026-09-02T10:00:00.000Z",
    updatedAt: "2026-09-02T13:00:00.000Z"
  },
  {
    id: 8,
    applicationId: "GM-2026-000107",
    citizenReference: "CIT-MH-998807",
    rationCardNo: "MH04-2026-000131",
    applicationType: "ADDRESS_UPDATE",
    currentStatus: "FAILED",
    sourceDepartment: "REVENUE",
    requestedAddress: "104, Majiwada, Thane West - 400601",
    officerComments: "Please provide supporting Revenue 7/12 extract document.",
    createdAt: "2026-09-02T08:30:00.000Z",
    updatedAt: "2026-09-02T09:00:00.000Z"
  },
  {
    id: 9,
    applicationId: "GM-2026-000108",
    citizenReference: "CIT-MH-998808",
    rationCardNo: "MH09-2026-000132",
    applicationType: "ADDRESS_UPDATE",
    currentStatus: "PROCESSING",
    sourceDepartment: "REVENUE",
    requestedAddress: "77, Rajarampuri 5th Lane, Kolhapur - 416008",
    createdAt: "2026-09-01T12:00:00.000Z",
    updatedAt: "2026-09-01T14:00:00.000Z"
  },
  {
    id: 10,
    applicationId: "GM-2026-000109",
    citizenReference: "CIT-MH-998809",
    rationCardNo: "MH09-2026-000133",
    applicationType: "MEMBER_ADDITION",
    currentStatus: "PENDING",
    sourceDepartment: "REVENUE",
    requestedAddress: "90, Shahupuri, Kolhapur",
    createdAt: "2026-09-01T09:00:00.000Z",
    updatedAt: "2026-09-01T09:00:00.000Z"
  }
];

export const FALLBACK_RATION_RECORDS: RationRecord[] = [
  {
    id: 1,
    rationCardNo: "MH12-2026-000124",
    holderName: "Rajesh Shantaram Patil",
    houseAddress: "Flat 201, Shanti Niketan, Prabhat Road, Deccan Gymkhana, Haveli, Pune - 411004",
    talukaCode: "TAL-HAV-02",
    districtCode: "DIST-PUN",
    verificationFlag: true,
    updateStatus: "ACTIVE",
    createdAt: "2026-08-01T00:00:00.000Z",
    updatedAt: "2026-09-05T04:30:05.000Z"
  },
  {
    id: 2,
    rationCardNo: "MH12-2026-000125",
    holderName: "Aarti Suresh Patil",
    houseAddress: "45, Lakshmi Chowk, Chinchwad, Pune",
    talukaCode: "TAL-HAV-02",
    districtCode: "DIST-PUN",
    verificationFlag: true,
    updateStatus: "ACTIVE",
    createdAt: "2026-08-02T00:00:00.000Z",
    updatedAt: "2026-09-05T00:00:00.000Z"
  },
  {
    id: 3,
    rationCardNo: "MH14-2026-000126",
    holderName: "Ganesh Ramchandra Kulkarni",
    houseAddress: "88, Station Road, Pimpri, Pune",
    talukaCode: "TAL-HAV-02",
    districtCode: "DIST-PUN",
    verificationFlag: true,
    updateStatus: "ACTIVE",
    createdAt: "2026-08-03T00:00:00.000Z",
    updatedAt: "2026-09-05T00:00:00.000Z"
  },
  {
    id: 4,
    rationCardNo: "MH12-2026-000127",
    holderName: "Savita Vilas Shinde",
    houseAddress: "101, Anand Nagar, Kothrud, Pune",
    talukaCode: "TAL-PUN-04",
    districtCode: "DIST-PUN",
    verificationFlag: false,
    updateStatus: "PENDING_VERIFICATION",
    createdAt: "2026-08-04T00:00:00.000Z",
    updatedAt: "2026-09-05T00:00:00.000Z"
  },
  {
    id: 5,
    rationCardNo: "MH15-2026-000128",
    holderName: "Mahesh Dinkar Jadhav",
    houseAddress: "14, College Road, Nashik",
    talukaCode: "TAL-NSK-01",
    districtCode: "DIST-NSK",
    verificationFlag: true,
    updateStatus: "ACTIVE",
    createdAt: "2026-08-05T00:00:00.000Z",
    updatedAt: "2026-09-05T00:00:00.000Z"
  }
];

export const FALLBACK_DASHBOARD_SUMMARY: DashboardSummary = {
  totalIncomingRequests: 15,
  pendingCount: 5,
  processingCount: 3,
  completedCount: 5,
  rejectedCount: 1,
  failedCount: 1,
  serviceStatus: [
    {
      name: "GovMesh Gateway Ingress",
      status: "OPERATIONAL",
      isConnected: true,
      note: "Connected & Ingesting inter-department messages"
    },
    {
      name: "Ration Database Core",
      status: "OPERATIONAL",
      isConnected: true,
      note: "Active replica connected"
    },
    {
      name: "Citizen Verification Hub",
      status: "OPERATIONAL",
      isConnected: true,
      note: "Live callbacks enabled"
    }
  ]
};

export const FALLBACK_RECENT_ACTIVITIES: RecentActivity[] = [
  {
    id: 1,
    timeAgo: "10 mins ago",
    timestamp: "2026-09-05T04:30:05.000Z",
    description: "GovMesh transaction GM-2026-000124 received from Revenue & Forest Department.",
    action: "APPLICATION_INGESTED",
    result: "SUCCESS",
    officerName: "GovMesh Gateway Ingress"
  },
  {
    id: 2,
    timeAgo: "8 mins ago",
    timestamp: "2026-09-05T04:32:00.000Z",
    description: "Officer viewed application details for address update (GM-2026-000124).",
    action: "APPLICATION_VIEWED",
    result: "SUCCESS",
    officerName: "Rajendra Sharma"
  },
  {
    id: 3,
    timeAgo: "Yesterday",
    timestamp: "2026-09-04T14:30:00.000Z",
    description: "Address update approved for Ration Card MH14-2026-000126.",
    action: "APPLICATION_APPROVED",
    result: "SUCCESS",
    officerName: "Rajendra Sharma"
  }
];

export const FALLBACK_APPLICATION_DETAIL_124: ApplicationDetail = {
  application: FALLBACK_APPLICATIONS[0],
  currentRationRecord: FALLBACK_RATION_RECORDS[0]
};
