package uz.pdp.inventorymanagementsystem.base

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import uz.pdp.inventorymanagementsystem.enums.Status
import uz.pdp.inventorymanagementsystem.model.Worker
import java.time.Instant
import java.util.*

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseModel {

    @Id
    @GeneratedValue
    @Column(updatable = false, nullable = false)
    var id: UUID? = null

    @CreatedBy
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    lateinit var createdBy: Worker

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    var createdAt: Instant? = null

    @LastModifiedBy
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by")
    lateinit var updatedBy: Worker

    @LastModifiedDate
    @Column(name = "updated_at")
    var updatedAt: Instant? = null

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    var status: Status = Status.ACTIVE

    @Column(nullable = false)
    var deleted: Boolean = false

    // var deletedAt: Instant? = null and deletedBy: Worker? = null can be added if needed
}
