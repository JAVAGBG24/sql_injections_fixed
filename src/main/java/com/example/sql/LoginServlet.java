package com.example.sql;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
// en servlet med namnet "LoginServlet" som hanterar förfrågningar på "/login".
@WebServlet(name = "LoginServlet", urlPatterns = "/login")
public class LoginServlet extends HttpServlet {

    // hämtar databas-URL från applikationens konfigurationsfil via Spring-annotering.
    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // hanterar POST-förfrågningar från klienten. en POST innebär att skapa något, spara något till databasen

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        // hämtar username och lösenord från formulärdata som skickas i förfrågan.

        String sql = "SELECT * FROM users WHERE username = '" + username + "' AND password = '" + password + "'";
        // skapar en SQL-query för att hitta användaren baserat på användarnamn och lösenord.

        try (Connection connection = DriverManager.getConnection(dbUrl);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            // skapar en databasanslutning, ett SQL-statement och kör frågan.

            if (resultSet.next()) {
                response.getWriter().write("<h2>Login Successful</h2>");
                // om användaren hittas i databasen, skicka ett success till klienten.
            } else {
                response.getWriter().write("<h2>Invalid username or password</h2>");
                // om inga resultat hittas, skicka ett felmeddelande.
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("<h2>Error occurred: " + e.getMessage() + "</h2>");
            // hanterar eventuella undantag och skriver ut felmeddelandet.
        }
    }
}