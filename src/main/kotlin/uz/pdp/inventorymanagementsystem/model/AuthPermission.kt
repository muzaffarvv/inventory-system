package uz.pdp.inventorymanagementsystem.model

import jakarta.persistence.*
import uz.pdp.inventorymanagementsystem.base.BaseModel

@Entity
@Table(name = "permissions")
class AuthPermission : BaseModel() {

    @Column(nullable = false, unique = true, length = 75)
    var code: String = ""

    @Column(nullable = false, length = 75)
    var name: String = ""
}

