package xyz.zelda.company.repo

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import xyz.zelda.company.model.Team

@Repository
interface TeamRepo : JpaRepository<Team?, String?> {
    fun findByCompanyId(companyId: String?): MutableList<Team?>?
}