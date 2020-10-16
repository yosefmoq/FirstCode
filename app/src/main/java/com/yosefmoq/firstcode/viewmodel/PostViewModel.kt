package com.yosefmoq.firstcode.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storageMetadata
import com.yosefmoq.firstcode.constant.ADD_POST
import com.yosefmoq.firstcode.constant.COLLECTION_POST
import com.yosefmoq.firstcode.constant.EDIT_POST
import com.yosefmoq.firstcode.database.PostData
import com.yosefmoq.firstcode.database.shLocal.Session
import com.yosefmoq.firstcode.network.PostServiceApi
import com.yosefmoq.firstcode.utils.NetworkUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import java.io.File
import kotlin.collections.ArrayList

class PostViewModel(application: Application) : BaseViewModel(application) {
    //API
    private val api = PostServiceApi()
    private val disposable = CompositeDisposable()

    // Firebase
    private val firebaseFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val firebaseStorage: FirebaseStorage = FirebaseStorage.getInstance()
    private val firebaseReference = firebaseStorage.reference
    private val firebaseReferencePosts = firebaseReference.child(COLLECTION_POST)
    private val collectionReference = firebaseFirestore.collection(COLLECTION_POST)

    //Live Data
    val postLoading = MutableLiveData<Boolean>()
    val postDataLive = MutableLiveData<List<PostData>>()
    val paganationFirebase = MutableLiveData<List<PostData>>()
    val addPostToFirebaseLiveData = MutableLiveData<Boolean>()
    val editPostToFirebaseLiveData = MutableLiveData<Boolean>()
    val removePostToFirebaseLiveData = MutableLiveData<Boolean>()

    fun removePost(postData: PostData) {
        postLoading.value = true
        if (NetworkUtils.isConnected(getApplication())) {
            launch {
                postDao.deletePost(postData)
                collectionReference.document(postData.id.toString())
                    .delete()
                    .addOnCompleteListener {
                        postLoading.value = false
                        if (it.isSuccessful) {
                            removePostToFirebaseLiveData.value = true
                        } else {
                            removePostToFirebaseLiveData.value = false
                            thereError()
                        }

                    }
                getLocalyPost()
            }
        } else {
            postData.needRemove = true
            launch {
                postDao.updatePost(postData)
                removePostToFirebaseLiveData.value = true
                postLoading.value = false
            }
        }

    }

