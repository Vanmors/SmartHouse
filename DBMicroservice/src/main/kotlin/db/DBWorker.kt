import com.fasterxml.jackson.databind.JsonNode
import json.generateDeviceJson
import json.generateDevicesJson
import model.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import repository.DeviceRepository
import repository.UserDevicesRepository
import repository.UserRepository

fun initDatabase() {
    Database.connect(
        url = "jdbc:postgresql://localhost:5432/mobile",
        driver = "org.postgresql.Driver",
        user = "postgres",
        password = "nav461"
    )

    transaction {
        SchemaUtils.create(User)
        SchemaUtils.create(Device)
        initDevices()
        SchemaUtils.create(UserDevices)
    }
}

fun initDevices() {
    val deviceRepository = DeviceRepository()
    deviceRepository.create("water_sensor")
    deviceRepository.create("light_sensor")
    deviceRepository.create("security_sensor")
    deviceRepository.create("smoke_sensor")

    deviceRepository.create("teapot")
    deviceRepository.create("humidifier")
    deviceRepository.create("air_conditioner")
    deviceRepository.create("vacuum_cleaner")
    deviceRepository.create("coffee_machine")
    deviceRepository.create("room_heating")
    deviceRepository.create("room_lighting")
}

fun insertIntoTable(operationId: Long, table: String, values: Map<String, Any>): String {
    var jsonOutput: String? = null
    when (table) {
        "user" -> {
            val userEntryData = UserEntryData(values["username"] as String, values["password"] as String)
            val repository = UserRepository()
            repository.create(userEntryData.userName, userEntryData.password)
            val result = repository.getUserAsJsonByName(userEntryData.userName, userEntryData.password)


            result?.let {
                // Преобразование результата в JSON
                val id = it[User.id].value
                val userName = it[User.userName]
                val password = it[User.password]
                jsonOutput = "{\"req\": $operationId, \"username\": \"$userName\", \"password\": \"$password\"}"
            }

        }

        "userDevices" -> {
            val deviceRepository = DeviceRepository()
            val userRepository = UserRepository()
            val userDevicesRepository = UserDevicesRepository()

            val deviceEntryData = DeviceEntryData(values["device"] as String)
            val userEntryData = UserEntryData(values["username"] as String, values["password"] as String)

            val deviceId = deviceRepository.findIdByName(deviceEntryData.deviceName)
            val userId = userRepository.findUser(userEntryData.userName, userEntryData.password)
            println(userId)
            println(deviceId)
            if (userId != null && deviceId != null) {
                userDevicesRepository.create(userId, deviceId)
            }
            if (userId == null || deviceId == null) return jsonOutput.toString()
            jsonOutput = "{\"req\": $operationId, \"data\" : { \"id\": \"${userDevicesRepository.getSeqDeviceByUserIdAndDeviceId(userId, deviceId)}\", \"type\": \"${deviceEntryData.deviceName}\" }}"
        }
    }
    println(jsonOutput)
    return jsonOutput.toString()
}

fun selectFromTable(operationId: Long, table: String, condition: JsonNode): String {
    val userDevicesRepository = UserDevicesRepository()
    var jsonOutput: String? = null
    when (table) {
        "userDevices" -> {
            val deviceId = condition.get("id")
            val userName = condition.get("userName").asText()
            if (deviceId == null) {
                val devicesWithSeq = userDevicesRepository.getDevicesForUser(userName)
                devicesWithSeq.forEach { device ->
                    println("ID: ${device.device.id.value}, Name: ${device.seqDevice}")
                }
                jsonOutput = generateDevicesJson(operationId, devicesWithSeq)
            } else {
                val device = userDevicesRepository.getDeviceBySeqDevice(deviceId.asLong())
                jsonOutput = device?.let { generateDeviceJson(operationId, it, deviceId.asLong()) }
            }
        }

        "user" -> {
            val userRepository = UserRepository()
            val userName = condition.get("username").asText()
            val password = condition.get("password").asText()
            val user = userRepository.findUserByNameAndPassword(userName, password)
            jsonOutput = "{\"req\": $operationId, \"username\": \"${user?.userName}\", \"password\": \"${user?.password}\"}"
        }
    }

    return jsonOutput.toString()

}


