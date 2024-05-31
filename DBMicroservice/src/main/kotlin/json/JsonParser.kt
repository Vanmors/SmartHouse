package json

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import insertIntoTable
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import messageBroker.sendJsonToRedis
import model.DeviceEntry
import repository.UserDevicesRepository
import selectFromTable

fun processJson(jsonString: String) {
    val objectMapper = ObjectMapper()
    val jsonNode: JsonNode = objectMapper.readTree(jsonString)

    val action = jsonNode.get("action").asText()
    val table = jsonNode.get("table").asText()
    val operationId = jsonNode.get("req").asLong()

    when (action) {
        "insert" -> {
            val dataNode = jsonNode.get("data")
            val values = mutableMapOf<String, Any>()

            dataNode.fields().forEach { (key, value) ->
                values[key] = value.asText()
            }

            val responseJson = insertIntoTable(operationId, table, values)
            println(responseJson)
            sendJsonToRedis(responseJson)
        }

        "select" -> {
            val condition = jsonNode.get("conditions")

                val responseJson = selectFromTable(operationId, table, condition)


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

@Serializable
data class DataWrapper(val req: Long, val data: List<DeviceData>)

@Serializable
data class DeviceWrapper (val req: Long, val data: DeviceData)

@Serializable
data class DeviceData(val id: Long, val type: String)

fun generateDevicesJson(req: Long, devices: List<UserDevicesRepository.DeviceWithSeq>): String {
    val deviceDataList = devices.map { DeviceData(id = it.seqDevice, type = it.device.name) }
    val dataWrapper = DataWrapper(req = req, data = deviceDataList)
    val jsonString = Json.encodeToString(dataWrapper)
    println(jsonString)

    return jsonString
}

fun generateDeviceJson(req: Long, device: DeviceEntry, deviceId: Long): String {
    val deviceData = DeviceData(id = deviceId, type = device.name)
    val dataWrapper = DeviceWrapper(req = req, data = deviceData)
    val jsonString = Json.encodeToString(dataWrapper)
    println(jsonString)
    return jsonString
}

