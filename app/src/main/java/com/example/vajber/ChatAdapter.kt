package com.example.vajber

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth

class ChatAdapter(private val messages: ArrayList<Message>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class SentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sentMessage = itemView.findViewById<TextView>(R.id.sentMessageTextView)!!
    }

    class ReceivedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val receivedMessage = itemView.findViewById<TextView>(R.id.receivedMessageTextView)!!
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return if (viewType == 1) {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.sent_message, parent, false)
            SentViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.received_message, parent, false)
            ReceivedViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.javaClass == SentViewHolder::class.java) {
            holder as SentViewHolder
            holder.sentMessage.text = messages[position].message
        } else {
            holder as ReceivedViewHolder
            holder.receivedMessage.text = messages[position].message
        }
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage = messages[position]
        return if (currentMessage.senderId == FirebaseAuth.getInstance().currentUser?.uid) {
            ITEM_SENT
        } else {
            ITEM_RECEIVED
        }
    }

    companion object {
        const val ITEM_SENT = 1
        const val ITEM_RECEIVED = 2
    }
}