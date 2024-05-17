package com.example.redis

import database.ClickHouseServiceImpl
import io.lettuce.core.RedisClient
import io.lettuce.core.pubsub.RedisPubSubAdapter
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection
import io.lettuce.core.pubsub.api.async.RedisPubSubAsyncCommands

const val alarmChannel = "LogChannel"
fun startRedisListener() {
    val redisClient: RedisClient = RedisClient.create("redis://user:@localhost:6379/1")
    val pubSubConnection: StatefulRedisPubSubConnection<String, String> = redisClient.connectPubSub()
    val pubSubCommands = pubSubConnection.sync()

    pubSubConnection.addListener(object : RedisPubSubAdapter<String, String>() {
        override fun message(channel: String, message: String) {
            if (channel == alarmChannel) {
                val clickHouseUrl = "jdbc:clickhouse://localhost:8123/default"
                val clickHouseService = ClickHouseServiceImpl(clickHouseUrl)
                clickHouseService.createTable()
                if (message != null) {
                    val log = message.split("/")
                    clickHouseService.insertMessage(log[0], log[1])
                }
                val logEntries = clickHouseService.getMessage()
                logEntries.forEach { logEntry ->
                    println("Log Message: ${logEntry.logMessage}, Log Time: ${logEntry.logTime}")
                }
                println("Got $message on channel $channel")
            }
        }
    })
    pubSubCommands.subscribe(alarmChannel)

    while (true) {
    }
}