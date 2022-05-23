package com.example.artsell.fragments;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.artsell.R;
import com.example.artsell.activities.LoginActivity;
import com.example.artsell.models.Users;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    FirebaseAuth auth;
    FirebaseDatabase db;
    FirebaseStorage storage;

    TextView profileEmail;
    EditText txtUsername, etStatus;
    CircleImageView profileImg;
    Button saveButton, logoutBtn;

    public ProfileFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();

        profileEmail = root.findViewById(R.id.profile_email);
        saveButton = root.findViewById(R.id.saveButton);
        etStatus = root.findViewById(R.id.etStatus);
        txtUsername = root.findViewById(R.id.txtUsername);
        logoutBtn = root.findViewById(R.id.logoutBtn);
        profileImg = root.findViewById(R.id.profileImg);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etStatus.getText().toString().equals("") && !txtUsername.getText().toString().equals("")) {
                    String status = etStatus.getText().toString();
                    String username = txtUsername.getText().toString();

                    HashMap<String, Object> obj = new HashMap<>();
                    obj.put("userName", username);
                    obj.put("status", status);

                    db.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                            .updateChildren(obj);

                    Toast.makeText(getActivity(), "Profile Updated.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Please Enter Username and Status", Toast.LENGTH_SHORT).show();
                }
            }
        });

        db.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Users users = snapshot.getValue(Users.class);
                        Picasso.get()
                                .load(users.getProfileImg())
                                .placeholder(R.drawable.profile)
                                .into(profileImg);

                        etStatus.setText(users.getStatus());
                        txtUsername.setText(users.getUserName());
                        profileEmail.setText(users.getMail());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 25);
            }
        });


        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
                Intent intent2 = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent2);
            }
        });
        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data.getData()!=null)
        {
            Uri sFile = data.getData();
            profileImg.setImageURI(sFile);
            final StorageReference reference = storage.getReference().child("profile_pic")
                    .child(FirebaseAuth.getInstance().getUid());

            reference.putFile(sFile).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            db.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                                    .child("profileImg").setValue(uri.toString());
                        }
                    });
                }
            });
        }
    }

    public void signOut() {
        auth.signOut();
    }

}