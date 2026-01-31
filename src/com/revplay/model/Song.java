package com.revplay.model;

import java.util.Date;

public class Song {

    private int songId;
    private String title;
    private String genre;
    private double duration;
    private Date releaseDate;
    private int artistId;
    private int albumId;
    private int playCount;

    public int getSongId() { return songId; }
    public void setSongId(int songId) { this.songId = songId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public double getDuration() { return duration; }
    public void setDuration(double duration) { this.duration = duration; }

    public Date getReleaseDate() { return releaseDate; }
    public void setReleaseDate(Date releaseDate) { this.releaseDate = releaseDate; }

    public int getArtistId() { return artistId; }
    public void setArtistId(int artistId) { this.artistId = artistId; }

    public int getAlbumId() { return albumId; }
    public void setAlbumId(int albumId) { this.albumId = albumId; }

    public int getPlayCount() { return playCount; }
    public void setPlayCount(int playCount) { this.playCount = playCount; }
}
