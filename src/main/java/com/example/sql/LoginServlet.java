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
import java.sql.PreparedStatement;
import java.sql.ResultSet;

// en servlet med namnet "LoginServlet" som hanterar förfrågningar på "/login".
@WebServlet(name = "LoginServlet", urlPatterns = "/login")
public class LoginServlet extends HttpServlet {

    // hämtar databas-URL från applikationens konfigurationsfil via Spring-annotering.
    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String user;

    @Value("${spring.datasource.password}")
    private String pw;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // hanterar POST-förfrågningar från klienten. en POST innebär att skapa något, spara något till databasen

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        // hämtar username och lösenord från formulärdata som skickas i förfrågan.

        // SÅ HÄR FÅR MAN ABSOLUT INTE SKRIVA SQL QUERIES
        //String sql = "SELECT * FROM users WHERE username = '" + username + "' AND password = '" + password + "'";
        // matade in: ' OR '1'='1 på username och password och fick success!

        // SÅ HÄR SKA VI ALLTID GÖRA, SÄKERT SÄTT
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";

        // skapar en SQL-query för att hitta användaren baserat på användarnamn och lösenord.

        //Statement statement = connection.createStatement();
        //ResultSet resultSet = statement.executeQuery(sql)) {
        // skapar en databasanslutning, ett SQL-statement och kör frågan.
        try (Connection connection = DriverManager.getConnection(dbUrl);

             // ATT TESTA!
             // se till att du är tillbaka i ursprunsläge alltså att du använder user postgres
             // att du inte har ändrat tillbaka i din config fil (om du ändrat något)
             // skapa en ny databas och börja från scratch, kör sql från md filen så att du
             // har ett user table och två users
             // kör SELECT * FROM users så att du är 100% säker på att du har users i din databas

             // ändra rad 50 till detta:
             // DriverManager.getConnection(dbUrl, user, pw);

             // se då till att du har, och att du stavat 100% korrekt:
             //     @Value("${spring.datasource.username}")
             //     private String user;
             //     @Value("${spring.datasource.password}")
             //     private String pw;

             // testa att starta och gå till http://localhost:8080/login.html
             // logga in med admin och password
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

             // binda parametrarna för att förhindra sql injection
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            // if (resultSet.next())
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    response.getWriter().write("<h2>Login Successful</h2>");
                }
                else {
                    response.getWriter().write("<h2>Invalid username or password</h2>");
                    // om inga resultat hittas, skicka ett felmeddelande.
                }
                // om användaren hittas i databasen, skicka ett success till klienten.
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("<h2>Error occurred: " + e.getMessage() + "</h2>");
            // hanterar eventuella undantag och skriver ut felmeddelandet.
        }
    }
}
