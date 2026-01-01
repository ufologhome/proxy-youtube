package com.example.youtubeproxy;

public class VideoItem {
    public String id;
    public String title;
    public String thumb;
    public String url;

    public VideoItem(String id, String title, String thumb, String url) {
        this.id = id;
        this.title = title;
        this.thumb = thumb;
        this.url = url;
    }
}
