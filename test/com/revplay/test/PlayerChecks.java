package com.revplay.test;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.revplay.service.PlayerService;
import com.revplay.service.RecentlyPlayedService;
import com.revplay.service.SongService;

public class PlayerChecks {

    @Mock
    private SongService songService;

    @Mock
    private RecentlyPlayedService recentService;

    private PlayerService playerService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        playerService = new PlayerService(songService, recentService);
        playerService.setUser(1); // set current user
    }

    //  PLAY SONG 
    @Test
    public void testPlaySong() {
        playerService.playSong(10);

        assertTrue(playerService.isPlaying());
        verify(songService).incrementPlayCount(10);
        verify(recentService).addPlayedSong(1, 10);
    }

    //  PAUSE SONG 
    @Test
    public void testPauseSong() {
        playerService.playSong(10);  // must be playing first
        playerService.pauseSong();

        // state test
        playerService.resumeSong(); // should work (means pause happened)
        verify(songService).incrementPlayCount(10);
    }

    //  RESUME SONG 
    @Test
    public void testResumeSongWhenPaused() {
        playerService.playSong(10);
        playerService.pauseSong();
        playerService.resumeSong();

        assertTrue(playerService.isPlaying());
    }

    //  STOP SONG 
    @Test
    public void testStopSong() {
        playerService.playSong(10);
        playerService.stopSong();

        assertFalse(playerService.isPlaying());
    }

    //  PAUSE WITHOUT PLAY 
    @Test
    public void testPauseWithoutPlaying() {
        playerService.pauseSong();

        assertFalse(playerService.isPlaying());
        verifyZeroInteractions(songService);
    }
}
