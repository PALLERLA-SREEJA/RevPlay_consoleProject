package com.revplay.daoImpl;

import java.sql.*;
import java.util.*;

import com.revplay.Dao.FavoriteDao;
import com.revplay.model.Song;
import com.revplay.util.DBConnection;

public class FavoriteDaoImpl implements FavoriteDao {

	@Override
    public boolean addFavorite(int userId, int songId) {
        String sql = "INSERT INTO FAVORITES VALUES (?, ?)";
        try {Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setInt(2, songId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

	@Override
    public boolean removeFavorite(int userId, int songId) {
        String sql = "DELETE FROM FAVORITES WHERE USER_ID=? AND SONG_ID=?";
        try {Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setInt(2, songId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }
    
	@Override
    public List<Song> getUserFavorites(int userId) {

        List<Song> list = new ArrayList<Song>();

        String sql = "SELECT s.* FROM SONGS s " +
                     "JOIN FAVORITES f ON s.SONG_ID = f.SONG_ID " +
                     "WHERE f.USER_ID=?";

        try {Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Song s = new Song();
                s.setSongId(rs.getInt("SONG_ID"));
                s.setTitle(rs.getString("TITLE"));
                s.setGenre(rs.getString("GENRE"));
                s.setDuration(rs.getDouble("DURATION"));
                s.setReleaseDate(rs.getDate("RELEASE_DATE"));
                list.add(s);
            }

        } catch (Exception e) { e.printStackTrace(); }

        return list;
    }

}
