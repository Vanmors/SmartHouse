package repository

import model.*
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction

class UserDevicesRepository: Repository<UserDevices> {
    override fun create(vararg params: Any): UserDevices {
        val userId = params[0] as Int
        val deviceId = params[1] as Int
        transaction {
            UserDevices.insert {
                it[UserDevices.userId] = userId
                it[UserDevices.deviceId] = deviceId
            }
        }

        return UserDevices
    }

    override fun readAll(): List<UserDevices> {
        TODO("Not yet implemented")
    }

    override fun delete(id: Int) {
        TODO("Not yet implemented")
    }

    override fun update(id: Int, entry: UserDevices) {
        TODO("Not yet implemented")
    }


}