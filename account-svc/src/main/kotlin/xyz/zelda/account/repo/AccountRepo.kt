package xyz.zelda.account.repo

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import xyz.zelda.account.model.Account

@Repository
interface AccountRepo : JpaRepository<Account?, String?> {
    fun findAccountById(id: String?): Account?
    fun findAccountByEmail(email: String?): Account?
    fun findAccountByPhoneNumber(phoneNumber: String?): Account?

    @Modifying(clearAutomatically = true)
    @Query("update Account account set account.email = :email, account.confirmedAndActive = true where account.id = :id")
    @Transactional
    fun updateEmailAndActivateById(@Param("email") email: String?, @Param("id") id: String?): Int
}