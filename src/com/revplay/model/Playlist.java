package com.revplay.model;

public class Playlist {

    private int playlistId;
    private int userId;
    private String name;
    private String description;
    private String privacy;

    public int getPlaylistId() { return playlistId; }
    public void setPlaylistId(int playlistId) { this.playlistId = playlistId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getPrivacy() { return privacy; }
    public void setPrivacy(String privacy) { this.privacy = privacy; }
}
