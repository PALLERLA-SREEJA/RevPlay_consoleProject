package com.revplay.ui;

import com.revplay.Dao.AlbumDao;
import com.revplay.Dao.ArtistDao;
import com.revplay.Dao.FavoriteDao;
import com.revplay.Dao.PlaylistDao;
import com.revplay.Dao.RecentlyPlayedDao;
import com.revplay.Dao.SongDao;
import com.revplay.daoImpl.AlbumDaoImpl;
import com.revplay.daoImpl.ArtistDaoImpl;
import com.revplay.daoImpl.FavoriteDaoImpl;
import com.revplay.daoImpl.PlaylistDaoImpl;
import com.revplay.daoImpl.RecentlyPlayedDaoImpl;
import com.revplay.daoImpl.SongDaoImpl;
import com.revplay.main.RevPlayApp;
import com.revplay.model.Artist;
import com.revplay.model.Playlist;
import com.revplay.model.Song;
import com.revplay.model.User;


import com.revplay.service.*;

import java.util.List;

public class UserMenuUI {

    //  CREATE DAO LAYER
    private static SongDao songDao=new SongDaoImpl();
    private static PlaylistDao playlistDao=new PlaylistDaoImpl();
    private static FavoriteDao favoriteDao=new FavoriteDaoImpl();
    private static RecentlyPlayedDao recentDao= new RecentlyPlayedDaoImpl();
    private static AlbumDao albumDao= new AlbumDaoImpl();
    private static ArtistDao artistDao = new ArtistDaoImpl();



    //  CREATE SERVICE LAYER (constructor injection)
    private static SongService songService = new SongService(songDao);
    private static PlaylistService playlistService = new PlaylistService(playlistDao);
    private static FavoriteService favoriteService = new FavoriteService(favoriteDao);
    private static RecentlyPlayedService recentService = new RecentlyPlayedService(recentDao);
    private static AlbumService albumService = new AlbumService(albumDao);
    private static ArtistService artistService = new ArtistService(artistDao);

    //  PlayerService depends on 2 services
    private static PlayerService playerService =
            new PlayerService(songService, recentService);

