package pl.edu.agh.oskamezy.plscrapper;

import pl.edu.agh.oskamezy.plscrapper.DatabaseUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;

public class SongsDAO {
    public void addSong(String date, String time, String artist, String title, String picture_url) throws SQLException {
        String query = "INSERT INTO songs (emit_date, emit_time, artist, title, picture_url) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, date);
            pstmt.setString(2, time);
            pstmt.setString(3, artist);
            pstmt.setString(4, title);
            pstmt.setString(5, picture_url);
            pstmt.executeUpdate();
        }
    }
    
    public void deleteSong(String date, String time) throws SQLException {
        String query = "DELETE FROM songs WHERE emit_date = ? AND emit_time = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, date);
            pstmt.setString(2, time);
            pstmt.executeUpdate();
        }
    }

    public CachedRowSet getAllSongs() throws SQLException {
        String query = "SELECT * FROM songs ORDER BY emit_date,emit_time";
        try (Connection conn = DatabaseUtil.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query)) {
            ResultSet rs = pstmt.executeQuery();
            CachedRowSet cachedRowSet = RowSetProvider.newFactory().createCachedRowSet();
            cachedRowSet.populate(rs);
            return cachedRowSet;
        }
    }
    
    
    
    public CachedRowSet getSongByDate(String date, String time) throws SQLException {
        String query = "SELECT * FROM songs WHERE emit_date = ? AND emit_time = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, date);
            pstmt.setString(2, time);
            ResultSet rs = pstmt.executeQuery();
            CachedRowSet cachedRowSet = RowSetProvider.newFactory().createCachedRowSet();
            cachedRowSet.populate(rs);
            return cachedRowSet;
        }
    }
    
    public CachedRowSet getSongByArtistOrTitle(String artist,String title) throws SQLException {
        String query = "SELECT * FROM songs WHERE artist LIKE ? OR title LIKE ?";
       
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, "%"+artist+"%");
            pstmt.setString(2, "%"+title+"%");
            ResultSet rs = pstmt.executeQuery();
            CachedRowSet cachedRowSet = RowSetProvider.newFactory().createCachedRowSet();
            cachedRowSet.populate(rs);
            return cachedRowSet;
            }
    	}
	}
