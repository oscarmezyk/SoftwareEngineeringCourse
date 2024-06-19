package pl.edu.agh.oskamezy.plscrapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;

public class QueryServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        out.println("<html><head><title>Search Records</title><link rel='stylesheet' type='text/css' href='style2.css'></head><body>");
        out.println("<nav>");
        out.println("<a href='add.html'>Add Record</a>");
        out.println("<a href='display.html'>Display Records</a>");
        out.println("<a href='query.html'>Search Records</a>");
        out.println("<a href='scrap.html'>Scrap Records</a>");
        out.println("</nav>");
        
        out.println("<h1>Search Records</h1>");
        out.println("<form action=\"/query\" method=\"get\">");
        out.println("<label for=\"artist\">Artist or title:</label>");
        out.println("<input type=\"text\" id=\"artist\" name=\"artist\">");
        out.println("<input type=\"submit\" value=\"Search\">");
        out.println("</form>");

        String artist = req.getParameter("artist");
        if (artist != null) {
            	try {
            	CachedRowSet resultSet = App.Songsdb.getSongByArtistOrTitle(artist, artist);
            	
                out.println("<table border='1'><tr><th>ID</th><th>Artist</th><th>Title</th><th>Emit Date</th><th>Emit Time</th><th>Url</th</tr>");

                while (resultSet.next()) {
                    out.println("<tr>");
                    out.println("<td>" + resultSet.getInt("id") + "</td>");
                    out.println("<td>" + resultSet.getString("artist") + "</td>");
                    out.println("<td>" + resultSet.getString("title") + "</td>");
                    out.println("<td>" + resultSet.getDate("emit_date") + "</td>");
                    out.println("<td>" + resultSet.getTime("emit_time") + "</td>");
                    //out.println("<td>" + resultSet.getString("picture_url") + "</td>");
                    out.println("<td><a href=\"" + resultSet.getString("picture_url") + "\" target=\"_blank\">" + resultSet.getString("picture_url") + "</a></td>");
                    
                    out.println("</tr>");
                }

                out.println("</table>");
            } catch (Exception e) {
                e.printStackTrace(out);
            }
        }

        out.println("</body></html>");
    }
}
