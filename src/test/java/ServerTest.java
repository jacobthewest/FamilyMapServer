import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ServerTest {

    @Test
    public void testMain() {
        boolean codeWorked = true;
        try {
            Server server = new Server();
            String[] args = new String[]{"8080"};
            server.main(args);
        } catch (Exception e) {
            codeWorked = false;
        }
        assertTrue(codeWorked);
    }
}
