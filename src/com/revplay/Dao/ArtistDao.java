package com.revplay.Dao;

import com.revplay.model.Artist;

public interface ArtistDao {
	public boolean profileExists(int userId);
	public boolean createProfile(Artist artist);
	public boolean updateProfile(Artist artist);
	public Artist getArtistByUser(int userId);
	

}
