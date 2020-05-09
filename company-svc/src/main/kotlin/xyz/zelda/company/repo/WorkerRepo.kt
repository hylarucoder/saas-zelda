package xyz.zelda.company.repo

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import xyz.zelda.company.model.Worker

@Repository
interface WorkerRepo : JpaRepository<Worker?, String?> {
    fun findByTeamId(teamId: String?): MutableList<Worker?>?
    fun findByUserId(userId: String?): MutableList<Worker?>?
    fun findByTeamIdAndUserId(teamId: String?, userId: String?): Worker?

    @Modifying(clearAutomatically = true)
    @Query("delete from Worker worker where worker.teamId = :teamId and worker.userId = :userId")
    @Transactional
    fun deleteWorker(@Param("teamId") teamId: String?, @Param("userId") userId: String?): Int
}