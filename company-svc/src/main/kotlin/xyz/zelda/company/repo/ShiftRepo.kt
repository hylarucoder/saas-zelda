package xyz.zelda.company.repo

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import xyz.zelda.company.model.Shift
import java.time.Instant

@Repository
interface ShiftRepo : JpaRepository<Shift?, String?> {
    fun findShiftById(shiftId: String?): Shift?

    @Modifying(clearAutomatically = true)
    @Query("delete from Shift shift where shift.id = :shiftId")
    @Transactional
    fun deleteShiftById(@Param("shiftId") shiftId: String?): Int

    @get:Query(value = "select cast(a.weekname as char) as week, greatest(a.count, coalesce(b.count,0)) as count from (select 0 as count, str_to_date(concat(year(start), week(start), ' Monday'), '%X%V %W') as weekname from shift where start < NOW() group by weekname) as a left join (select count(distinct(user_id)) as count, str_to_date(concat(year(start), week(start), ' Monday'), '%X%V %W') as weekname from shift where start < NOW() and user_id != '' and published is true group by weekname) as b on a.weekname = b.weekname", nativeQuery = true)
    val scheduledPerWeekList: MutableList<IScheduledPerWeek?>?

    interface IScheduledPerWeek {
        val week: String?
        val count: Int
    }

    @get:Query(value = "select count(distinct(user_id)) from shift where shift.start <= NOW() and shift.stop > NOW() and user_id <> '' and shift.published = true", nativeQuery = true)
    val peopleOnShifts: Int

    // order by start asc
    @Query(value = "select shift from Shift shift where shift.teamId = :teamId and shift.userId = :userId and shift.start >= :startTime and shift.start < :endTime order by shift.start asc")
    fun listWorkerShifts(@Param("teamId") teamId: String?, @Param("userId") userId: String?, @Param("startTime") start: Instant?, @Param("endTime") end: Instant?): MutableList<Shift?>?

    // no order
    @Query(value = "select shift from Shift shift where shift.teamId = :teamId and shift.userId = :userId and shift.start >= :startTime and shift.start < :endTime")
    fun listShiftByUserId(@Param("teamId") teamId: String?, @Param("userId") userId: String?, @Param("startTime") start: Instant?, @Param("endTime") end: Instant?): MutableList<Shift?>?

    @Query(value = "select shift from Shift shift where shift.teamId = :teamId and shift.jobId = :jobId and shift.start >= :startTime and shift.start < :endTime")
    fun listShiftByJobId(@Param("teamId") teamId: String?, @Param("jobId") jobId: String?, @Param("startTime") start: Instant?, @Param("endTime") end: Instant?): MutableList<Shift?>?

    @Query(value = "select shift from Shift shift where shift.teamId = :teamId and shift.userId = :userId and shift.jobId = :jobId and shift.start >= :startTime and shift.start < :endTime")
    fun listShiftByUserIdAndJobId(@Param("teamId") teamId: String?, @Param("userId") userId: String?, @Param("jobId") jobId: String?, @Param("startTime") start: Instant?, @Param("endTime") end: Instant?): MutableList<Shift?>?

    @Query(value = "select shift from Shift shift where shift.teamId = :teamId and shift.start >= :startTime and shift.start < :endTime")
    fun listShiftByTeamIdOnly(@Param("teamId") teamId: String?, @Param("startTime") start: Instant?, @Param("endTime") end: Instant?): MutableList<Shift?>?
}