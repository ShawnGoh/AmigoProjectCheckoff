package com.example.infosys1d_amigoproject.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.infosys1d_amigoproject.R;
import com.example.infosys1d_amigoproject.chat_tab.MessageActivity;
import com.example.infosys1d_amigoproject.models.Userdataretrieval;
import com.example.infosys1d_amigoproject.models.users_display;
import com.example.infosys1d_amigoproject.models.users_private;
import com.example.infosys1d_amigoproject.projectmanagement_tab.Project;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ApplicantAdapter extends RecyclerView.Adapter<ApplicantAdapter.Viewholder> {

    private Context mcontext;
    private ArrayList<users_display> mUsers_display;
    private ArrayList<String> mUsers_ids;
    DatabaseReference myref;
    Project project;


    public ApplicantAdapter(Context mcontext, ArrayList<users_display> mUsers_display, ArrayList<String> mUsers_ids, Project project){
        this.mUsers_display = mUsers_display;
        this.mUsers_ids = mUsers_ids;
        this.mcontext = mcontext;
        this.project = project;
    }

    public class Viewholder extends RecyclerView.ViewHolder{
        public TextView username;
        public ImageView profile_image;
        public ImageButton accept, reject;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            accept = itemView.findViewById(R.id.applicant_item_accept);
            reject = itemView.findViewById(R.id.applicant_item_reject);
            username = itemView.findViewById(R.id.Chat_username);
            profile_image = itemView.findViewById(R.id.Chat_Profile_Image);
        }
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.useritem_applicant, parent, false);


        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {

        String user_id = mUsers_ids.get(position);
        System.out.println(user_id+ "123456");
        users_display user = mUsers_display.get(position);
        holder.username.setText(user.getName());
        myref = FirebaseDatabase.getInstance().getReference("Projects").child(project.getProjectID());
        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> memberlist = project.getUsersinProject();
                List<String> applicantlst = project.getApplicantsinProject();
                System.out.println(memberlist.toString());
                if (!memberlist.contains(user_id)){
                    memberlist.add(user_id);
                    HashMap<String, Object> hashmap = new HashMap<>();
                    hashmap.put("usersinProject", memberlist);
                    myref.updateChildren(hashmap);
                    applicantlst.remove(user_id);
                    HashMap<String, Object> hashmap2 = new HashMap<>();
                    hashmap2.put("applicantsinProject", applicantlst);
                    myref.updateChildren(hashmap2);
                }
            }
        });

        holder.reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        if(user.getProfile_picture().equals("none")){
            holder.profile_image.setImageResource(R.mipmap.ic_launcher);
        }
        else{
            Picasso.get().load(user.getProfile_picture()).into(holder.profile_image);
        }


    }



    @Override
    public int getItemCount() {
        return mUsers_display.size();
    }


    }
