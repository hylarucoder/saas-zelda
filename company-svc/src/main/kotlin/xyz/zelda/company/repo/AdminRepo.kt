package xyz.zelda.company.repo

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import xyz.zelda.company.model.Admin

@Repository
interface AdminRepo : JpaRepository<Admin?, String?> {
    fun findByCompanyId(companyId: String?): MutableList<Admin?>?
    fun findByUserId(userId: String?): MutableList<Admin?>?
    fun findByCompanyIdAndUserId(companyId: String?, userId: String?): Admin?

    @Modifying(clearAutomatically = true)
    @Query("delete from Admin admin where admin.companyId = :companyId and admin.userId = :userId")
    @Transactional
    fun deleteAdmin(@Param("companyId") companyId: String?, @Param("userId") userId: String?): Int
}