    public static void showMenu(User user) {

        playerService.setUser(user.getUserId());

        while (true) {
            System.out.println("\n=== USER MENU ===");
            System.out.println("1. Browse Songs");
            System.out.println("2. Create Playlist");
            System.out.println("3. View My Playlists");
            System.out.println("4. Add Song to Playlist");
            System.out.println("5. Remove Song from Playlist");
            System.out.println("6. Delete Playlist");
            System.out.println("7. Add Favorite");
            System.out.println("8. Remove Favorite");
            System.out.println("9. View My Favorite Songs");
            System.out.println("10. Play / Pause Song");
            System.out.println("11. Recently Played Songs");
            System.out.println("12. Search Song By Album");
            System.out.println("13. Browse Songs by Category");
            System.out.println("14. View Public Playlists");
            System.out.println("15. Logout");
            System.out.print("Choice: ");

            String input = RevPlayApp.sc.nextLine();

            if (input.isEmpty()) {
                System.out.println("Please enter a choice.");
                continue;
            }

            int ch;

            try {
                ch = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Try again.");
                continue;
            }

            if (ch < 1 || ch > 15) {
                System.out.println("Wrong choice number. Try again.");
                continue;
            }

            switch (ch) {

                case 1:
                    List<Song> songs = songService.viewAllSongs();
                    for (Song s : songs) {
                        System.out.println(s.getSongId() + " - " + s.getTitle());
                    }
                    break;
                    
                case 2:

                    // 🎵 PLAYLIST NAME
                    System.out.print("Name: ");
                    String name = RevPlayApp.sc.nextLine().trim();

                    if (name.isEmpty()) {
                        System.out.println("Playlist name cannot be empty.");
                        break;
                    }

                    // Only letters, numbers, spaces allowed
                    if (!name.matches("[a-zA-Z0-9 ]+")) {
                        System.out.println("Playlist name must contain only letters and numbers.");
                        break;
                    }

                    // 📝 DESCRIPTION
                    System.out.print("Desc: ");
                    String d = RevPlayApp.sc.nextLine().trim();

                    if (d.isEmpty()) {
                        System.out.println("Description cannot be empty.");
                        break;
                    }

                    // 🔐 PRIVACY
                    System.out.print("Privacy(PUBLIC/PRIVATE): ");
                    String p = RevPlayApp.sc.nextLine().trim().toUpperCase();

                    if (!p.equals("PUBLIC") && !p.equals("PRIVATE")) {
                        System.out.println("Privacy must be PUBLIC or PRIVATE.");
                        break;
                    }

                    // ✅ CREATE PLAYLIST
                    if (playlistService.create(user.getUserId(), name, d, p))
                        System.out.println("Playlist created successfully!");
                    else
                        System.out.println("Could not create playlist.");

                    break;


                case 3:
                    try {
                        List<Playlist> pls = playlistService.getMine(user.getUserId());

                        // No playlists exist
                        if (pls == null || pls.isEmpty()) {
                            System.out.println("You have no playlists.");
                            break;
                        }

                        //  Show playlists
                        System.out.println("\n--- My Playlists ---");
                        for (Playlist pl : pls) {
                            System.out.println(pl.getPlaylistId() + " - " + pl.getName() 
                                + " (" + pl.getPrivacy() + ")");
                        }

                        System.out.print("Enter Playlist ID to open (0 to cancel): ");
                        String inputId = RevPlayApp.sc.nextLine();

                        // Empty input
                        if (inputId == null || inputId.trim().isEmpty()) {
                            System.out.println("Cancelled.");
                            break;
                        }

                        int selectedId;
                        try {
                            selectedId = Integer.parseInt(inputId);
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid number.");
                            break;
                        }

                        if (selectedId == 0) break;

                        // Validate playlist belongs to user
                        boolean valid = false;
                        for (Playlist pl : pls) {
                            if (pl.getPlaylistId() == selectedId) {
                                valid = true;
                                break;
                            }
                        }

                        if (!valid) {
                            System.out.println("No playlist found with that ID.");
                            break;
                        }

                        //  Get songs inside playlist
                        List<Song> songsInPlaylist = playlistService.getSongsInPlaylist(selectedId);

                        if (songsInPlaylist == null || songsInPlaylist.isEmpty()) {
                            System.out.println("This playlist has no songs.");
                            break;
                        }

                        //  Show songs
                        System.out.println("\n--- Songs in Playlist ---");
                        for (Song s : songsInPlaylist) {
                            System.out.println(s.getSongId() + " - " + s.getTitle());
                        }

                    } catch (Exception e) {
                        System.out.println("Error loading playlist.");
                    }
                    break;


                case 4:
                    // ¹ Get user's playlists
                    List<Playlist> playlists = playlistService.getMine(user.getUserId());

                    // ´ STOP if no playlists
                    if (playlists == null || playlists.isEmpty()) {
                        System.out.println("No playlists present to add songs.");
                        break;
                    }

                    // ¹ Show playlists
                    System.out.println("\n--- My Playlists ---");
                    for (Playlist pl : playlists) {
                        System.out.println(pl.getPlaylistId() + " - " + pl.getName());
                    }

                    System.out.print("Playlist ID: ");
                    int pid = Integer.parseInt(RevPlayApp.sc.nextLine());

                    // ´ Validate playlist ID
                    boolean validPlaylist = false;
                    for (Playlist pl : playlists) {
                        if (pl.getPlaylistId() == pid) {
                            validPlaylist = true;
                            break;
                        }
                    }

                    if (!validPlaylist) {
                        System.out.println("â Œ Invalid Playlist ID.");
                        break;
                    }

                    // ¹ Get all songs
                    List<Song> allSongs = songService.viewAllSongs();

                    // ´ STOP if no songs exist
                    if (allSongs == null || allSongs.isEmpty()) {
                        System.out.println("No songs available.");
                        break;
                    }

                    // ¹ Show songs
                    System.out.println("\n--- Available Songs ---");
                    for (Song s : allSongs) {
                        System.out.println(s.getSongId() + " - " + s.getTitle());
                    }

                    System.out.print("Song ID: ");
                    int sid = Integer.parseInt(RevPlayApp.sc.nextLine());

                    // ´ Validate song ID
                    boolean validSong = false;
                    for (Song s : allSongs) {
                        if (s.getSongId() == sid) {
                            validSong = true;
                            break;
                        }
                    }

                    if (!validSong) {
                        System.out.println("No song with that ID.");
                        break;
                    }

                    // ¹ Add to playlist
                    if (playlistService.addSong(pid, sid))
                        System.out.println("Song added to playlist!");
                    else
                        System.out.println("Failed to add song.");

                    break;




                case 5:
                    List<Playlist> playlists2 = playlistService.getMine(user.getUserId());

                    // ´ STOP if no playlists
                    if (playlists2 == null || playlists2.isEmpty()) {
                        System.out.println("No playlists available.");
                        break;
                    }

                    // ¹ Show playlists
                    System.out.println("\n--- My Playlists ---");
                    for (Playlist pl : playlists2) {
                        System.out.println(pl.getPlaylistId() + " - " + pl.getName());
                    }

                    System.out.print("Playlist ID: ");
                    int pid2 = Integer.parseInt(RevPlayApp.sc.nextLine());

                    // ´ Validate playlist ID
                    boolean validPlaylist2 = false;
                    for (Playlist pl : playlists2) {
                        if (pl.getPlaylistId() == pid2) {
                            validPlaylist2 = true;
                            break;
                        }
                    }

                    if (!validPlaylist2) {
                        System.out.println("â Œ Invalid Playlist ID.");
                        break;
                    }

                    // ¹ Show songs inside playlist
                    List<Song> playlistSongs = playlistService.getSongsInPlaylist(pid2);

                    if (playlistSongs == null || playlistSongs.isEmpty()) {
                        System.out.println("This playlist has no songs.");
                        break;
                    }

                    System.out.println("\n--- Songs in Playlist ---");
                    for (Song s : playlistSongs) {
                        System.out.println(s.getSongId() + " - " + s.getTitle());
                    }

                    System.out.print("Song ID to remove: ");
                    int sid2 = Integer.parseInt(RevPlayApp.sc.nextLine());

                    // ´ Validate song exists in playlist
                    boolean validSong2 = false;
                    for (Song s : playlistSongs) {
                        if (s.getSongId() == sid2) {
                            validSong2 = true;
                            break;
                        }
                    }

                    if (!validSong2) {
                        System.out.println("No such song in this playlist.");
                        break;
                    }

                    if (playlistService.removeSong(pid2, sid2))
                        System.out.println("Song removed from playlist!");
                    else
                        System.out.println("Failed to remove song.");

                    break;


                case 6:
                    List<Playlist> myPlaylists = playlistService.getMine(user.getUserId());

                    // ´ STOP IF EMPTY
                    if (myPlaylists == null || myPlaylists.isEmpty()) {
                        System.out.println("There are no playlists to delete.");
                        break;
                    }

                    // ¹ Show playlists
                    System.out.println("\n--- My Playlists ---");
                    for (Playlist pl : myPlaylists) {
                        System.out.println(pl.getPlaylistId() + " - " + pl.getName());
                    }

                    System.out.print("Playlist ID: ");
                    pid = Integer.parseInt(RevPlayApp.sc.nextLine());

                    // ´ Check ID exists
                    boolean found = false;
                    for (Playlist pl : myPlaylists) {
                        if (pl.getPlaylistId() == pid) {
                            found = true;
                            break;
                        }
                    }

                    if (!found) {
                        System.out.println("No playlist with that ID.");
                        break;
                    }

                    if (playlistService.delete(pid))
                        System.out.println("Playlist deleted successfully!");
                    else
                        System.out.println("Could not delete playlist.");

                    break;


                case 7:

                    try {
                        //  Get all songs
                        List<Song> allSongsFav = songService.viewAllSongs();

                        if (allSongsFav == null || allSongsFav.isEmpty()) {
                            System.out.println("No songs available to favorite.");
                            break;
                        }

                        //  Display songs
                        System.out.println("\n--- Available Songs ---");
                        for (Song s : allSongsFav) {
                            System.out.println(s.getSongId() + " - " + s.getTitle());
                        }

                        System.out.print("Enter Song ID to add to favorites (0 to cancel): ");
                        String input1 = RevPlayApp.sc.nextLine();

                        // Blank input
                        if (input1.isEmpty()) {
                            System.out.println("Operation cancelled.");
                            break;
                        }

                        int favSongId;

                        // Not a number
                        try {
                            favSongId = Integer.parseInt(input1);
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid number entered.");
                            break;
                        }

                        if (favSongId == 0) {
                            System.out.println("Operation cancelled.");
                            break;
                        }

                        // Validate song exists
                        boolean songExists = false;
                        for (Song s : allSongsFav) {
                            if (s.getSongId() == favSongId) {
                                songExists = true;
                                break;
                            }
                        }

                        if (!songExists) {
                            System.out.println("No song with that ID.");
                            break;
                        }

                        // Check duplicate favorite
                        List<Song> favList = favoriteService.getFavorites(user.getUserId());

                        boolean alreadyFav = false;
                        if (favList != null) {
                            for (Song s : favList) {
                                if (s.getSongId() == favSongId) {
                                    alreadyFav = true;
                                    break;
                                }
                            }
                        }

                        if (alreadyFav) {
                            System.out.println("Song already in favorites.");
                            break; // 🚫 STOP DB CALL
                        }

                        //  Safe DB insert
                        if (favoriteService.add(user.getUserId(), favSongId))
                            System.out.println("Song added to favorites!");
                        else
                            System.out.println("Could not add to favorites.");

                    } catch (Exception e) {
                        System.out.println("Unexpected error occurred.");
                    }

                    break;


                case 8:

                    try {
                        //  Fetch user's favorite songs
                        List<Song> favSongs = favoriteService.getFavorites(user.getUserId());

                        // No favorites exist
                        if (favSongs == null || favSongs.isEmpty()) {
                            System.out.println("You have no favorite songs to remove.");
                            break;
                        }

                        //  Show favorite songs
                        System.out.println("\n--- Your Favorite Songs ---");
                        for (Song s : favSongs) {
                            System.out.println(s.getSongId() + " - " + s.getTitle());
                        }

                        System.out.print("Enter Song ID to remove (0 to cancel): ");
                        String input1 = RevPlayApp.sc.nextLine();

                        // Empty input
                        if (input1.isEmpty()) {
                            System.out.println("Operation cancelled.");
                            break;
                        }

                        int removeId;

                        // Non-numeric input
                        try {
                            removeId = Integer.parseInt(input1);
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid number entered.");
                            break;
                        }

                        // Cancel operation
                        if (removeId == 0) {
                            System.out.println("Operation cancelled.");
                            break;
                        }

                        // Validate song exists in favorites
                        boolean found1 = false;
                        for (Song s : favSongs) {
                            if (s.getSongId() == removeId) {
                                found1 = true;
                                break;
                            }
                        }

                        if (!found1) {
                            System.out.println("That song is not in your favorites.");
                            break;
                        }

                        //  Safe removal
                        if (favoriteService.remove(user.getUserId(), removeId))
                            System.out.println("Favorite removed successfully!");
                        else
                            System.out.println("Could not remove favorite.");

                    } catch (Exception e) {
                        System.out.println("Unexpected error occurred.");
                    }

                    break;


                case 9:
                    List<Song> favs = favoriteService.getFavorites(user.getUserId());

                    if (favs == null || favs.isEmpty()) {
                        System.out.println("No favorite songs.");
                        break;
                    }

                    System.out.println("\n--- My Favorite Songs ---");
                    for (Song s : favs) {
                        System.out.println(s.getSongId() + " - " + s.getTitle());
                    }
                    break;
                    
                case 10:

                    try {
                        while (true) {

                            // If NO SONG is currently playing → ask user to pick one
                            if (!playerService.isPlaying()) {

                                List<Song> songsList = songService.viewAllSongs();

                                if (songsList == null || songsList.isEmpty()) {
                                    System.out.println("No songs available to play.");
                                    break;
                                }

                                System.out.println("\n--- Available Songs ---");
                                for (Song s : songsList) {
                                    System.out.println(s.getSongId() + " - " + s.getTitle());
                                }

                                System.out.print("Enter Song ID to play (0 to exit): ");
                                String in = RevPlayApp.sc.nextLine();

                                if (in.isEmpty()) break;

                                int songId;
                                try {
                                    songId = Integer.parseInt(in);
                                } catch (Exception e) {
                                    System.out.println("Invalid input.");
                                    continue;
                                }

                                if (songId == 0) break;

                                // 🔍 Check song exists
                                boolean exists = false;
                                for (Song s : songsList) {
                                    if (s.getSongId() == songId) {
                                        exists = true;
                                        break;
                                    }
                                }

                                if (!exists) {
                                    System.out.println("No song with that ID.");
                                    continue;
                                }

                                // ▶ Play song + increase play count
                                playerService.playSong(songId);
                            }

                            // 🎵 PLAYER CONTROLS
                            System.out.println("\n🎵 PLAYER MENU");
                            System.out.println("1. Pause");
                            System.out.println("2. Resume");
                            System.out.println("3. Stop");
                            System.out.println("4. Exit Player");

                            String choice = RevPlayApp.sc.nextLine();

                            if (choice.isEmpty()) continue;

                            int pc;
                            try {
                                pc = Integer.parseInt(choice);
                            } catch (Exception e) {
                                System.out.println("Invalid number.");
                                continue;
                            }

                            switch (pc) {
                                case 1:
                                    playerService.pauseSong();
                                    break;

                                case 2:
                                    playerService.resumeSong();
                                    break;

                                case 3:
                                    playerService.stopSong();
                                    break;

                                case 4:
                                    // ⭐ THIS LINE FIXES YOUR PROBLEM
                                    playerService.stopSong();   // reset player state
                                    break;

                                default:
                                    System.out.println("Invalid choice.");
                            }

                            if (pc == 4) break; // Exit to main menu
                        }

                    } catch (Exception e) {
                        System.out.println("Player error occurred.");
                    }

                    break;

                case 11:
                    try {
                        List<Song> recent = recentService.getRecentSongs(user.getUserId());

                        if (recent == null || recent.isEmpty()) {
                            System.out.println("No recently played songs.");
                            break;
                        }

                        System.out.println("\n--- Recently Played ---");
                        for (Song s : recent) {
                            System.out.println(s.getSongId() + " - " + s.getTitle());
                        }

                    } catch (Exception e) {
                        System.out.println("Unable to load recently played songs right now.");
                    }
                    break;
                    
                case 12:

                    try {
                        System.out.print("Enter Album Name: ");
                        String albumName = RevPlayApp.sc.nextLine();

                        // Empty check
                        if (albumName == null || albumName.trim().isEmpty()) {
                            System.out.println("Album name cannot be empty.");
                            break;
                        }

                        int albumId;

                        try {
                            albumId = albumService.findAlbumIdByName(albumName.trim());
                        } catch (Exception e) {
                            System.out.println("Error finding album.");
                            break;
                        }

                        // Album not found
                        if (albumId <= 0) {
                            System.out.println("No album found with that name.");
                            break;
                        }

                        List<Song> songsInAlbum;

                        try {
                            songsInAlbum = songService.getSongsByAlbumId(albumId);
                        } catch (Exception e) {
                            System.out.println("Error retrieving songs.");
                            break;
                        }

                        if (songsInAlbum == null || songsInAlbum.isEmpty()) {
                            System.out.println("No songs found in this album.");
                            break;
                        }

                        System.out.println("\n--- Songs in Album ---");
                        for (Song s : songsInAlbum) {
                            System.out.println(s.getSongId() + " - " + s.getTitle());
                        }

                    } catch (Exception e) {
                        System.out.println("Unexpected error occurred.");
                    }

                    break;
                case 13:
                    try {
                        System.out.println("\nBrowse By:");
                        System.out.println("1. Genre");
                        System.out.println("2. Artist");
                        System.out.println("3. Album");
                        System.out.println("4. Song Name");
                        System.out.print("Choice: ");

                        String input2 = RevPlayApp.sc.nextLine();

                        // Empty input check
                        if (input2 == null || input2.trim().isEmpty()) {
                            System.out.println("Choice cannot be empty.");
                            break;
                        }

                        int opt;
                        try {
                            opt = Integer.parseInt(input2);
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid choice. Numbers only.");
                            break;
                        }

                        List<Song> result = null;

                        switch (opt) {

                            case 1:
                                System.out.print("Enter Genre Keyword: ");
                                String genreInput = RevPlayApp.sc.nextLine().trim();

                                if (genreInput.isEmpty()) {
                                    System.out.println("Genre cannot be empty.");
                                    break;
                                }

                                //  Step 1: Find matching genres
                                List<String> genres = songService.searchGenres(genreInput);

                                if (genres == null || genres.isEmpty()) {
                                    System.out.println("No matching genres found.");
                                    break;
                                }

                                System.out.println("\n--- Matching Genres ---");
                                for (int i = 0; i < genres.size(); i++) {
                                    System.out.println((i + 1) + ". " + genres.get(i));
                                }

                                System.out.print("Select Genre Number: ");
                                int gChoice;
                                try {
                                    gChoice = Integer.parseInt(RevPlayApp.sc.nextLine());
                                } catch (Exception e) {
                                    System.out.println("Invalid selection.");
                                    break;
                                }

                                if (gChoice < 1 || gChoice > genres.size()) {
                                    System.out.println("Invalid genre number.");
                                    break;
                                }

                                String selectedGenre = genres.get(gChoice - 1);

                                //  Step 2: Get songs
                                result = songService.getSongsByGenre(selectedGenre);
                                break;


                            case 2:
                                System.out.print("Enter Artist Name Keyword: ");
                                String text = RevPlayApp.sc.nextLine().trim();

                                if (text.isEmpty()) {
                                    System.out.println("Artist name cannot be empty.");
                                    break;
                                }

                                List<Artist> artists = artistService.searchArtistsByName(text);

                                if (artists == null || artists.isEmpty()) {
                                    System.out.println("No artists found.");
                                    break;
                                }

                                System.out.println("\n--- Matching Artists ---");
                                int i = 1;
                                for (Artist a : artists) {
                                    System.out.println(i + ". Artist ID: " + a.getArtistId() + " | Genre: " + a.getGenre());
                                    i++;
                                }

                                System.out.print("Select Artist Number: ");
                                int choice;
                                try {
                                    choice = Integer.parseInt(RevPlayApp.sc.nextLine());
                                } catch (Exception e) {
                                    System.out.println("Invalid selection.");
                                    break;
                                }

                                if (choice < 1 || choice > artists.size()) {
                                    System.out.println("Invalid choice.");
                                    break;
                                }

                                int selectedArtistId = artists.get(choice - 1).getArtistId();

                                result = songService.viewSongsByAlbum(selectedArtistId);  // ❌ WAIT — WRONG



                            case 3:
                                System.out.print("Enter Album Name: ");
                                String album = RevPlayApp.sc.nextLine().trim();

                                if (album.isEmpty()) {
                                    System.out.println("Album name cannot be empty.");
                                    break;
                                }

                                //  Step 1: Get matching albums
                                List<com.revplay.model.Album> albums = albumService.searchAlbumsByName(album);

                                if (albums == null || albums.isEmpty()) {
                                    System.out.println("No albums found.");
                                    break;
                                }

                                System.out.println("\n--- Matching Albums ---");
                                for (com.revplay.model.Album a : albums) {
                                    System.out.println(a.getAlbumId() + " - " + a.getAlbumName());
                                }

                                System.out.print("Enter Album ID to view songs: ");
                                int aid;
                                try {
                                    aid = Integer.parseInt(RevPlayApp.sc.nextLine());
                                } catch (Exception e) {
                                    System.out.println("Invalid ID.");
                                    break;
                                }

                                //  Step 2: Get songs of selected album
                                result = songService.getSongsByAlbumId(aid);
                                break;


                            case 4:
                                System.out.print("Enter Song Name: ");
                                String title = RevPlayApp.sc.nextLine();

                                if (title == null || title.trim().isEmpty()) {
                                    System.out.println("Song name cannot be empty.");
                                    break;
                                }

                                result = songService.searchSongsByTitle(title.trim());
                                break;

                            default:
                                System.out.println("Invalid category selection.");
                                break;
                        }

                        // No results found
                        if (result == null || result.isEmpty()) {
                            System.out.println("No songs found.");
                            break;
                        }

                        //  Display Results
                        System.out.println("\n--- Songs Found ---");
                        for (Song s : result) {
                            System.out.println(s.getSongId() + " - " + s.getTitle());
                        }

                    } catch (Exception e) {
                        System.out.println("Unexpected error while searching songs.");
                    }
                    break;
                    
                case 14:
                    try {
                        List<Playlist> publicPlaylists = playlistService.getPublicPlaylists(user.getUserId());

                        if (publicPlaylists == null || publicPlaylists.isEmpty()) {
                            System.out.println("No public playlists available.");
                            break;
                        }

                        System.out.println("\n--- Public Playlists ---");
                        for (Playlist pl : publicPlaylists) {
                            System.out.println(pl.getPlaylistId() + " - " + pl.getName());
                        }

                        System.out.print("Enter Playlist ID to view songs (0 to cancel): ");
                        String inputId = RevPlayApp.sc.nextLine();

                        if (inputId == null || inputId.trim().isEmpty()) {
                            System.out.println("Cancelled.");
                            break;
                        }

                        int pid1;
                        try {
                            pid1 = Integer.parseInt(inputId);
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid number.");
                            break;
                        }

                        if (pid1 == 0) break;

                        // Validate ID
                        boolean exists = false;
                        for (Playlist pl : publicPlaylists) {
                            if (pl.getPlaylistId() == pid1) {
                                exists = true;
                                break;
                            }
                        }

                        if (!exists) {
                            System.out.println("No playlist found with that ID.");
                            break;
                        }

                        List<Song> songs1 = playlistService.getSongsInPlaylist(pid1);

                        if (songs1 == null || songs1.isEmpty()) {
                            System.out.println("This playlist has no songs.");
                            break;
                        }

                        System.out.println("\n--- Songs in Public Playlist ---");
                        for (Song s : songs1) {
                            System.out.println(s.getSongId() + " - " + s.getTitle());
                        }

                    } catch (Exception e) {
                        System.out.println("Error loading public playlists.");
                    }
                    break;

                case 15:
                    return;
            }
        }
    }
}
