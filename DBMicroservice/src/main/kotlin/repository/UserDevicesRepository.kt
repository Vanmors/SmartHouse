package repository

import model.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class UserDevicesRepository : Repository<UserDevices> {
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


    fun getDeviceBySeqDevice(seqDevice: Long): DeviceEntry? {
        return transaction {
            val deviceId = UserDevices
                .slice(UserDevices.deviceId)
                .select { UserDevices.seqDevice eq seqDevice }
                .map { it[UserDevices.deviceId] }
                .firstOrNull()

            deviceId?.let {
                DeviceEntry.findById(it)
            }
        }
    }

    data class DeviceWithSeq(val device: DeviceEntry, val seqDevice: Long)

    fun getDevicesForUser(username: String): List<DeviceWithSeq> {
        return transaction {
            val user = UserEntry.find { User.userName eq username }.firstOrNull()
            if (user != null) {
                val query = UserDevices.innerJoin(Device)
                    .slice(UserDevices.seqDevice, Device.id, Device.name)
                    .select { UserDevices.userId eq user.id }

                query.map {
                    val deviceId = it[Device.id]
                    val seqDevice = it[UserDevices.seqDevice]
                    val device = DeviceEntry.findById(deviceId) ?: throw IllegalStateException("Device not found")
                    DeviceWithSeq(device, seqDevice)
                }
            } else {
                emptyList()
            }
        }
    }

    fun getSeqDeviceByUserIdAndDeviceId(userIdValue: Int, deviceIdValue: Int): Long? {
        return transaction {
            UserDevices.select {
                (UserDevices.userId eq userIdValue) and (UserDevices.deviceId eq deviceIdValue)
            }.map { it[UserDevices.seqDevice] }
                .firstOrNull()
        }
    }

}