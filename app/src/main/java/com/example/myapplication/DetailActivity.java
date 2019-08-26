package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

public class DetailActivity extends Activity {

    ImageView ivAvatar;
    TextView ivFavourite;
    TextView tvStars;
    TextView tvOpenIssue;
    TextView tvOwner;
    TextView tvRepoName;

    RepoFavoriteDB db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ivAvatar = (ImageView)findViewById(R.id.avatar);
        ivFavourite = (TextView) findViewById(R.id.ivFavoruite);
        tvStars = (TextView)findViewById(R.id.tvStars);
        tvOpenIssue = (TextView)findViewById(R.id.tvOpenIssues);
        tvOwner = (TextView)findViewById(R.id.tvOwner);
        tvRepoName = (TextView)findViewById(R.id.tvRepoName);

        db = new RepoFavoriteDB(this);

        Bundle bundle=new Bundle();
        bundle=getIntent().getExtras();
        String url;
        if (bundle!=null) {
            final Repository repository= (Repository) bundle.getSerializable("repository");
            if (repository!=null)
            {
                 Glide.with(this).load(repository.getAvatarURL()).into(ivAvatar);
                 tvOwner.setText(repository.getOwner());
                 tvStars.setText(String.valueOf(repository.getStars()));
                 tvOpenIssue.setText(repository.getOpenIssue());
                 tvRepoName.setText(repository.getRepoName());
                 if (repository.isFavoruite()) {
                    ivFavourite.setBackgroundResource(R.mipmap.favourite);
                 }

                Intent i = new Intent();
                i.putExtra("repository", repository);
                setResult(1, i);

                 ivFavourite.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         if (repository.isFavoruite()){
                             if (db.deleteRepository(repository)) {
                                 repository.setFavoruite(false);
                                 ivFavourite.setBackgroundResource(R.mipmap.unfavourite);
                             }
                         }else {
                             if (db.insertMessages(repository)) {
                                 repository.setFavoruite(true);
                                 ivFavourite.setBackgroundResource(R.mipmap.favourite);
                             }
                         }
                         Intent i = new Intent();
                         i.putExtra("repository", repository);
                         setResult(1, i);
                     }
                 });
            }
        }
    }
}
