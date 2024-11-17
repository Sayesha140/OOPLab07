import java.io.*;

public class PowerUser extends User implements CanRead, CanWrite {
    public PowerUser(String userId, String username, String email, String password) {
        super(userId, username, email, password, "Power");
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
    public void writeContent(String content) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("User.csv", true))) {
            bw.write(content);
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    @Override
    public void performAction() {
        viewContents();
        writeContent("NewUserID,NewUser,NewEmail,NewPassword,Regular");
    }
}
