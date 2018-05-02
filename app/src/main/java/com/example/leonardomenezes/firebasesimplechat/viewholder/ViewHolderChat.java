package com.example.leonardomenezes.firebasesimplechat.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.leonardomenezes.firebasesimplechat.R;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by leonardomenezes on 14/03/2018.
 */

public class ViewHolderChat extends RecyclerView.ViewHolder {

    public @BindView(R.id.yourMessageView) RelativeLayout yourMessageLayout;
    public @BindView(R.id.your_message) TextView yourMessage;
    public @BindView(R.id.your_date) TextView yourDate;
    public @BindView(R.id.other_name) TextView otherName;
    public @BindView(R.id.other_message)TextView othersMessage;
    public @BindView(R.id.otherMessageView) RelativeLayout othersMessageLayout;
    public @BindView(R.id.other_date) TextView othersDate;
    public @BindView(R.id.otherPicture) ImageView profilePicture;

    public ViewHolderChat(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
