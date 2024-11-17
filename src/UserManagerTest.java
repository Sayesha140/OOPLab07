import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class UserManagerTest {

    private UserManager userManager;

    @BeforeEach
    void setUp() throws IOException {

        File userFile = new File("User.csv");
        File adminFile = new File("Admin.csv");


        try (BufferedWriter userWriter = new BufferedWriter(new FileWriter(userFile))) {
            userWriter.write("U1,JohnDoe,johndoe@example.com,password,Regular\n");
            userWriter.write("U2,JaneSmith,janesmith@example.com,pass123,Power\n");
        }


        try (BufferedWriter adminWriter = new BufferedWriter(new FileWriter(adminFile))) {
            adminWriter.write("A1,AdminOne,adminone@example.com,adminpass\n");
        }


        userManager = UserManager.getInstance();
    }

    @Test
    void testAuthenticateRegularUser() {
        User user = userManager.authenticate("U1", "password");
        assertNotNull(user);
        assertEquals("Regular", user.getUserType());
        assertTrue(user instanceof RegularUser);
    }

    @Test
    void testAuthenticatePowerUser() {
        User user = userManager.authenticate("U2", "pass123");
        assertNotNull(user);
        assertEquals("Power", user.getUserType());
        assertTrue(user instanceof PowerUser);
    }

    @Test
    void testAuthenticateAdminUser() {
        User user = userManager.authenticate("A1", "adminpass");
        assertNotNull(user);
        assertEquals("Admin", user.getUserType());
        assertTrue(user instanceof AdminUser);
    }

    @Test
    void testInvalidAuthentication() {
        User user = userManager.authenticate("U1", "wrongpass");
        assertNull(user);
    }

    @Test
    void testAddRegularUser() {
        User newUser = new RegularUser("U3", "NewUser", "newuser@example.com", "newpass");
        userManager.addUser(newUser);

        User authenticatedUser = userManager.authenticate("U3", "newpass");
        assertNotNull(authenticatedUser);
        assertEquals("Regular", authenticatedUser.getUserType());
    }

    @Test
    void testAddAdminUser() {
        AdminUser newAdmin = new AdminUser("A2", "NewAdmin", "newadmin@example.com", "admin123");
        userManager.addUser(newAdmin);

        User authenticatedUser = userManager.authenticate("A2", "admin123");
        assertNotNull(authenticatedUser);
        assertEquals("Admin", authenticatedUser.getUserType());
    }
}
