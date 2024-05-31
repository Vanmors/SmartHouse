import json.processJson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import messageBroker.startRedisListener

suspend fun main(args: Array<String>) {
    val json1 = """
        { "req": 1, "action": "insert", "table": "user", "data": { "username": "Ilya",  "password": "rhtgbvds" } }
        """.trimIndent()
//    val json5 = """
//        {
//          "req": 123,
//          "action": "select",
//          "table": "user",
//          "conditions": {
//              "username": "Ilya",
//              "password": "rhtgbvds"
//          }
//        }
//    """.trimIndent()
    val json2 =
        """{
        "req": 2,
        "action": "insert",
        "table": "userDevices",
        "data": {
            "username": "Ilya",
            "password": "rhtgbvds",
            "device": "light_sensor"
        }
    }
    """.trimIndent()
    val json3 = """{
        "req": 3,
        "action": "select",
        "table": "userDevices",
        "conditions": {
            "userName": "Ilya"
        }
    }""".trimIndent()
//    val json4 = """
//        {
//"req": 5,
//"action": "select",
//"table": "userDevices",
//"conditions": {
//  "userName": "Ilya",
//  "password": "123123",
//  "id": 1
//}
//}
//    """.trimIndent()
    initDatabase()
//    processJson(json1)
//    processJson(json2)
//    selectFromTable(3, "userDevices", "John Doe")
//    processJson(json2)
    // Запуск Redis слушателя в фоновом режиме
    val job = GlobalScope.launch {
        startRedisListener()
    }
    job.join()


    // {
//      "id": 1,
//      "data": {
//            ""
//       }
//        }

//    while (true) {
//        subscribe()
//    }

    // Инициализация базы данных

//

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

