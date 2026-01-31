package com.revplay.daoImpl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.revplay.Dao.AlbumDao;
import com.revplay.model.Album;
import com.revplay.util.DBConnection;

public class AlbumDaoImpl implements AlbumDao {

	@Override
    public boolean createAlbum(Album album) {
        String sql = "INSERT INTO ALBUMS VALUES (ALBUM_SEQ.NEXTVAL, ?, ?, ?)";

        try {Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, album.getArtistId());
            ps.setString(2, album.getAlbumName());
            ps.setDate(3, new java.sql.Date(album.getReleaseDate().getTime()));

            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

	@Override
    public List<Album> getAlbumsByArtist(int artistId) {
        List<Album> list = new ArrayList<Album>();
        String sql = "SELECT * FROM ALBUMS WHERE ARTIST_ID=?";

        try{Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, artistId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Album a = new Album();
                a.setAlbumId(rs.getInt("ALBUM_ID"));
                a.setArtistId(rs.getInt("ARTIST_ID"));
                a.setAlbumName(rs.getString("ALBUM_NAME"));
                a.setReleaseDate(rs.getDate("RELEASE_DATE"));
                list.add(a);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
    
	@Override
    public boolean updateAlbum(int albumId, String name) {
        String sql = "UPDATE ALBUMS SET ALBUM_NAME=? WHERE ALBUM_ID=?";
        try {Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, name);
            ps.setInt(2, albumId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

	@Override
    public boolean deleteAlbum(int albumId) {
        String sql = "DELETE FROM ALBUMS WHERE ALBUM_ID=?";
        try {Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, albumId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }
	
	@Override
    public int getAlbumIdByName(String name) {
        String sql = "SELECT ALBUM_ID FROM ALBUMS WHERE LOWER(ALBUM_NAME)=LOWER(?)";

        try {Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("ALBUM_ID");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1; // not found
    }

}
