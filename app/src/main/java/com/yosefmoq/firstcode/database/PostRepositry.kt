package com.yosefmoq.firstcode.database

class PostRepositry(private val postDao: PostDao) {

    suspend fun addPost(postData: PostData){
        postDao.insertPost(postData)
    }
}