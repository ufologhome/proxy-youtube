package com.example.youtubeproxy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.Holder> {

    public interface Click {
        void onClick(VideoItem v);
    }

    private final List<VideoItem> videos;
    private final Click click;

    public VideoAdapter(List<VideoItem> videos, Click click) {
        this.videos = videos;
        this.click = click;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup p, int v) {
        return new Holder(LayoutInflater.from(p.getContext())
                .inflate(R.layout.item_video, p, false));
    }

    @Override
    public void onBindViewHolder(Holder h, int i) {
        VideoItem v = videos.get(i);
        h.title.setText(v.title);
        Picasso.get().load(v.thumb).into(h.thumb);
        h.itemView.setOnClickListener(x -> click.onClick(v));
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    static class Holder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView thumb;
        Holder(View v) {
            super(v);
            title = v.findViewById(R.id.title);
            thumb = v.findViewById(R.id.thumbnail);
        }
    }
}
