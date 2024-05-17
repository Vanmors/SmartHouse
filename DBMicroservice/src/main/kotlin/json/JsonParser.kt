package json

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import insertIntoTable
import messageBroker.sendJsonToRedis

fun processJson(jsonString: String) {
    val objectMapper = ObjectMapper()
    val jsonNode: JsonNode = objectMapper.readTree(jsonString)

    val action = jsonNode.get("action").asText()
    val table = jsonNode.get("table").asText()

    when (action) {
        "insert" -> {
            val dataNode = jsonNode.get("data")
            val values = mutableMapOf<String, Any>()

            dataNode.fields().forEach { (key, value) ->
                values[key] = value.asText()
            }

            val responseJson = insertIntoTable(table, values)
            println(responseJson)
            sendJsonToRedis(responseJson)
        }
        "select" -> {
            var responseJson: String
            if (table.equals("device")){
                responseJson = "{\"id\": 1, \"type\": \"teapot\"}"
            } else {
                responseJson = "[{\"id\": 1, \"type\": \"teapot\"}, {\"id\": 2, \"type\": \"water_sensor\"}]"
            }
            println(responseJson)
            sendJsonToRedis(responseJson)
//            val conditionsNode = jsonNode.get("conditions")
//            val conditions = mutableMapOf<String, Any>()
//
//            conditionsNode.fields().forEach { (key, value) ->
//                conditions[key] = value.get("gte").asInt()
//            }
//
//            selectFromTable(table, conditions)
        }
        else -> {
            println("Unsupported action: $action")
        }
    }
}