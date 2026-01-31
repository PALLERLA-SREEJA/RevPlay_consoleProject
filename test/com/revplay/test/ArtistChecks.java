package com.revplay.test;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.revplay.Dao.ArtistDao;
import com.revplay.model.Artist;
import com.revplay.service.ArtistService;

public class ArtistChecks {

    @Mock
    private ArtistDao artistDao;

    private ArtistService service;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        service = new ArtistService(artistDao);
    }

    //  CREATE PROFILE - SUCCESS 
    @Test
    public void testCreateProfileSuccess() {

        when(artistDao.profileExists(1)).thenReturn(false);
        when(artistDao.createProfile(any(Artist.class))).thenReturn(true);

        boolean result = service.createProfile(1, "Bio", "Pop", "insta");

        assertTrue(result);
        verify(artistDao).profileExists(1);
        verify(artistDao).createProfile(any(Artist.class));
    }

    //  CREATE PROFILE - ALREADY EXISTS 
    @Test
    public void testCreateProfileAlreadyExists() {

        when(artistDao.profileExists(1)).thenReturn(true);

        boolean result = service.createProfile(1, "Bio", "Pop", "insta");

        assertFalse(result);
        verify(artistDao).profileExists(1);
        verify(artistDao, never()).createProfile(any(Artist.class));
    }

    //  UPDATE PROFILE 
    @Test
    public void testUpdateProfile() {

        when(artistDao.updateProfile(any(Artist.class))).thenReturn(true);

        boolean result = service.updateProfile(1, "NewBio", "Rock", "yt");

        assertTrue(result);
        verify(artistDao).updateProfile(any(Artist.class));
    }

    //  GET PROFILE 
    @Test
    public void testGetProfile() {

        Artist artist = new Artist();
        when(artistDao.getArtistByUser(1)).thenReturn(artist);

        Artist result = service.getProfile(1);

        assertEquals(artist, result);
        verify(artistDao).getArtistByUser(1);
    }

    //  PROFILE EXISTS 
    @Test
    public void testProfileExists() {

        when(artistDao.profileExists(1)).thenReturn(true);

        boolean result = service.profileExists(1);

        assertTrue(result);
        verify(artistDao).profileExists(1);
    }
}
