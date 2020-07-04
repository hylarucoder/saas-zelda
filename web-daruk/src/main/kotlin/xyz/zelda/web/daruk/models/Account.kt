package xyz.zelda.web.daruk.models

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object AdminStaffs : IntIdTable() {
    val name = varchar("name", 50).index()
    val city = reference("city", Cities)
    val age = integer("age")
}

object Cities: IntIdTable() {
    val name = varchar("name", 50)
}

class AdminStaff(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<AdminStaff>(AdminStaffs)

    var name by AdminStaffs.name
    var city by City referencedOn AdminStaffs.city
    var age by AdminStaffs.age
}

class City(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<City>(Cities)

    var name by Cities.name
    val users by AdminStaff referrersOn AdminStaffs.city
}