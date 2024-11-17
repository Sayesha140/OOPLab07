import java.io.*;

public class RegularUser extends User implements CanRead {
    public RegularUser(String userId, String username, String email, String password) {
        super(userId, username, email, password, "Regular");
    }

    @Override
    public void viewContents() {
        try (BufferedReader br = new BufferedReader(new FileReader("User.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    @Override
    public void performAction() {
        viewContents();
    }
}
