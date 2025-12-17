package uz.pdp.inventorymanagementsystem.repo

import org.springframework.stereotype.Repository
import uz.pdp.inventorymanagementsystem.base.BaseRepo
import uz.pdp.inventorymanagementsystem.model.Product
import java.util.Optional
import java.util.UUID

@Repository
interface ProductRepo : BaseRepo<Product> {

    override fun findById(id: UUID): Optional<Product?>
    override fun findAll(): List<Product>

    fun findTopByOrderByCreatedAtDesc(): Product?


    fun findByNameAndDeletedFalse(name: String): Product?

    fun findAllByDeletedFalse(): Set<Product>


}