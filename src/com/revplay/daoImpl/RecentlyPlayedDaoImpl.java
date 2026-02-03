package com.revplay.daoImpl;

import java.sql.*;
import java.util.*;

import com.revplay.Dao.RecentlyPlayedDao;
import com.revplay.model.Song;
import com.revplay.util.DBConnection;

public class RecentlyPlayedDaoImpl implements RecentlyPlayedDao {

    @Override
    public void addEntry(int userId, int songId) {

        String sql = "INSERT INTO RECENTLY_PLAYED VALUES (RECENT_SEQ.NEXTVAL, ?, ?, SYSTIMESTAMP)";

        try {Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, userId);
            ps.setInt(2, songId);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Song> getRecentSongs(int userId) {

        List<Song> list = new ArrayList<Song>();

        String sql =
        	    "SELECT * FROM (" +
        	    " SELECT s.SONG_ID, s.TITLE " +
        	    " FROM SONGS s JOIN RECENTLY_PLAYED r ON s.SONG_ID = r.SONG_ID " +
        	    " WHERE r.USER_ID=? " +
        	    " ORDER BY r.PLAYED_AT DESC" +
        	    ") WHERE ROWNUM <= 5";


        try {
        	Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Song s = new Song();
                s.setSongId(rs.getInt("SONG_ID"));
                s.setTitle(rs.getString("TITLE"));
                list.add(s);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}