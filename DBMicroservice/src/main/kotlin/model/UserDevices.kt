package model

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object UserDevices : Table() {
    val userId = reference("user_id", User, onDelete = ReferenceOption.CASCADE)
    val deviceId = reference("device_id", Device, onDelete = ReferenceOption.CASCADE)
    val seqDevice = long("seqDevice").autoIncrement()
    override val primaryKey = PrimaryKey(userId, deviceId, name = "PK_UserDevice")
}

 //Сущность UserDevicesEntry
//class UserDevicesEntry(id: EntityID<Int>) : IntEntity(id) {
//    companion object : IntEntityClass<UserDevicesEntry>(UserDevices)
//
//    var userId by UserDevices.userId
//    var deviceId by UserDevices.deviceId
//}