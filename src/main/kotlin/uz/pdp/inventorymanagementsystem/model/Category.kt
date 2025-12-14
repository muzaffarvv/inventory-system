package uz.pdp.inventorymanagementsystem.model

import jakarta.persistence.*
import uz.pdp.inventorymanagementsystem.base.BaseModel

@Entity
@Table(name = "categories")
class Category : BaseModel() {

    @Column(nullable = false)
    var name: String = ""

    // Parent category
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    var parent: Category? = null

    // Children categories
    @OneToMany(
        mappedBy = "parent",
        fetch = FetchType.LAZY,
        cascade = [CascadeType.ALL],
        orphanRemoval = true
    )
    var children: MutableList<Category> = ArrayList()
}
