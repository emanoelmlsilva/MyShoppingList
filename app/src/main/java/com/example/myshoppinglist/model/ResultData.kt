import java.net.ConnectException

sealed class ResultData<out R> {
    data class Success<out T>(val data: T) : ResultData<T>()
    data class Error(val exception: Exception) : ResultData<Nothing>()
    data class NotConnectionService<out T>(val data: T) : ResultData<T>()
}
