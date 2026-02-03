package com.revplay.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayerService {

    private static final Logger logger = LoggerFactory.getLogger(PlayerService.class);

    private int currentSongId = -1;
    private boolean isPlaying = false;
    private boolean isPaused = false;

    private SongService songService;
    private RecentlyPlayedService recentService;
    private int currentUserId;

    public PlayerService(SongService songService, RecentlyPlayedService recentService) {
        this.songService = songService;
        this.recentService = recentService;
    }

    //  PLAY SONG
    public void playSong(int songId) {

        logger.info("User {} started playing songId: {}", currentUserId, songId);

        isPlaying = true;
        isPaused = false;
        currentSongId = songId;

        System.out.println(" Playing Song ID: " + songId);

        try {
            songService.incrementPlayCount(songId);
            recentService.addPlayedSong(currentUserId, songId);
        } catch (Exception e) {
            logger.error("Error while playing songId: {}", songId, e);
        }
    }

    //  PAUSE SONG
    public void pauseSong() {

        if (!isPlaying) {
            logger.warn("Pause attempted but no song is playing");
            System.out.println("No song is playing.");
            return;
        }

        isPaused = true;
        logger.info("Song paused. songId: {}", currentSongId);
        System.out.println(" Song Paused.");
    }

    //  RESUME SONG
    public void resumeSong() {

        if (isPlaying && isPaused) {
            isPaused = false;
            logger.info("Song resumed. songId: {}", currentSongId);
            System.out.println(" Song Resumed.");
        } else {
            logger.warn("Resume attempted but song not paused");
            System.out.println("Song is not paused.");
        }
    }

    //  STOP SONG
    public void stopSong() {

        logger.info("Song stopped. songId: {}", currentSongId);

        isPlaying = false;
        isPaused = false;
        currentSongId = -1;

        System.out.println(" Song Stopped.");
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setUser(int userId) {
        this.currentUserId = userId;
        logger.debug("PlayerService set for userId: {}", userId);
    }
}
