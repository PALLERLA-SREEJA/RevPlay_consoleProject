package com.revplay.test;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.util.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.revplay.Dao.FavoriteDao;
import com.revplay.model.Song;
import com.revplay.service.FavoriteService;

public class FavoriteChecks {

    @Mock
    private FavoriteDao favoriteDao;

    private FavoriteService service;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        service = new FavoriteService(favoriteDao);
    }

    //  ADD FAVORITE 
    @Test
    public void testAddFavorite() {
        when(favoriteDao.addFavorite(1, 10)).thenReturn(true);

        boolean result = service.add(1, 10);

        assertTrue(result);
        verify(favoriteDao).addFavorite(1, 10);
    }

    //  REMOVE FAVORITE 
    @Test
    public void testRemoveFavorite() {
        when(favoriteDao.removeFavorite(1, 10)).thenReturn(true);

        boolean result = service.remove(1, 10);

        assertTrue(result);
        verify(favoriteDao).removeFavorite(1, 10);
    }

    //  GET FAVORITES 
    @Test
    public void testGetFavorites() {
        List<Song> songs = new ArrayList<Song>();
        when(favoriteDao.getUserFavorites(1)).thenReturn(songs);

        List<Song> result = service.getFavorites(1);

        assertEquals(songs, result);
        verify(favoriteDao).getUserFavorites(1);
    }
}
