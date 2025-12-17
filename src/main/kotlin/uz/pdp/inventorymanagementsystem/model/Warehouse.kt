package uz.pdp.inventorymanagementsystem.model

import jakarta.persistence.*
import uz.pdp.inventorymanagementsystem.base.BaseModel

@Entity
@Table(
    name = "warehouses",
    uniqueConstraints = [UniqueConstraint(columnNames = ["name", "address"])],
    indexes = [
        Index(name = "idx_warehouse_code", columnList = "code"),
        Index(name = "idx_warehouse_name", columnList = "name")
    ]
)
class Warehouse : BaseModel() {

    @Column(nullable = false, unique = true, length = 75)
    var name: String = ""

    @Column(length = 255)
    var address: String? = null

    @Column(nullable = false, unique = true, length = 20)
    var code: String = ""

    @OneToMany(mappedBy = "warehouse", fetch = FetchType.LAZY)
    var suppliers: MutableSet<Supplier> = HashSet()
}
