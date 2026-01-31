package com.revplay.Dao;

import java.util.List;

import com.revplay.model.Song;

public interface RecentlyPlayedDao {
	public void addEntry(int userId, int songId);
	public List<Song> getRecentSongs(int userId);
	

}
