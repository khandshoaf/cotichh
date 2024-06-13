package database;

import bot.obj.Category;
import bot.obj.Truyen;
import org.apache.avro.generic.GenericData;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static java.sql.DriverManager.getConnection;

public class Jdbc {
    private static final String URL = "jdbc:mysql://localhost:3306/dbStories";
    private static final String USER = "root";
    private static final String PASSWORD = "123456";

    public Jdbc() {
    }

    public static void saveStory(Truyen truyen) {
        String sql = "INSERT INTO truyen (title, img, linkUrl, info, sapo, category) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = conn.prepareStatement(sql)) {

            // Inserting the Truyen object data into the database
            statement.setString(1, truyen.getHeader());
            statement.setString(2, truyen.getImg());
            statement.setString(3, truyen.getUrl());
            statement.setString(4, truyen.getInfo());
            statement.setString(5, truyen.getContent());
            statement.setString(6, truyen.getCategory());

            statement.executeUpdate();
            statement.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void saveCategory(Category category) {
        String sql = "INSERT INTO theloai (category,link) VALUES (?, ?)";

        try (Connection conn = getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = conn.prepareStatement(sql)) {
            // Inserting the theer loai object data into the database
            statement.setString(1, category.getName());
            statement.setString(2, category.getLink());

            statement.executeUpdate();
            statement.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void saveBot(int index, String name, int number) throws SQLException {
        Connection conn = getConnection(URL, USER, PASSWORD);
        String sql = "INSERT INTO bot "
                + "VALUES(?,?,?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, index);
        pstmt.setString(2, name);
        pstmt.setInt(3, number);

        int rowAffected = pstmt.executeUpdate();

        pstmt.close();
        conn.close();
    }
    public static void savePage(int index, String name, String url) {
        Connection conn = null;
        try {
            conn = getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        String sql = "INSERT INTO page "
                + "VALUES(?,?,?,?,?)";
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, 0);
            pstmt.setString(2, name);
            pstmt.setString(3, url);
            pstmt.setInt(4, 0);
            pstmt.setInt(5, 0);

            int rowAffected = pstmt.executeUpdate();

            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("truyen da duoc update");

    }
    public String numberBotPage() {
        Connection conn = null;
        try {
            conn = getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        String sql = "SELECT number FROM bot where name = \"Bot page\"";
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                System.out.println(rs.getInt(1));
                String number = rs.getString(1);
                return number;
            }
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "loi truy van";
    }


    public void updateNumberBotPage(int number, String name) {
        Connection conn = null;
        try {
            conn = getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        String sql = "Update bot set number = \"" + number + "\" where name = \"" + name + "\"";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            int rowAffected = pstmt.executeUpdate();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean categoryExists(Category category) {
        boolean exists = false;
        String query = "SELECT COUNT(*) FROM theloai WHERE category = ? AND link = ?";
        Connection connection = null;

        try {
            connection = getConnection(URL, USER, PASSWORD);

            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, category.getName());
                statement.setString(2, category.getLink());

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        int count = resultSet.getInt(1);
                        exists = count > 0;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle any potential exceptions
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    // Handle closing connection exception
                }
            }
        }
        return exists;
    }

    public static boolean storyExists(Truyen truyen) {
        boolean exists = false;
        String query = "SELECT COUNT(*) FROM truyen WHERE title = ? AND linkUrl = ?";
        Connection connection = null;

        try {
            connection = getConnection(URL, USER, PASSWORD);

            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, truyen.getHeader());
                statement.setString(2, truyen.getUrl());

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        int count = resultSet.getInt(1);
                        exists = count > 0;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle any potential exceptions
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    // Handle closing connection exception
                }
            }
        }
        return exists;
    }
}
