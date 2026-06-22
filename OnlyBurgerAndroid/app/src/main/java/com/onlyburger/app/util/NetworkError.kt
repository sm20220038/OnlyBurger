package com.onlyburger.app.util

import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException

/**
 * Turns an exception from a network call into a short, user-friendly message.
 * The backend returns ProblemDetails JSON ({ "detail": "...", "title": "..." }) on
 * errors, so we try to surface that text; otherwise we fall back to a generic message.
 */
fun Throwable.toUserMessage(): String = when (this) {
    is HttpException -> parseHttpError(this)
    is IOException ->
        "Cannot reach the server. Check your connection and that the backend is running."
    else -> message ?: "Something went wrong. Please try again."
}

private fun parseHttpError(exception: HttpException): String {
    val body = runCatching { exception.response()?.errorBody()?.string() }.getOrNull()
    if (!body.isNullOrBlank()) {
        runCatching {
            val json = JSONObject(body)
            val detail = json.optString("detail").takeIf { it.isNotBlank() }
            val title = json.optString("title").takeIf { it.isNotBlank() }
            detail ?: title
        }.getOrNull()?.let { return it }
    }
    return when (exception.code()) {
        401 -> "Invalid credentials or your session expired. Please log in again."
        403 -> "You are not allowed to do that."
        404 -> "Not found."
        else -> "Request failed (${exception.code()})."
    }
}
