package com.revplay.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.revplay.Dao.ArtistDao;
import com.revplay.daoImpl.ArtistDaoImpl;
import com.revplay.model.Artist;

public class ArtistService {

    private static final Logger logger = LoggerFactory.getLogger(ArtistService.class);

    private ArtistDao artistDao=new ArtistDaoImpl();

    public ArtistService(ArtistDao artistDao) {
        this.artistDao = artistDao;
    }

    //  CREATE PROFILE
    public boolean createProfile(int userId, String bio, String genre, String social) {

        logger.info("Artist profile creation attempt for userId: {}", userId);

        try {
            if (artistDao.profileExists(userId)) {
                logger.warn("Artist profile already exists for userId: {}", userId);
                return false;
            }

            Artist artist = new Artist();
            artist.setUserId(userId);
            artist.setBio(bio);
            artist.setGenre(genre);
            artist.setSocialLinks(social);

            boolean created = artistDao.createProfile(artist);

            if (created)
                logger.info("Artist profile created successfully for userId: {}", userId);
            else
                logger.warn("Artist profile creation failed for userId: {}", userId);

            return created;

        } catch (Exception e) {
            logger.error("Error creating artist profile for userId: {}", userId, e);
            return false;
        }
    }

    //  UPDATE PROFILE
    public boolean updateProfile(int userId, String bio, String genre, String social) {

        logger.info("Updating artist profile for userId: {}", userId);

        try {
            Artist artist = new Artist();
            artist.setUserId(userId);
            artist.setBio(bio);
            artist.setGenre(genre);
            artist.setSocialLinks(social);

            return artistDao.updateProfile(artist);

        } catch (Exception e) {
            logger.error("Error updating artist profile for userId: {}", userId, e);
            return false;
        }
    }

    //  GET PROFILE
    public Artist getProfile(int userId) {

        logger.debug("Fetching artist profile for userId: {}", userId);

        try {
            return artistDao.getArtistByUser(userId);
        } catch (Exception e) {
            logger.error("Error fetching artist profile for userId: {}", userId, e);
            return null;
        }
    }

    //  PROFILE EXISTS
    public boolean profileExists(int userId) {

        logger.debug("Checking artist profile existence for userId: {}", userId);

        try {
            return artistDao.profileExists(userId);
        } catch (Exception e) {
            logger.error("Error checking profile existence for userId: {}", userId, e);
            return false;
        }
    }
    

    public List<Artist> searchArtistsByName(String text) {

        logger.info("Searching artists matching: {}", text);

        try {
            return artistDao.searchArtistsByName(text);
        } catch (Exception e) {
            logger.error("Error searching artists", e);
            return null;
        }
    }

}
