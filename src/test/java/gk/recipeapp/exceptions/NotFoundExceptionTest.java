package gk.recipeapp.exceptions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class NotFoundExceptionTest {

    public static final String EXPECTED_STRING = "expected string";

    @Test
    @DisplayName("test no args notfound exception constructor")
    void testNoArgsNotfoundExceptionConstructor() {
        assertThrows(NotFoundException.class, () -> {
            throw new NotFoundException();
        });
    }

    @Test
    @DisplayName("test one atg notfound exception constructor")
    void testOneAtgNotfoundExceptionConstructor() {
        final var exception = assertThrows(NotFoundException.class, () -> {
            throw new NotFoundException(EXPECTED_STRING);
        });
        assertEquals(EXPECTED_STRING, exception.getMessage());
    }

}