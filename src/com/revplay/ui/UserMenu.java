package com.revplay.ui;

import com.revplay.Dao.UserDao;
import com.revplay.daoImpl.UserDaoImpl;
import com.revplay.main.RevPlayApp;
import com.revplay.model.User;


import com.revplay.service.UserService;

public class UserMenu {

    //  DAO layer
    private static UserDao userDao=new UserDaoImpl();

    //  Service layer (constructor injection)
    private static UserService userService = new UserService(userDao);

    public static void register() {

        System.out.print("Enter Username: ");
        String username = RevPlayApp.sc.nextLine();

        System.out.print("Enter Gmail: ");
        String email = RevPlayApp.sc.nextLine();

        if (!email.endsWith("@gmail.com")) {
            System.out.println("Invalid Gmail format!");
            return;
        }

        System.out.print("Enter Password: ");
        String password = RevPlayApp.sc.nextLine();

        System.out.print("Enter Role (USER/ARTIST): ");
        String role = RevPlayApp.sc.nextLine();

        boolean success = userService.register(username, email, password, role);

        if (success)
            System.out.println("Registration successful!");
        else
            System.out.println("Registration failed!");
    }

    public static User login() {

        System.out.print("Enter Email or Username: ");
        String input = RevPlayApp.sc.nextLine();

        System.out.print("Enter Password: ");
        String password = RevPlayApp.sc.nextLine();

        User user = userService.login(input, password);

        if (user != null) {
            System.out.println("Login successful!");
            return user;
        } else {
            System.out.println("Invalid credentials!");
            return null;
        }
    }
}
