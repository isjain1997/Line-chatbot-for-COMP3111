package com.example.bot.spring;

import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.*;
import java.net.URISyntaxException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;

@Slf4j
public class SQLDatabaseEngine extends DatabaseEngine {
	@Override
	String search(String text) throws Exception {
		//Write your code here
		
		String response = null;
		
		try {
		
		PreparedStatement stmt = getConnection().prepareStatement("SELECT response FROM coolChatbotTable where keyword like concat('%', ?, '%')" );
		stmt.setString(1, text);
		
		ResultSet rs = stmt.executeQuery();
		while (rs.next() ) {
			response = rs.getString("response");
			}
		rs.close();
		stmt.close();
		}
			catch (Exception e) {
				System.out.println(e);
			}
		if (response != null)
			return response;
		throw new Exception("NOT FOUND");
		}
	
	
	private Connection getConnection() throws URISyntaxException, SQLException {
		Connection connection;
		URI dbUri = new URI(System.getenv("DATABASE_URL"));

		String username = dbUri.getUserInfo().split(":")[0];
		String password = dbUri.getUserInfo().split(":")[1];
		String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath() +  "?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";

		log.info("Username: {} Password: {}", username, password);
		log.info ("dbUrl: {}", dbUrl);
		
		connection = DriverManager.getConnection(dbUrl, username, password);

		return connection;
	}

}

