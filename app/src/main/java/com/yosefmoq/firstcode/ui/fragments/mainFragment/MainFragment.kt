package com.yosefmoq.firstcode.ui.fragments.mainFragment

import android.Manifest
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.nguyenhoanglam.imagepicker.model.Config
import com.nguyenhoanglam.imagepicker.model.Image
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker
import com.squareup.picasso.Picasso
import com.yosefmoq.firstcode.R
import com.yosefmoq.firstcode.constant.ADD_POST
import com.yosefmoq.firstcode.constant.EDIT_POST
import com.yosefmoq.firstcode.database.PostData
import com.yosefmoq.firstcode.database.shLocal.Session
import com.yosefmoq.firstcode.interfaceI.OnPostClickListener
import com.yosefmoq.firstcode.receiver.NetworkReceiver
import com.yosefmoq.firstcode.utils.ConnectionLiveData
import com.yosefmoq.firstcode.viewmodel.PostViewModel
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_main.*
import pub.devrel.easypermissions.EasyPermissions

class MainFragment : Fragment(), OnPostClickListener {
    private lateinit var mNetworkReceiver: BroadcastReceiver
    private lateinit var viewModel: PostViewModel
    private lateinit var sweetAlertDialog: SweetAlertDialog
    private lateinit var postAdapter: PostAdapter
    private lateinit var mNav: NavController
    private lateinit var civProfile: CircleImageView
    private lateinit var images: ArrayList<Image>
    private lateinit var etTitleText: AppCompatEditText
    private var postsList: MutableList<PostData> = mutableListOf()
    private lateinit var layoutManager: LinearLayoutManager
    private val PERMISSION_CODE: Int = 5034
    private var selectedPostToEdit = -1;
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initItems()
        initClicks()
    }

    private fun initClicks() {
        flaAdd.setOnClickListener {
            showAlertDialogButtonClicked(ADD_POST, PostData())
        }
        rvPosts.addOnScrollListener(mScrollListener)
    }

    private fun initItems() {
        initAlert()
/*
        mNetworkReceiver = NetworkReceiver()
        requireActivity().registerReceiver(mNetworkReceiver, IntentFilter(android.net.conn.CONNECTIVITY_CHANGE));
*/

        viewModel = ViewModelProvider(this).get(PostViewModel::class.java)
        if (postsList.size == 0) {
            viewModel.getRemotlyPost()
        }
        mNav = findNavController()
        postAdapter = PostAdapter(requireContext(), postsList, this)
        rvPosts.adapter = postAdapter
        layoutManager = LinearLayoutManager(requireContext())
        rvPosts.layoutManager = layoutManager
        observe()
        images = arrayListOf()

    }

    private fun initAlert() {
        sweetAlertDialog = SweetAlertDialog(requireContext(), SweetAlertDialog.PROGRESS_TYPE)
        sweetAlertDialog.setTitleText("Loading")
        sweetAlertDialog.create()
    }

    private fun observe() {
        val connectionLiveData = ConnectionLiveData(requireContext())
        connectionLiveData.observe(requireActivity(), { isConnected ->
            if(isConnected){
                viewModel.sync()
            }
        })

        viewModel.removePostToFirebaseLiveData.observe(requireActivity(), {
            if (it) {
                viewModel.getLocalyPost()
            }
        })
        viewModel.editPostToFirebaseLiveData.observe(requireActivity(), {
            if (it) {
                viewModel.getLocalyPost()

                selectedPostToEdit = -1
            } else {
                selectedPostToEdit = -1
            }
        })
        viewModel.postDataLive.observe(requireActivity(), {
            postAdapter.addAll(it)
        })
        viewModel.addPostToFirebaseLiveData.observe(requireActivity(), {
            if (it) {
                viewModel.getLocalyPost()
            }
        })
        viewModel.paganationFirebase.observe(requireActivity(), {
            postAdapter.add(it)
        })
        viewModel.postLoading.observe(requireActivity(), {
            if (it) {
                sweetAlertDialog.show()
            } else {
                sweetAlertDialog.hide()
            }
        })
    }

    override fun onPostClick(post: PostData) {
        val bundle = Bundle()
        bundle.putSerializable("post", post)
        mNav.navigate(R.id.action_mainFragment_to_detailsFragment, bundle)
    }

    override fun onEditClick(position: Int, post: PostData) {
        showAlertDialogButtonClicked(EDIT_POST, post)
        selectedPostToEdit = position
    }

    override fun onRemoveClick(position: Int, post: PostData) {
        viewModel.removePost(post)
    }

    private fun showAlertDialogButtonClicked(title: String, post: PostData) {
        // create an alert builder
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setTitle(title)
        // set the custom layout
        val customLayout: View = layoutInflater.inflate(R.layout.fragment_add_post, null)
        builder.setView(customLayout)
        // add a button
        builder.setPositiveButton(
            "OK"
        ) { dialog, which -> // send data from  the AlertDialog to the Activity
            val titleText = etTitleText.text.toString()
            if (images.size != 0&&title== ADD_POST) {
                val postData = PostData()
                val id = Session.getInstance(requireContext())!!.getLocalSave()!!.getNextId()
                postData.id =  id
                Session.getInstance(requireContext())!!.getLocalSave()!!.setNextId(id + 1)
                postData.title = titleText
                postData.filePath = images.get(0).path
                viewModel.uploadImage(postData, title)

            }else if(images.size!=0&&title == EDIT_POST) {
                val postData = postsList.get(selectedPostToEdit)
                postData.title = titleText
                postData.filePath = images.get(0).path
                viewModel.uploadImage(postData, title)
            }else if(images.size==0 && title == EDIT_POST){
                val postData = postsList.get(selectedPostToEdit)
                postData.title = titleText
                viewModel.editPost(postsList.get(selectedPostToEdit))
            }else{
                Toast.makeText(requireContext(), "please Choose Image", Toast.LENGTH_SHORT).show()

            }
        }
        // create and show the alert dialog
        val dialog: AlertDialog = builder.create()
        dialog.show()
        etTitleText = customLayout.findViewById(R.id.etTitle)
        civProfile = customLayout.findViewById(R.id.profile_image)
        if (title == EDIT_POST) {
            etTitleText.setText(post.title)
            Picasso.get().load(post.url).placeholder(R.drawable.holder).into(civProfile)
        }
        civProfile.setOnClickListener {
            handleNewImage()
        }
    }

    private fun handleNewImage() {
        if (EasyPermissions.hasPermissions(
                requireActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        ) {
            ImagePicker.with(this)
                .setFolderMode(false)
                .setMultipleMode(true)
                .setMaxSize(1)
                .setShowCamera(true)
                .setSelectedImages(images)
                .setLimitMessage(getString(R.string.limit_selection_1_images_only))
                .start()


        } else {
            EasyPermissions.requestPermissions(
                this,
                getString(R.string.get_pic_msg),
                PERMISSION_CODE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }
    }

    private fun sendDialogDataToActivity(data: String) {
        Toast.makeText(requireContext(), data, Toast.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (data != null) {
                if (requestCode == Config.RC_PICK_IMAGES) {
                    if (resultCode == Activity.RESULT_OK) {
                        images = data.getParcelableArrayListExtra(Config.EXTRA_IMAGES)!!
                        civProfile.setImageURI(images.get(0).uri)
                    }
                }
            }

        }
    }

    var mScrollListener: RecyclerView.OnScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            val visibleItemCount: Int = layoutManager.getChildCount()
            val totalItemCount: Int = layoutManager.getItemCount()
            val pastVisibleItems: Int = layoutManager.findFirstVisibleItemPosition()
            if (pastVisibleItems + visibleItemCount >= totalItemCount) {
                viewModel.getPaganationFirebase()
            }
        }
    }

}