package uz.pdp.inventorymanagementsystem.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import jakarta.servlet.http.HttpServletRequest

@RestControllerAdvice
class GlobalExceptionHandler {

    // BaseException uchun handler
    @ExceptionHandler(BaseException::class)
    fun handleBaseException(ex: BaseException, request: HttpServletRequest?): ResponseEntity<ResponseVO<Void>> {
        val source = ex.stackTrace.firstOrNull()?.let { "${it.className}.${it.methodName}(${it.lineNumber})" }
        val response = ResponseVO<Void>(
            status = 400,
            errors = ex.fieldErrors ?: mapOf("error" to ex.errorCode.name),
            data = null,
            source = source
        )
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response)
    }

    // Validation xatolari (DTO validation)
    @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException::class)
    fun handleValidation(ex: org.springframework.web.bind.MethodArgumentNotValidException, request: HttpServletRequest?): ResponseEntity<ResponseVO<Void>> {
        val errors = ex.bindingResult.fieldErrors.associate { it.field to (it.defaultMessage ?: "Invalid value") }
        val response = ResponseVO<Void>(
            status = 400,
            errors = errors,
            data = null,
            source = request?.requestURI
        )
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response)
    }

    // Generic fallback for unhandled exceptions
    @ExceptionHandler(Exception::class)
    fun handleGeneric(ex: Exception, request: HttpServletRequest?): ResponseEntity<ResponseVO<Void>> {
        val source = ex.stackTrace.firstOrNull()?.let { "${it.className}.${it.methodName}(${it.lineNumber})" }
        val response = ResponseVO<Void>(
            status = 500,
            errors = mapOf("unexpectedError" to (ex.message ?: "Unknown error")),
            data = null,
            source = source
        )
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response)
    }
}
