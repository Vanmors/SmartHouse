package messageBroker

import io.lettuce.core.RedisClient
import io.lettuce.core.pubsub.RedisPubSubAdapter
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection
import json.processJson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

const val requestDBChannel = "requestDBChannel"
fun startRedisListener() {
    val redisClient: RedisClient = RedisClient.create("redis://user:@localhost:6379/1")
    val pubSubConnection: StatefulRedisPubSubConnection<String, String> = redisClient.connectPubSub()
    val pubSubCommands = pubSubConnection.sync()

    pubSubConnection.addListener(object : RedisPubSubAdapter<String, String>() {
        override fun message(channel: String, message: String) {
            if (channel == requestDBChannel) {
                println(message)
                GlobalScope.launch {
                    processJson(message)
                }
            }
        }
    })
    pubSubCommands.subscribe(requestDBChannel)

    while (true) {
    }
}