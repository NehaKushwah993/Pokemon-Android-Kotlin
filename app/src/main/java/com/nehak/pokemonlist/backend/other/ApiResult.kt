package com.nehak.pokemonlist.backend.other

/**
 * Created by Neha Kushwah on 8/9/21.
 */
data class ApiResult<out T>(val status: Status, val data: T?, val message: String?) {
    companion object {
        fun <T> success(data: T?): ApiResult<T> {
            return ApiResult(Status.SUCCESS, data, null)
        }

        fun <T> error(msg: String): ApiResult<T> {
            return ApiResult(Status.ERROR, null, msg)
        }
    }

    enum class Status {
        SUCCESS,
        ERROR
    }
}
