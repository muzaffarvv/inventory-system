package uz.pdp.inventorymanagementsystem.model

import uz.pdp.inventorymanagementsystem.base.BaseModel
import jakarta.persistence.*
import java.math.BigDecimal
import kotlin.collections.HashSet

@Entity
@Table(name = "products")
class Product : BaseModel() {

    @Column(nullable = false, unique = true, length = 125)
    var name: String = ""

    @Column
    var description: String = ""

    @Column(nullable = false)
    var price: BigDecimal = BigDecimal.ZERO       // tan narxi (default 0)

    @Column
    var profit: Int? = null                       // % foyda

    @Column(unique = true, nullable = false, length = 30)
    var productCode: String = ""

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    var brand: Brand? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    var category: Category? = null

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "product_tags",
        joinColumns = [JoinColumn(name = "product_id")],
        inverseJoinColumns = [JoinColumn(name = "tag_id")]
    )
    var tags: MutableSet<Tag> = HashSet()

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uom_id")
    var uom: UoM? = null                          // Unit of Measure

}
