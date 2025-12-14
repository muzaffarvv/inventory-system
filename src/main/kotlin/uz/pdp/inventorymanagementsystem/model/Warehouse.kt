package uz.pdp.inventorymanagementsystem.model

import jakarta.persistence.*
import uz.pdp.inventorymanagementsystem.base.BaseModel

@Entity
@Table(name = "warehouses")
class Warehouse : BaseModel() {

    @Column(nullable = false, length = 150)
    var name: String = ""

    @Column(length = 255)
    var address: String? = null
}
