package xyz.zelda.account.repo

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import xyz.zelda.account.model.AccountSecret

@Repository
interface AccountSecretRepo : JpaRepository<AccountSecret?, String?> {
    fun findAccountSecretByEmail(email: String?): AccountSecret?

    @Modifying(clearAutomatically = true)
    @Query("update AccountSecret accountSecret set accountSecret.passwordHash = :passwordHash where accountSecret.id = :id")
    @Transactional
    fun updatePasswordHashById(@Param("passwordHash") passwordHash: String?, @Param("id") id: String?): Int
}