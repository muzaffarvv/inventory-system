package uz.pdp.inventorymanagementsystem.model

import jakarta.persistence.*
import uz.pdp.inventorymanagementsystem.base.BaseModel

@Entity
@Table(name = "roles")
class AuthRole : BaseModel() {

    @Column(nullable = false, unique = true, length = 50)
    var code: String = ""        // unique code, masalan "ADMIN"

    @Column(nullable = false, length = 75)
    var name: String = ""        // display nom, masalan "Administrator"

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "role_permissions",
        joinColumns = [JoinColumn(name = "role_id")],
        inverseJoinColumns = [JoinColumn(name = "permission_id")]
    )
    var permissions: MutableSet<AuthPermission> = HashSet()
}