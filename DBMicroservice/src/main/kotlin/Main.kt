import io.lettuce.core.RedisClient
import json.processJson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import messageBroker.startRedisListener
import model.UserEntry
import org.jetbrains.exposed.sql.transactions.transaction
import repository.UserRepository


suspend fun main(args: Array<String>) {

    initDatabase()

    // Запуск Redis слушателя в фоновом режиме
    val job = GlobalScope.launch {
        startRedisListener()
    }
    job.join()


//    while (true) {
//        subscribe()
//    }

    // Инициализация базы данных

//
//    val json1 = """
//        { "action": "insert", "table": "user", "data": { "username": "Ilya",  "password": "rhtgbvds" } }
//        """.trimIndent()
//    val json2 =
//        """{
//        "action": "insert",
//        "table": "userDevices",
//        "data": {
//            "username": "John Doe",
//            "password": "cewd2wqDQWEC",
//            "device": "water_sensor"
//        }
//    }
//    """.trimIndent()
//
////    processJson(json1)
//    processJson(json2)

//    val redisClient = RedisClient.create("redis://localhost:6379")
//    val connection = redisClient.connectPubSub()
//
//    // Подписка на канал
//    connection.sync().subscribe("my_channel")
//
//    // Получение сообщений из канала
//    while (true) {
//        val message = connection.sync().next()
//        val json = message.body.toString()
//
//        // Распарсивание JSON-строки в объект User с помощью kotlinx.serialization
//        val user = Json.decodeFromString<User>(json)
//
//        // Вывод данных пользователя в консоль
//        println("User: ${user.name}, Age: ${user.age}, Email: ${user.email}")
//    }

}

