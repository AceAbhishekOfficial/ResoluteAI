package com.abhishek.resoluteai;

import static android.content.ContentValues.TAG;
import static android.view.View.GONE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Map;

public class RecordsActivity extends AppCompatActivity
{

    ListView listView;
    boolean dataLoaded =false;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);


        listView = findViewById(R.id.listView);
        progressBar = findViewById(R.id.progressBar);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Create the list of items
        ArrayList<ListItem> items = new ArrayList<>();
        String userId =(FirebaseAuth.getInstance().getUid()).toString();

        db.collection(userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()){


                            for (QueryDocumentSnapshot document : task.getResult())
                            {
                                Object data = document.getData();
                                String date = ((Map<?, ?>) data).get("date").toString();
                                String message = ((Map<?, ?>) data).get("message").toString();
                                String time = ((Map<?, ?>) data).get("time").toString();

                                items.add(new ListItem(date,time,message));
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }

                            CustomListAdapter adapter = new CustomListAdapter(RecordsActivity.this, items);
                            progressBar.setVisibility(GONE);
                            // Set the adapter for the ListView
                            listView.setAdapter(adapter);


                        }else {

                            Toast.makeText(RecordsActivity.this,"Failed to load data",Toast.LENGTH_LONG).show();

                        }
                        dataLoaded=true;
                    }
                });


    }

}