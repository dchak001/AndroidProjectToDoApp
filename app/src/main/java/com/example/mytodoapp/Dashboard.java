package com.example.mytodoapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

public class Dashboard extends AppCompatActivity  {
    TextView mname;
    TextView memail;
    private RecyclerView recyclerView;
    static int id=0;
    DrawerLayout dl;
    NavigationView nv;
    TextView tv;
    ActionBarDrawerToggle toggle;
    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_main);
        Bundle b= getIntent().getExtras();
        id=Integer.parseInt(b.getString("id"));
        final int id1=id;
        final String name=b.getString("username");
        //Initialize views from the layout
        Toolbar toolbar = findViewById(R.id.toolbar);
        // setSupportActionBar(toolbar);
        dl = findViewById(R.id.drawerLayout);
        nv = findViewById(R.id.nav_view);

        //Generating nav header
        View navHeader = nv.inflateHeaderView(R.layout.nav_header);
        tv = navHeader.findViewById(R.id.navUser);

        //ActionBarDrawerToggle is initialized to sync drawer open and closed states
        toggle = new ActionBarDrawerToggle(this, dl,toolbar,R.string.navigation_drawer_open , R.string.navigation_drawer_close);

        dl.addDrawerListener(toggle);
        toggle.syncState();

        //The Hamburger icon is applied to the action bar for working with the nav drawer
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //On clicking of any menu items, actions will be performed accordingly
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {


            @Override
            @NonNull
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id){
                    case R.id.nav_home:
                        Toast.makeText(Dashboard.this, "This activity", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.edit_profile:
                    {Intent intent=new Intent(Dashboard.this, ProfileActivity.class);
                    Bundle b=new Bundle();
                    b.putString("name",name);
                    Log.i("dashboard"," "+id1);
                    b.putString("id",Integer.toString(id1));
                    intent.putExtras(b);
                    startActivity(intent);
                    //finish();
                        break;}
                    case R.id.nav_logout:
                    {
                        logOut();
                    }
                        break;
                    default:
                        return true;
                }

                return true;
            }
        });



        tv.setText(name);


        Realm realm=Realm.getDefaultInstance();
        RealmResults<Task> task=realm.where(Task.class).equalTo("userId",id).sort("dueDate", Sort.ASCENDING).findAll();
        if(task.size()>0)
            Log.i("dashboard","task exist");
        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        if(task.size()==0)
        {
//            Toast.makeText(this, "Fragment to be loaded", Toast.LENGTH_SHORT).show();
            ft.add(R.id.frame_container,new Fragment2());}
        //ft.addToBackStack(null);}
        else
        {
            RealmResults<Task> t1=realm.where(Task.class).equalTo("userId",id).and().equalTo("checked","true").findAll();
            if(t1.size()>0)
            {   realm.beginTransaction();

                t1.deleteAllFromRealm();
                realm.commitTransaction();
                task = realm.where(Task.class).equalTo("userId", id).findAll();
            }

            ft.add(R.id.frame_container,new Fragment1(task));


        }
        ft.commit();
        realm.close();

    }

    @Override
    public void onResume() {
        super.onResume();
        Realm realm=Realm.getDefaultInstance();
        MyUser user=realm.where(MyUser.class).equalTo("id",id).findFirst();
        tv.setText(user.getName());
        RealmResults<Task> task=realm.where(Task.class).equalTo("userId",id).sort("dueDate", Sort.ASCENDING).findAll();
        if(task.size()>0)
            Log.i("dashboard","task exist");
        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        if(task.size()==0)
        {
//
            ft.add(R.id.frame_container,new Fragment2());}
        else
        {

            ft.replace(R.id.frame_container,new Fragment1(task));

            //ft.addToBackStack(null);
        }
        ft.commit();
        realm.close();

    }

    public void createTask(View view)
    {
        Intent intent=new Intent(this, CreateTask.class);
        intent.putExtra("userId",id);
        startActivity(intent);
    }
    public void logOut() {

        startActivity(new Intent(this, Login_Activity.class));
        finish();

    }






    public void allDone(View view) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Task> t = realm.where(Task.class).equalTo("userId", id).and().equalTo("checked", "false").findAll();

        try {
            realm.beginTransaction();
            for (Task t1 : t) {
                t1.setChecked("true");
            }
            realm.commitTransaction();

        } catch (Exception e) {
            realm.cancelTransaction();
        } finally {
            realm.close();
        }
        t = realm.where(Task.class).equalTo("userId", id).findAll();
        if (t.size() > 0)
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new Fragment1(t)).commit();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawerLayout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            startActivity(new Intent(this, Login_Activity.class));
            finish();
        }
    }
}
