package com.revplay.test;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.util.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.revplay.Dao.SongDao;
import com.revplay.model.Song;
import com.revplay.service.SongService;

public class SongChecks {

    @Mock
    private SongDao songDao;

    private SongService songService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        songService = new SongService(songDao);
    }

    //  UPLOAD SONG 
    @Test
    public void testUploadSong() {
        when(songDao.addSong(any(Song.class))).thenReturn(true);

        boolean result = songService.uploadSong("Shape", "Pop", 3.5,
                new Date(), 1, 10);

        assertTrue(result);
        verify(songDao).addSong(any(Song.class));
    }

    //  VIEW ARTIST SONGS 
    @Test
    public void testViewMySongs() {
        List<Song> mockList = new ArrayList<Song>();
        when(songDao.getSongsByArtist(1)).thenReturn(mockList);

        List<Song> result = songService.viewMySongs(1);

        assertEquals(mockList, result);
        verify(songDao).getSongsByArtist(1);
    }

    //  UPDATE SONG 
    @Test
    public void testUpdateSong() {
        when(songDao.updateSong(any(Song.class))).thenReturn(true);

        boolean result = songService.updateSong(5, "NewTitle", "Rock", 4.0, new Date());

        assertTrue(result);
        verify(songDao).updateSong(any(Song.class));
    }

    //  DELETE SONG 
    @Test
    public void testDeleteSong() {
        when(songDao.deleteSong(5)).thenReturn(true);

        boolean result = songService.deleteSong(5);

        assertTrue(result);
        verify(songDao).deleteSong(5);
    }

    //  PLAY COUNT 
    @Test
    public void testGetPlayCount() {
        when(songDao.getPlayCount(2)).thenReturn(100);

        int count = songService.getPlayCount(2);

        assertEquals(100, count);
        verify(songDao).getPlayCount(2);
    }

    @Test
    public void testIncrementPlayCount() {
        songService.incrementPlayCount(2);
        verify(songDao).incrementPlayCount(2);
    }

    //  FAVORITES 
    @Test
    public void testGetFavoritesCount() {
        when(songDao.getFavoritesCount(3)).thenReturn(50);

        int count = songService.getFavoritesCount(3);

        assertEquals(50, count);
        verify(songDao).getFavoritesCount(3);
    }

    //  VIEW ALL SONGS 
    @Test
    public void testViewAllSongs() {
        List<Song> list = new ArrayList<Song>();
        when(songDao.getAllSongs()).thenReturn(list);

        List<Song> result = songService.viewAllSongs();

        assertEquals(list, result);
        verify(songDao).getAllSongs();
    }

    //  SONG EXISTS (Business Logic) 
    @Test
    public void testSongExistsTrue() {
        Song s = new Song();
        s.setSongId(10);

        List<Song> list = new ArrayList<Song>();
        list.add(s);

        when(songDao.getAllSongs()).thenReturn(list);

        boolean exists = songService.songExists(10);

        assertTrue(exists);
    }

    @Test
    public void testSongExistsFalse() {
        List<Song> list = new ArrayList<Song>();
        when(songDao.getAllSongs()).thenReturn(list);

        boolean exists = songService.songExists(99);

        assertFalse(exists);
    }

    //  BROWSING FEATURES 
    @Test
    public void testGetSongsByGenre() {
        when(songDao.getSongsByGenre("Pop")).thenReturn(new ArrayList<Song>());

        songService.getSongsByGenre("Pop");

        verify(songDao).getSongsByGenre("Pop");
    }

    @Test
    public void testSearchSongsByTitle() {
        when(songDao.searchSongsByTitle("Love")).thenReturn(new ArrayList<Song>());

        songService.searchSongsByTitle("Love");

        verify(songDao).searchSongsByTitle("Love");
    }
}
