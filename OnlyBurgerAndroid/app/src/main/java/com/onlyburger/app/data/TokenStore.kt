package com.onlyburger.app.data

import android.content.Context
import com.onlyburger.app.data.remote.dto.AuthResponse

/**
 * Persists the logged-in user's session (JWT plus a little profile info) in
 * SharedPreferences so the user stays signed in across app launches.
 */
class TokenStore(context: Context) {

    private val prefs = context.getSharedPreferences("onlyburger_session", Context.MODE_PRIVATE)

    val token: String?
        get() = prefs.getString(KEY_TOKEN, null)

    val username: String?
        get() = prefs.getString(KEY_USERNAME, null)

    val role: String?
        get() = prefs.getString(KEY_ROLE, null)

    val isLoggedIn: Boolean
        get() = !token.isNullOrBlank()

    val isAdmin: Boolean
        get() = role == "Admin"

    fun save(auth: AuthResponse) {
        prefs.edit()
            .putString(KEY_TOKEN, auth.token)
            .putInt(KEY_USER_ID, auth.userId)
            .putString(KEY_USERNAME, auth.username)
            .putString(KEY_ROLE, auth.role)
            .apply()
    }

    fun clear() {
        prefs.edit().clear().apply()
    }

    private companion object {
        const val KEY_TOKEN = "token"
        const val KEY_USER_ID = "userId"
        const val KEY_USERNAME = "username"
        const val KEY_ROLE = "role"
    }
}
