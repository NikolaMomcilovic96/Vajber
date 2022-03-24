package com.example.vajber

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vajber.databinding.ActivityChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var messages: ArrayList<Message>
    private lateinit var mDbRef: DatabaseReference
    private lateinit var senderRoom: String
    private lateinit var receiverRoom: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val fullName = intent.getStringExtra("name")
        val receiverUid = intent.getStringExtra("uid")

        val senderUid = FirebaseAuth.getInstance().currentUser?.uid
        mDbRef = FirebaseDatabase.getInstance().reference

        senderRoom = receiverUid + senderUid
        receiverRoom = senderUid + receiverUid

        supportActionBar?.title = fullName

        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        messages = ArrayList()
        chatAdapter = ChatAdapter(messages)

        binding.chatRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.chatRecyclerView.adapter = chatAdapter

        mDbRef.child("chats").child(senderRoom).child("message")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    messages.clear()
                    for (child in snapshot.children) {
                        val message = child.getValue(Message::class.java)
                        messages.add(message!!)
                    }
                    chatAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

        binding.sendButton.setOnClickListener {
            val message = binding.messageInputText.text.toString()

            mDbRef.child("chats").child(senderRoom).child("message").push()
                .setValue(Message(message, senderUid)).addOnSuccessListener {
                    mDbRef.child("chats").child(receiverRoom).child("message").push()
                        .setValue(Message(message, senderUid))
                }
            binding.messageInputText.setText("")
        }
    }
}