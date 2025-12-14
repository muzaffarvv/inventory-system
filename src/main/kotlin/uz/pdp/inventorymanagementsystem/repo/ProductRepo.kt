package uz.pdp.inventorymanagementsystem.repo

import org.springframework.stereotype.Repository
import uz.pdp.inventorymanagementsystem.base.BaseRepo
import uz.pdp.inventorymanagementsystem.model.Product
import java.util.Optional
import java.util.UUID

@Repository
interface ProductRepo : BaseRepo<Product> {
    // name, code, id boyicha tekshiruv uchun kerak
    fun findByName(name: String): Product?
    fun findByCode(code: String): Product?
    override fun findById(id: UUID): Optional<Product?>
    override fun findAll(): List<Product>



    // -------------------------------
    // ID bo'yicha bitta product
    fun findActiveProductById(id: UUID): Product?
    fun findNonActiveProductById(id: UUID): Product?
    fun findDeletedProductById(id: UUID): Product?

    // -------------------------------
    // Name bo'yicha bitta product
    fun findActiveProductByName(name: String): Product?
    fun findNonActiveProductByName(name: String): Product?
    fun findDeletedProductByName(name: String): Product?

    // -------------------------------
    // Barcha productlar
    fun findAllActive(): List<Product>
    fun findAllNonActive(): List<Product>
    fun findAllDeleted(): List<Product>
    fun findAllActiveAndNonDeleted(): List<Product>
    fun findAllNonActiveAndNonDeleted(): List<Product>

    // -------------------------------
    // Name orqali mavjudligini tekshirish
    fun existsActiveProductByName(name: String): Boolean
    fun existsNonActiveProductByName(name: String): Boolean

}