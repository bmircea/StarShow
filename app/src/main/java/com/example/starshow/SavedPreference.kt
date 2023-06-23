package com.example.starshow

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

object SavedPreference {
    const val EMAIL = "email"
    const val USERNAME = "username"
    const val PASSWORD = ""

    private fun getSharedPreference(ctx: Context?): SharedPreferences? {
        return PreferenceManager.getDefaultSharedPreferences(ctx)
    }

    private fun editor(ctx: Context, const:String, string:String)
    {
        getSharedPreference(ctx)?.edit()?.putString(const, string)?.apply()
    }

    fun getEmail(ctx: Context) = getSharedPreference(ctx)?.getString(EMAIL, "")

    fun setEmail(ctx: Context, email:String){
        editor(ctx, EMAIL, email)
    }

    fun setUsername(ctx: Context, username:String)
    {
        editor(ctx, USERNAME, username)
    }

    fun getUsername(ctx: Context) = getSharedPreference(ctx)?.getString(USERNAME, "")

    fun setPass(ctx:Context, pass:String)
    {
        editor(ctx, PASSWORD, pass)
    }

    fun getPass(ctx:Context)
    {
        getSharedPreference(ctx)?.getString(PASSWORD, "")
    }
}