package com.revplay.Dao;

import java.util.List;

import com.revplay.model.Song;

public interface FavoriteDao {
	public boolean addFavorite(int userId, int songId);
	public boolean removeFavorite(int userId, int songId);
	public List<Song> getUserFavorites(int userId);

}
