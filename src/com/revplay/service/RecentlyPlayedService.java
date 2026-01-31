package com.revplay.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.revplay.Dao.RecentlyPlayedDao;
import com.revplay.daoImpl.RecentlyPlayedDaoImpl;
import com.revplay.model.Song;

public class RecentlyPlayedService {

    private static final Logger logger = LoggerFactory.getLogger(RecentlyPlayedService.class);

    private RecentlyPlayedDao dao=new RecentlyPlayedDaoImpl();

    public RecentlyPlayedService(RecentlyPlayedDao dao) {
        this.dao = dao;
    }

    //  ADD PLAYED SONG
    public void addPlayedSong(int userId, int songId) {

        logger.info("Adding songId: {} to recently played for userId: {}", songId, userId);

        try {
            dao.addEntry(userId, songId);
        } catch (Exception e) {
            logger.error("Error adding recently played entry. userId: {}, songId: {}", userId, songId, e);
        }
    }

    //  GET RECENT SONGS
    public List<Song> getRecentSongs(int userId) {

        logger.debug("Fetching recently played songs for userId: {}", userId);

        try {
            return dao.getRecentSongs(userId);
        } catch (Exception e) {
            logger.error("Error fetching recently played songs for userId: {}", userId, e);
            return null;
        }
    }
}
