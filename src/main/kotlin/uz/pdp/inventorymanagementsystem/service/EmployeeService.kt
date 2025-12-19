package uz.pdp.inventorymanagementsystem.service

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import uz.pdp.inventorymanagementsystem.base.BaseServiceImpl
import uz.pdp.inventorymanagementsystem.dto.EmployeeCreateDTO
import uz.pdp.inventorymanagementsystem.dto.EmployeeResponseDTO
import uz.pdp.inventorymanagementsystem.dto.EmployeeUpdateDTO
import uz.pdp.inventorymanagementsystem.enums.Status
import uz.pdp.inventorymanagementsystem.exception.ValidationException
import uz.pdp.inventorymanagementsystem.mapper.EmployeeMapper
import uz.pdp.inventorymanagementsystem.model.Employee
import uz.pdp.inventorymanagementsystem.repo.EmployeeRepo
import uz.pdp.inventorymanagementsystem.utils.CodeGenerator
import java.util.UUID

@Service
class EmployeeService(
    override val repository: EmployeeRepo,
    override val mapper: EmployeeMapper,
    private val warehouseService: WarehouseService,
    private val authRoleService: AuthRoleService,
    private val passwordEncoder: PasswordEncoder
) : BaseServiceImpl<
        Employee,
        EmployeeCreateDTO,
        EmployeeUpdateDTO,
        EmployeeResponseDTO,
        EmployeeMapper,
        EmployeeRepo
        >(repository, mapper) {

    /* ===================== CRUD ===================== */
    override fun create(dto: EmployeeCreateDTO): EmployeeResponseDTO {
        validateCreate(dto)

        val entity = convertCreateDtoToEntity(dto)

        val saved = saveAndRefresh(entity)
        return mapper.toDTO(saved)
    }

    override fun getById(id: UUID): EmployeeResponseDTO {
        val entity = getByIdOrThrow(id)
        return mapper.toDTO(entity)
    }

    override fun update(id: UUID, dto: EmployeeUpdateDTO): EmployeeResponseDTO {
        val entity = getByIdOrThrow(id)

        validateUpdate(id, dto)

        updateEntityFromDto(entity, dto)

        val updated = saveAndRefresh(entity)
        return mapper.toDTO(updated)
    }

    override fun delete(id: UUID) {
        val entity = getByIdOrThrow(id)
      //  validateDelete(id)
        entity.deleted = true
        saveAndRefresh(entity)
    }

    /* ===================== HELPERS ===================== */

    fun getByWarehouseId(warehouseId: UUID): Set<EmployeeResponseDTO> =
        repository.findAllByDeletedFalse() // for auditing (with inactive)
            .filter { it.warehouse.id == warehouseId }
            .map(mapper::toDTO)
            .toSet()

    fun getByIdOrThrow(id: UUID): Employee =
        getEntityOrNull(id) ?: throw ValidationException(
            mapOf("employee" to "Employee with id '$id' not found")
        )

    fun getActive(id: UUID): Employee =
        getByIdOrThrow(id).apply {
            if (status != Status.ACTIVE) throw ValidationException(
                mapOf("employee" to "Employee with id '$id' is INACTIVE")
            )
        }

    private fun getCodeLastNumber(): Int {
        // Oxirgi code ni olish, masalan "EMP-0001"
        val lastCode = repository.findTopByOrderByCreatedAtDesc()?.employeeCode ?: return 0
        return lastCode.takeLast(4).toIntOrNull() ?: 0
    }

    /* ===================== MAPPING ===================== */
    override fun convertCreateDtoToEntity(dto: EmployeeCreateDTO): Employee {
        val errors = mutableMapOf<String, String>()

        val warehouse = try {
            warehouseService.getActive(dto.warehouseId)
        } catch (e: Exception) {
            errors["warehouse"] = "Warehouse with id '${dto.warehouseId}' is not ACTIVE or not found"
            throw ValidationException(errors)
        }

        if (errors.isNotEmpty()) throw ValidationException(errors)

        return Employee().apply {
            firstName = dto.firstName
            lastName = dto.lastName
            phone = dto.phone
            password = passwordEncoder.encode(dto.password)
            this.warehouse = warehouse
            employeeCode = CodeGenerator.forEmployee(getCodeLastNumber())
        }
    }

    override fun updateEntityFromDto(entity: Employee, dto: EmployeeUpdateDTO) {
        dto.firstName?.let { entity.firstName = it }
        dto.lastName?.let { entity.lastName = it }
        dto.phone?.let { entity.phone = it }
        dto.password?.let { entity.password = it }
        dto.warehouseId?.let { entity.warehouse = warehouseService.getActive(it) }

        dto.roleCodes?.let { codes ->
            val roles = codes.map { code ->
                authRoleService.getByCode(code)
            }.toMutableSet()
            entity.roles.clear()
            entity.roles.addAll(roles)
        }

    }

    /* ===================== VALIDATION ===================== */
    override fun validateCreate(dto: EmployeeCreateDTO) {
        val errors = mutableMapOf<String, String>()
        if (repository.existsByPhoneAndDeletedFalse(dto.phone)) {
            errors["phone"] = "Employee with phone '${dto.phone}' already exists"
        }
        if (errors.isNotEmpty()) throw ValidationException(errors)
    }

    override fun validateUpdate(id: UUID, dto: EmployeeUpdateDTO) {
        val current = getByIdOrThrow(id)
        val errors = mutableMapOf<String, String>()

        dto.phone?.let { phone ->
            repository.findByPhone(phone)
                ?.takeIf { it.id != current.id }
                ?.let { errors["phone"] = "Employee with phone '$phone' already exists" }
        }

        if (errors.isNotEmpty()) throw ValidationException(errors)
    }
}
