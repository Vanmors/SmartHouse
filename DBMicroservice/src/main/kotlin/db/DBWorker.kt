import com.fasterxml.jackson.databind.ObjectMapper
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

fun insertIntoTable(table: String, values: Map<String, Any>): String {
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
//                val userMap = mapOf(
//                    "id" to it[User.id].value,
//                    "username" to it[User.userName],
//                    "password" to it[User.password]
//                )
                return "{\"id\": $id, \"username\": \"$userName\", \"password\": \"$password\"}"
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
        }
    }
    return ""
}