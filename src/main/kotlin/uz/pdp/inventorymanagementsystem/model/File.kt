package uz.pdp.inventorymanagementsystem.model

import jakarta.persistence.*
import uz.pdp.inventorymanagementsystem.base.BaseModel

@Entity
@Table(name = "files")
class File : BaseModel() {

    @Column(name = "org_name")
    var orgName: String = ""

    @Column(name = "key_name")
    var keyName: String = ""   // Server/storage uchun key name

    @Column(name = "size")
    var size: Int = 0

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    var product: Product? = null     // Product (1)  <------>  (*) File
}