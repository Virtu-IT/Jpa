package be.abis.exercise;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class DataSourceTest {

    @Autowired
    DataSource dataSource;

    @Test
    public void connectionIsSetUpCorrectly() throws SQLException {
        assertNotNull(dataSource);
        try (Connection con = dataSource.getConnection()) {
            assertFalse(con.isClosed());
            System.out.println("connected to " + con.getMetaData().getURL());
        }
    }
}
