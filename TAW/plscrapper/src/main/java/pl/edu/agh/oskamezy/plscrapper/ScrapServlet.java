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
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;

public class ScrapServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        out.println("<html><head><title>Scrap Records</title><link rel='stylesheet' type='text/css' href='style2.css'></head><body>");
        out.println("<nav>");
        out.println("<a href='add.html'>Add Record</a>");
        out.println("<a href='display.html'>Display Records</a>");
        out.println("<a href='query.html'>Search Records</a>");
        out.println("<a href='scrap.html'>Scrap Records</a>");
        out.println("</nav>");
        
        out.println("<h1>Scrap Records</h1>");
        out.println("<form action=\"/scrap\" method=\"post\">");

        out.println("<label for=\"day\">Day:</label>");
        out.println("<input type=\"text\" id=\"day\" name=\"day\">");
        
        out.println("<label for=\"start_date\">Start date:</label>");
        out.println("<input type=\"text\" id=\"start_date\" name=\"start_date\">");
       
        out.println("<label for=\"end_date\">End date:</label>");
        out.println("<input type=\"text\" id=\"end_date\" name=\"end_date\">");
        
        out.println("<label for=\"start_time\">Start time:</label>");
        out.println("<input type=\"text\" id=\"start_time\" name=\"start_time\">");
        
        out.println("<label for=\"end_time\">End time:</label>");
        out.println("<input type=\"text\" id=\"end_time\" name=\"end_time\">");
        
        out.println("<input type=\"submit\" value=\"Scrap!\" id=\"scrap\" >");

        out.println("<label for=\"save\">Save directly to database and do not display:</label>");
        out.println("<input type=\"checkbox\" value=\"true\" id=\"save\" name=\"save\" >");
        
        out.println("</form>");

        String day = req.getParameter("day");
        String start_date = req.getParameter("start_date");
        String end_date = req.getParameter("end_date");
        String start_time = req.getParameter("start_time");
        String end_time = req.getParameter("end_time");
        
        if (day.isBlank()) {
        	day = "NULL";
        }
        if (start_date.isBlank() || end_date.isBlank() || start_time.isBlank() || end_time.isBlank()) {
        	return;
        }
        
        String[] args = {day,start_time,end_time,start_date,end_date};

        String save = req.getParameter("save");
        
        if (save == null) {
         	try {
                List<String[]> songs = App.runSingleScrapper(args);

                out.println("<table border='1'><tr><th>Artist</th><th>Title</th><th>Emit Date</th><th>Emit Time</th><th>url</th></tr>");
                for (String[] row : songs) {
                    out.println("<tr>");
                    out.println("<td>" + row[0] + "</td>");
                    out.println("<td>" + row[1] + "</td>");
                    out.println("<td>" + row[2] + "</td>");
                    out.println("<td>" + row[3]+ "</td>");
                    out.println("<td><a href=\"" + row[4]+ "\" target=\"_blank\">" + row[4]+ "</a></td>");
                    out.println("</tr>");
                }
                out.println("</table>");
                out.println("</body></html>");
            } catch (Exception e) {
                e.printStackTrace(out);
                resp.sendRedirect("/scrap");
            }
        } else {
            try {
            	App.runScrapper(args);
            } catch (Exception e) {
                e.printStackTrace(out);
                resp.sendRedirect("/display");
            }
            out.println("</body></html>");
            resp.sendRedirect("/display");
        }
         	
    }
    
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        out.println("<html><head><title>Scrap Records</title><link rel='stylesheet' type='text/css' href='style2.css'></head><body>");
        out.println("<nav>");
        out.println("<a href='add.html'>Add Record</a>");
        out.println("<a href='display.html'>Display Records</a>");
        out.println("<a href='query.html'>Search Records</a>");
        out.println("<a href='scrap.html'>Scrap Records</a>");
        out.println("</nav>");
        
        out.println("<h1>Scrap Records</h1>");
        out.println("<form action=\"/scrap\" method=\"post\">");

        out.println("<label for=\"day\">Day:</label>");
        out.println("<input type=\"text\" id=\"day\" name=\"day\">");
        
        out.println("<label for=\"start_date\">Start date:</label>");
        out.println("<input type=\"text\" id=\"start_date\" name=\"start_date\">");
       
        out.println("<label for=\"end_date\">End date:</label>");
        out.println("<input type=\"text\" id=\"end_date\" name=\"end_date\">");
        
        out.println("<label for=\"start_time\">Start time:</label>");
        out.println("<input type=\"text\" id=\"start_time\" name=\"start_time\">");
        
        out.println("<label for=\"end_time\">End time:</label>");
        out.println("<input type=\"text\" id=\"end_time\" name=\"end_time\">");
        
        out.println("<input type=\"submit\" value=\"Scrap!\" id=\"scrap\" >");

        out.println("<label for=\"save\">Save directly to database and do not display:</label>");
        out.println("<input type=\"checkbox\" value=\"true\" id=\"save\" name=\"save\" >");
        
        out.println("</form>");
        out.println("</body></html>");
    }
}
