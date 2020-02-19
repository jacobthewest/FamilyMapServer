package DataAccessTest;

import DataAccess.Database;
import DataAccess.DatabaseException;
import Model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


//https://stackoverflow.com/questions/25878045/errorjava-invalid-source-release-8-in-intellij-what-does-it-mean


public class UserDaoTest {
    private Database db;
    private User user;

    @BeforeEach
    public void reset() {
        setDbToNull();
        setUserToNull();
    }

    public void setDbToNull() {
        this.db = null;
    }

    public void setUserToNull() {
        this.user = null;
    }
}

