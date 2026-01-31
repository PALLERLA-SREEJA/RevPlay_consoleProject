package com.revplay.daoImpl;

import java.sql.*;
import java.util.*;

import com.revplay.Dao.PlaylistDao;
import com.revplay.model.Song;

import com.revplay.model.Playlist;
import com.revplay.util.DBConnection;

public class PlaylistDaoImpl implements PlaylistDao{

	@Override
    public boolean createPlaylist(Playlist p) {
        String sql = "INSERT INTO PLAYLISTS (USER_ID, NAME, DESCRIPTION, PRIVACY) VALUES (?, ?, ?, ?)";

        try{
       Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, p.getUserId());
            ps.setString(2, p.getName());
            ps.setString(3, p.getDescription());
            ps.setString(4, p.getPrivacy());

            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

	@Override
    public List<Playlist> getUserPlaylists(int userId) {
        List<Playlist> list = new ArrayList<Playlist>();
        String sql = "SELECT * FROM PLAYLISTS WHERE USER_ID=?";

        try {
       Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Playlist p = new Playlist();
                p.setPlaylistId(rs.getInt("PLAYLIST_ID"));
                p.setName(rs.getString("NAME"));
                p.setDescription(rs.getString("DESCRIPTION"));
                p.setPrivacy(rs.getString("PRIVACY"));
                list.add(p);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

	@Override
    public boolean addSong(int playlistId, int songId) {
        String sql = "INSERT INTO PLAYLIST_SONGS VALUES (?, ?)";
        try {
        Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, playlistId);
            ps.setInt(2, songId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

	@Override
    public boolean removeSong(int playlistId, int songId) {
        String sql = "DELETE FROM PLAYLIST_SONGS WHERE PLAYLIST_ID=? AND SONG_ID=?";
        
        try {Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, playlistId);
            ps.setInt(2, songId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

	@Override
    public boolean deletePlaylist(int id) {
        try {Connection con = DBConnection.getConnection();
            PreparedStatement ps1 = con.prepareStatement("DELETE FROM PLAYLIST_SONGS WHERE PLAYLIST_ID=?");
            ps1.setInt(1, id);
            ps1.executeUpdate();

            PreparedStatement ps2 = con.prepareStatement("DELETE FROM PLAYLISTS WHERE PLAYLIST_ID=?");
            ps2.setInt(1, id);
            return ps2.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }
    
	@Override
    public boolean updatePlaylist(int id, String name, String desc, String privacy) {
        String sql = "UPDATE PLAYLISTS SET NAME=?, DESCRIPTION=?, PRIVACY=? WHERE PLAYLIST_ID=?";
        try {Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, name);
            ps.setString(2, desc);
            ps.setString(3, privacy);
            ps.setInt(4, id);

            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }
    
	@Override
    public List<Song> getSongsInPlaylist(int playlistId) {
        List<Song> songs = new ArrayList<Song>();

        String sql = "SELECT s.SONG_ID, s.TITLE FROM SONGS s " +
                     "JOIN PLAYLIST_SONGS ps ON s.SONG_ID = ps.SONG_ID " +
                     "WHERE ps.PLAYLIST_ID = ?";

        try {Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, playlistId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Song s = new Song();
                s.setSongId(rs.getInt("SONG_ID"));
                s.setTitle(rs.getString("TITLE"));
                songs.add(s);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return songs;
    }
    
 
	@Override
    public List<Playlist> getPublicPlaylists(int currentUserId) {

        List<Playlist> list = new ArrayList<Playlist>();

        String sql = "SELECT * FROM PLAYLISTS WHERE PRIVACY='PUBLIC' AND USER_ID<>?";

        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, currentUserId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Playlist p = new Playlist();
                p.setPlaylistId(rs.getInt("PLAYLIST_ID"));
                p.setName(rs.getString("NAME"));
                p.setDescription(rs.getString("DESCRIPTION"));
                p.setPrivacy(rs.getString("PRIVACY"));
                list.add(p);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }


}
