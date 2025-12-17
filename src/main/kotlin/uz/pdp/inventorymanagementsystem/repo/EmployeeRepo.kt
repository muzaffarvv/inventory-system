package uz.pdp.inventorymanagementsystem.repo

import uz.pdp.inventorymanagementsystem.base.BaseRepo
import uz.pdp.inventorymanagementsystem.model.Employee

interface EmployeeRepo : BaseRepo<Employee> {
    fun existsByPhoneAndDeletedFalse(phone: String): Boolean

    fun findTopByOrderByCreatedAtDesc(): Employee?
    fun findByPhone(phone: String): Employee?

    fun findAllByDeletedFalse(): Set<Employee>
}