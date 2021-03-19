package gk.recipeapp.converters;

import gk.recipeapp.commands.NotesCommand;
import gk.recipeapp.domain.Notes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class NotesCommandToNotesTest {
    private static final String ID_VALUE = "1";
    private static final String RECIPE_NOTES = "abrakadabra";

    NotesCommandToNotes converter;

    @BeforeEach
    void setUp() {
        converter = new NotesCommandToNotes();
    }

    @Test
    @DisplayName("test null")
    void testNull() {
        assertNull(converter.convert(null));
    }

    @Test
    @DisplayName("test empty object")
    void testEmptyObject() {
        assertNotNull(converter.convert(new NotesCommand()));
    }

    @Test
    @DisplayName("test conversion")
    void testConversion() {
        final NotesCommand notesCommand = new NotesCommand();
        notesCommand.setRecipeNotes(RECIPE_NOTES);

        final Notes notes = converter.convert(notesCommand);

        assertNotNull(notes);
        assertEquals(RECIPE_NOTES, notes.getRecipeNotes());
    }
}
