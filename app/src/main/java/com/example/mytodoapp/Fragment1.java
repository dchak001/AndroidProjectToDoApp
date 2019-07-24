package com.example.mytodoapp;



import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import io.realm.Realm;
import io.realm.RealmResults;

public class Fragment1 extends Fragment implements MyAdapter.OnItemClickListener {
    RealmResults<Task> mtask;
    static int count;
    Fragment1(RealmResults<Task> results)
    {
        mtask=results;
        count=0;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview=inflater.inflate(R.layout.fragment1, container, false);
        RecyclerView recyclerView = (RecyclerView)rootview.findViewById(R.id.task_recyclerview);
        // recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        MyAdapter mAdapter = new MyAdapter(mtask,this);
        recyclerView.setAdapter(mAdapter);
        return rootview;
    }

    @Override
    public int onButtonClick(int position) {
        Realm realm = Realm.getDefaultInstance();
        try {
            Log.i("hi", "position : " + position + " count : " + count);
            Log.i("hi", "task size : " + mtask.size());
            Task t = mtask.get(position);
            count++;

            realm.beginTransaction();
            // t.deleteFromRealm();
            t.setChecked("true");

            realm.copyToRealmOrUpdate(t);
            realm.commitTransaction();
            Log.i("hello", "taskname : " + t.getTaskName() + " checked : " + t.getChecked());
            Log.i("hi", "task size : " + mtask.size()+" due Date : "+t.getDueDate());
            Log.i("hello", "clicked");
        } catch (ArrayIndexOutOfBoundsException e) {
            Log.i("exception", "exception caused in position " + position);
        } finally {


            realm.close();
        }


        return 0;
    }

    public void onItemClick(int position)
    {
        Task t=mtask.get(position);
        FragmentTransaction ft=getActivity().getSupportFragmentManager().beginTransaction();
        ft.add(R.id.frame_container,new TaskDetails(t.getTaskName(),t.getTaskDetails(),t.getDueDate()));
        ft.addToBackStack(null);
        ft.commit();
    }
}