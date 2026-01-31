package com.revplay.test;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.revplay.Dao.UserDao;
import com.revplay.model.User;
import com.revplay.service.UserService;

public class UserChecks {

    @Mock
    private UserDao userDao;

    private UserService userService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        userService = new UserService(userDao);
    }

    //  TEST REGISTER
    @Test
    public void testRegisterSuccess() {
        when(userDao.registerUser(any(User.class))).thenReturn(true);

        boolean result = userService.register("sreejs", "sreejs@mail.com", "1234", "admin");

        assertTrue(result);
        verify(userDao).registerUser(any(User.class));
    }

    //  TEST LOGIN
    @Test
    public void testLoginSuccess() {
        User mockUser = new User();
        mockUser.setUsername("sreejs");

        when(userDao.login("sreejs", "1234")).thenReturn(mockUser);

        User result = userService.login("sreejs", "1234");

        assertNotNull(result);
        assertEquals("sreejs", result.getUsername());
        verify(userDao).login("sreejs", "1234");
    }

    //  TEST VERIFY USER
    @Test
    public void testVerifyUser() {
        when(userDao.verifyUser("mail@test.com", "sreejs")).thenReturn(true);

        boolean result = userService.verifyUser("mail@test.com", "sreejs");

        assertTrue(result);
        verify(userDao).verifyUser("mail@test.com", "sreejs");
    }

    //  TEST RESET PASSWORD
    @Test
    public void testResetPassword() {
        when(userDao.updatePassword("mail@test.com", "newpass")).thenReturn(true);

        boolean result = userService.resetPassword("mail@test.com", "newpass");

        assertTrue(result);
        verify(userDao).updatePassword("mail@test.com", "newpass");
    }
}
