package pl.edu.agh.oskamezy.plscrapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class AddRecordServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        out.println("<html><head><title>Add Records</title><link rel='stylesheet' type='text/css' href='style2.css'></head><body>");
        out.println("<nav>");
        out.println("<a href='add.html'>Add Record</a>");
        out.println("<a href='display.html'>Display Records</a>");
        out.println("<a href='query.html'>Search Records</a>");
        out.println("</nav>");
        
        out.println("<h1>Add Records</h1>");
        
        String artist = req.getParameter("artist");
        String title = req.getParameter("title");
        String emitDate = req.getParameter("emit_date");
        String emitTime = req.getParameter("emit_time");

        if (artist.isBlank() || title.isBlank()) {
        	 out.println("<html><body>");
             out.println("<h1>No input!</h1>");
             out.println("</body></html>");
        	return;
        }
        
        if (emitDate.isBlank() ) {
        	emitDate = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE).toString();
        }

        if (emitTime.isBlank() ) {
        	emitTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")).toString();
        }
        
        
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/songs", "admin", "admin");
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO songs (artist, title, emit_date, emit_time) VALUES (?, ?, ?, ?)")) {

            preparedStatement.setString(1, artist);
            preparedStatement.setString(2, title);
            preparedStatement.setDate(3, java.sql.Date.valueOf(emitDate));
            preparedStatement.setTime(4, java.sql.Time.valueOf(emitTime));

            int rows = preparedStatement.executeUpdate();
            if (rows > 0) {
                out.println("<html><body>");
                out.println("<h1>Record added successfully!</h1>");
                out.println("</body></html>");
            }
        } catch (Exception e) {
            e.printStackTrace(out);
        }
    }
}
