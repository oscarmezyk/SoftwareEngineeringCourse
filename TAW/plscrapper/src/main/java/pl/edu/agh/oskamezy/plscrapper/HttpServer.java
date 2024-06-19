package pl.edu.agh.oskamezy.plscrapper;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;


public class HttpServer {
    public static void startHttpServer() throws Exception {
        Server server = new Server(8383);

        // Create a ResourceHandler to serve static content
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setResourceBase("src/main/resources/webapp");
        resourceHandler.setDirectoriesListed(true);
        resourceHandler.setWelcomeFiles(new String[]{"add.html", "display.html", "query.html","scrap.html"});

        // Create a ServletContextHandler for servlets
        ServletContextHandler servletHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        servletHandler.setContextPath("/");
        servletHandler.addServlet(new ServletHolder(new DisplayRecordsServlet()), "/display");
        servletHandler.addServlet(new ServletHolder(new QueryServlet()), "/query");
        servletHandler.addServlet(new ServletHolder(new AddRecordServlet()), "/add");
        servletHandler.addServlet(new ServletHolder(new ScrapServlet()), "/scrap");

        // Combine handlers
        org.eclipse.jetty.server.handler.HandlerList handlers = new org.eclipse.jetty.server.handler.HandlerList();
        handlers.setHandlers(new org.eclipse.jetty.server.Handler[]{resourceHandler, servletHandler});
        
        server.setHandler(handlers);
        server.start();
        server.join();
    }
}
