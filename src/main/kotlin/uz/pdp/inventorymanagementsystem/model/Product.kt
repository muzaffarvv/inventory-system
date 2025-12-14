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

    @Column
    var weight: Double? = null                    // Og'irlik

    @Column(nullable = false)
    var price: BigDecimal = BigDecimal.ZERO       // Narx (default 0)

    @Column
    var profit: Int? = null                       // Foyda

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_id")
    var currency: Currency? = null                // Valyuta

    @Column(unique = true, nullable = false)
    var productCode: String = ""                  // Takrorlanmas mahsulot raqami
}
