package repository

import model.User
import model.UserEntry
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

class UserRepository: Repository<UserEntry> {

    override fun create(vararg params: Any): UserEntry {
        val userName = params[0] as String
        val password = params[1] as String

        return transaction {
            UserEntry.new {
                this.userName = userName
                this.password = password
            }
        }
    }

    override fun readAll(): List<UserEntry> {
        return transaction {
            UserEntry.all().toList()
        }
    }

    override fun update(id: Int, entry: UserEntry) {
        transaction {
            val userEntry = UserEntry.findById(id)
            userEntry?.userName = entry.userName
        }
    }

    override fun delete(id: Int) {
        transaction {
            UserEntry.findById(id)?.delete()
        }
    }
    fun findUser(userName: String, password: String): Int? {
        return transaction {
            UserEntry.find { (User.userName eq userName) and (User.password eq password) }
                .firstOrNull()?.id?.value
        }
    }

    fun getUserAsJsonByName(userName: String, password: String): ResultRow? {
        return transaction {
            User.select() {
                (User.userName eq userName) and
                        (User.password eq password)
            }.firstOrNull()
        }
    }

}