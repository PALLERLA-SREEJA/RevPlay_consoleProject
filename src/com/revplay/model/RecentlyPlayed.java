package com.revplay.model;

import java.sql.Timestamp;

public class RecentlyPlayed {

    private int id;
    private int userId;
    private int songId;
    private Timestamp playedAt;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getSongId() { return songId; }
    public void setSongId(int songId) { this.songId = songId; }

    public Timestamp getPlayedAt() { return playedAt; }
    public void setPlayedAt(Timestamp playedAt) { this.playedAt = playedAt; }
}