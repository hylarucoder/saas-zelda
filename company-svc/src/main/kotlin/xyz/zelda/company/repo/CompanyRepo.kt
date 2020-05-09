package xyz.zelda.company.repo

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import xyz.zelda.company.model.Company

@Repository
interface CompanyRepo : JpaRepository<Company?, String?> {
    fun findCompanyById(id: String?): Company?
}