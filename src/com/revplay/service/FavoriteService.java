package com.revplay.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.revplay.Dao.FavoriteDao;
import com.revplay.daoImpl.FavoriteDaoImpl;
import com.revplay.model.Song;

public class FavoriteService {

    private static final Logger logger = LoggerFactory.getLogger(FavoriteService.class);

    private FavoriteDao favoriteDao=new FavoriteDaoImpl();

    public FavoriteService(FavoriteDao favoriteDao) {
        this.favoriteDao = favoriteDao;
    }

    //  ADD FAVORITE
    public boolean add(int userId, int songId) {

        logger.info("User {} adding songId {} to favorites", userId, songId);

        try {
            boolean added = favoriteDao.addFavorite(userId, songId);

            if (!added)
                logger.warn("Favorite add failed. userId: {}, songId: {}", userId, songId);

            return added;

        } catch (Exception e) {
            logger.error("Error adding favorite. userId: {}, songId: {}", userId, songId, e);
            return false;
        }
    }

    //  REMOVE FAVORITE
    public boolean remove(int userId, int songId) {

        logger.warn("User {} removing songId {} from favorites", userId, songId);

        try {
            return favoriteDao.removeFavorite(userId, songId);
        } catch (Exception e) {
            logger.error("Error removing favorite. userId: {}, songId: {}", userId, songId, e);
            return false;
        }
    }

    //  GET FAVORITES
    public List<Song> getFavorites(int userId) {

        logger.debug("Fetching favorites for userId: {}", userId);

        try {
            return favoriteDao.getUserFavorites(userId);
        } catch (Exception e) {
            logger.error("Error fetching favorites for userId: {}", userId, e);
            return null;
        }
    }
}
