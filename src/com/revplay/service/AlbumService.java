package com.revplay.service;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.revplay.Dao.AlbumDao;

import com.revplay.model.Album;

public class AlbumService {

    private static final Logger logger = LoggerFactory.getLogger(AlbumService.class);

    private AlbumDao albumDao;

    public AlbumService(AlbumDao albumDao) {
        this.albumDao = albumDao;
    }


    //  CREATE ALBUM
    public boolean createAlbum(int artistId, String name, Date releaseDate) {

        logger.info("Creating album '{}' for artistId: {}", name, artistId);

        try {
            Album album = new Album();
            album.setArtistId(artistId);
            album.setAlbumName(name);
            album.setReleaseDate(releaseDate);

            boolean created = albumDao.createAlbum(album);

            if (created)
                logger.info("Album created successfully: {}", name);
            else
                logger.warn("Album creation failed: {}", name);

            return created;

        } catch (Exception e) {
            logger.error("Error creating album '{}' for artistId: {}", name, artistId, e);
            return false;
        }
    }

    //  VIEW ARTIST ALBUMS
    public List<Album> viewMyAlbums(int artistId) {

        logger.debug("Fetching albums for artistId: {}", artistId);

        try {
            return albumDao.getAlbumsByArtist(artistId);
        } catch (Exception e) {
            logger.error("Error fetching albums for artistId: {}", artistId, e);
            return null;
        }
    }

    //  UPDATE ALBUM
    public boolean updateAlbum(int albumId, String name) {

        logger.info("Updating albumId: {}", albumId);

        try {
            return albumDao.updateAlbum(albumId, name);
        } catch (Exception e) {
            logger.error("Error updating albumId: {}", albumId, e);
            return false;
        }
    }

    //  DELETE ALBUM
    public boolean deleteAlbum(int albumId) {

        logger.warn("Deleting albumId: {}", albumId);

        try {
            return albumDao.deleteAlbum(albumId);
        } catch (Exception e) {
            logger.error("Error deleting albumId: {}", albumId, e);
            return false;
        }
    }

    //  FIND ALBUM ID BY NAME
    public int findAlbumIdByName(String name) {

        logger.debug("Finding album ID for album name: {}", name);

        try {
            return albumDao.getAlbumIdByName(name);
        } catch (Exception e) {
            logger.error("Error finding albumId for name: {}", name, e);
            return -1;
        }
    }
    
 //  SEARCH ALBUMS BY NAME (for Browse by Album feature)
    public List<Album> searchAlbumsByName(String name) {

        logger.debug("Searching albums by name: {}", name);

        try {
            return albumDao.searchAlbumsByName(name);
        } catch (Exception e) {
            logger.error("Error searching albums by name: {}", name, e);
            return null;
        }
    }
    


}
