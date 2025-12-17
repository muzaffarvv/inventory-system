package uz.pdp.inventorymanagementsystem.config

import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.transaction.annotation.Transactional
import uz.pdp.inventorymanagementsystem.model.Employee
import uz.pdp.inventorymanagementsystem.service.AuthPermissionService
import uz.pdp.inventorymanagementsystem.service.AuthRoleService
import uz.pdp.inventorymanagementsystem.repo.EmployeeRepo
import uz.pdp.inventorymanagementsystem.repo.WarehouseRepo
import org.slf4j.LoggerFactory

@Configuration
class DataLoader {

    private val log = LoggerFactory.getLogger(DataLoader::class.java)

    @Bean
    @Transactional
    fun initDataLoader(
        permissionService: AuthPermissionService,
        roleService: AuthRoleService,
        employeeRepo: EmployeeRepo,
        warehouseRepo: WarehouseRepo,
        passwordEncoder: PasswordEncoder
    ) = CommandLineRunner {

        log.info("Initializing default permissions, roles, and admin user...")

        // ===================== Permissions =====================
        val readUsers = permissionService.createIfNotExists("Read users", "READ_USERS")
        val createProduct = permissionService.createIfNotExists("Create product", "CREATE_PRODUCT")
        val manageWarehouse = permissionService.createIfNotExists("Manage warehouse", "MANAGE_WAREHOUSE")
        val adminPermission = permissionService.createIfNotExists("Admin permission", "ADMIN_PERMISSION")

        // ===================== Roles =====================
        val adminRole = roleService.createIfNotExists(
            name = "Administrator",
            code = "ADMIN",
            permissions = setOf(readUsers, createProduct, manageWarehouse, adminPermission)
        )

        val userRole = roleService.createIfNotExists(
            name = "User",
            code = "USER",
            permissions = setOf(readUsers)
        )

        // ===================== Default ADMIN Employee =====================
        val defaultAdminPhone = "+998900000000"
        if (employeeRepo.findByPhone(defaultAdminPhone) == null) {
            val warehouse = warehouseRepo.findTopByOrderByCreatedAtDesc()
                ?: throw IllegalStateException("Warehouse not found for default admin")

            employeeRepo.save(
                Employee().apply {
                    firstName = "System"
                    lastName = "Admin"
                    phone = defaultAdminPhone
                    employeeCode = "EMP-001"
                    password = passwordEncoder.encode("admin123")
                    this.warehouse = warehouse
                    roles = mutableSetOf(adminRole)
                }
            )
            log.info("Default ADMIN user created with phone $defaultAdminPhone")
        } else {
            log.info("Default ADMIN user already exists")
        }

        log.info("DataLoader initialization complete")
    }
}
