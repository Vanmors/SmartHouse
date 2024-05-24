package messageBroker

import com.fasterxml.jackson.databind.ObjectMapper
import io.lettuce.core.RedisClient
import io.lettuce.core.RedisFuture
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection
import io.lettuce.core.pubsub.api.async.RedisPubSubAsyncCommands


fun sendJsonToRedis(response: String) {
    val redisClient: RedisClient = RedisClient.create("redis://user:@localhost:6379/1")
    val pubSubConnection: StatefulRedisPubSubConnection<String, String> = redisClient.connectPubSub()

    val async: RedisPubSubAsyncCommands<String, String> = pubSubConnection.async()
    val redisFuture: RedisFuture<Long> = async.publish("responseDBChannel", response.toString())
}
