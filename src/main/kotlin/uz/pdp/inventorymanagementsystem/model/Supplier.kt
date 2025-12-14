package uz.pdp.inventorymanagementsystem.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import uz.pdp.inventorymanagementsystem.base.BaseModel

@Entity
@Table(name = "suppliers")
class Supplier : BaseModel() {

    @Column(nullable = false)
    var name: String = ""

    @Column(nullable = false, length = 20)
    var phone: String = ""

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "supplier_brands",
        joinColumns = [JoinColumn(name = "supplier_id")],
        inverseJoinColumns = [JoinColumn(name = "brand_id")]
    )
    var brands: MutableSet<Brand> = HashSet()
}
