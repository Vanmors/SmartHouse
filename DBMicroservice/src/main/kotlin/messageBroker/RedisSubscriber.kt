package messageBroker

import io.lettuce.core.RedisClient
import io.lettuce.core.pubsub.RedisPubSubAdapter
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection
import json.processJson

const val alarmChannel = "DBChannel"
fun startRedisListener() {
    val redisClient: RedisClient = RedisClient.create("redis://user:@localhost:6379/1")
    val pubSubConnection: StatefulRedisPubSubConnection<String, String> = redisClient.connectPubSub()
    val pubSubCommands = pubSubConnection.sync()

    pubSubConnection.addListener(object : RedisPubSubAdapter<String, String>() {
        override fun message(channel: String, message: String) {
            if (channel == alarmChannel) {
                println(message)
                processJson(message)
            }
        }
    })
    pubSubCommands.subscribe(alarmChannel)

    while (true) {
    }
}