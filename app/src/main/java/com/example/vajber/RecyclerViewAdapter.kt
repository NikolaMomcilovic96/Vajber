package com.example.vajber

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth

class RecyclerViewAdapter(private val context: Context, private val users: ArrayList<User>) :
    RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val fullname = itemView.findViewById<TextView>(R.id.fullnameTextView)!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.fullname.text = users[position].fullName
        holder.itemView.setOnClickListener {
            val intent = Intent(context, ChatActivity::class.java).apply {
                putExtra("name", users[position].fullName)
                putExtra("uid", users[position].uid)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return users.size
    }
}