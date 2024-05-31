package repository

import model.Device
import model.DeviceEntry
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.transactions.transaction

class DeviceRepository : Repository<DeviceEntry> {

    override fun create(vararg params: Any): DeviceEntry {
        val deviceName = params[0] as String
        return transaction {
            val existingDevice = DeviceEntry.find { Device.name eq deviceName }.firstOrNull()
            if (existingDevice == null) {
                DeviceEntry.new {
                    this.name = deviceName
                }
            } else {
                existingDevice
            }
        }
    }

    override fun readAll(): List<DeviceEntry> {
        return transaction {
            DeviceEntry.all().toList()
        }
    }

    override fun update(id: Int, entry: DeviceEntry) {
        transaction {
            val devicesEntry = DeviceEntry.findById(id)
            devicesEntry?.name = entry.name
        }
    }

    override fun delete(id: Int) {
        transaction {
            DeviceEntry.findById(id)?.delete()
        }
    }

    fun findIdByName(deviceName: String): Int? {
        return transaction {
            DeviceEntry.find { Device.name eq deviceName }
                .firstOrNull()?.id?.value
        }
    }

    fun findByCondition(condition: Op<Boolean>): List<DeviceEntry> {
        return transaction {
            DeviceEntry.find { condition }.toList()
        }
    }

}