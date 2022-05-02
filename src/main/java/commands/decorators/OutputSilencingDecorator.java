package commands.decorators;

import commands.Command;
import core.State;
import org.telegram.telegrambots.meta.api.objects.Update;

public class OutputSilencingDecorator implements Command {

    private final Command command;

    public OutputSilencingDecorator(Command command) {
        this.command = command;
    }

    @Override
    public String perform(String[] args, Update update, State state) {
        command.perform(args, update, state);
        return null;
    }
}
