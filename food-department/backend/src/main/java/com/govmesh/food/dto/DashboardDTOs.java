package com.govmesh.food.dto;

import java.util.List;

public class DashboardDTOs {

    public static class SummaryDTO {
        private long totalIncomingRequests;
        private long pendingCount;
        private long processingCount;
        private long completedCount;
        private long rejectedCount;
        private long failedCount;
        private List<ServiceStatusItem> serviceStatus;

        public SummaryDTO() {}
        public SummaryDTO(long totalIncomingRequests, long pendingCount, long processingCount, long completedCount, long rejectedCount, long failedCount, List<ServiceStatusItem> serviceStatus) {
            this.totalIncomingRequests = totalIncomingRequests;
            this.pendingCount = pendingCount;
            this.processingCount = processingCount;
            this.completedCount = completedCount;
            this.rejectedCount = rejectedCount;
            this.failedCount = failedCount;
            this.serviceStatus = serviceStatus;
        }

        public long getTotalIncomingRequests() { return totalIncomingRequests; }
        public void setTotalIncomingRequests(long totalIncomingRequests) { this.totalIncomingRequests = totalIncomingRequests; }

        public long getPendingCount() { return pendingCount; }
        public void setPendingCount(long pendingCount) { this.pendingCount = pendingCount; }

        public long getProcessingCount() { return processingCount; }
        public void setProcessingCount(long processingCount) { this.processingCount = processingCount; }

        public long getCompletedCount() { return completedCount; }
        public void setCompletedCount(long completedCount) { this.completedCount = completedCount; }

        public long getRejectedCount() { return rejectedCount; }
        public void setRejectedCount(long rejectedCount) { this.rejectedCount = rejectedCount; }

        public long getFailedCount() { return failedCount; }
        public void setFailedCount(long failedCount) { this.failedCount = failedCount; }

        public List<ServiceStatusItem> getServiceStatus() { return serviceStatus; }
        public void setServiceStatus(List<ServiceStatusItem> serviceStatus) { this.serviceStatus = serviceStatus; }

        public static SummaryDTOBuilder builder() { return new SummaryDTOBuilder(); }

        public static class SummaryDTOBuilder {
            private long totalIncomingRequests;
            private long pendingCount;
            private long processingCount;
            private long completedCount;
            private long rejectedCount;
            private long failedCount;
            private List<ServiceStatusItem> serviceStatus;

            public SummaryDTOBuilder totalIncomingRequests(long totalIncomingRequests) { this.totalIncomingRequests = totalIncomingRequests; return this; }
            public SummaryDTOBuilder pendingCount(long pendingCount) { this.pendingCount = pendingCount; return this; }
            public SummaryDTOBuilder processingCount(long processingCount) { this.processingCount = processingCount; return this; }
            public SummaryDTOBuilder completedCount(long completedCount) { this.completedCount = completedCount; return this; }
            public SummaryDTOBuilder rejectedCount(long rejectedCount) { this.rejectedCount = rejectedCount; return this; }
            public SummaryDTOBuilder failedCount(long failedCount) { this.failedCount = failedCount; return this; }
            public SummaryDTOBuilder serviceStatus(List<ServiceStatusItem> serviceStatus) { this.serviceStatus = serviceStatus; return this; }

            public SummaryDTO build() {
                return new SummaryDTO(totalIncomingRequests, pendingCount, processingCount, completedCount, rejectedCount, failedCount, serviceStatus);
            }
        }
    }

    public static class ServiceStatusItem {
        private String name;
        private String status;
        private boolean isConnected;
        private String note;

        public ServiceStatusItem() {}
        public ServiceStatusItem(String name, String status, boolean isConnected, String note) {
            this.name = name;
            this.status = status;
            this.isConnected = isConnected;
            this.note = note;
        }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }

        public boolean isConnected() { return isConnected; }
        public void setConnected(boolean isConnected) { this.isConnected = isConnected; }

        public String getNote() { return note; }
        public void setNote(String note) { this.note = note; }

        public static ServiceStatusItemBuilder builder() { return new ServiceStatusItemBuilder(); }

        public static class ServiceStatusItemBuilder {
            private String name;
            private String status;
            private boolean isConnected;
            private String note;

            public ServiceStatusItemBuilder name(String name) { this.name = name; return this; }
            public ServiceStatusItemBuilder status(String status) { this.status = status; return this; }
            public ServiceStatusItemBuilder isConnected(boolean isConnected) { this.isConnected = isConnected; return this; }
            public ServiceStatusItemBuilder note(String note) { this.note = note; return this; }

            public ServiceStatusItem build() {
                return new ServiceStatusItem(name, status, isConnected, note);
            }
        }
    }

    public static class RecentActivityDTO {
        private Long id;
        private String timeAgo;
        private String timestamp;
        private String description;
        private String action;
        private String result;
        private String officerName;

        public RecentActivityDTO() {}
        public RecentActivityDTO(Long id, String timeAgo, String timestamp, String description, String action, String result, String officerName) {
            this.id = id;
            this.timeAgo = timeAgo;
            this.timestamp = timestamp;
            this.description = description;
            this.action = action;
            this.result = result;
            this.officerName = officerName;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getTimeAgo() { return timeAgo; }
        public void setTimeAgo(String timeAgo) { this.timeAgo = timeAgo; }

        public String getTimestamp() { return timestamp; }
        public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        public String getAction() { return action; }
        public void setAction(String action) { this.action = action; }

        public String getResult() { return result; }
        public void setResult(String result) { this.result = result; }

        public String getOfficerName() { return officerName; }
        public void setOfficerName(String officerName) { this.officerName = officerName; }

        public static RecentActivityDTOBuilder builder() { return new RecentActivityDTOBuilder(); }

        public static class RecentActivityDTOBuilder {
            private Long id;
            private String timeAgo;
            private String timestamp;
            private String description;
            private String action;
            private String result;
            private String officerName;

            public RecentActivityDTOBuilder id(Long id) { this.id = id; return this; }
            public RecentActivityDTOBuilder timeAgo(String timeAgo) { this.timeAgo = timeAgo; return this; }
            public RecentActivityDTOBuilder timestamp(String timestamp) { this.timestamp = timestamp; return this; }
            public RecentActivityDTOBuilder description(String description) { this.description = description; return this; }
            public RecentActivityDTOBuilder action(String action) { this.action = action; return this; }
            public RecentActivityDTOBuilder result(String result) { this.result = result; return this; }
            public RecentActivityDTOBuilder officerName(String officerName) { this.officerName = officerName; return this; }

            public RecentActivityDTO build() {
                return new RecentActivityDTO(id, timeAgo, timestamp, description, action, result, officerName);
            }
        }
    }
}
