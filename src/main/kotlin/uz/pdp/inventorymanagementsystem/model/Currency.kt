package uz.pdp.inventorymanagementsystem.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import uz.pdp.inventorymanagementsystem.base.BaseModel

@Entity
@Table(name = "currencies")
class Currency : BaseModel() {

    @Column(nullable = false, unique = true)
    var code: String = ""   // masalan: "USD", "UZS", "EUR"

    @Column(nullable = false)
    var name: String = ""   // display nomi, masalan: "US Dollar", "Uzbek so'm yoki sum", "Euro"
}