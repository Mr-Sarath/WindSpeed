package com.app.utils

import org.json.JSONObject
import retrofit2.Response
import java.lang.Exception

object ResponseCodeManager {
    fun checkResponse(response: Response<*>): String {
        when (response.code()) {
            401 -> {
                return Constants.STS_401
            }
            403 -> {
                return Constants.STS_403
            }
            404 -> {
                return Constants.STS_404
            }
            408 -> {
                return Constants.STS_408
            }
            422 -> {
                return try {
                    val errorString = response.errorBody()?.byteStream()?.bufferedReader()
                        .use { it?.readText() } // defaults to UTF-8
                    val json = JSONObject(errorString)
                    json["message"].toString()
                } catch (e: Exception) {
                    "Invalid Credentials"
                }
            }
            500 -> {
                return Constants.STS_500
            }
            502 -> {
                return Constants.STS_502
            }
            503 -> {
                return Constants.STS_503
            }
            else -> {
                return Constants.STS_DEFAULT
            }
        }

    }

}