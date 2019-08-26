package com.example.myapplication;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;


public class AdapterRepository extends ArrayAdapter<Repository> {

    Context context;
    private LayoutInflater mInflater;
    public static ArrayList<Repository> repositories;

    public AdapterRepository(Context context, ArrayList<Repository> repositories) {
        super(context, R.layout.list_item, repositories);

        this.repositories = repositories;
        this.context = context;
    }


    @Override
    public int getCount() {
        return repositories.size();
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final Holder myHolder;
        if (convertView == null) {
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.list_item, null);

            myHolder=new Holder();
            myHolder.ivStar = (ImageView) convertView.findViewById(R.id.ivStars);
            myHolder.repoName = (TextView) convertView.findViewById(R.id.repoName);
            convertView.setTag(myHolder);
        }else {
            myHolder= (Holder) convertView.getTag();
        }


        myHolder.repoName.setText(repositories.get(position).getRepoName());
        if (repositories.get(position)!=null && repositories.get(position).isFavoruite()) {
            myHolder.ivStar.setVisibility(View.VISIBLE);
        }else {
            myHolder.ivStar.setVisibility(View.GONE);
        }


        return convertView;
    }


    class Holder {
        ImageView ivStar;
        TextView  repoName;
    }

}
