package uz.pdp.inventorymanagementsystem.base

interface BaseMapper<E : BaseModel, R : BaseDTO> {

    fun toDTO(entity: E): R

    fun toDTOList(entities: List<E>): List<R> = entities.map { toDTO(it) }

    fun mapBaseFields(entity: E, dto: R) {
        dto.id = entity.id
        dto.createdAt = entity.createdAt
        dto.updatedAt = entity.updatedAt
        dto.deleted = entity.deleted
    }
}
