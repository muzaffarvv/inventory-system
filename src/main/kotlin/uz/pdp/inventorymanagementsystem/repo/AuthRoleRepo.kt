package uz.pdp.inventorymanagementsystem.repo

import uz.pdp.inventorymanagementsystem.base.BaseRepo
import uz.pdp.inventorymanagementsystem.model.AuthRole
import java.util.UUID

interface AuthRoleRepo : BaseRepo<AuthRole>{

    fun existsByCodeAndDeletedFalse(code: String) : Boolean

    fun findByCode(code: String): AuthRole?
    override fun findByIdAndDeletedFalse(id: UUID): AuthRole?
}
