package com.revplay.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.revplay.Dao.UserDao;
import com.revplay.daoImpl.UserDaoImpl;
import com.revplay.model.User;

public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private UserDao userDao=new UserDaoImpl();

    // Constructor Injection
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    //  REGISTER
    public boolean register(String username, String email, String password, String role) {

        logger.info("Registration attempt for username: {}, email: {}", username, email);

        try {
            User user = new User();
            user.setUsername(username);
            user.setEmail(email);
            user.setPassword(password);
            user.setRole(role.trim().toUpperCase());

            boolean result = userDao.registerUser(user);

            if (result)
                logger.info("User registered successfully: {}", username);
            else
                logger.warn("Registration failed for user: {}", username);

            return result;

        } catch (Exception e) {
            logger.error("Error during registration for user: {}", username, e);
            return false;
        }
    }

    //  LOGIN
    public User login(String input, String password) {

        logger.info("Login attempt for input: {}", input);

        try {
            User user = userDao.login(input, password);

            if (user != null)
                logger.info("Login successful for user: {}", user.getUsername());
            else
                logger.warn("Login failed for input: {}", input);

            return user;

        } catch (Exception e) {
            logger.error("Login error for input: {}", input, e);
            return null;
        }
    }

    //  VERIFY USER
    public boolean verifyUser(String email, String username) {

        logger.info("Verifying user: {} with email: {}", username, email);

        try {
            boolean exists = userDao.verifyUser(email, username);

            if (!exists)
                logger.warn("User verification failed: {}", username);

            return exists;

        } catch (Exception e) {
            logger.error("Error verifying user: {}", username, e);
            return false;
        }
    }

    //  RESET PASSWORD
    public boolean resetPassword(String email, String newPassword) {

        logger.info("Password reset attempt for email: {}", email);

        try {
            boolean updated = userDao.updatePassword(email, newPassword);

            if (updated)
                logger.info("Password reset successful for email: {}", email);
            else
                logger.warn("Password reset failed for email: {}", email);

            return updated;

        } catch (Exception e) {
            logger.error("Error resetting password for email: {}", email, e);
            return false;
        }
    }
}
