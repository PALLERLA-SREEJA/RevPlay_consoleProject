package com.revplay.daoImpl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.revplay.Dao.SongDao;
import com.revplay.model.Song;
import com.revplay.util.DBConnection;

public class SongDaoImpl implements SongDao{


    // ================= ADD SONG =================
    public boolean addSong(Song song) {
        String sql = "INSERT INTO SONGS (SONG_ID, ALBUM_ID, ARTIST_ID, TITLE, GENRE, DURATION, RELEASE_DATE, PLAY_COUNT) " +
                     "VALUES (SONG_SEQ.NEXTVAL, ?, ?, ?, ?, ?, ?, 0)";
        try {Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, song.getAlbumId());
            ps.setInt(2, song.getArtistId());
            ps.setString(3, song.getTitle());
            ps.setString(4, song.getGenre());
            ps.setDouble(5, song.getDuration());
            ps.setDate(6, new java.sql.Date(song.getReleaseDate().getTime()));

            return ps.executeUpdate() > 0;

        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    // ================= GET ALL SONGS =================
    public List<Song> getAllSongs() {
        List<Song> list = new ArrayList<Song>();
        String sql = "SELECT * FROM SONGS";

        try {Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(extractSong(rs));
            }
        } catch (Exception e) { e.printStackTrace(); }

        return list;
    }

    // ================= GET SONGS BY ARTIST ID =================
    public List<Song> getSongsByArtist(int artistId) {
        List<Song> list = new ArrayList<Song>();
        String sql = "SELECT * FROM SONGS WHERE ARTIST_ID=?";

        try {Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, artistId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(extractSong(rs));

        } catch (Exception e) { e.printStackTrace(); }

        return list;
    }

    // ================= GET SONGS BY ALBUM ID =================
    public List<Song> getSongsByAlbum(int albumId) {
        List<Song> songs = new ArrayList<Song>();
        String sql = "SELECT * FROM SONGS WHERE ALBUM_ID=?";

        try {Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, albumId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) songs.add(extractSong(rs));

        } catch (Exception e) { e.printStackTrace(); }

        return songs;
    }

    // ================= BROWSE BY GENRE =================
    public List<Song> getSongsByGenre(String genre) {
        List<Song> list = new ArrayList<Song>();
        String sql = "SELECT * FROM SONGS WHERE UPPER(GENRE)=UPPER(?)";

        try {Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, genre);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(extractSong(rs));

        } catch (Exception e) { e.printStackTrace(); }

        return list;
    }

    // ================= SEARCH BY SONG TITLE =================
    public List<Song> searchSongsByTitle(String title) {
        List<Song> list = new ArrayList<Song>();
        String sql = "SELECT * FROM SONGS WHERE LOWER(TITLE) LIKE LOWER(?)";

        try {Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, "%" + title + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(extractSong(rs));

        } catch (Exception e) { e.printStackTrace(); }

        return list;
    }

    // ================= SEARCH BY ARTIST NAME =================
    public List<Song> getSongsByArtistName(String artistName) {
        List<Song> list = new ArrayList<Song>();
        String sql = "SELECT s.* FROM SONGS s " +
                     "JOIN ARTISTS a ON s.ARTIST_ID = a.ARTIST_ID " +
                     "JOIN USERS u ON a.USER_ID = u.USER_ID " +
                     "WHERE LOWER(u.USERNAME) LIKE LOWER(?)";

        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + artistName + "%");

            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(extractSong(rs));

        } catch (Exception e) { e.printStackTrace(); }

        return list;
    }


    // ================= SEARCH BY ALBUM NAME =================
    public List<Song> getSongsByAlbumName(String albumName) {
        List<Song> list = new ArrayList<Song>();
        String sql = "SELECT s.* FROM SONGS s JOIN ALBUMS al ON s.ALBUM_ID=al.ALBUM_ID WHERE LOWER(al.ALBUM_NAME) LIKE LOWER(?)";

        try {Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, "%" + albumName + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(extractSong(rs));

        } catch (Exception e) { e.printStackTrace(); }

        return list;
    }

    // ================= UPDATE SONG =================
    public boolean updateSong(Song song) {
        String sql = "UPDATE SONGS SET TITLE=?, GENRE=?, DURATION=?, RELEASE_DATE=? WHERE SONG_ID=?";
        try {Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, song.getTitle());
            ps.setString(2, song.getGenre());
            ps.setDouble(3, song.getDuration());
            ps.setDate(4, new java.sql.Date(song.getReleaseDate().getTime()));
            ps.setInt(5, song.getSongId());

            return ps.executeUpdate() > 0;

        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    // ================= DELETE SONG =================
    public boolean deleteSong(int songId) {
        String sql = "DELETE FROM SONGS WHERE SONG_ID=?";
        try {Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, songId);
            return ps.executeUpdate() > 0;

        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    // ================= PLAY COUNT =================
    public int getPlayCount(int songId) {
        String sql = "SELECT PLAY_COUNT FROM SONGS WHERE SONG_ID=?";
        try {Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, songId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);

        } catch (Exception e) { e.printStackTrace(); }

        return 0;
    }

    public void incrementPlayCount(int songId) {
        String sql = "UPDATE SONGS SET PLAY_COUNT = PLAY_COUNT + 1 WHERE SONG_ID=?";
        try {Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, songId);
            ps.executeUpdate();

        } catch (Exception e) { e.printStackTrace(); }
    }

    // ================= FAVORITE COUNT =================
    public int getFavoritesCount(int songId) {
        String sql = "SELECT COUNT(*) FROM FAVORITES WHERE SONG_ID=?";
        try {Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, songId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);

        } catch (Exception e) { e.printStackTrace(); }

        return 0;
    }

    // ================= COMMON RESULTSET MAPPER =================
    private Song extractSong(ResultSet rs) throws SQLException {
        Song s = new Song();
        s.setSongId(rs.getInt("SONG_ID"));
        s.setTitle(rs.getString("TITLE"));
        s.setGenre(rs.getString("GENRE"));
        s.setDuration(rs.getDouble("DURATION"));
        s.setReleaseDate(rs.getDate("RELEASE_DATE"));
        s.setArtistId(rs.getInt("ARTIST_ID"));
        s.setAlbumId(rs.getInt("ALBUM_ID"));
        s.setPlayCount(rs.getInt("PLAY_COUNT"));
        return s;
    }
    
    public boolean artistExists(String artistName) {
        boolean exists = false;
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "SELECT 1 FROM ARTISTS a " +
                         "JOIN USERS u ON a.USER_ID = u.USER_ID " +
                         "WHERE LOWER(u.USERNAME) = LOWER(?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, artistName);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) exists = true;

        } catch (Exception e) { e.printStackTrace(); }

        return exists;
    }

}