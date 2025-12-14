package uz.pdp.inventorymanagementsystem.base

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.repository.NoRepositoryBean
import java.util.*

/**
 * BaseRepository
 *
 * Generic repository barcha entitylar uchun.
 * Soft delete va pagination bilan ishlash imkonini beradi.
 */
@NoRepositoryBean
interface BaseRepo<T : BaseModel> : JpaRepository<T, UUID>, JpaSpecificationExecutor<T> {

    /**
     * findByIdAndDeletedFalse
     * Maqsadi: Berilgan ID bo'yicha faqat deleted = false bo'lgan entityni olish
     * Agar entity soft delete qilingan bo'lsa, null qaytaradi
     */
    fun findByIdAndDeletedFalse(id: UUID): T?

    /**
     * trash
     * Maqsadi: Berilgan ID bo'yicha entityni soft delete qilish
     * deleted = true qilib saqlaydi
     */
    fun trash(id: UUID): T?

    /**
     * trashList
     * Maqsadi: Bir nechta entitylarni soft delete qilish
     * IDs ro'yxatidagi barcha entitylar deleted = true qilinadi
     */
    fun trashList(ids: List<UUID>): List<T>

    /**
     * findAllNotDeleted
     * Maqsadi: Barcha faqat faol (deleted = false) entitylarni olish
     * DB queryda deleted = false bo'ladi
     */
    fun findAllNotDeleted(): List<T>

    /**
     * findAllNotDeletedForPageable
     * Maqsadi: Faol entitylarni paginated (Pageable) olish
     * DB queryda deleted = false va limit/offset qo'llanadi
     */
    fun findAllNotDeletedForPageable(pageable: Pageable): Page<T>

    /**
     * saveAndRefresh
     * Maqsadi: Entityni saqlash va darhol EntityManager orqali DB holatini refresh qilish
     * Sababi: cache va DB sinxronizatsiyasini kafolatlash
     */
    fun saveAndRefresh(t: T): T
}
