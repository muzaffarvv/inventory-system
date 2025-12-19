package uz.pdp.inventorymanagementsystem.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import uz.pdp.inventorymanagementsystem.base.BaseModel

@Entity
@Table(name = "notifications")
class Notification : BaseModel() {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_item_id", nullable = false)
    lateinit var warehouseItem: WarehouseItem

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false)
    lateinit var warehouse: Warehouse          // Audit va reporting uchun

    @Column(nullable = false)
    var alertBeforeDays: Int = 3               // Admin belgilaydi

    @Column(length = 255)
    var message: String? = null
}
