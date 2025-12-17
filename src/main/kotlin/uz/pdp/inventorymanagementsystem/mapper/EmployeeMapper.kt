package uz.pdp.inventorymanagementsystem.mapper

import org.springframework.stereotype.Component
import uz.pdp.inventorymanagementsystem.base.BaseMapper
import uz.pdp.inventorymanagementsystem.dto.AuthRoleDTO
import uz.pdp.inventorymanagementsystem.dto.EmployeeCreateDTO
import uz.pdp.inventorymanagementsystem.dto.EmployeeResponseDTO
import uz.pdp.inventorymanagementsystem.model.Employee
import uz.pdp.inventorymanagementsystem.model.Warehouse

@Component
class EmployeeMapper(
    private val warehouseMapper: WarehouseMapper
) : BaseMapper<Employee, EmployeeResponseDTO> {

    override fun toDTO(entity: Employee): EmployeeResponseDTO {
        return EmployeeResponseDTO(
            firstName = entity.firstName,
            lastName = entity.lastName,
            phone = entity.phone,
            employeeCode = entity.employeeCode,
            warehouseResponseDTO = warehouseMapper.toDTO(entity.warehouse),
            roles = entity.roles.map { AuthRoleDTO.fromEntity(it) }.toSet()
        ).apply {
            mapBaseFields(entity, this)
        }
    }

    fun toEntity(dto: EmployeeCreateDTO, warehouse: Warehouse): Employee {
        return Employee().apply {
            firstName = dto.firstName
            lastName = dto.lastName
            phone = dto.phone
            password = dto.password
            this.warehouse = warehouse
        }
    }
}