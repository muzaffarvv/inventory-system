package uz.pdp.inventorymanagementsystem.model

import jakarta.persistence.*
import uz.pdp.inventorymanagementsystem.base.BaseModel
import uz.pdp.inventorymanagementsystem.enums.WarehouseTransactionType
import java.time.LocalDate

@Entity
@Table(name = "warehouse_transactions")
class WarehouseTransaction : BaseModel() {

    @Column(nullable = false)
    var date: LocalDate = LocalDate.now()

    @Column(length = 255)
    var description: String? = null

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 25)
    lateinit var type: WarehouseTransactionType   // INBOUND / OUTBOUND

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false)
    lateinit var warehouse: Warehouse

    @OneToMany(
        mappedBy = "transaction",
        cascade = [CascadeType.ALL],
        orphanRemoval = true
    )
    var items: MutableList<WarehouseItem> = mutableListOf()

    @Column(nullable = false, unique = true, length = 100)
    var factoryNumber: String = "" // faktura / hujjat raqami
}
