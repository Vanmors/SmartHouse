package database

import model.Logs

interface ClickHouseService {
    fun createTable()
    fun insertMessage(service: String, message: String)
    fun getMessage(): MutableList<Logs>
    fun close()
}