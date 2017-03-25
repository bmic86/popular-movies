package com.example.android.popularmovies1.data.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popularmovies1.R;
import com.example.android.popularmovies1.data.entities.MovieRelatedVideoListItem;
import com.example.android.popularmovies1.listeners.MovieRelatedVideoListItemClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.VideosViewHolder> {

    private ArrayList<MovieRelatedVideoListItem> videos;
    private MovieRelatedVideoListItemClickListener videoClickListener;

    public VideosAdapter(MovieRelatedVideoListItemClickListener videoClickListener) {
        videos = new ArrayList<>();
        this.videoClickListener = videoClickListener;
    }

    public VideosAdapter(ArrayList<MovieRelatedVideoListItem> videos,
                         MovieRelatedVideoListItemClickListener videoClickListener)
    {
        this.videos = videos;
        this.videoClickListener = videoClickListener;
    }

    public VideosAdapter(String data, MovieRelatedVideoListItemClickListener videoClickListener) throws JSONException {
        videos = new ArrayList<>();
        this.videoClickListener = videoClickListener;

        if(data != null && !data.isEmpty()) {
            JSONObject json = new JSONObject(data);
            JSONArray results = json.getJSONArray("results");
            for (int i=0; i<results.length(); ++i) {
                videos.add(new MovieRelatedVideoListItem(results.getJSONObject(i)));
            }
        }
    }

    @Override
    public VideosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.videos_list_item, parent, false);
        return new VideosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VideosViewHolder holder, int position) {
        holder.bind(videos.get(position));
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    public ArrayList<MovieRelatedVideoListItem> getVideos() {
        return videos;
    }

    public class VideosViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView nameTextView;
        MovieRelatedVideoListItem listItem;

        public VideosViewHolder(View itemView) {
            super(itemView);
            nameTextView = (TextView) itemView.findViewById(R.id.tv_video_name);
            itemView.setOnClickListener(this);
        }

        void bind(MovieRelatedVideoListItem video) {
            listItem = video;
            nameTextView.setText(video.getName());
        }

        @Override
        public void onClick(View view) {
            videoClickListener.onClick(listItem);
        }
    }
}
