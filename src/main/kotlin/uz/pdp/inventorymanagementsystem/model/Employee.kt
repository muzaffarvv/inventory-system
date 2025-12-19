package uz.pdp.inventorymanagementsystem.model

import jakarta.persistence.*
import uz.pdp.inventorymanagementsystem.base.BaseModel

@Entity
@Table(name = "employees")
class Employee : BaseModel() {

    @Column(nullable = false, length = 50)
    var firstName: String = ""

    @Column(nullable = false, length = 50)
    var lastName: String = ""

    @Column(nullable = false, unique = true, length = 30)
    var phone: String = ""

    @Column(nullable = false, unique = true, length = 50)
    var employeeCode: String = ""   // tizim generatsiya qiladi

    @Column(nullable = false, length = 75)
    var password: String = ""

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false)
    lateinit var warehouse: Warehouse

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "employee_roles",
        joinColumns = [JoinColumn(name = "employee_id")],
        inverseJoinColumns = [JoinColumn(name = "role_id")]
    )
    var roles: MutableSet<AuthRole> = HashSet()
}
