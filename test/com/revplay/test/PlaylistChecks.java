package com.revplay.test;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.util.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.revplay.Dao.PlaylistDao;
import com.revplay.model.Playlist;
import com.revplay.model.Song;
import com.revplay.service.PlaylistService;

public class PlaylistChecks {

    @Mock
    private PlaylistDao dao;

    private PlaylistService service;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        service = new PlaylistService(dao);
    }

    //  CREATE PLAYLIST 
    @Test
    public void testCreatePlaylist() {
        when(dao.createPlaylist(any(Playlist.class))).thenReturn(true);

        boolean result = service.create(1, "MyHits", "Fav songs", "PUBLIC");

        assertTrue(result);
        verify(dao).createPlaylist(any(Playlist.class));
    }

    //  GET USER PLAYLISTS 
    @Test
    public void testGetMine() {
        List<Playlist> list = new ArrayList<Playlist>();
        when(dao.getUserPlaylists(1)).thenReturn(list);

        List<Playlist> result = service.getMine(1);

        assertEquals(list, result);
        verify(dao).getUserPlaylists(1);
    }

    //  ADD SONG 
    @Test
    public void testAddSong() {
        when(dao.addSong(10, 20)).thenReturn(true);

        boolean result = service.addSong(10, 20);

        assertTrue(result);
        verify(dao).addSong(10, 20);
    }

    //  REMOVE SONG 
    @Test
    public void testRemoveSong() {
        when(dao.removeSong(10, 20)).thenReturn(true);

        boolean result = service.removeSong(10, 20);

        assertTrue(result);
        verify(dao).removeSong(10, 20);
    }

    //  DELETE PLAYLIST 
    @Test
    public void testDeletePlaylist() {
        when(dao.deletePlaylist(5)).thenReturn(true);

        boolean result = service.delete(5);

        assertTrue(result);
        verify(dao).deletePlaylist(5);
    }

    //  UPDATE PLAYLIST 
    @Test
    public void testUpdatePlaylist() {
        when(dao.updatePlaylist(5, "New", "Desc", "PRIVATE")).thenReturn(true);

        boolean result = service.update(5, "New", "Desc", "PRIVATE");

        assertTrue(result);
        verify(dao).updatePlaylist(5, "New", "Desc", "PRIVATE");
    }

    //  GET SONGS IN PLAYLIST 
    @Test
    public void testGetSongsInPlaylist() {
        List<Song> songs = new ArrayList<Song>();
        when(dao.getSongsInPlaylist(5)).thenReturn(songs);

        List<Song> result = service.getSongsInPlaylist(5);

        assertEquals(songs, result);
        verify(dao).getSongsInPlaylist(5);
    }
}
