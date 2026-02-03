package com.revplay.Dao;

import java.util.List;

import com.revplay.model.Song;

public interface SongDao {
	public boolean addSong(Song song);
	public List<Song> getAllSongs();
	public List<Song> getSongsByArtist(int artistId);
	public List<Song> getSongsByAlbum(int albumId);
	public List<Song> getSongsByGenre(String genre);
	public List<Song> searchSongsByTitle(String title);
	public List<Song> getSongsByArtistName(String artistName);
	public List<Song> getSongsByAlbumName(String albumName);
	public boolean updateSong(Song song);
	public boolean deleteSong(int songId);
	public int getPlayCount(int songId);
	public void incrementPlayCount(int songId);
	public int getFavoritesCount(int songId);
	public boolean artistExists(String artistName);
	public List<String> searchGenres(String text);


}
