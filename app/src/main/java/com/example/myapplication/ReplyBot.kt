package com.example.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage
import com.google.firebase.ml.naturallanguage.smartreply.FirebaseTextMessage
import com.google.firebase.ml.naturallanguage.smartreply.SmartReplySuggestion
import com.google.firebase.ml.naturallanguage.smartreply.SmartReplySuggestionResult
import com.google.mlkit.nl.smartreply.TextMessage

class ReplyBot : AppCompatActivity() {
    lateinit var etSendMessage : EditText
    lateinit var tvReply : TextView
    lateinit var btnSendMessage : Button
    lateinit var conversation:ArrayList<FirebaseTextMessage>
    var userId="123456" //in production application comes from user uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_reply_bot)

        etSendMessage=findViewById(R.id.etSendMessage)

        tvReply=findViewById(R.id.tvResult)
        btnSendMessage=findViewById(R.id.btnSendMessage)

        conversation= ArrayList()

        btnSendMessage.setOnClickListener {
            val message = etSendMessage.text.toString().trim()
            conversation.add(
                FirebaseTextMessage.createForRemoteUser(
                    message,
                    System.currentTimeMillis(),
                    userId
                )
            )
            //conversation.add(FirebaseTextMessage.createForLocalUser("heading out now", System.currentTimeMillis()))


            val smartReply = FirebaseNaturalLanguage.getInstance().smartReply

            smartReply.suggestReplies(conversation).addOnSuccessListener { result ->
                if (result.getStatus() == SmartReplySuggestionResult.STATUS_NOT_SUPPORTED_LANGUAGE) {
                    // The conversation's language isn't supported, so the the result doesn't contain any suggestions.
                    tvReply.text = "No Reply found as the language is not supported"
                } else if (result.getStatus() == SmartReplySuggestionResult.STATUS_SUCCESS) {
                    // Task completed successfully\
                    var reply = ""
                    for (suggestion: SmartReplySuggestion in result.suggestions) {
                        reply = reply + suggestion.text + "\n"
                    }
                    tvReply.text = reply

                }
            }.addOnFailureListener {
                // Task failed with an exception
                tvReply.text = "Error ${it.message}"
            }
        }

    }
}