package xyz.zelda.company.repo

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import xyz.zelda.company.model.Directory

@Repository
interface DirectoryRepo : JpaRepository<Directory?, String?> {
    fun findByCompanyIdAndUserId(companyId: String?, userId: String?): Directory?
    fun findByCompanyId(companyId: String?, pageable: Pageable?): Page<Directory?>?

    @Modifying(clearAutomatically = true)
    @Query("update Directory directory set directory.internalId = :internalId where directory.companyId = :companyId and directory.userId = :userId")
    @Transactional
    fun updateInternalIdByCompanyIdAndUserId(@Param("internalId") internalId: String?, @Param("companyId") companyId: String?, @Param("userId") userId: String?): Int
}