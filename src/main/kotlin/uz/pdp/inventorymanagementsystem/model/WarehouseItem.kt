package uz.pdp.inventorymanagementsystem.model

import jakarta.persistence.*
import uz.pdp.inventorymanagementsystem.base.BaseModel
import java.math.BigDecimal
import java.time.Instant

@Entity
@Table(name = "warehouse_items")
class WarehouseItem : BaseModel() {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id", nullable = false)
    lateinit var transaction: WarehouseTransaction

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    lateinit var product: Product

    @Column(nullable = false)
    var quantity: BigDecimal = BigDecimal.ZERO

    @Column(nullable = false)
    var unitPrice: BigDecimal = BigDecimal.ZERO

    @Column(nullable = false)
    var totalPrice: BigDecimal = BigDecimal.ZERO // unitPrice * quantity

    @Column(name = "expire_at")
    var expireAt: Instant? = null
}