    fun getLocalyPost() {
        postLoading.value = true

        postDao.getAllPosts(false).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableSingleObserver<List<PostData>>() {
                override fun onSuccess(t: List<PostData>) {
                    postsRetrive(t)
                }

                override fun onError(e: Throwable) {
                    postLoading.value = false

                }
            })
    }

    fun getRemotlyPost() {
        postLoading.value = true
        if (Session.getInstance(getApplication())!!.getLocalSave()!!.isFirstTime()) {
            if (NetworkUtils.isConnected(getApplication())) {
                disposable.add(
                    api.getPosts().subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(object : DisposableSingleObserver<List<PostData>>() {
                            override fun onSuccess(t: List<PostData>) {
                                postLoading.value = false
//                        savePostsLocaly(t.asReversed())
                                Session.getInstance(getApplication())!!.getLocalSave()!!
                                    .setNextId((t.last().id!! + 1))

                                savePostsInFirebase(t.asReversed())
                            }

                            override fun onError(e: Throwable) {
                                postLoading.value = false

                            }

                        }

                        )
                )
            } else {
                Toast.makeText(
                    getApplication(),
                    "No internet Connection, check your connection and try again later",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            getLocalyPost()
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

    fun savePostsLocaly(posts: List<PostData>) {
        launch {

            posts.forEach {
                postDao.insertPost(it)
            }
            postsRetrive(posts);
        }
    }

    private fun postsRetrive(posts: List<PostData>) {
        postLoading.value = false
        postDataLive.value = posts
    }


    private fun savePostsInFirebase(posts: List<PostData>) {
        launch {
            postDao.deleteAll()
            var i = 0
            posts.forEach {
                i++
                if (i < 100) {
                    collectionReference.document(it.id.toString()).set(it)
                    if (i < 20) {
                        postDao.insertPost(it)
                        Session.getInstance(getApplication())!!.getLocalSave()!!.setLastId(it.id!!)
                        Log.v("ttt", "Last id in firebase" + posts.last().id!!.toString())


                    } else {
                        postLoading.value = false
                        getLocalyPost()
                    }
                    Session.getInstance(getApplication())!!.getLocalSave()!!
                        .setLastIdInFirebase(it.id!!)

                } else {
                    return@forEach
                }
            }
            Session.getInstance(getApplication())!!.getLocalSave()!!.setFirstTime(false)

        }
    }

    fun getPaganationFirebase() {
        postLoading.value = true
        val postDataList = ArrayList<PostData>()
        val postId = Session.getInstance(getApplication())!!.getLocalSave()!!.getLastId()
        if (postId <= Session.getInstance(getApplication())!!.getLocalSave()!!
                .getLastIdInFirebase()
        ) {
            postLoading.value = false
        } else {
            collectionReference.orderBy("id", Query.Direction.DESCENDING)
                .whereLessThan("id", postId)
                .limit(10)
                .get().addOnCompleteListener {
                    postLoading.value = false
                    if (it.isSuccessful) {
                        it.result!!.forEach {
                            postDataList.add(it.toObject(PostData::class.java))
                            launch {
                                postDao.insertPost(it.toObject(PostData::class.java))
                            }
                        }
                        if (postDataList.size != 0) {
                            paganationFirebase.value = postDataList
                            Session.getInstance(getApplication())!!.getLocalSave()!!
                                .setLastId(postDataList.last().id!!)
                        } else {
                            Toast.makeText(getApplication(), "no more data", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
        }

    }

    fun addPost(postData: PostData) {
        postLoading.value = true
        if (NetworkUtils.isConnected(getApplication())) {
            launch {
                postDao.insertPost(postData)
                collectionReference.document(postData.id.toString())
                    .set(postData).addOnCompleteListener {
                        postLoading.value = false
                        if (it.isSuccessful) {
                            addPostToFirebaseLiveData.value = true
                        } else {
                            thereError()
                            addPostToFirebaseLiveData.value = false
                        }
                    }
            }
        } else {
            postData.needAdded = true
            launch {
                postDao.insertPost(postData)
                addPostToFirebaseLiveData.value = true
                postLoading.value = false
            }
        }


    }

    fun uploadImage(postData: PostData, type: String) {
        if (NetworkUtils.isConnected(getApplication())) {
            postLoading.value = true
            val name = "${postData.id}_post"
            val riversRef =
                firebaseReferencePosts.child("$name.jpg")
            val mStorageMetadata = storageMetadata {
                contentType = "image/jpg"
            }
            riversRef.putFile(Uri.fromFile(File(postData.filePath!!)), mStorageMetadata)
                .continueWithTask { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let {
                            throw it
                            Log.v("ttt", it.localizedMessage)
                        }
                    }
                    riversRef.downloadUrl
                }.addOnCompleteListener {
                    if (it.isSuccessful) {
                        if(postData.needAdded!!||postData.needEdit!!){
                            postData.needEdit = false
                            postData.needAdded = false
                            launch {
                                postDao.updatePost(postData)
                            }
                        }
                        if (type == ADD_POST) {
                            postData.url = it.result!!.toString()
                            addPost(postData)
                        } else {
                            postData.url = it.result!!.toString()
                            editPost(postData)
                        }
                    } else {
                        thereError()
                    }
                }.addOnFailureListener {

                }
        } else {
            if (type == ADD_POST) {
                addPost(postData)
            } else {
                editPost(postData)
            }
        }

    }

    fun thereError() {
        Toast.makeText(
            getApplication(),
            "There Error Please Try Again Later",
            Toast.LENGTH_SHORT
        ).show()

    }

    fun editPost(postData: PostData) {
        postLoading.value = true
        if (NetworkUtils.isConnected(getApplication())) {
            launch {
                postDao.updatePost(postData)
                collectionReference.document(postData.id.toString())
                    .set(postData)
                    .addOnCompleteListener {
                        postLoading.value = false

                        if (it.isSuccessful) {
                            editPostToFirebaseLiveData.value = true
                            Log.v("ttt", "success")

                        } else {
                            editPostToFirebaseLiveData.value = false
                            Log.v("ttt", "error")
                        }

                    }
            }
        } else {
            postData.needEdit = true
            launch {
                postDao.updatePost(postData)
                editPostToFirebaseLiveData.value = true
                postLoading.value = false
            }
        }

    }

    @SuppressLint("CheckResult")
    fun sync() {
        postDao.getSync().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableSingleObserver<List<PostData>>() {
                override fun onSuccess(t: List<PostData>) {
                    t.forEach {post->
                        if (post.needEdit!!) {
                            uploadImage(post, EDIT_POST)

                        } else if(post.needAdded!!){
                            uploadImage(post, ADD_POST)
                        }else if (post.needRemove!!) {
                            collectionReference.document(post.id.toString()).delete().addOnCompleteListener {
                                if(it.isSuccessful){
                                    launch {
                                        postDao.deletePost(post)
                                    }
                                }else{
                                    Log.v("ttt","removeError")
                                }
                            }
                        }
                    }
                }

                override fun onError(e: Throwable) {
                    Log.v("ttt", e.localizedMessage!!)
                }

            })
    }

}