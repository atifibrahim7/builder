package application;

import java.time.chrono.AbstractChronology;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Scanner;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.*;
import java.sql.*;
import java.util.List;
import java.util.Map;
import javafx.scene.Scene;
import javafx.stage.*;
class DBHandler {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "abbasi123";

    private Connection conn;

    // Constructor to establish the connection
    public DBHandler() {
        try {
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("Database connected successfully.");
        } catch (SQLException e) {
            System.err.println("Error: Failed to connect to the database.");
            System.exit(1); // Terminate the program on connection failure
        }
    }

    public void test()
    {
        // Database credentials and URL

        // Test the connection
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            System.out.println("Connection successful!");

            // Create and execute a query
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT 1"); // Simple test query

            // Process the result
            while (resultSet.next()) {
                System.out.println("Query Result: " + resultSet.getInt(1));
            }
        } catch (Exception e) {
            System.out.println("Connection failed: " + e.getMessage());

        }
    }
    // Add a Profile
    public void addProfile(String username, String name, String email, String password, String type) {
    	String query = "INSERT INTO profile (username, name, email, password, type) VALUES (?, ?, ?, ?, ?::user_type)";

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setString(2, name);
            pstmt.setString(3, email);
            pstmt.setString(4, password);
            pstmt.setString(5, type);
            pstmt.executeUpdate();
            System.out.println("Profile added successfully.");
        } catch (SQLException e) {
            System.err.println("Error: Unable to add profile for username: " + username);
            e.printStackTrace(); // Print the stack trace for debugging
        }
    }


    // Retrieve all JobHunters
    public List<String> getAllJobHunters() {
        List<String> jobHunters = new ArrayList<>();
        String query = "SELECT * FROM JobHunter";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                String username = rs.getString("username");
                String name = rs.getString("name");
                jobHunters.add("Username: " + username + ", Name: " + name);
            }
        } catch (SQLException e) {
            System.err.println("Error: Unable to retrieve job hunters.");
            e.printStackTrace(); // Print the stack trace for debugging
        }
        return jobHunters;
    }

    public boolean isProfile(String username) {
        String query = "SELECT * FROM Profile WHERE username = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            return rs.next(); // Returns true if username exists, false if it doesn't
        } catch (SQLException e) {
            System.err.println("Error: Unable to check profile for username: " + username);
            return false; // Return false in case of database errors
        }
    }
    
    public boolean isCompany(String name) {
        String query = "SELECT * FROM Company WHERE name = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();
            return rs.next(); // Returns true if username exists, false if it doesn't
        } catch (SQLException e) {
            System.err.println("Error: Unable to check company" + name);
            return false; // Return false in case of database errors
        }
    }

    public Map<String, String> verify(String username, String password) {
        String query = "SELECT username, type FROM Profile WHERE username = ? AND password = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // Extract user details into a map
                    Map<String, String> userDetails = new HashMap<>();
                    userDetails.put("username", rs.getString("username"));
                    userDetails.put("type", rs.getString("type"));
                    return userDetails;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error: Unable to verify credentials for username: " + username);
            e.printStackTrace();
        }
        return null; // Return null if no user is found or an error occurs
    }

    public ResultSet getUser(String username,String type) {
        String query = "SELECT * FROM ? WHERE username = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setString(2, type);
            return pstmt.executeQuery();
        } catch (SQLException e) {
            System.err.println("Error: Unable to verify credentials for username: " + username);
            return null; // Return false in case of database errors
        }
    }
    
    public void add_jobhunter(String name,String username,String password,String email)
    {
    	String query = "INSERT INTO JobHunter (username, name, email, password) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setString(2, name);
            pstmt.setString(3, email);
            pstmt.setString(4, password);
            pstmt.executeUpdate();
            System.out.println("JH added successfully.");
        } catch (SQLException e) {
            System.err.println("Error: Unable to add JobHunter for username: " + username);
        }
    }
    public void add_recruiter(String name, String username, String password, String email) {
        String query = "INSERT INTO Recruiter (username, name, email, password) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setString(2, name);
            pstmt.setString(3, email);
            pstmt.setString(4, password);
            pstmt.executeUpdate();
            System.out.println("Recruiter added successfully.");
        } catch (SQLException e) {
            System.err.println("Error: Unable to add recruiter for username: " + username);
        }
    }
   
    
    public void add_employer(String name, String username, String password, String email, String company) {
    	 String query = "INSERT INTO Employer (username, name, email, password  , company ) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setString(2, name);
            pstmt.setString(3, email);
            pstmt.setString(4, password);
            pstmt.setString(5, company); 
            pstmt.executeUpdate();
            System.out.println("Employer added successfully.");
        } catch (SQLException e) {
            System.err.println("Error: Unable to add employer for username: " + username);
            e.printStackTrace();
        }
    }


    // Delete a Profile
    public void deleteProfile(String username) {
        String query = "DELETE FROM Profile WHERE username = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.executeUpdate();
            System.out.println("Profile deleted successfully.");
        } catch (SQLException e) {
            System.err.println("Error: Unable to delete profile for username: " + username);
        }
    }

    // Close the connection
    public void close() {
        try {
            if (conn != null) {
                conn.close();
                System.out.println("Connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("Error: Unable to close the database connection.");
        }
    }

}

class Resume{
    protected String education;
    protected String past_experience;
    protected String skills;

    public Resume()
    {

    }
    public Resume(String education, String past_experience, String skills){
        this.education = education;
        this.past_experience = past_experience;
        this.skills = skills;
    }
    public void display_resume(){
        System.out.println("Education: " + education);
        System.out.println("Experience: " + past_experience);
        System.out.println("Skills: " + skills);
    }
}

class Company
{
    protected String C_name;
    protected String address;
    protected String email;
    public Company(){}
    public Company(String C_name, String address, String email){
        this.C_name = C_name;
        this.address = address;
        this.email = email;
    }
}

abstract class profile{
    protected String name;
    protected String username;
    protected String email;
    protected String password;
}

class job_hunter extends profile{
    protected Resume resume;
    public Company company;
    public job_hunter(){}
    public job_hunter(String name,String username,String email,String password){
        this.name=name;
        this.username=username;
        this.email=email;
        this.password=password;
    }
    public void add_company(Company c)
    {
        company=c;
    }
    public void attach_resume(Resume r){
        resume=r;
    }
}
class employer extends profile{
    Company company;
}

class Recruiter extends profile{
    public Recruiter(String name,String username,String email,String password){
    this.name=name;
    this.username=username;
    this.email=email;
    this.password=password;
    }
}

class application
{
    job_hunter applicant;
    String date;
}

class job_vacancy
{
    Company company;
    String details;
    String requirements;
    String location;
    String date_posted;
    String deadline;
    ArrayList<application> applications;
    Recruiter recruiter;
}



public class Controller {
	
	
	
	@FXML
    private TextField usernameField; // For username input

    @FXML
    private PasswordField passwordField; // For password input

    
	
    static DBHandler db = new DBHandler();
    static String curr_user;
    static String curr_type;
    public void controller_start() {
    	db.test();
        System.out.println("Hello world!");
    }
    @FXML
    public void login() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (db.isProfile(username)) {
            Map<String, String> user = db.verify(username, password);
            if (user != null) {
                curr_user = user.get("username");
                curr_type = user.get("type");
                System.out.println("Login successful! Welcome, " + curr_user + " [" + curr_type + "]");
                UserSession.currentUsername = username;
                UserSession.currentRole = curr_type ; 
            } else {
                System.out.println("Invalid username or password.");
            }
        } else {
            System.out.println("No profile found with the provided username.");
        }
    }

    @FXML
    private TextField jhnameField, jhusernameField, jhemailField , companyField;

    @FXML
    private PasswordField jhpasswordField;

    @FXML
    public void register_job_hunter() {
        String username = jhusernameField.getText();
        String password = jhpasswordField.getText();
        String email = jhemailField.getText();
        String name = jhnameField.getText();

        if (db.isProfile(username)) {
            System.out.println("Profile already exists. Please login instead.");
            return;
        }

        db.addProfile(username, name, email, password, "JobHunter");
        db.add_jobhunter(name, username, password, email);
        curr_user = username;
        curr_type = "JobHunter";
        System.out.println("Registration successful! Welcome, " + curr_user);
        
        // After successful registration, redirect to login or home page
        goToLogin();
    }

    @FXML
    public void goToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Scene loginScene = new Scene(loader.load());
            Stage stage = (Stage) jhusernameField.getScene().getWindow();
            stage.setScene(loginScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    public void goToRegister() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("registerJH.fxml"));
            Scene registerScene = new Scene(loader.load());
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(registerScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void register_employer() {
        // Retrieve input from the fields
        String username = jhusernameField.getText().trim();
        String password = jhpasswordField.getText().trim();
        String email = jhemailField.getText().trim();
        String name = jhnameField.getText().trim();
        String company_name = companyField.getText().trim();

        // Check if the profile already exists
        if (db.isProfile(username)) {
            // Profile already exists, prompt for login or notify user
            System.out.println("Profile already exists. Please log in.");
            return;
        }

        // Check if the company exists
        if (db.isCompany(company_name)) {
            // Company doesn't exist, prompt for creation or notify user
            System.out.println("Company does not exist. Please register the company first.");
            return;
        }

        // Add the profile and employer details to the database
        db.addProfile(username, name, email, password, "Employer");
        db.add_employer(name, username, password, email, company_name);

        // Update the current user context
        curr_user = username;
        curr_type = "Employer";  // Update this to reflect the correct user type

        // Optionally, notify the user of successful registration
        System.out.println("Registration successful. Welcome, " + name + "!");
    }
    public void register_recruiter()
    {
        String username =jhusernameField.getText();
        String password=jhusernameField.getText();
        String email=jhusernameField.getText();
        String name=jhusernameField.getText();
        if(db.isProfile(username))
        {
            //profile already exists login?
        	return;
        }
        db.addProfile(username,name,email,password,"Recruiter");
        db.add_recruiter(name, username, password, email);
        curr_user = username;
        curr_type = "Recruiter";
    }

}



