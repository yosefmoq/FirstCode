package com.yosefmoq.firstcode.ui.fragments

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.squareup.picasso.Picasso
import com.yosefmoq.firstcode.Extentions.toDate
import com.yosefmoq.firstcode.R
import com.yosefmoq.firstcode.database.PostData
import kotlinx.android.synthetic.main.fragment_details.*
import java.io.File

class DetailsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val post:PostData = arguments?.getSerializable("post") as PostData
        tvTitle.text = post.title
        if(post.filePath!!.isNotEmpty()){
            ivItem.setImageURI(Uri.fromFile(File(post.filePath!!)))
        }else{
            Picasso.get().load(post.url).placeholder(R.drawable.holder).into(ivItem)

        }
    }
}