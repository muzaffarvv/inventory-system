package uz.pdp.inventorymanagementsystem.model

import jakarta.persistence.*
import uz.pdp.inventorymanagementsystem.base.BaseModel

@Entity
@Table(name = "suppliers")
class Supplier : BaseModel() {

    @Column(nullable = false)
    var about: String = ""

    @Column(nullable = false, length = 20)
    var phone: String = ""

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    var brand: Brand? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id")
    var warehouse: Warehouse? = null
}
