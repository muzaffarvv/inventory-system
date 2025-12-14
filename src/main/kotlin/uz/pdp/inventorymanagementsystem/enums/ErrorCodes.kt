package uz.pdp.inventorymanagementsystem.enums

enum class ErrorCodes(val code: Long) {
    PRODUCT_NOT_FOUND(200),
    WORKER_NOT_FOUND(201),
    WORKER_ALREADY_EXISTS(202),
    CATEGORY_ALREADY_EXISTS(203),
    WAREHOUSE_NOT_FOUND(204)
}
