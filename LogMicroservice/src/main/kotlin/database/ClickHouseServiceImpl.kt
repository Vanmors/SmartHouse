package database

import model.Logs
import java.sql.*

class ClickHouseServiceImpl(private val url: String) : ClickHouseService {
    private val connection: Connection = DriverManager.getConnection(url)

    override fun createTable() {
        val insertStatement: PreparedStatement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS logs (\n" +
                "    service String,\n" +
                "    log_message String,\n" +
                "    log_time DateTime DEFAULT now()\n" +
                ") ENGINE = MergeTree()\n" +
                "ORDER BY log_time;\n")
        insertStatement.executeUpdate()
    }

    override fun insertMessage(service: String, message: String) {
        val insertStatement: PreparedStatement = connection.prepareStatement("INSERT INTO logs (log_message, service) VALUES (?, ?)")
        insertStatement.setString(1, message)
        insertStatement.setString(2, service)
        insertStatement.executeUpdate()
        insertStatement.close()
    }

    override fun getMessage(): MutableList<Logs> {
        val statement = connection.createStatement()
        val query = "SELECT log_message, log_time FROM logs"
        val resultSet: ResultSet = statement.executeQuery(query)

        val logEntries = mutableListOf<Logs>()

        while (resultSet.next()) {
            val logMessage = resultSet.getString("log_message")
            val logTime = resultSet.getString("log_time")
            val log = Logs(logMessage, logTime)
            logEntries.add(log)
//            println("log_message: $logMessage, log_time: $logTime")
        }
        return logEntries
    }

    override fun close() {
        try {
            connection.close()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }
}