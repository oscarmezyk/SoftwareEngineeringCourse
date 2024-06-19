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
import java.util.ArrayList;
import java.util.List;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;

public class DisplayRecordsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        out.println("<html><head><title>Display Records</title><link rel='stylesheet' type='text/css' href='style2.css'></head><body>");
        out.println("<nav>");
        out.println("<a href='add.html'>Add Record</a>");
        out.println("<a href='display.html'>Display Records</a>");
        out.println("<a href='query.html'>Search Records</a>");
        out.println("<a href='scrap.html'>Scrap Records</a>");
        out.println("</nav>");
        out.println("<h1>Song Records</h1>");
        out.println("<form action='/display' method='get'>");
        out.println("<input type='submit' value='Display Records'>");
        out.println("</form>");

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/songs", "admin", "admin");
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM songs ORDER BY emit_date,emit_time")) {

            List<String> artists = new ArrayList<>();
            List<String> titles = new ArrayList<>();
            List<String> emitDates = new ArrayList<>();
            List<String> emitTimes = new ArrayList<>();
            List<String> urls = new ArrayList<>();


            while (resultSet.next()) {
                artists.add(resultSet.getString("artist"));
                titles.add(resultSet.getString("title"));
                emitDates.add(resultSet.getDate("emit_date").toString());
                emitTimes.add(resultSet.getTime("emit_time").toString());
                urls.add(resultSet.getString("picture_url").toString());

            }

            out.println("<table>");
            out.println("<tr>");
            out.println("<th>Artist</th>");
            out.println("<th>Title</th>");
            out.println("<th>Emit Date</th>");
            out.println("<th>Emit Time</th>");
            out.println("<th>Url</th>");
            out.println("</tr>");

            for (int i = 0; i < artists.size(); i++) {
                out.println("<tr>");
                out.println("<td>");
                out.println(artists.get(i));
                out.println("</td>");
                out.println("<td>");
                out.println(titles.get(i));
                out.println("</td>");
                out.println("<td>");
                out.println(emitDates.get(i));
                out.println("</td>");
                out.println("<td>");
                out.println(emitTimes.get(i));
                out.println("</td>");
                out.println("<td><a href=\"");
                out.println(urls.get(i));
                out.println("\" target=\"_blank\">" +urls.get(i) + "</a></td>");
                out.println("</tr>");
            }

            out.println("</table>");
        } catch (Exception e) {
            e.printStackTrace(out);
        }

        out.println("</body></html>");
    }
}
