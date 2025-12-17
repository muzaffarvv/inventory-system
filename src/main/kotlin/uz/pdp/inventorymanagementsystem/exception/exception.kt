package uz.pdp.inventorymanagementsystem.exception

import uz.pdp.inventorymanagementsystem.enums.ErrorCodes
import java.util.UUID

// Base exception barcha custom exceptionlar uchun
sealed class BaseException(
    val errorCode: ErrorCodes,                 // xatoning kodini belgilaydi
    val fieldErrors: Map<String, String>? = null // optional: field-level xatolar
) : RuntimeException()

// Concrete exceptions
class ValidationException(
    fieldErrors: Map<String, String>
) : BaseException(ErrorCodes.VALIDATION_ERROR, fieldErrors)

class BrandNotFoundException(msg: String) : BaseException(ErrorCodes.BRAND_NOT_FOUND)
class BrandAlreadyExistsException(name: String) : BaseException(ErrorCodes.BRAND_ALREADY_EXISTS)

class TagNotFoundException(msg: String) : BaseException(ErrorCodes.TAG_NOT_FOUND)
class TagAlreadyExistsException(name: String) : BaseException(ErrorCodes.TAG_ALREADY_EXISTS)

class UoMNotFoundException(msg: String) : BaseException(ErrorCodes.UOM_NOT_FOUND)
class UoMAlreadyExistsException(msg: String) : BaseException(ErrorCodes.UOM_ALREADY_EXISTS)

class SupplierNotFoundException(msg: String) : BaseException(ErrorCodes.SUPPLIER_NOT_FOUND)

class EmployeeNotFoundException(msg: String) : BaseException(ErrorCodes.EMPLOYEE_NOT_FOUND)

class CategoryNotFoundException(msg: String) : BaseException(ErrorCodes.CATEGORY_NOT_FOUND)

class RoleNotFoundException(msg: String) : BaseException(ErrorCodes.ROLE_NOT_FOUND)
class PermissionNotFoundException(msg: String) : BaseException(ErrorCodes.PERMISSION_NOT_FOUND)

class CategoryAlreadyExistsException(msg: String) : BaseException(ErrorCodes.CATEGORY_ALREADY_EXISTS)
class ProductNotFoundException(msg: String) : BaseException(ErrorCodes.PRODUCT_NOT_FOUND)
class FileNotFoundException(msg: String) : BaseException(ErrorCodes.FILE_NOT_FOUND)
class WarehouseNotFoundException(msg: String) : BaseException(ErrorCodes.WAREHOUSE_NOT_FOUND)
