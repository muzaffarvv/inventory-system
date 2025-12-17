package uz.pdp.inventorymanagementsystem.model

import uz.pdp.inventorymanagementsystem.base.BaseModel
import jakarta.persistence.*
import java.math.BigDecimal
import kotlin.collections.HashSet

@Entity
@Table(name = "products")
class Product : BaseModel() {  // yaroqlilik muddati qo'shish kerak

    @Column(nullable = false, length = 125)
    var name: String = ""                         // Mahsulot nomi

    @Column
    var description: String = ""                  // Tavsif

    @Column(nullable = false)
    var price: BigDecimal = BigDecimal.ZERO       // Narx (default 0)

    @Column
    var profit: Int? = null                       // Foyda


    @Column(unique = true, nullable = false, length = 30)
    var productCode: String = ""                  // Takrorlanmas mahsulot raqami

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    var brand: Brand? = null                      // Brand (tanlanadi)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    var category: Category? = null                // Category (tanlanadi)

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "product_tags",
        joinColumns = [JoinColumn(name = "product_id")],
        inverseJoinColumns = [JoinColumn(name = "tag_id")]
    )
    var tags: MutableSet<Tag> = HashSet()         // Tags (bir nechta bo'lishi mumkin)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uom_id")
    var uom: UoM? = null                          // Unit of Measurement

}
