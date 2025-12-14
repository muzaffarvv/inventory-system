package uz.pdp.inventorymanagementsystem.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import uz.pdp.inventorymanagementsystem.base.BaseModel

@Entity
@Table(name = "uom") // unit of measurement
class UoM : BaseModel() {

    @Column(nullable = false, unique = true, length = 15)
    var code: String = ""   // masalan: "KG", "L", "PCS"

    @Column(nullable = false, length = 75)
    var name: String = ""   // display nomi, masalan: "Kilogram", "Liter", "Dona" maybe "Yashik"
}