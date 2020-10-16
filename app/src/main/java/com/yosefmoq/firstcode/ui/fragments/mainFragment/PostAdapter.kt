package com.yosefmoq.firstcode.ui.fragments.mainFragment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yosefmoq.firstcode.R
import com.yosefmoq.firstcode.database.PostData
import com.yosefmoq.firstcode.interfaceI.OnPostClickListener
import kotlinx.android.synthetic.main.item_post.view.*

class PostAdapter(val mContext: Context,
                  var items: MutableList<PostData>,
                  var onPostClickListener: OnPostClickListener?) :
    RecyclerView.Adapter<PostAdapter.PostViewHolder>() {
    class PostViewHolder(view: View):RecyclerView.ViewHolder(view) {
        fun onBind(context: Context, items: MutableList<PostData>, position: Int,onPostClickListener: OnPostClickListener?){
            itemView.tvPostText.text = items.get(position).title
            itemView.clPostParent.setOnClickListener {
                onPostClickListener?.onPostClick(items.get(position))
            }
            itemView.btnRemove.setOnClickListener {
                onPostClickListener?.onRemoveClick(position,items.get(position))
            }
            itemView.btnEdit.setOnClickListener {
                onPostClickListener?.onEditClick(position,items.get(position))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return PostViewHolder(
            LayoutInflater.from(mContext).inflate(
                R.layout.item_post, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.onBind(mContext, items, position,onPostClickListener)
    }

    override fun getItemCount() = items.size

    fun addAll(posts:List<PostData>){
        this.items.clear()
        this.items.addAll(posts)
        notifyDataSetChanged()
    }
    fun add(posts:List<PostData>){
        this.items.addAll(posts)
        notifyDataSetChanged()
    }
    fun edit(position:Int,postData: PostData){
        this.items.set(position,postData)
        notifyItemChanged(position)
    }
}