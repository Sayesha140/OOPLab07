import java.io.*;
import java.util.*;


public class UserManager {
    private static UserManager instance;
    private Map<String, User> users = new HashMap<>();
    private Map<String, AdminUser> adminUsers = new HashMap<>();

    private UserManager() {
        loadUsers();
        loadAdminUsers();
    }

    public static synchronized UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }


    void loadUsers() {
        try (BufferedReader br = new BufferedReader(new FileReader("User.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] details = line.split(",");
                if (details.length != 5) continue;

                String userId = details[0];
                String username = details[1];
                String email = details[2];
                String password = details[3];
                String userType = details[4];

                User user;
                if ("Regular".equalsIgnoreCase(userType)) {
                    user = new RegularUser(userId, username, email, password);
                } else if ("Power".equalsIgnoreCase(userType)) {
                    user = new PowerUser(userId, username, email, password);
                } else {
                    continue;
                }
                users.put(userId, user);
            }
        } catch (IOException e) {
            System.out.println("Error loading users from User.csv: " + e.getMessage());
        }
    }


    private void loadAdminUsers() {
        try (BufferedReader br = new BufferedReader(new FileReader("Admin.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] details = line.split(",");
                if (details.length != 4) continue;

                String userId = details[0];
                String username = details[1];
                String email = details[2];
                String password = details[3];

                AdminUser admin = new AdminUser(userId, username, email, password);
                adminUsers.put(userId, admin);
            }
        } catch (IOException e) {
            System.out.println("Error loading admin users from Admin.csv: " + e.getMessage());
        }
    }


    public User authenticate(String userId, String password) {
        if (adminUsers.containsKey(userId)) {
            AdminUser admin = adminUsers.get(userId);
            if (admin.getPassword().equals(password)) {
                return admin;
            }
        } else if (users.containsKey(userId)) {
            User user = users.get(userId);
            if (user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }


    public void addUser(User user) {
        if (user instanceof AdminUser) {
            adminUsers.put(user.getUserId(), (AdminUser) user);
            saveAdminUsers();
        } else {
            users.put(user.getUserId(), user);
            saveUsers();
        }
    }


    private void saveUsers() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("User.csv"))) {
            for (User user : users.values()) {
                bw.write(String.join(",", user.getUserId(), user.getUsername(), user.getEmail(), user.getPassword(), user.getUserType()));
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving users to User.csv: " + e.getMessage());
        }
    }


    private void saveAdminUsers() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("Admin.csv"))) {
            for (AdminUser admin : adminUsers.values()) {
                bw.write(String.join(",", admin.getUserId(), admin.getUsername(), admin.getEmail(), admin.getPassword()));
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving admin users to Admin.csv: " + e.getMessage());
        }
    }
}
