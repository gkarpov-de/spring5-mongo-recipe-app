package gk.recipeapp.converters;

import gk.recipeapp.commands.NotesCommand;
import gk.recipeapp.domain.Notes;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class NotesCommandToNotes implements Converter<NotesCommand, Notes> {
    @Override
    public Notes convert(@Nullable final NotesCommand notesCommand) {
        if (notesCommand == null) {
            return null;
        }

        final Notes notes = new Notes();
        notes.setId(notesCommand.getId());
        notes.setRecipeNotes(notesCommand.getRecipeNotes());
        return notes;
    }
}
