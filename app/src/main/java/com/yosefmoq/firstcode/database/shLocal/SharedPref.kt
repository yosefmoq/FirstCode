package com.yosefmoq.firstcode.database.shLocal

import android.content.Context
import android.content.SharedPreferences

class SharedPref {


    companion object{
        private val SHARED_PREFS_FILE_NAME = "firstCode"

        fun getPrefs(context: Context): SharedPreferences {
            return context.getSharedPreferences(SHARED_PREFS_FILE_NAME, Context.MODE_PRIVATE)
        }

        fun savePref(context: Context, key: String?, value: Boolean) {
            getPrefs(context).edit().putBoolean(key, value).apply()
        }

        fun getBoolean(context: Context, key: String?): Boolean {
            return getPrefs(context).getBoolean(key, false)
        }

        fun getBoolean(context: Context, key: String?, defaultValue: Boolean): Boolean {
            return getPrefs(context).getBoolean(key, defaultValue)
        }

        fun save(context: Context, key: String?, value: String?) {
            getPrefs(context).edit().putString(key, value).apply()
        }

        fun save(context: Context, key: String?, value: Boolean) {
            getPrefs(context).edit().putBoolean(key, value).apply()
        }

        fun getString(context: Context, key: String?): String? {
            return getPrefs(context).getString(key, "")
        }

        fun getString(context: Context, key: String?, defaultValue: String?): String? {
            return getPrefs(context).getString(key, defaultValue)
        }


        fun save(context: Context, key: String?, value: Int) {
            getPrefs(context).edit().putInt(key, value).apply()
        }

        fun getInt(context: Context, key: String?): Int {
            return getPrefs(context).getInt(key, 0)
        }

        fun getInt(context: Context, key: String?, defaultValue: Int): Int {
            return getPrefs(context).getInt(key, defaultValue)
        }

        //Floats
        fun save(context: Context, key: String?, value: Float) {
            getPrefs(context).edit().putFloat(key, value).apply()
        }

        fun getFloat(context: Context, key: String?): Float {
            return getPrefs(context).getFloat(key, 0f)
        }

        fun getFloat(context: Context, key: String?, defaultValue: Float): Float {
            return getPrefs(context).getFloat(key, defaultValue)
        }

        //Longs
        fun save(context: Context, key: String?, value: Long) {
            getPrefs(context).edit().putLong(key, value).apply()
        }

        fun getLong(context: Context, key: String?): Long {
            return getPrefs(context).getLong(key, 0)
        }

        fun getLong(context: Context, key: String?, defaultValue: Long): Long {
            return getPrefs(context).getLong(key, defaultValue)
        }

        //StringSets
        fun save(context: Context, key: String?, value: Set<String?>?) {
            getPrefs(context).edit().putStringSet(key, value).apply()
        }

        fun getStringSet(context: Context, key: String?): Set<String?>? {
            return getPrefs(context).getStringSet(key, null)
        }

        fun getStringSet(context: Context, key: String?, defaultValue: Set<String?>?): Set<String?>? {
            return getPrefs(context).getStringSet(key, defaultValue)
        }

        fun removeKey(context: Context, key: String?) {
            getPrefs(context).edit().remove(key).apply()
        }
    }


}