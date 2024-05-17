package model

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

data class UserEntryData(val userName: String, val password: String)

object User : IntIdTable() {
    val userName = varchar("username", 255)
    val password = varchar("password", 255)
}

// Определение сущности
class UserEntry(id: EntityID<Int>) : Entity<Int>(id) {
    companion object : EntityClass<Int, UserEntry>(User)
    var userName by User.userName
    var password by User.password
}