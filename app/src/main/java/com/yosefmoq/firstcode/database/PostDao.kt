package com.yosefmoq.firstcode.database

import androidx.room.*
import io.reactivex.Single
@Dao
interface PostDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(postData: PostData):Long

    @Delete
    suspend fun deletePost(postData: PostData):Int

    @Query("SELECT * FROM post_table WHERE id=:id")
    fun getPostByPostId(id:Int):Single<PostData>

    @Query("SELECT * FROM post_table WHERE needRemove =:needRemove ORDER BY id DESC")
    fun getAllPosts(needRemove: Boolean):Single<List<PostData>>

    @Query("DELETE FROM post_table")
    suspend fun deleteAll();

    @Query("SELECT id FROM post_table ORDER BY id DESC LIMIT 1")
    fun getLastId():Int

    @Update()
    suspend fun updatePost(postData: PostData):Int

    @Query("SELECT * From post_table Where needAdd =:sync OR needEdit =:sync OR needRemove =:sync")
    fun getSync(sync:Boolean?=true):Single<List<PostData>>
}