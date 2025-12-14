package uz.pdp.inventorymanagementsystem.base

import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.support.JpaEntityInformation
import org.springframework.data.jpa.repository.support.SimpleJpaRepository
import java.util.*

class BaseRepoImpl<T : BaseModel>(
    entityInformation: JpaEntityInformation<T, UUID>,
    private val entityManager: EntityManager
) : SimpleJpaRepository<T, UUID>(entityInformation, entityManager), BaseRepo<T> {

    // Specification: deleted = false bo'lgan entitylarni tanlash
    private val isNotDeletedSpecification = Specification<T> { root, _, cb ->
        cb.equal(root.get<Boolean>("deleted"), false)
    }

    /**
     * findByIdAndDeletedFalse
     * Maqsadi: Berilgan ID bo'yicha entityni olish, agar deleted = true bo'lsa null qaytaradi
     * DB query: SELECT * FROM table WHERE id = :id AND deleted = false;
     */
    override fun findByIdAndDeletedFalse(id: UUID) =
        findOne(
            Specification.where(isNotDeletedSpecification) // deleted = false
                .and { root, _, cb -> cb.equal(root.get<UUID>("id"), id) } // id = ?
        ).orElse(null)

    /**
     * trash
     * Maqsadi: Entityni soft delete qilish (deleted = true)
     * Ishlash: ID bo'yicha entity topiladi, deleted = true qilinadi va saqlanadi
     */
    @Transactional
    override fun trash(id: UUID): T? = findById(id).orElse(null)?.apply {
        deleted = true
        save(this)
    }

    /**
     * trashList
     * Maqsadi: Bir nechta entitylarni soft delete qilish
     * Ishlash: IDlar bo'yicha entitylar olinadi, deleted = true qilinadi va saveAll orqali saqlanadi
     */
    @Transactional
    override fun trashList(ids: List<UUID>): List<T> {
        val entities = findAllById(ids)
        entities.forEach { it.deleted = true }
        return saveAll(entities)
    }

    /**
     * findAllNotDeleted
     * Maqsadi: Barcha faqat faol (deleted = false) entitylarni olish
     * Ishlash: Specification yordamida DBga query yuboradi
     */
    override fun findAllNotDeleted(): List<T> = findAll(isNotDeletedSpecification)

    /**
     * findAllNotDeletedForPageable
     * Maqsadi: Faol entitylarni paginated (Pageable) olish
     * Ishlash: Specification + Pageable
     */
    override fun findAllNotDeletedForPageable(pageable: org.springframework.data.domain.Pageable) =
        findAll(isNotDeletedSpecification, pageable)

    /**
     * saveAndRefresh
     * Maqsadi: Entityni saqlash va darhol EntityManager yordamida DB holatini refresh qilish
     * Ishlash: save -> entityManager.refresh(this)
     * Sababi: cache va DB sinxronizatsiyasini kafolatlash
     */
    @Transactional
    override fun saveAndRefresh(t: T): T {
        return save(t).apply { entityManager.refresh(this) }
    }
}
