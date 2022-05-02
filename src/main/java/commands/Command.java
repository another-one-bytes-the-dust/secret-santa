package commands;

import core.State;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface Command {
    String perform(String[] args, Update update, State state);
}
