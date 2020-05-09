package xyz.zelda.company.repo

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import xyz.zelda.company.model.Job

@Repository
interface JobRepo : JpaRepository<Job?, String?> {
    fun findJobByTeamId(teamId: String?): MutableList<Job?>?
    fun findJobById(id: String?): Job?
}