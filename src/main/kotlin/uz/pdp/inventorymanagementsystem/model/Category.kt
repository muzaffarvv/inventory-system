package uz.pdp.inventorymanagementsystem.model

import jakarta.persistence.*
import uz.pdp.inventorymanagementsystem.base.BaseModel

@Entity
@Table(name = "categories")
class Category : BaseModel() {

    @Column(nullable = false, unique = true, length = 50)
    var name: String = ""

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    var parent: Category? = null

    @OneToMany(
        mappedBy = "parent",
        fetch = FetchType.LAZY,
        cascade = [CascadeType.ALL],
        orphanRemoval = true
    )
    var children: MutableList<Category> = ArrayList()

    @Column(name = "is_last", nullable = false)
    var isLast: Boolean = true // default true
}

