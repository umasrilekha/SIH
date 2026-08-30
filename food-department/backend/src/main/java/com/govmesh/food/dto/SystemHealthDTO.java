package com.govmesh.food.dto;

import java.time.LocalDateTime;
import java.util.List;

public class SystemHealthDTO {
    private String departmentName;
    private String environment;
    private LocalDateTime timestamp;
    private List<ComponentHealth> components;

    public SystemHealthDTO() {}
    public SystemHealthDTO(String departmentName, String environment, LocalDateTime timestamp, List<ComponentHealth> components) {
        this.departmentName = departmentName;
        this.environment = environment;
        this.timestamp = timestamp;
        this.components = components;
    }

    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }

    public String getEnvironment() { return environment; }
    public void setEnvironment(String environment) { this.environment = environment; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public List<ComponentHealth> getComponents() { return components; }
    public void setComponents(List<ComponentHealth> components) { this.components = components; }

    public static SystemHealthDTOBuilder builder() { return new SystemHealthDTOBuilder(); }

    public static class SystemHealthDTOBuilder {
        private String departmentName;
        private String environment;
        private LocalDateTime timestamp;
        private List<ComponentHealth> components;

        public SystemHealthDTOBuilder departmentName(String departmentName) { this.departmentName = departmentName; return this; }
        public SystemHealthDTOBuilder environment(String environment) { this.environment = environment; return this; }
        public SystemHealthDTOBuilder timestamp(LocalDateTime timestamp) { this.timestamp = timestamp; return this; }
        public SystemHealthDTOBuilder components(List<ComponentHealth> components) { this.components = components; return this; }

        public SystemHealthDTO build() {
            return new SystemHealthDTO(departmentName, environment, timestamp, components);
        }
    }

    public static class ComponentHealth {
        private String name;
        private String category;
        private String status;
        private boolean isConfigured;
        private String message;
        private String version;

        public ComponentHealth() {}
        public ComponentHealth(String name, String category, String status, boolean isConfigured, String message, String version) {
            this.name = name;
            this.category = category;
            this.status = status;
            this.isConfigured = isConfigured;
            this.message = message;
            this.version = version;
        }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }

        public boolean isConfigured() { return isConfigured; }
        public void setConfigured(boolean isConfigured) { this.isConfigured = isConfigured; }

        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }

        public String getVersion() { return version; }
        public void setVersion(String version) { this.version = version; }

        public static ComponentHealthBuilder builder() { return new ComponentHealthBuilder(); }

        public static class ComponentHealthBuilder {
            private String name;
            private String category;
            private String status;
            private boolean isConfigured;
            private String message;
            private String version;

            public ComponentHealthBuilder name(String name) { this.name = name; return this; }
            public ComponentHealthBuilder category(String category) { this.category = category; return this; }
            public ComponentHealthBuilder status(String status) { this.status = status; return this; }
            public ComponentHealthBuilder isConfigured(boolean isConfigured) { this.isConfigured = isConfigured; return this; }
            public ComponentHealthBuilder message(String message) { this.message = message; return this; }
            public ComponentHealthBuilder version(String version) { this.version = version; return this; }

            public ComponentHealth build() {
                return new ComponentHealth(name, category, status, isConfigured, message, version);
            }
        }
    }
}
