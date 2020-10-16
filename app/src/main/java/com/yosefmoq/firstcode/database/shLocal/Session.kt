package com.yosefmoq.firstcode.database.shLocal

import android.content.Context

class Session(val mContext: Context) {



    companion object{
        private var instance: Session? = null
        private var localSave: LocalSave? = null

        fun getInstance(context: Context?): Session? {
            if (instance == null) {
                instance = Session(context!!)
                localSave = LocalSave.getInstance(context)
            }
            return instance
        }

    }

    fun getLocalSave(): LocalSave? {
        return localSave
    }


    private fun getmContext(): Context? {
        return mContext
    }



    fun clear() {
        localSave!!.clear()
    }

}