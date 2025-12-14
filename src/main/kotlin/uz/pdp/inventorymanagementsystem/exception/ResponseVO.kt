package uz.pdp.inventorymanagementsystem.exception

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class ResponseVO<T>(
    val status: Int,
    val errors: Map<String, String>? = null,  // field-level yoki global errors
    val data: T? = null,                      // success response uchun entity/DTO
    val timestamp: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
    val source: String? = null                // exception qayerdan kelgani
)
