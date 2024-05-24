import org.junit.Test;
import static org.junit.Assert.*;
import android.util.Patterns;

public class UserLoginScreenTest {

    @Test
    public void testEmailValidation() {
        assertTrue(isEmailValid("test@example.com"));
        assertTrue(isEmailValid("user.name @domain.co"));
        assertTrue(isEmailValid("user_name@domain.com"));

        assertFalse(isEmailValid("invalid-email"));
        assertFalse(isEmailValid("user@.com"));
        assertFalse(isEmailValid("user@domain"));
        assertFalse(isEmailValid("user@domain.c"));
    }

    @Test
    public void testPasswordValidation() {
        assertTrue(isPasswordValid("password123"));
        assertTrue(isPasswordValid("123456"));
        assertTrue(isPasswordValid("a1b2c3d4"));

        assertFalse(isPasswordValid("12345"));
        assertFalse(isPasswordValid("abc"));
        assertFalse(isPasswordValid(""));
        assertFalse(isPasswordValid(" "));
    }

    private boolean isEmailValid(String email) {
        return email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isPasswordValid(String password) {
        return password != null && password.length() >= 6;
    }
}
