package uz.pdp.inventorymanagementsystem.repo

import uz.pdp.inventorymanagementsystem.base.BaseRepo
import uz.pdp.inventorymanagementsystem.model.AuthPermission

interface AuthPermissionRepo : BaseRepo<AuthPermission> {
    fun existsByCodeAndDeletedFalse(code: String): Boolean
    fun findByCode(code: String): AuthPermission?
    fun existsByCode(code: String): Boolean
}