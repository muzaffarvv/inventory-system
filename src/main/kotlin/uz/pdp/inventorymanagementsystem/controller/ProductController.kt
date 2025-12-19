
package uz.pdp.inventorymanagementsystem.controller

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import uz.pdp.inventorymanagementsystem.dto.ProductCreateDTO
import uz.pdp.inventorymanagementsystem.dto.ProductResponseDTO
import uz.pdp.inventorymanagementsystem.dto.ProductUpdateDTO
import uz.pdp.inventorymanagementsystem.service.ProductService
import java.util.UUID
import jakarta.validation.Valid

@RestController
@RequestMapping("/api/v1/products")
class ProductController(
    private val productService: ProductService
) {

    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun create(
        @Valid @RequestPart("product") dto: ProductCreateDTO,
        @RequestPart("files", required = false) files: List<MultipartFile>?
    ): ResponseEntity<ProductResponseDTO> =
        ResponseEntity.status(HttpStatus.CREATED)
            .body(productService.createWithFiles(dto, files))

    @GetMapping("/{id}")
    fun getById(@PathVariable id: UUID): ResponseEntity<ProductResponseDTO> =
        ResponseEntity.ok(productService.getById(id))

    @GetMapping
    fun getAll(): ResponseEntity<List<ProductResponseDTO>> =
        ResponseEntity.ok(productService.getAll())

    @PutMapping("/{id}", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun update(
        @PathVariable id: UUID,
        @Valid @RequestPart("product") dto: ProductUpdateDTO,
        @RequestPart("files", required = false) files: List<MultipartFile>?
    ): ResponseEntity<ProductResponseDTO> =
        ResponseEntity.ok(productService.updateWithFiles(id, dto, files))

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID): ResponseEntity<Unit> {
        productService.delete(id)
        return ResponseEntity.noContent().build()
    }
}