package com.example.dorixona.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dorixona.R
import com.example.dorixona.databinding.BookItemBinding
import com.example.dorixona.databinding.CommentItemBinding
import com.example.dorixona.model.Comment
import com.example.dorixona.model.User


class CommentAdapter(var commentList: List<Comment>) :
    RecyclerView.Adapter<CommentAdapter.CommentHolder>() {

    class CommentHolder(binding: CommentItemBinding) : RecyclerView.ViewHolder(binding.root) {
        var user = binding.userName
        var text = binding.comment

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentHolder {

        return CommentHolder(
            CommentItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return commentList.size
    }

    override fun onBindViewHolder(holder: CommentHolder, position: Int) {
        var comment = commentList[position]
        holder.user.text = comment.username
        holder.text.text = comment.text

    }
}