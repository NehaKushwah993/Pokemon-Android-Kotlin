package com.nehak.pokemonlist.backend.other

import com.android.volley.*
import com.android.volley.toolbox.HttpHeaderParser
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import java.io.UnsupportedEncodingException
import kotlin.Throws

class GsonRequest<T>(
    method: Int,
    url: String?, private val clazz: Class<T>, private val headers: Map<String, String>?,
    listener: Response.Listener<T>, errorListener: Response.ErrorListener?
) : Request<T>(method, url, errorListener) {
    private val gson: Gson = Gson()
    private val listener: Response.Listener<T> = listener

    @Throws(AuthFailureError::class)
    override fun getHeaders(): Map<String, String> {
        return headers ?: super.getHeaders()
    }

    override fun deliverResponse(response: T) {
        listener.onResponse(response)
    }

    override fun parseNetworkResponse(response: NetworkResponse): Response<T> {
        return try {
            val json = String(
                response.data,
                charset("UTF-8")
            )
            Response.success(
                gson.fromJson(json, clazz),
                HttpHeaderParser.parseCacheHeaders(response)
            )
        } catch (e: UnsupportedEncodingException) {
            Response.error(ParseError(e))
        } catch (e: JsonSyntaxException) {
            Response.error(ParseError(e))
        }
    }

}