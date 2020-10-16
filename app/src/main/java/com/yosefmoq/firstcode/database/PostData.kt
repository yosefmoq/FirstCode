package com.yosefmoq.firstcode.database

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.yosefmoq.firstcode.utils.FilePath
import java.io.Serializable

@Entity(tableName = "post_table")
data class PostData (
    @ColumnInfo(name = "albumId")
    @SerializedName("albumId")
    var albumId:Int? = 0,
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    @SerializedName("id")
    var id:Int?  =1,
    @ColumnInfo(name = "title")
    @SerializedName("title")
    var title:String? ="",
    @ColumnInfo(name ="url")
    @SerializedName("url")
    var url:String? = "",
    @ColumnInfo(name = "thumbnailUrl")
    @SerializedName("thumbnailUrl")
    var thumbnailUrl:String? = "",
    @ColumnInfo(name = "needAdd")
    @SerializedName("needAdd")
    var needAdded:Boolean? = false,
    @ColumnInfo(name = "needEdit")
    @SerializedName("needEdit")
    var needEdit:Boolean? = false,
    @ColumnInfo(name = "needRemove")
    @SerializedName("needRemove")
    var needRemove:Boolean? = false,
    @ColumnInfo(name = "filePath")
    @SerializedName("filepath")
    var filePath: String? = ""
):Serializable{
}