package com.revplay.main;

import java.util.Scanner;

import com.revplay.Dao.UserDao;
import com.revplay.daoImpl.UserDaoImpl;
import com.revplay.model.User;
import com.revplay.service.UserService;
import com.revplay.ui.ArtistMenu;
import com.revplay.ui.UserMenu;
import com.revplay.ui.UserMenuUI;

public class RevPlayApp {

    public static Scanner sc = new Scanner(System.in);

    //  DAO Layer
    private static UserDao userDao=new UserDaoImpl();

    // Service Layer (constructor injection)
    private static UserService userService = new UserService(userDao);

    public static void main(String[] args) {

        while (true) {
            System.out.println("\n===== REVPLAY PORTAL=====");
            System.out.println("1. Create Account");
            System.out.println("2. Login");
            System.out.println("3. Recover Credentials");
            System.out.println("4. Close Appllication");
            System.out.print("Select Option: ");

            String input = sc.nextLine();

            if (input == null || input.trim().isEmpty()) {
                System.out.println("Selection is required.");
                continue;
            }

            int choice;

            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid entry. Please enter digits only(1-4).");
                continue;
            }

            switch (choice) {

                case 1:
                    UserMenu.register();
                    break;

                case 2:
                    User user = UserMenu.login();
                    if (user != null) {
                        if ("ARTIST".equalsIgnoreCase(user.getRole()))
                            ArtistMenu.showMenu(user);
                        else
                            UserMenuUI.showMenu(user);
                    }
                    break;

                case 3:
                    handleForgotPassword();
                    break;

                case 4:
                    System.out.println("Application closed. See you again!");
                    System.exit(0);

                default:
                    System.out.println("Please choose a valid option (1-4).");
            }
        }
    }

    // Separated logic (clean design)
    private static void handleForgotPassword() {
        try {
            System.out.print("Provide your registered Email ID: ");
            String email = sc.nextLine();

            System.out.print("Provide your Account Name: ");
            String username = sc.nextLine();

            if (email.trim().isEmpty() || username.trim().isEmpty()) {
                System.out.println("Input fields must not be blank.");
                return;
            }

            boolean verified = userService.verifyUser(email, username);

            if (!verified) {
                System.out.println("Account not located. Please re-check details");
                return;
            }

            System.out.print("Set New Password: ");
            String newPass = sc.nextLine();

            if (newPass.trim().isEmpty()) {
                System.out.println("Password cannot be blank.");
                return;
            }

            if (userService.resetPassword(email, newPass))
                System.out.println("Credentials updated successfully! You may sign in now.");
            else
                System.out.println("Unable to update credentials");

        } catch (Exception e) {
            System.out.println("Unexpected error occurred. Please try again.");
        }
    }
}
