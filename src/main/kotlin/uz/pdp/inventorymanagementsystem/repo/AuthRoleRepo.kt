package uz.pdp.inventorymanagementsystem.repo

import uz.pdp.inventorymanagementsystem.base.BaseRepo
import uz.pdp.inventorymanagementsystem.model.AuthRole

interface AuthRoleRepo : BaseRepo<AuthRole>{
    fun findByCode(code: String): AuthRole?
}