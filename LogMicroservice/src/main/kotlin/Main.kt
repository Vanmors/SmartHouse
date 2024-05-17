import com.example.redis.startRedisListener
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

suspend fun main(args: Array<String>) {

    val job = GlobalScope.launch {
        startRedisListener()
    }
    job.join()


}