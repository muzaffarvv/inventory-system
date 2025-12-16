package uz.pdp.inventorymanagementsystem.enums

enum class ErrorCodes(val code: Long) {
    BRAND_NOT_FOUND(206),
    BRAND_ALREADY_EXISTS(207),

    TAG_NOT_FOUND(208),
    TAG_ALREADY_EXISTS(209),

    PRODUCT_NOT_FOUND(200),

    WAREHOUSE_NOT_FOUND(204),

    WORKER_NOT_FOUND(201),

    FILE_NOT_FOUND(205),

    WORKER_ALREADY_EXISTS(202),

    CATEGORY_ALREADY_EXISTS(203),
}
