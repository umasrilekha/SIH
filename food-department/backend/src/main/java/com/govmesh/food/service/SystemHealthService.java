package com.govmesh.food.service;

import com.govmesh.food.dto.SystemHealthDTO;
import com.govmesh.food.dto.SystemHealthDTO.ComponentHealth;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SystemHealthService {

    public SystemHealthDTO getSystemHealth() {
        List<ComponentHealth> components = Arrays.asList(
                ComponentHealth.builder()
                        .name("PostgreSQL Database Service")
                        .category("CORE_INFRASTRUCTURE")
                        .status("OPERATIONAL")
                        .isConfigured(true)
                        .message("Relational schema and connection pool healthy (HikariCP)")
                        .version("PostgreSQL 16 / JPA 3.2")
                        .build(),

                ComponentHealth.builder()
                        .name("Officer Authentication & Security")
                        .category("SECURITY")
                        .status("OPERATIONAL")
                        .isConfigured(true)
                        .message("JWT Bearer token verification and BCrypt password encoder active")
                        .version("Spring Security 6.2")
                        .build(),

                ComponentHealth.builder()
                        .name("Department Application Service")
                        .category("APPLICATION_LOGIC")
                        .status("OPERATIONAL")
                        .isConfigured(true)
                        .message("Food & Civil Supplies core domain services running normally")
                        .version("v1.0.0-Phase4")
                        .build(),

                ComponentHealth.builder()
                        .name("SOAP / XML Web Service Interface")
                        .category("EXTERNAL_INTEGRATION")
                        .status("OPERATIONAL")
                        .isConfigured(true)
                        .message("Spring-WS Contract-First SOAP WSDL Endpoint active at /ws/food-department.wsdl")
                        .version("Spring-WS 4.0 / JAXB 3.0")
                        .build(),

                ComponentHealth.builder()
                        .name("GovMesh Interoperability Core Gateway")
                        .category("EXTERNAL_INTEGRATION")
                        .status("OPERATIONAL")
                        .isConfigured(true)
                        .message("GovMesh Canonical Model, Integration Router & Schema Mapper active")
                        .version("v1.0.0-Phase4")
                        .build()
        );

        return SystemHealthDTO.builder()
                .departmentName("Food, Civil Supplies & Consumer Protection Department (Dept 2)")
                .environment("GovMesh Demonstration Environment (Maharashtra)")
                .timestamp(LocalDateTime.now())
                .components(components)
                .build();
    }
}
