package com.revplay.test;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.util.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.revplay.Dao.RecentlyPlayedDao;
import com.revplay.model.Song;
import com.revplay.service.RecentlyPlayedService;

public class RecentlyPlayedChecks {

    @Mock
    private RecentlyPlayedDao dao;

    private RecentlyPlayedService service;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        service = new RecentlyPlayedService(dao);
    }

    //  ADD PLAYED SONG 
    @Test
    public void testAddPlayedSong() {
        service.addPlayedSong(1, 101);

        verify(dao).addEntry(1, 101);
    }

    //  GET RECENT SONGS 
    @Test
    public void testGetRecentSongs() {
        List<Song> mockSongs = new ArrayList<Song>();
        when(dao.getRecentSongs(1)).thenReturn(mockSongs);

        List<Song> result = service.getRecentSongs(1);

        assertEquals(mockSongs, result);
        verify(dao).getRecentSongs(1);
    }
}
