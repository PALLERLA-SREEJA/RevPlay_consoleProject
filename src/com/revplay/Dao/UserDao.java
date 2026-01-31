package com.revplay.Dao;

import com.revplay.model.User;

public interface UserDao {
	 public boolean registerUser(User user);
	 public User login(String input, String password);
	 public boolean verifyUser(String email, String username);
	 public boolean updatePassword(String email, String newPassword);

}
