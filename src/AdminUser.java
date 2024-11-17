import java.io.*;

public class AdminUser extends User implements CanRead, CanWrite, CanModify, CanRenameFile {
    public AdminUser(String userId, String username, String email, String password) {
        super(userId, username, email, password, "Admin");
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
    public void modifyUserDetails(String userId, String newDetails) {

        try (BufferedReader br = new BufferedReader(new FileReader("User.csv"));
             BufferedWriter bw = new BufferedWriter(new FileWriter("User_temp.csv"))) {

            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith(userId)) {
                    bw.write(newDetails); // Replace with new details
                } else {
                    bw.write(line); // Keep the original line
                }
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error modifying user details: " + e.getMessage());
        }
        new File("User.csv").delete();
        new File("User_temp.csv").renameTo(new File("User.csv"));
    }

    @Override
    public void renameFile(String oldName, String newName) {
        File file = new File(oldName);
        if (file.renameTo(new File(newName))) {
            System.out.println("File renamed successfully.");
        } else {
            System.out.println("Failed to rename file.");
        }
    }

    @Override
    public void performAction() {
        viewContents();
        writeContent("NewAdminUserID,AdminUser,AdminEmail,AdminPassword,Admin");
        modifyUserDetails("UserID123", "UpdatedUserID,UpdatedUser,UpdatedEmail,UpdatedPassword,Regular");
        renameFile("User.csv", "UserBackup.csv");
    }
}
