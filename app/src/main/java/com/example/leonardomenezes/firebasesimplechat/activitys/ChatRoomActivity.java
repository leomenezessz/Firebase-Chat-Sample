package com.example.leonardomenezes.firebasesimplechat.activitys;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.leonardomenezes.firebasesimplechat.Util;
import com.example.leonardomenezes.firebasesimplechat.model.Message;
import com.example.leonardomenezes.firebasesimplechat.R;
import com.example.leonardomenezes.firebasesimplechat.viewholder.ViewHolderChat;
import com.example.leonardomenezes.firebasesimplechat.model.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by leonardomenezes on 14/03/2018.
 */

public class ChatRoomActivity extends DefaultActivity {

    @BindView(R.id.chat_list) RecyclerView recyclerView;
    @BindView(R.id.button_send_message) FloatingActionButton sendButton;
    @BindView(R.id.button_send_not_able) FloatingActionButton sendButtonNotAble;
    @BindView(R.id.message_chat)EditText inputMessage;
    @BindView(R.id.chat_list_progress) ProgressBar progressBar;
    @BindView(R.id.progress_text) TextView progressText;
    @BindView(R.id.no_messages) TextView noMessageText;
    @BindView(R.id.sad_icon) ImageView sadIcon;
    private LinearLayoutManager linearLayoutManager;
    private DatabaseReference databaseReference;
    private FirebaseRecyclerAdapter<Message, ViewHolderChat> firebaseRecyclerAdapter;
    private User user;
    private ChildEventListener childEventListener;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        ButterKnife.bind(this);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(10000);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        user = (User) getIntent().getExtras().getSerializable("user");

        databaseReference = FirebaseDatabase.getInstance().getReference("Chat");

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() > 0) {
                    sendButton.setVisibility(View.VISIBLE);
                    sendButtonNotAble.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == 0) {
                    sendButton.setVisibility(View.INVISIBLE);
                    sendButtonNotAble.setVisibility(View.VISIBLE);
                    YoYo.with(Techniques.RubberBand).duration(400).playOn(sendButtonNotAble);
                }
            }
        };

        inputMessage.addTextChangedListener(textWatcher);

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Message, ViewHolderChat>(Message.class, R.layout.chat_item_list, ViewHolderChat.class, databaseReference) {
            @Override
            protected void populateViewHolder(ViewHolderChat viewHolder, Message model, int position) {

                disableProgress();

                if (model.getSenderUid().equals(user.getUid())){
                    viewHolder.othersMessageLayout.setVisibility(View.INVISIBLE);
                    viewHolder.yourMessage.setText(model.getMessage());
                    viewHolder.yourDate.setText(model.getDate());
                }else {
                    viewHolder.yourMessageLayout.setVisibility(View.INVISIBLE);
                    viewHolder.othersMessage.setText(model.getMessage());
                    viewHolder.otherName.setText(model.getSenderName());
                    viewHolder.othersDate.setText(model.getDate());
                    loadProfilePicture(Util.decodeStringBase64(model.getUserPicture()), viewHolder.profilePicture);
                }
            }
        };

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String message = inputMessage.getText().toString();

                databaseReference.push().setValue(new Message()
                        .setMessage(message)
                        .setUserPicture(user.getProfilePicture())
                        .setSenderName(user.getName())
                        .setDate(Util.getCurrentTime())
                        .setSenderUid(user.getUid()));

                inputMessage.setText("");
            }
        });

         childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                updateChatList();
                disableNoMessages();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                showNoMessages();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                showToast(databaseError.getMessage(), Toast.LENGTH_SHORT);

            }
        };

        databaseReference.addChildEventListener(childEventListener);
        recyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.notifyDataSetChanged();


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                if (linearLayoutManager.getItemCount()==0){
                    showNoMessages();
                    disableProgress();
                }
            }
        }, 4000);
    }

    private void showNoMessages() {
        if (linearLayoutManager.getItemCount() == 0) {
            sadIcon.setVisibility(View.VISIBLE);
            noMessageText.setVisibility(View.VISIBLE);
        }
    }

    private void disableNoMessages() {
        if (sadIcon.getVisibility() == View.VISIBLE) {
            sadIcon.setVisibility(View.GONE);
            noMessageText.setVisibility(View.GONE);
        }
    }

    private void updateChatList(){
        if (firebaseRecyclerAdapter.getItemCount()==0){
            return;
        }

        recyclerView.smoothScrollToPosition(firebaseRecyclerAdapter.getItemCount() -1);
    }

    private void loadProfilePicture(byte [] pictureBytes, ImageView imageView){
        Glide.with(this).load(pictureBytes)
                .fallback(R.drawable.ic_fallback_user)
                .skipMemoryCache(true)
                .into(imageView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        firebaseRecyclerAdapter.cleanup();
        databaseReference.removeEventListener(childEventListener);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setIcon(R.drawable.ic_fallback_user).setTitle("Do you really want to leave ?")
            .setMessage("If you leave your user will be eraser, because your user is locally saved.")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Util.clearSavedUser(ChatRoomActivity.this);
                    finish();
                }
            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).show();
        }

        return true;
    }

    private void disableProgress(){
        if (progressBar.getVisibility() == View.VISIBLE){
            progressBar.setVisibility(View.GONE);
            progressText.setVisibility(View.GONE);
        }
    }
}
