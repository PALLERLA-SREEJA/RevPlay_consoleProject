package com.revplay.ui;


import com.revplay.Dao.AlbumDao;
import com.revplay.Dao.ArtistDao;
import com.revplay.Dao.SongDao;
import com.revplay.daoImpl.AlbumDaoImpl;
import com.revplay.daoImpl.ArtistDaoImpl;
import com.revplay.daoImpl.SongDaoImpl;
import com.revplay.main.RevPlayApp;
import com.revplay.model.Album;
import com.revplay.model.Song;
import com.revplay.model.User;
import com.revplay.service.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ArtistMenu {

	//  DAO Layer
    private static ArtistDao artistDao=new ArtistDaoImpl();
    private static AlbumDao albumDao=new AlbumDaoImpl();
    private static SongDao songDao=new SongDaoImpl();

    //  Service Layer (constructor injection)
    private static ArtistService artistService = new ArtistService(artistDao);
    private static AlbumService albumService = new AlbumService(albumDao);
    private static SongService songService = new SongService(songDao);
    public static void showMenu(User user) {

        while (true) {
            System.out.println("\n=== ARTIST MENU ===");
            System.out.println("1. Create Artist Profile");
            System.out.println("2. Update Profile");
            System.out.println("3. Create Album");
            System.out.println("4. Add Song to Album");
            System.out.println("5. View My Songs");
            System.out.println("6. View My Albums");
            System.out.println("7. View Songs in Album");
            System.out.println("8. Update Song");
            System.out.println("9. Update Album");
            System.out.println("10. Delete Song");
            System.out.println("11. Delete Album");
            System.out.println("12. View Play Count");
            System.out.println("13. View Favorites Count");
            System.out.println("14. Logout");

            // âœ… SAFE INPUT HANDLING
            String input = RevPlayApp.sc.nextLine();

            if (input == null || input.trim().isEmpty()) {
                System.out.println("Please enter a choice.");
                continue;
            }

            int choice;

            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid choice. Enter numbers only.");
                continue;
            }

            switch (choice) {
                case 1: createProfile(user); break;
                case 2: updateProfile(user); break;
                case 3: createAlbum(user); break;
                case 4: addSong(user); break;
                case 5: viewSongs(user); break;
                case 6: viewAlbums(user); break;
                case 7: viewSongsInAlbum(user); break;
                case 8: updateSong(user); break;
                case 9: updateAlbum(user); break;
                case 10: deleteSong(); break;
                case 11: deleteAlbum(); break;
                case 12: showPlayCount(user); break;
                case 13: showFavoritesCount(user); break;
                case 14: return;
                default: System.out.println("Invalid choice. Select between 1-14.");
            }
        }
    }


    private static void createProfile(User user) {
        if (artistService.profileExists(user.getUserId())) {
            System.out.println("Profile already exists! Use Update.");
            return;
        }

        System.out.print("Bio: ");
        String bio = RevPlayApp.sc.nextLine();
        System.out.print("Genre: ");
        String genre = RevPlayApp.sc.nextLine();
        System.out.print("Social Links: ");
        String social = RevPlayApp.sc.nextLine();

        artistService.createProfile(user.getUserId(), bio, genre, social);
        System.out.println("Profile created!");
    }

    private static void updateProfile(User user) {
        System.out.print("New Bio: ");
        String bio = RevPlayApp.sc.nextLine();
        System.out.print("New Genre: ");
        String genre = RevPlayApp.sc.nextLine();
        System.out.print("New Social Links: ");
        String social = RevPlayApp.sc.nextLine();

        artistService.updateProfile(user.getUserId(), bio, genre, social);
        System.out.println("Profile updated!");
    }

    private static void createAlbum(User user) {
        try {
            System.out.print("Album Name: ");
            String name = RevPlayApp.sc.nextLine();

            // âœ… Strict date validation
            System.out.print("Release Date (yyyy-MM-dd): ");
            String dateInput = RevPlayApp.sc.nextLine();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false);   // â­� makes date strict

            Date date;
            try {
                date = sdf.parse(dateInput);
            } catch (Exception e) {
                System.out.println("â�Œ Invalid date format! Use yyyy-MM-dd");
                return;   // STOP here
            }

            if (albumService.createAlbum(user.getUserId(), name, date))
                System.out.println("Album created!");
            else
                System.out.println("Album creation failed!");

        } catch (Exception e) {
            System.out.println("Invalid input.");
        }
    }


    private static void addSong(User user) {
        try {
            // Get only albums that belong to this artist
            List<Album> albums = albumService.viewMyAlbums(user.getUserId());

            if (albums.isEmpty()) {
                System.out.println("â�Œ No albums found. Create album first.");
                return;
            }

            // Show valid albums
            for (Album a : albums) {
                System.out.println(a.getAlbumId() + " - " + a.getAlbumName());
            }

            System.out.print("Album ID: ");
            int albumId = Integer.parseInt(RevPlayApp.sc.nextLine());

            // ðŸ”� Validate album belongs to this artist
            boolean validAlbum = false;
            for (Album a : albums) {
                if (a.getAlbumId() == albumId) {
                    validAlbum = true;
                    break;
                }
            }

            if (!validAlbum) {
                System.out.println("â�Œ Invalid Album ID.");
                return;
            }

            System.out.print("Title: ");
            String title = RevPlayApp.sc.nextLine();

            System.out.print("Genre: ");
            String genre = RevPlayApp.sc.nextLine();

            // â�Œ Validate genre
            if (!genre.matches("^[a-zA-Z ,]+$")) {
                System.out.println("â�Œ Genre should contain only letters and commas.");
                return;
            }
            
            System.out.print("Duration: ");
            double dur = Double.parseDouble(RevPlayApp.sc.nextLine());

            // âœ… STRICT DATE VALIDATION
            System.out.print("Release Date (yyyy-MM-dd): ");
            String dateInput = RevPlayApp.sc.nextLine();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false);   // â­� prevents invalid dates

            Date date;
            try {
                date = sdf.parse(dateInput);
            } catch (Exception e) {
                System.out.println("â�Œ Invalid date format! Use yyyy-MM-dd");
                return;   // stop upload
            }

            boolean ok = songService.uploadSong(title, genre, dur, date, user.getUserId(), albumId);

            if (ok)
                System.out.println("Song uploaded successfully!");
            else
                System.out.println("Song upload failed!");

        } catch (NumberFormatException e) {
            System.out.println("â�Œ Duration and Album ID must be numbers.");
        } catch (Exception e) {
            System.out.println("Invalid input.");
        }
    }


    private static void viewSongs(User user) {

        List<Song> songs = songService.viewMySongs(user.getUserId());

        if (songs == null || songs.isEmpty()) {
            System.out.println("No Songs Available.");
            return;
        }

        System.out.println("\n--- My Songs ---");
        for (Song s : songs) {
            System.out.println(s.getSongId() + " - " + s.getTitle());
        }
    }

    private static void viewAlbums(User user) {

        List<Album> albums = albumService.viewMyAlbums(user.getUserId());

        if (albums == null || albums.isEmpty()) {
            System.out.println("No albums created yet.");
            return;
        }

        System.out.println("\n--- My Albums ---");
        for (Album a : albums) {
            System.out.println(a.getAlbumId() + " - " + a.getAlbumName());
        }
    }


    private static void viewSongsInAlbum(User user) {

        List<Album> albums = albumService.viewMyAlbums(user.getUserId());

        if (albums.isEmpty()) {
            System.out.println("No albums found.");
            return;
        }

        for (Album a : albums) {
            System.out.println(a.getAlbumId() + " - " + a.getAlbumName());
        }

        System.out.print("Album ID: ");
        int albumId = Integer.parseInt(RevPlayApp.sc.nextLine());

        boolean validAlbum = false;
        for (Album a : albums) {
            if (a.getAlbumId() == albumId) {
                validAlbum = true;
                break;
            }
        }

        if (!validAlbum) {
            System.out.println("Invalid Album ID.");
            return;
        }

        List<Song> songs = songService.viewSongsByAlbum(albumId);

        if (songs.isEmpty()) {
            System.out.println("No songs in this album.");
            return;
        }

        for (Song s : songs) {
            System.out.println(s.getSongId() + " - " + s.getTitle());
        }
    }


    private static void updateSong(User user) {
        try {
            // ðŸ”¹ Get songs list
            List<Song> songs = songService.viewMySongs(user.getUserId());

            // ðŸ”´ STOP if no songs
            if (songs == null || songs.isEmpty()) {
                System.out.println("No Songs Available to update.");
                return;
            }

            // ðŸ”¹ Show songs
            System.out.println("\n--- My Songs ---");
            for (Song s : songs) {
                System.out.println(s.getSongId() + " - " + s.getTitle());
            }

            System.out.print("Song ID: ");
            int id = Integer.parseInt(RevPlayApp.sc.nextLine());

            // ðŸ”´ CHECK if ID exists
            boolean found = false;
            for (Song s : songs) {
                if (s.getSongId() == id) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                System.out.println("No song available with that ID.");
                return;
            }

            // ðŸ”¹ Ask details ONLY if ID valid
            System.out.print("New Title: ");
            String title = RevPlayApp.sc.nextLine();

            System.out.print("New Genre: ");
            String genre = RevPlayApp.sc.nextLine();

            // â�Œ Validate genre
            if (!genre.matches("^[a-zA-Z ,]+$")) {
                System.out.println("â�Œ Genre should contain only letters and commas.");
                return;
            }


            System.out.print("New Duration: ");
            double dur = Double.parseDouble(RevPlayApp.sc.nextLine());

            // âœ… STRICT DATE VALIDATION
            System.out.print("New Release Date (yyyy-MM-dd): ");
            String dateInput = RevPlayApp.sc.nextLine();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false);

            Date date;
            try {
                date = sdf.parse(dateInput);
            } catch (Exception e) {
                System.out.println("â�Œ Invalid date format! Use yyyy-MM-dd");
                return;  // STOP update
            }

            if (songService.updateSong(id, title, genre, dur, date))
                System.out.println("Song updated!");
            else
                System.out.println("Song not found!");

        } catch (NumberFormatException e) {
            System.out.println("â�Œ Duration and Song ID must be numbers.");
        } catch (Exception e) {
            System.out.println("Invalid input.");
        }
    }

    private static void updateAlbum(User user) {

        try {
            // ðŸ”¹ Get albums
            List<Album> albums = albumService.viewMyAlbums(user.getUserId());

            // ðŸ”´ STOP if empty
            if (albums == null || albums.isEmpty()) {
                System.out.println("No albums created yet.");
                return;
            }

            // ðŸ”¹ Show albums
            System.out.println("\n--- My Albums ---");
            for (Album a : albums) {
                System.out.println(a.getAlbumId() + " - " + a.getAlbumName());
            }

            System.out.print("Album ID: ");
            int id = Integer.parseInt(RevPlayApp.sc.nextLine());

            // ðŸ”´ VALIDATE ID
            boolean found = false;
            for (Album a : albums) {
                if (a.getAlbumId() == id) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                System.out.println("No album available with that ID.");
                return; // â­� STOP HERE
            }

            // ðŸ”¹ Ask new name only if ID valid
            System.out.print("New Album Name: ");
            String name = RevPlayApp.sc.nextLine();

            if (albumService.updateAlbum(id, name))
                System.out.println("Album updated!");
            else
                System.out.println("Album not found!");

        } catch (Exception e) {
            System.out.println("Invalid input");
        }
    }



    private static void deleteSong() {
        System.out.print("Song ID to delete: ");
        int id = Integer.parseInt(RevPlayApp.sc.nextLine());
        if (songService.deleteSong(id))
            System.out.println("Song deleted!");
        else
            System.out.println("Song not found!");
    }

    private static void deleteAlbum() {
        System.out.print("Album ID to delete: ");
        int id = Integer.parseInt(RevPlayApp.sc.nextLine());
        if (albumService.deleteAlbum(id))
            System.out.println("Album deleted!");
        else
            System.out.println("Album not found!");
    }

    private static void showPlayCount(User user) {

        try {
            // ðŸ”¹ Get songs of this artist
            List<Song> songs = songService.viewMySongs(user.getUserId());

            // ðŸ”´ STOP if no songs
            if (songs == null || songs.isEmpty()) {
                System.out.println("No songs available.");
                return;
            }

            // ðŸ”¹ Show songs
            System.out.println("\n--- My Songs ---");
            for (Song s : songs) {
                System.out.println(s.getSongId() + " - " + s.getTitle());
            }

            System.out.print("Song ID: ");
            int id = Integer.parseInt(RevPlayApp.sc.nextLine());

            // ðŸ”´ Validate ID exists
            boolean found = false;
            for (Song s : songs) {
                if (s.getSongId() == id) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                System.out.println("No song available with that ID.");
                return;
            }

            System.out.println("Play Count: " + songService.getPlayCount(id));

        } catch (Exception e) {
            System.out.println("Invalid input");
        }
    }

    private static void showFavoritesCount(User user) {

        try {
            // ðŸ”¹ Get songs of this artist
            List<Song> songs = songService.viewMySongs(user.getUserId());

            // ðŸ”´ STOP if no songs
            if (songs == null || songs.isEmpty()) {
                System.out.println("No songs available.");
                return;
            }

            // ðŸ”¹ Show songs
            System.out.println("\n--- My Songs ---");
            for (Song s : songs) {
                System.out.println(s.getSongId() + " - " + s.getTitle());
            }

            System.out.print("Song ID: ");
            int id = Integer.parseInt(RevPlayApp.sc.nextLine());

            // ðŸ”´ Validate ID
            boolean found = false;
            for (Song s : songs) {
                if (s.getSongId() == id) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                System.out.println("No song available with that ID.");
                return;
            }

            System.out.println("Favorites: " + songService.getFavoritesCount(id));

        } catch (Exception e) {
            System.out.println("Invalid input");
        }
    }

}
