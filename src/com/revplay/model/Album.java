package com.revplay.model;

import java.util.Date;

public class Album {

    private int albumId;
    private int artistId;
    private String albumName;
    private Date releaseDate;

    public int getAlbumId() { return albumId; }
    public void setAlbumId(int albumId) { this.albumId = albumId; }

    public int getArtistId() { return artistId; }
    public void setArtistId(int artistId) { this.artistId = artistId; }

    public String getAlbumName() { return albumName; }
    public void setAlbumName(String albumName) { this.albumName = albumName; }

    public Date getReleaseDate() { return releaseDate; }
    public void setReleaseDate(Date releaseDate) { this.releaseDate = releaseDate; }
}
