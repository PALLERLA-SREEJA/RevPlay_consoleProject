package com.revplay.Dao;

import java.util.List;
import com.revplay.model.Album;

public interface AlbumDao {

    public boolean createAlbum(Album album);
    public List<Album> getAlbumsByArtist(int artistId);
    public boolean updateAlbum(int albumId, String name);
    public boolean deleteAlbum(int albumId);
    public int getAlbumIdByName(String name);
    public List<Album> searchAlbumsByName(String name);

}
