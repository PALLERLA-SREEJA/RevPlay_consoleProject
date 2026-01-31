package com.revplay.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.revplay.Dao.PlaylistDao;
import com.revplay.daoImpl.PlaylistDaoImpl;
import com.revplay.model.Playlist;
import com.revplay.model.Song;

public class PlaylistService {

    private static final Logger logger = LoggerFactory.getLogger(PlaylistService.class);

    private PlaylistDao dao=new PlaylistDaoImpl();

    public PlaylistService(PlaylistDao dao) {
        this.dao = dao;
    }

    //  CREATE PLAYLIST
    public boolean create(int userId, String name, String desc, String privacy) {

        logger.info("Creating playlist '{}' for userId: {}", name, userId);

        try {
            Playlist p = new Playlist();
            p.setUserId(userId);
            p.setName(name);
            p.setDescription(desc);
            p.setPrivacy(privacy);

            boolean created = dao.createPlaylist(p);

            if (created)
                logger.info("Playlist created successfully: {}", name);
            else
                logger.warn("Playlist creation failed: {}", name);

            return created;

        } catch (Exception e) {
            logger.error("Error creating playlist for userId: {}", userId, e);
            return false;
        }
    }

    //  GET USER PLAYLISTS
    public List<Playlist> getMine(int userId) {
        logger.debug("Fetching playlists for userId: {}", userId);
        return dao.getUserPlaylists(userId);
    }

    //  ADD SONG TO PLAYLIST
    public boolean addSong(int pid, int sid) {

        logger.info("Adding songId: {} to playlistId: {}", sid, pid);

        try {
            return dao.addSong(pid, sid);
        } catch (Exception e) {
            logger.error("Error adding songId: {} to playlistId: {}", sid, pid, e);
            return false;
        }
    }

    //  REMOVE SONG
    public boolean removeSong(int pid, int sid) {

        logger.warn("Removing songId: {} from playlistId: {}", sid, pid);

        try {
            return dao.removeSong(pid, sid);
        } catch (Exception e) {
            logger.error("Error removing songId: {} from playlistId: {}", sid, pid, e);
            return false;
        }
    }

    //  DELETE PLAYLIST
    public boolean delete(int id) {

        logger.warn("Deleting playlistId: {}", id);

        try {
            return dao.deletePlaylist(id);
        } catch (Exception e) {
            logger.error("Error deleting playlistId: {}", id, e);
            return false;
        }
    }

    //  UPDATE PLAYLIST
    public boolean update(int id, String name, String desc, String privacy) {

        logger.info("Updating playlistId: {}", id);

        try {
            return dao.updatePlaylist(id, name, desc, privacy);
        } catch (Exception e) {
            logger.error("Error updating playlistId: {}", id, e);
            return false;
        }
    }

    //  GET SONGS IN PLAYLIST
    public List<Song> getSongsInPlaylist(int playlistId) {

        logger.debug("Fetching songs in playlistId: {}", playlistId);

        try {
            return dao.getSongsInPlaylist(playlistId);
        } catch (Exception e) {
            logger.error("Error fetching songs for playlistId: {}", playlistId, e);
            return null;
        }
    }
    
    public List<Playlist> getPublicPlaylists(int userId) {
        return dao.getPublicPlaylists(userId);
    }
}
