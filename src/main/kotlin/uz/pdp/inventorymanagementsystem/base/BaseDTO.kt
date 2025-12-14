
package uz.pdp.inventorymanagementsystem.base

import java.time.Instant
import java.util.UUID

abstract class BaseDTO {
    var id: UUID? = null
    var createdAt: Instant? = null
    var updatedAt: Instant? = null
    var deleted: Boolean = false
}