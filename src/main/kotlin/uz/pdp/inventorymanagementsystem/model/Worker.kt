package uz.pdp.inventorymanagementsystem.model

import jakarta.persistence.*
import uz.pdp.inventorymanagementsystem.base.BaseModel

@Entity
@Table(name = "workers")
class Worker : BaseModel() {

    @Column(nullable = false, length = 50)
    var firstName: String = ""

    @Column(nullable = false, length = 50)
    var lastName: String = ""

    @Column(nullable = false, unique = true, length = 30)
    var phone: String = ""

    @Column(nullable = false, unique = true, length = 50)
    var workerCode: String = ""   // tizim generatsiya qiladi

    @Column(nullable = false)
    var password: String = ""

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false)
    lateinit var warehouse: Warehouse
}
