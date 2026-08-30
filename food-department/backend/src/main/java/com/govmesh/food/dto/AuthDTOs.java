package com.govmesh.food.dto;

import java.time.LocalDateTime;

public class AuthDTOs {

    public static class LoginRequest {
        private String username;
        private String password;

        public LoginRequest() {}
        public LoginRequest(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    public static class AuthResponse {
        private String token;
        private String tokenType;
        private Long expiresInMs;
        private UserDTO user;

        public AuthResponse() {}
        public AuthResponse(String token, String tokenType, Long expiresInMs, UserDTO user) {
            this.token = token;
            this.tokenType = tokenType;
            this.expiresInMs = expiresInMs;
            this.user = user;
        }

        public String getToken() { return token; }
        public void setToken(String token) { this.token = token; }

        public String getTokenType() { return tokenType; }
        public void setTokenType(String tokenType) { this.tokenType = tokenType; }

        public Long getExpiresInMs() { return expiresInMs; }
        public void setExpiresInMs(Long expiresInMs) { this.expiresInMs = expiresInMs; }

        public UserDTO getUser() { return user; }
        public void setUser(UserDTO user) { this.user = user; }

        public static AuthResponseBuilder builder() { return new AuthResponseBuilder(); }

        public static class AuthResponseBuilder {
            private String token;
            private String tokenType = "Bearer";
            private Long expiresInMs;
            private UserDTO user;

            public AuthResponseBuilder token(String token) { this.token = token; return this; }
            public AuthResponseBuilder tokenType(String tokenType) { this.tokenType = tokenType; return this; }
            public AuthResponseBuilder expiresInMs(Long expiresInMs) { this.expiresInMs = expiresInMs; return this; }
            public AuthResponseBuilder user(UserDTO user) { this.user = user; return this; }

            public AuthResponse build() {
                return new AuthResponse(token, tokenType, expiresInMs, user);
            }
        }
    }

    public static class UserDTO {
        private Long id;
        private String username;
        private String fullName;
        private String role;
        private String department;
        private String employeeId;
        private Boolean isActive;
        private LocalDateTime createdAt;

        public UserDTO() {}
        public UserDTO(Long id, String username, String fullName, String role, String department, String employeeId, Boolean isActive, LocalDateTime createdAt) {
            this.id = id;
            this.username = username;
            this.fullName = fullName;
            this.role = role;
            this.department = department;
            this.employeeId = employeeId;
            this.isActive = isActive;
            this.createdAt = createdAt;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }

        public String getFullName() { return fullName; }
        public void setFullName(String fullName) { this.fullName = fullName; }

        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }

        public String getDepartment() { return department; }
        public void setDepartment(String department) { this.department = department; }

        public String getEmployeeId() { return employeeId; }
        public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }

        public Boolean getIsActive() { return isActive; }
        public void setIsActive(Boolean isActive) { this.isActive = isActive; }

        public LocalDateTime getCreatedAt() { return createdAt; }
        public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

        public static UserDTOBuilder builder() { return new UserDTOBuilder(); }

        public static class UserDTOBuilder {
            private Long id;
            private String username;
            private String fullName;
            private String role;
            private String department;
            private String employeeId;
            private Boolean isActive;
            private LocalDateTime createdAt;

            public UserDTOBuilder id(Long id) { this.id = id; return this; }
            public UserDTOBuilder username(String username) { this.username = username; return this; }
            public UserDTOBuilder fullName(String fullName) { this.fullName = fullName; return this; }
            public UserDTOBuilder role(String role) { this.role = role; return this; }
            public UserDTOBuilder department(String department) { this.department = department; return this; }
            public UserDTOBuilder employeeId(String employeeId) { this.employeeId = employeeId; return this; }
            public UserDTOBuilder isActive(Boolean isActive) { this.isActive = isActive; return this; }
            public UserDTOBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

            public UserDTO build() {
                return new UserDTO(id, username, fullName, role, department, employeeId, isActive, createdAt);
            }
        }
    }
}
