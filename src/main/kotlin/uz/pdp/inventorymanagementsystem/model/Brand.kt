package uz.pdp.inventorymanagementsystem.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.ManyToMany
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import uz.pdp.inventorymanagementsystem.base.BaseModel

@Entity
@Table(name = "brands")
class Brand : BaseModel() {

    @Column(name = "name", unique = true)
    var name: String = ""

    @Column(name = "country")
    var country: String = ""

    @OneToMany(mappedBy = "brand", fetch = FetchType.LAZY)
    var suppliers: MutableSet<Supplier> = HashSet()
}
