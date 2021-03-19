package gk.recipeapp.commands;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NotesCommand {
    private String id;
    private String recipeNotes;
}
