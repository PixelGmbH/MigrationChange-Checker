package de.pixel.mcc;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MigrationChangeCheckerTest {

    private static final Path filePath = Paths.get("src/test/resources/drop-tables.sql");

    @Test
    void useMd5() {
        //WHEN

        //THEN
        final Executable test = () ->
                MigrationChangeChecker.setup()
                        .withHashAlgorithm(HashAlgorithm.MD5)
                        .withHashPair("drop-tables.sql", "27ced4797d3267eb7eb64eacad7078f8")
                        .verifyFile(filePath);

        //VERIFY
        assertDoesNotThrow(test);
    }

    @Test
    void useSHA() {
        //WHEN

        //THEN
        final Executable test = () ->
                MigrationChangeChecker.setup()
                        .withHashAlgorithm(HashAlgorithm.SHA2_256)
                        .withHashPair("drop-tables.sql", "d935cbc38978caa65f78bba917e18a07a8718a336aa69e89f5f09d0796ecac81")
                        .verifyFile(filePath);

        //VERIFY
        assertDoesNotThrow(test);
    }

    @Test
    void setupMultipleFiles() {
        //WHEN

        //THEN
        final Executable test = () ->
                MigrationChangeChecker.setup()
                        .withHashAlgorithm(HashAlgorithm.SHA2_256)
                        .withHashPair("magic-file.sql", "1234567891234567891234567891234567891234567891234567891234567891")
                        .withHashPair("drop-tables.sql", "d935cbc38978caa65f78bba917e18a07a8718a336aa69e89f5f09d0796ecac81")
                        .verifyFile(filePath);

        //VERIFY
        assertDoesNotThrow(test);
    }

    @Test
    void invalidHash() {
        //WHEN

        //THEN
        final Executable test = () ->
                MigrationChangeChecker.setup()
                        .withHashPair("drop-tables.sql", "123-56")
                        .verifyFile(filePath);

        //VERIFY
        assertThrows(AssertionError.class, test);
    }

    @Test
    void fileNotFoundInMap() {
        //WHEN

        //THEN
        final Executable test = () ->
                MigrationChangeChecker.setup()
                        .withHashPair("wrong-sql.sql", "")
                        .verifyFile(filePath);

        //VERIFY
        assertThrows(AssertionError.class, test);
    }

    @Test
    void defaultHashMethod() {
        //WHEN

        //THEN
        final Executable test = () ->
                MigrationChangeChecker.setup()
                        .withHashPair("drop-tables.sql", "d935cbc38978caa65f78bba917e18a07a8718a336aa69e89f5f09d0796ecac81")
                        .verifyFile(filePath);

        //VERIFY
        assertDoesNotThrow(test);
    }
}
