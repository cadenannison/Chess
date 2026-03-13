package dataacess;

import dataaccess.DataAccessException;
import dataaccess.MYSQLDataAccess;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

public class DataAccessTests {

    private static MYSQLDataAccess dataAccess;
    @BeforeAll
    static void makeDataAccess() throws DataAccessException {
        dataAccess = new MYSQLDataAccess();
    }

    @BeforeEach
    void clearDataAccess() throws DataAccessException {
        dataAccess.clear();
    }



}
