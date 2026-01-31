package com.revplay.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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
}
