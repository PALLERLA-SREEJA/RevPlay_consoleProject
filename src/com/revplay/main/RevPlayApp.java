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

    // 🔹 DAO Layer
    private static UserDao userDao=new UserDaoImpl();

    // 🔹 Service Layer (constructor injection)
    private static UserService userService = new UserService(userDao);

    public static void main(String[] args) {

        while (true) {
            System.out.println("\n===== REVPLAY =====");
            System.out.println("1. create account");
            System.out.println("2. Login");
            System.out.println("3. Recover credentials");
            System.out.println("4. Exit");
            System.out.print("Choice: ");

            String input = sc.nextLine();

            if (input == null || input.trim().isEmpty()) {
                System.out.println("Please enter a choice.");
                continue;
            }

            int choice;

            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Numbers only (1-4).");
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
                    System.out.println("Exiting RevPlay. Goodbye!");
                    System.exit(0);

                default:
                    System.out.println("Invalid choice. Select 1-4.");
            }
        }
    }

    // 🔹 Separated logic (clean design)
    private static void handleForgotPassword() {
        try {
            System.out.print("Enter your registered Email: ");
            String email = sc.nextLine();

            System.out.print("Enter your Username: ");
            String username = sc.nextLine();

            if (email.trim().isEmpty() || username.trim().isEmpty()) {
                System.out.println("Fields cannot be empty.");
                return;
            }

            boolean verified = userService.verifyUser(email, username);

            if (!verified) {
                System.out.println("User not found. Check email/username.");
                return;
            }

            System.out.print("Enter New Password: ");
            String newPass = sc.nextLine();

            if (newPass.trim().isEmpty()) {
                System.out.println("Password cannot be empty.");
                return;
            }

            if (userService.resetPassword(email, newPass))
                System.out.println("Password reset successful! You can login now.");
            else
                System.out.println("Password reset failed.");

        } catch (Exception e) {
            System.out.println("Something went wrong. Try again.");
        }
    }
}
