package com.revplay.Dao;

import java.util.List;

import com.revplay.model.Playlist;
import com.revplay.model.Song;

public interface PlaylistDao {
	public boolean createPlaylist(Playlist p);
	public List<Playlist> getUserPlaylists(int userId);
	public boolean addSong(int playlistId, int songId);
	public boolean removeSong(int playlistId, int songId);
	public boolean deletePlaylist(int id);
	public boolean updatePlaylist(int id, String name, String desc, String privacy);
	public List<Song> getSongsInPlaylist(int playlistId);
	public List<Playlist> getPublicPlaylists(int currentUserId);
	
}
