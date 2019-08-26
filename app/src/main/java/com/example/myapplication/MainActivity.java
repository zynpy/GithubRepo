package com.example.myapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();
    private ListView lv;

    Button btnSubmit;
    EditText userName;

    ArrayList<Repository> repositoryList;
    RepoFavoriteDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        repositoryList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.list);
        btnSubmit = (Button)findViewById(R.id.submit);
        userName = (EditText) findViewById(R.id.userName);
        db = new RepoFavoriteDB(this);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userName.getText().length()>0) {
                    new GetRepositories().execute(String.valueOf(userName.getText()));
                }
                else
                    Toast.makeText(MainActivity.this, "Please Enter UserName", Toast.LENGTH_LONG).show();
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent=new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("repository", repositoryList.get(position));
                startActivityForResult(intent,1);
            }
        });

        final Spinner spinnerTest=(Spinner)findViewById(R.id.spinnerTest);
        ArrayList<String>testList = new ArrayList<>();
        testList.add("andresoviedo");testList.add("AmalH");testList.add("thadeubatista");testList.add("zynpy");
        ArrayAdapter<String> adapterKonu = new ArrayAdapter<String>(MainActivity.this, R.layout.support_simple_spinner_dropdown_item, testList);
        spinnerTest.setAdapter(adapterKonu);
       spinnerTest.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               userName.setText(spinnerTest.getSelectedItem().toString());
           }
           @Override
           public void onNothingSelected(AdapterView<?> parent) {}
       });
        userName.setHint("Enter UserName");


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

         Repository repository = (Repository) data.getExtras().getSerializable("repository");
        for (Repository repo:repositoryList){
             if(repo.getRepoName().equals(repository.getRepoName()))
                 repo.setFavoruite(repository.isFavoruite());
         }
        ((BaseAdapter) lv.getAdapter()).notifyDataSetChanged();
    }


    private class GetRepositories extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... users) {
            repositoryList.clear();
            List<String> repoNames = db.getRepositories(users[0]);
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String url = "https://api.github.com/users/"+users[0]+"/repos";
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONArray repositories = new JSONArray(jsonStr);
                    // Getting JSON Array node
                    for (int i = 0; i < repositories.length(); i++) {
                        JSONObject obj = repositories.getJSONObject(i);
                        JSONObject ownerObj = obj.getJSONObject("owner");
                        String repoName = obj.getString("name");
                        String avatarURL = ownerObj.getString("avatar_url");
                        String owner = ownerObj.getString("login");
                        String openIssue = obj.getString("open_issues_count");

                        Repository repository = new Repository(repoName, avatarURL, owner, 0 , openIssue);
                        repository.setFavoruite(repoNames.contains(repoName));
                        repositoryList.add(repository);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Json parsing error: " + e.getMessage(),  Toast.LENGTH_LONG).show();
                        }
                    });

                }

            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Couldn't get json from server. Check LogCat for possible errors!", Toast.LENGTH_LONG).show();
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            AdapterRepository adapter = new AdapterRepository(MainActivity.this, repositoryList);
            lv.setAdapter(adapter);
        }
    }
}
