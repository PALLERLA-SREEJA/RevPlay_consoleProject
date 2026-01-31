package com.revplay.test;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.util.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.revplay.Dao.AlbumDao;
import com.revplay.model.Album;
import com.revplay.service.AlbumService;

public class AlbumChecks {

    @Mock
    private AlbumDao albumDao;

    private AlbumService service;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        service = new AlbumService(albumDao);
    }

    //  CREATE ALBUM 
    @Test
    public void testCreateAlbum() {
        when(albumDao.createAlbum(any(Album.class))).thenReturn(true);

        boolean result = service.createAlbum(1, "Hits", new Date());

        assertTrue(result);
        verify(albumDao).createAlbum(any(Album.class));
    }

    //  VIEW ARTIST ALBUMS 
    @Test
    public void testViewMyAlbums() {
        List<Album> albums = new ArrayList<Album>();
        when(albumDao.getAlbumsByArtist(1)).thenReturn(albums);

        List<Album> result = service.viewMyAlbums(1);

        assertEquals(albums, result);
        verify(albumDao).getAlbumsByArtist(1);
    }

    //  UPDATE ALBUM 
    @Test
    public void testUpdateAlbum() {
        when(albumDao.updateAlbum(5, "NewName")).thenReturn(true);

        boolean result = service.updateAlbum(5, "NewName");

        assertTrue(result);
        verify(albumDao).updateAlbum(5, "NewName");
    }

    //  DELETE ALBUM 
    @Test
    public void testDeleteAlbum() {
        when(albumDao.deleteAlbum(5)).thenReturn(true);

        boolean result = service.deleteAlbum(5);

        assertTrue(result);
        verify(albumDao).deleteAlbum(5);
    }

    //  FIND ALBUM ID BY NAME 
    @Test
    public void testFindAlbumIdByName() {
        when(albumDao.getAlbumIdByName("Hits")).thenReturn(10);

        int id = service.findAlbumIdByName("Hits");

        assertEquals(10, id);
        verify(albumDao).getAlbumIdByName("Hits");
    }
}
