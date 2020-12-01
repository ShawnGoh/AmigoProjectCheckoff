package com.example.infosys1d_amigoproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.infosys1d_amigoproject.Utils.FirebaseMethods;
import com.example.infosys1d_amigoproject.projectmanagement.ExploreProjectListings;
import com.example.infosys1d_amigoproject.projectmanagement.Project;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DiscoverFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DiscoverFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    String s1[] = {"Learn Python with Me :)", "two", "three", "four", "five"};
    String s2[] = {"one", "two", "three", "four", "five"};
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button seeAllButton;
    private SearchView searchView;
    MyAdapter myAdapter;
    RecyclerView recyclerView;
    FirebaseMethods firebaseMethods;
    DatabaseReference databaseReference;
    public DiscoverFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DiscoverFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DiscoverFragment newInstance(String param1, String param2) {
        DiscoverFragment fragment = new DiscoverFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        LayoutInflater inflater2 = LayoutInflater.from(container.getContext());
        View view = inflater2.inflate(R.layout.fragment_discover, container, false);

        recyclerView = view.findViewById(R.id.suggestedRecycler);

        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        seeAllButton = view.findViewById(R.id.seeAllButton1);
        seeAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(container.getContext(), ExploreProjectListings.class));

            }
        });
        databaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference projref = databaseReference.child("Projects");
        ArrayList<Project> projectList = new ArrayList<>();
        myAdapter = new MyAdapter(projectList);
        recyclerView.setAdapter(myAdapter);
        projref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                projectList.clear();
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                   // System.out.println("Test 1234556789" + postSnapshot.getValue().toString());
                    Project project = postSnapshot.getValue(Project.class);
                    System.out.println(project.getThumbnail());
                    projectList.add(project);

                    // here you can access to name property like university.name

                }
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: ");
            }
        });

     //   Project new_proj = new Project("random url", "test Title", "test description ","userid");

        firebaseMethods = new FirebaseMethods(container.getContext());
//        Project new_proj = new Project("random url", "test Title", "test description ",
//                new ArrayList<String>(Arrays.asList("hello","java","C++")),
//                new ArrayList<String>(Arrays.asList("3Qyanm1Rl6WgR0eB4v8oUFULth72",firebaseMethods.getUserID())),
//                firebaseMethods.getUserID());
//        MyAdapter myAdapter = new MyAdapter(new ArrayList<Project>(Arrays.asList(new_proj,new_proj)));




        searchView = view.findViewById(R.id.searchBarHome);
        searchView.setFocusable(false);
//        EditText editText = (EditText) searchView.findViewById(R.id.searchBarHome);
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    SearchFragment searchFragment = new SearchFragment();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, searchFragment);
                    transaction.commit();
                    MainActivity.menu_bottom.setVisibility(View.GONE);
                }
            }
        });
        return view;
    }
}