package com.yosefmoq.firstcode.interfaceI

import com.yosefmoq.firstcode.database.PostData

interface OnPostClickListener {
    fun onPostClick(post:PostData)
    fun onEditClick(position:Int ,post: PostData)
    fun onRemoveClick(position:Int,post: PostData)
}