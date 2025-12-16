package uz.pdp.inventorymanagementsystem.repo

import uz.pdp.inventorymanagementsystem.base.BaseRepo
import uz.pdp.inventorymanagementsystem.model.AuthPermission

interface AuthPermissionRepo : BaseRepo<AuthPermission> {
    fun findByCode(code: String): AuthPermission?
}