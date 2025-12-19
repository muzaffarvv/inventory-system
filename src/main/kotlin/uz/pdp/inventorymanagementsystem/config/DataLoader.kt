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
import uz.pdp.inventorymanagementsystem.enums.Status
import uz.pdp.inventorymanagementsystem.model.Warehouse

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

        log.info("Starting DataLoader...")

        // ===== PERMISSIONS =====
        val readUsers = permissionService.createIfNotExists("Read users", "READ_USERS")
        val createProduct = permissionService.createIfNotExists("Create product", "CREATE_PRODUCT")
        val manageWarehouse = permissionService.createIfNotExists("Manage warehouse", "MANAGE_WAREHOUSE")
        val adminPermission = permissionService.createIfNotExists("Admin permission", "ADMIN_PERMISSION")

        // ===== ROLE =====
        val adminRole = roleService.createIfNotExists(
            name = "Administrator",
            code = "ADMIN",
            permissions = setOf(readUsers, createProduct, manageWarehouse, adminPermission)
        )

        // ===== WAREHOUSE (AUTO CREATE) =====
        val warehouse = warehouseRepo.findTopByOrderByCreatedAtDesc()
            ?: warehouseRepo.save(
                Warehouse().apply {
                    name = "Main Warehouse"
                    code = "WH-001"
                    status = Status.ACTIVE
                    deleted = false
                }
            )

        // ===== ADMIN EMPLOYEE =====
        if (employeeRepo.findByPhone("+998900000000") == null) {
            employeeRepo.save(
                Employee().apply {
                    firstName = "System"
                    lastName = "Admin"
                    phone = "+998900000000"
                    employeeCode = "EMP-001"
                    password = passwordEncoder.encode("admin123")
                    this.warehouse = warehouse
                    roles = mutableSetOf(adminRole)
                }
            )
            log.info("Default ADMIN employee created")
        } else {
            log.info("Admin employee already exists")
        }

        log.info("DataLoader finished successfully")
    }
}