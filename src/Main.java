public class Main {
    public static void main(String[] args) {
        UserManager userManager = UserManager.getInstance();
        userManager.loadUsers();

        User user = userManager.authenticate("UserID123", "password123");
        if (user != null) {
            user.performAction();
        } else {
            System.out.println("Authentication failed!");
        }
    }
}
