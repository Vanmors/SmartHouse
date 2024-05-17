package model

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

data class DeviceEntryData(val deviceName: String)

object Device : IntIdTable() {
    val name = varchar("name", 100).uniqueIndex()
}

class DeviceEntry(id: EntityID<Int>) : Entity<Int>(id) {
    companion object : EntityClass<Int, DeviceEntry>(Device)
    var name by Device.name
}
