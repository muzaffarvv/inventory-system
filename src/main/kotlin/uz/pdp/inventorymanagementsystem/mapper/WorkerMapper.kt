package uz.pdp.inventorymanagementsystem.mapper

import org.springframework.stereotype.Component
import uz.pdp.inventorymanagementsystem.base.BaseMapper
import uz.pdp.inventorymanagementsystem.dto.WorkerCreateDTO
import uz.pdp.inventorymanagementsystem.dto.WorkerResponseDTO
import uz.pdp.inventorymanagementsystem.model.Warehouse
import uz.pdp.inventorymanagementsystem.model.Worker

@Component
class WorkerMapper(
    private val warehouseMapper: WarehouseMapper
) : BaseMapper<Worker, WorkerResponseDTO> {

    override fun toDTO(entity: Worker): WorkerResponseDTO {
        return WorkerResponseDTO(
            firstName = entity.firstName,
            lastName = entity.lastName,
            phone = entity.phone,
            workerCode = entity.workerCode,
            warehouseResponseDTO = warehouseMapper.toDTO(entity.warehouse)
        ).apply {
            mapBaseFields(entity, this)
        }
    }

    fun toEntity(dto: WorkerCreateDTO, warehouse: Warehouse): Worker {
        return Worker().apply {
            firstName = dto.firstName
            lastName = dto.lastName
            phone = dto.phone
            password = dto.password
            this.warehouse = warehouse
        }
    }
}