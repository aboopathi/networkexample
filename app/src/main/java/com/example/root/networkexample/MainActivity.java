package com.example.root.networkexample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements Callback<GitHubUser> ,UserFragment.OnFragmentInteractionListener{

    private FrameLayout mframeLayout;
    private EditText meditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mframeLayout= (FrameLayout) findViewById(R.id.user);

        showListFragment();

    }

    private void showListFragment() {

        android.app.FragmentTransaction fragmentTransaction= getFragmentManager().beginTransaction();
        UserFragment fragment = UserFragment.newInstance("param1","param2");
        fragmentTransaction.add(R.id.user, fragment);
        fragmentTransaction.commit();

    }

    public void onClick(View view) {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GitAPI.END_POINT)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        GitAPI githubUserAPI = retrofit.create(GitAPI.class);

        switch (view.getId()) {
            case R.id.loadUserData:
                // prepare call in Retrofit 2.0

                Call<GitHubUser> callUser = githubUserAPI.getUser("vogella");
                //asynchronous call
                callUser.enqueue(this);
//                Call<List<GitHubRepo>> callRepos = githubUserAPI.getRepos("vogella");
//                //asynchronous call
//                callRepos.enqueue(repos);
                break;
        }
    }


    Callback repos = new Callback<List<GitHubRepo>>() {


        @Override
        public void onResponse(Call<List<GitHubRepo>> call, Response<List<GitHubRepo>> response) {
            if (response.isSuccessful()) {
                List<GitHubRepo> repos = response.body();
                StringBuilder builder = new StringBuilder();
                for (GitHubRepo repo : repos) {
                    builder.append(repo.name + " " + repo.toString());
                }
                Toast.makeText(MainActivity.this, builder.toString(), Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(MainActivity.this, "Error code " + response.code(), Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        public void onFailure(Call<List<GitHubRepo>> call, Throwable t) {
            Toast.makeText(MainActivity.this, "Did not work " + t.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onResponse(Call<GitHubUser> call, Response<GitHubUser> response) {
        int code = response.code();
        if (code == 200) {
            GitHubUser user = response.body();
            Toast.makeText(this, "Got the user: " + user.email, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Did not work: " + String.valueOf(code), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onFailure(Call<GitHubUser> call, Throwable t) {
        Toast.makeText(this, "Nope", Toast.LENGTH_LONG).show();

    }
    public void onFragmentInteraction(String string) {
        if (!TextUtils.isEmpty(string)) {
            Toast.makeText(MainActivity.this, string, Toast.LENGTH_SHORT).show();
        }
    }



}
