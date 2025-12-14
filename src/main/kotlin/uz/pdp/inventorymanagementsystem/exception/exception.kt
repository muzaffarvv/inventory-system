package uz.pdp.inventorymanagementsystem.exception

import uz.pdp.inventorymanagementsystem.enums.ErrorCodes

// Base exception barcha custom exceptionlar uchun
sealed class BaseException(
    val errorCode: ErrorCodes,                 // xatoning kodini belgilaydi
    val fieldErrors: Map<String, String>? = null // optional: field-level xatolar
) : RuntimeException()

// Concrete exceptions
class ProductNotFoundException : BaseException(ErrorCodes.PRODUCT_NOT_FOUND)
class WorkerNotFoundException : BaseException(ErrorCodes.WORKER_NOT_FOUND)
class WorkerAlreadyExistsException : BaseException(ErrorCodes.WORKER_ALREADY_EXISTS)
class CategoryAlreadyExistsException : BaseException(ErrorCodes.CATEGORY_ALREADY_EXISTS)
class WarehouseNotFoundException : BaseException(ErrorCodes.WAREHOUSE_NOT_FOUND)
