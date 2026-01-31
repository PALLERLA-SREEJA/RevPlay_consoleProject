package com.revplay.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.revplay.Dao.SongDao;
import com.revplay.daoImpl.SongDaoImpl;
import com.revplay.model.Song;

public class SongService {

    private static final Logger logger = LoggerFactory.getLogger(SongService.class);

    private SongDao songDao= new SongDaoImpl();

    public SongService(SongDao songDao) {
        this.songDao = songDao;
    }

    // ================= UPLOAD SONG =================
    public boolean uploadSong(String title, String genre, double duration,
                              Date releaseDate, int artistId, int albumId) {

        logger.info("Uploading song: {} by artistId: {}", title, artistId);

        try {
            Song song = new Song();
            song.setTitle(title);
            song.setGenre(genre);
            song.setDuration(duration);
            song.setReleaseDate(releaseDate);
            song.setArtistId(artistId);
            song.setAlbumId(albumId);

            boolean result = songDao.addSong(song);

            if (result)
                logger.info("Song uploaded successfully: {}", title);
            else
                logger.warn("Song upload failed: {}", title);

            return result;

        } catch (Exception e) {
            logger.error("Error uploading song: {}", title, e);
            return false;
        }
    }

    // ================= ARTIST SONGS =================
    public List<Song> viewMySongs(int artistId) {
        logger.debug("Fetching songs for artistId: {}", artistId);
        return songDao.getSongsByArtist(artistId);
    }

    // ================= ALBUM SONGS =================
    public List<Song> viewSongsByAlbum(int albumId) {
        logger.debug("Fetching songs for albumId: {}", albumId);
        return songDao.getSongsByAlbum(albumId);
    }

    public List<Song> getSongsByAlbumId(int albumId) {
        logger.debug("Fetching songs by albumId (search): {}", albumId);
        return songDao.getSongsByAlbum(albumId);
    }

    // ================= UPDATE SONG =================
    public boolean updateSong(int songId, String title, String genre,
                              double dur, Date date) {

        logger.info("Updating songId: {}", songId);

        try {
            Song s = new Song();
            s.setSongId(songId);
            s.setTitle(title);
            s.setGenre(genre);
            s.setDuration(dur);
            s.setReleaseDate(date);

            boolean updated = songDao.updateSong(s);

            if (updated)
                logger.info("Song updated successfully: {}", songId);
            else
                logger.warn("Song update failed: {}", songId);

            return updated;

        } catch (Exception e) {
            logger.error("Error updating songId: {}", songId, e);
            return false;
        }
    }

    // ================= DELETE SONG =================
    public boolean deleteSong(int songId) {
        logger.warn("Deleting songId: {}", songId);
        return songDao.deleteSong(songId);
    }

    // ================= PLAY COUNT =================
    public int getPlayCount(int songId) {
        logger.debug("Fetching play count for songId: {}", songId);
        return songDao.getPlayCount(songId);
    }

    public void incrementPlayCount(int songId) {
        logger.info("Incrementing play count for songId: {}", songId);
        songDao.incrementPlayCount(songId);
    }

    // ================= FAVORITES COUNT =================
    public int getFavoritesCount(int songId) {
        logger.debug("Fetching favorites count for songId: {}", songId);
        return songDao.getFavoritesCount(songId);
    }

    // ================= ALL SONGS =================
    public List<Song> viewAllSongs() {
        logger.debug("Fetching all songs");
        return songDao.getAllSongs();
    }

    // ================= SONG EXISTS =================
    public boolean songExists(int songId) {
        logger.debug("Checking if song exists: {}", songId);

        List<Song> songs = songDao.getAllSongs();
        for (Song s : songs) {
            if (s.getSongId() == songId) {
                logger.info("Song exists: {}", songId);
                return true;
            }
        }

        logger.warn("Song does not exist: {}", songId);
        return false;
    }

    // ================= BROWSING FEATURES =================
    public List<Song> getSongsByGenre(String genre) {
        logger.info("Browsing songs by genre: {}", genre);
        return songDao.getSongsByGenre(genre);
    }

    public List<Song> getSongsByArtistName(String name) {

        if (!songDao.artistExists(name)) {
            System.out.println("No artist found with that name.");
            return new ArrayList<Song>();
        }

        List<Song> songs = songDao.getSongsByArtistName(name);

        if (songs.isEmpty()) {
            System.out.println("Artist exists but has no songs.");
        }

        return songs;
    }


    public List<Song> getSongsByAlbumName(String albumName) {
        logger.info("Browsing songs by album name: {}", albumName);
        return songDao.getSongsByAlbumName(albumName);
    }

    public List<Song> searchSongsByTitle(String title) {
        logger.info("Searching songs by title: {}", title);
        return songDao.searchSongsByTitle(title);
    }
}
