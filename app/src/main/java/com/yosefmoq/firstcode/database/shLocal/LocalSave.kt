package com.yosefmoq.firstcode.database.shLocal

import android.content.Context

class LocalSave(val mContext: Context) {
    private val LAST_ID = "lastId"
    private val IS_FIRST = "isFirstTime"
    private val LAST_ID_IN_FIREBASE ="lastIdInFirebase"
    private val NEXT_ID = "nextId"

    companion object{
        private var instance: LocalSave? = null

        fun getInstance(context: Context): LocalSave? {
            if (instance == null) {
                instance = LocalSave(context)
            }
            return instance
        }
    }


    private fun getmContext(): Context? {
        return mContext
    }


    fun setFirstTime(isFirstTime: Boolean) {
        SharedPref.save(getmContext()!!, IS_FIRST, isFirstTime)
    }
    fun setNextId(id: Int){
        SharedPref.save(getmContext()!!,NEXT_ID,id)
    }
    fun getNextId():Int{
        return SharedPref.getInt(getmContext()!!,NEXT_ID,1)
    }
    fun isFirstTime(): Boolean {
        return SharedPref.getBoolean(getmContext()!!, IS_FIRST, true)
    }
    fun setLastId(id:Int){
        SharedPref.save(mContext,LAST_ID,id)
    }
    fun getLastId():Int{
        return SharedPref.getInt(mContext,LAST_ID,0)
    }
    fun setLastIdInFirebase(id:Int){
        SharedPref.save(mContext,LAST_ID_IN_FIREBASE,id)
    }
    fun getLastIdInFirebase():Int{
        return SharedPref.getInt(mContext,LAST_ID_IN_FIREBASE,0)
    }

    fun clear() {
        SharedPref.removeKey(getmContext()!!, LAST_ID)
        SharedPref.removeKey(getmContext()!!, IS_FIRST)
    }


}