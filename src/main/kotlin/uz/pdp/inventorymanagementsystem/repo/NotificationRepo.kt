package uz.pdp.inventorymanagementsystem.repo

import org.springframework.stereotype.Repository
import uz.pdp.inventorymanagementsystem.base.BaseRepo
import uz.pdp.inventorymanagementsystem.model.Notification
import uz.pdp.inventorymanagementsystem.model.WarehouseItem

@Repository
interface NotificationRepo : BaseRepo<Notification> {
    fun findByWarehouseItemAndDeletedFalse(item: WarehouseItem): Notification?
}