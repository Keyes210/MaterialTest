package com.alexlowe.materialtest;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class NavigationDrawerFragment extends Fragment {

    private RecyclerView mRecylerView;

    public static final String PREF_FILE_NAME ="testpref";
    public static final String KEY_USER_DRAWER_AWARE ="user_drawer_aware";
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private VivzAdapter adapter;
    private boolean mUserDrawerAware;
    private View containerView;
    private  boolean mFromSavedInstanceState;

    public NavigationDrawerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mUserDrawerAware = Boolean.valueOf(readFromPreferences(getActivity(), KEY_USER_DRAWER_AWARE, "false"));

        if(savedInstanceState != null)mFromSavedInstanceState = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //made variable so that I can add elements to it
        View layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        mRecylerView = (RecyclerView) layout.findViewById(R.id.drawer_list);
        adapter = new VivzAdapter(getActivity(), getData());
        mRecylerView.setAdapter(adapter);
        mRecylerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return layout;
    }

    public static List<Item> getData(){
        List<Item> data = new ArrayList<>();
        int[] icons = {R.drawable.ic_number1, R.drawable.ic_number2, R.drawable.ic_number3,
                R.drawable.ic_number4};
        String[] titles = {"Mario","Pat","Mike","Rob"};

        for (int i = 0; i < 100 /*titles.length && i < icons.length*/; i++){
            Item current = new Item();
            current.setTitle(titles[i % titles.length]); //pretty nifty
            current.setIconId(icons[i % icons.length]); //% part is to use 4 items again and again

            data.add(current);
        }

        return data;
    }


    public void setUp(int fragmentID, DrawerLayout drawerLayout, final Toolbar toolbar) {
        containerView = getActivity().findViewById(fragmentID);

        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open,
                R.string.drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if(!mUserDrawerAware){
                    mUserDrawerAware = true;
                    saveToPreferences(getActivity(), KEY_USER_DRAWER_AWARE, String.valueOf(mUserDrawerAware));
                }
                //forces app to redraw actionbar
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            /*@Override  took out b/c this stops hamburger animation
            public void onDrawerSlide(View drawerView, float slideOffset) {
                if(slideOffset < 0.6) {
                    //makes rest of the app darken based on how far the toolbar is pulled out
                    toolbar.setAlpha(1 - slideOffset);
                }
            }*/
        };
        //check if drawer is being created for the first time
        if(!mUserDrawerAware && !mFromSavedInstanceState){
            mDrawerLayout.openDrawer(containerView);
        }

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
    }

    //does not depend on any object so it can be made static
    public static void saveToPreferences(Context context, String preferenceName, String preferenceValue){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME,
                //so our app is the only one that can edit it
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(preferenceName, preferenceValue);
        //could use commit() here too, but it's not asynch, so it's slower, but w/ apply you don't get result back
        editor.apply();
    }

    public static String readFromPreferences(Context context, String preferenceName, String defaultValue){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME,
                Context.MODE_PRIVATE);
        return sharedPreferences.getString(preferenceName, defaultValue);
    }
}
