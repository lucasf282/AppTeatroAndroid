package br.com.appteatro.appteatro.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import br.com.appteatro.appteatro.R;
import br.com.appteatro.appteatro.domain.model.ChatMessage;

public class ChatRoomActivity extends AppCompatActivity {

    private FloatingActionButton fab;
    private ListView listOfMessages;
    private EditText input;

    private DatabaseReference root;
    private FirebaseListAdapter<ChatMessage> adapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        String room_name = getIntent().getExtras().get("room_name").toString();
        setTitle(" Sala - " + room_name);
        root = FirebaseDatabase.getInstance().getReference().child(room_name);


        input = (EditText) findViewById(R.id.input);
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                enableSubmitIfReady();
            }
        });

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setEnabled(Boolean.FALSE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Read the input field and push a new instance
                // of ChatMessage to the Firebase database
                root.push().setValue(new ChatMessage(input.getText().toString(),
                        FirebaseAuth.getInstance()
                                .getCurrentUser()
                                .getDisplayName())
                );

                // Clear the input
                input.setText("");
            }
        });

        displayChatMessages();

    }

    private void displayChatMessages() {
        listOfMessages = (ListView) findViewById(R.id.list_of_messages);

        adapter = new FirebaseListAdapter<ChatMessage>(this, ChatMessage.class,
                R.layout.message, root) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {
                // Get references to the views of message.xml
                TextView messageText = (TextView) v.findViewById(R.id.message_text);
                TextView messageUser = (TextView) v.findViewById(R.id.message_user);
                TextView messageTime = (TextView) v.findViewById(R.id.message_time);

                // Set their text
                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());

                // Format the date before showing it
                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                        model.getMessageTime()));
            }
        };

        listOfMessages.setAdapter(adapter);
    }

    private void enableSubmitIfReady(){
        boolean isReady = input.getText().toString().length()>0;
        fab.setEnabled(isReady);
    }

}
