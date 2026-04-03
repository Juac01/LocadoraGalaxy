package model.dao;
import java.sql.*;
import java.util.Properties;
import java.io.FileInputStream;



public class ConectaBD {

	  public static Connection conexao() {
		  Connection conecta = null;
	  try {
		  Properties props = new Properties();
          props.load(new FileInputStream("config.properties")); 
          
	      String jdbcDriver = "com.mysql.cj.jdbc.Driver";
	      String dbUrl = props.getProperty("db.url");;
	      String dbUser = props.getProperty("db.user");
	      String dbPassword = props.getProperty("db.password");

	      // Load the MySQL JDBC driverrr
	      Class.forName(jdbcDriver);

	      // Establish the conecta
	      conecta = DriverManager.getConnection(dbUrl, dbUser, dbPassword);

	      if (conecta != null) {
	          System.out.println("Conexão bem-sucedida com o banco de dados MySQL!");
	      }
	  } catch (Exception e) {
	      System.out.println("Erro ao conectar ao banco de dados: " + e.getMessage());
	      e.printStackTrace();
	  }
	  return conecta;
	}


}
