package uz.pdp.inventorymanagementsystem.controller

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import uz.pdp.inventorymanagementsystem.dto.ProductCreateDTO
import uz.pdp.inventorymanagementsystem.dto.ProductResponseDTO
import uz.pdp.inventorymanagementsystem.dto.ProductUpdateDTO
import uz.pdp.inventorymanagementsystem.service.FileStorageService
import uz.pdp.inventorymanagementsystem.service.ProductService
import java.util.UUID

@RestController
class ProductController(
    private val productService: ProductService,
) {


    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun create(
        @RequestPart dto: ProductCreateDTO,
        @RequestPart(required = false) files: List<MultipartFile>
    ): ProductResponseDTO =
        productService.createWithFiles(dto, files)

    @PutMapping("/{id}", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun update(
        @PathVariable id: UUID,
        @RequestPart dto: ProductUpdateDTO,
        @RequestPart(required = false) files: List<MultipartFile>
    ): ProductResponseDTO =
        productService.updateWithFiles(id, dto, files)


}