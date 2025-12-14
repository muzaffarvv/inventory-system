package uz.pdp.inventorymanagementsystem.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import uz.pdp.inventorymanagementsystem.base.BaseModel

@Entity
@Table(name = "tags")
class Tag : BaseModel() {

    @Column(name = "name")
    var name: String = ""
}