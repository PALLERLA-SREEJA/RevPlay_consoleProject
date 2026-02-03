package com.revplay.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import com.revplay.Dao.ArtistDao;
import com.revplay.model.Artist;
import com.revplay.util.DBConnection;

public class ArtistDaoImpl implements ArtistDao {

    @Override
    public boolean profileExists(int userId) {
        String sql = "SELECT * FROM ARTISTS WHERE USER_ID=?";

        try {Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            return rs.next();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean createProfile(Artist artist) {
        String sql = "INSERT INTO ARTISTS (USER_ID, BIO, GENRE, SOCIAL_LINKS) VALUES (?, ?, ?, ?)";

        try {Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, artist.getUserId());
            ps.setString(2, artist.getBio());
            ps.setString(3, artist.getGenre());
            ps.setString(4, artist.getSocialLinks());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateProfile(Artist artist) {
        String sql = "UPDATE ARTISTS SET BIO=?, GENRE=?, SOCIAL_LINKS=? WHERE USER_ID=?";

        try {Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, artist.getBio());
            ps.setString(2, artist.getGenre());
            ps.setString(3, artist.getSocialLinks());
            ps.setInt(4, artist.getUserId());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Artist getArtistByUser(int userId) {
        String sql = "SELECT * FROM ARTISTS WHERE USER_ID=?";

        try {Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Artist artist = new Artist();
                artist.setArtistId(rs.getInt("ARTIST_ID"));
                artist.setUserId(rs.getInt("USER_ID"));
                artist.setBio(rs.getString("BIO"));
                artist.setGenre(rs.getString("GENRE"));
                artist.setSocialLinks(rs.getString("SOCIAL_LINKS"));
                return artist;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @Override
    public List<Artist> searchArtistsByName(String text) {

        List<Artist> list = new java.util.ArrayList<Artist>();

        String sql =
            "SELECT a.ARTIST_ID, a.USER_ID, a.BIO, a.GENRE, a.SOCIAL_LINKS, u.USERNAME " +
            "FROM ARTISTS a JOIN USERS u ON a.USER_ID = u.USER_ID " +
            "WHERE LOWER(u.USERNAME) LIKE LOWER(?)";

        try {Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, "%" + text + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Artist a = new Artist();
                a.setArtistId(rs.getInt("ARTIST_ID"));
                a.setUserId(rs.getInt("USER_ID"));
                a.setBio(rs.getString("BIO"));
                a.setGenre(rs.getString("GENRE"));
                a.setSocialLinks(rs.getString("SOCIAL_LINKS"));
                list.add(a);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }


}
