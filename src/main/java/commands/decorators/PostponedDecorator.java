package commands.decorators;

import commands.Command;
import commands.Commands;
import core.State;
import org.telegram.telegrambots.meta.api.objects.Update;

public class PostponedDecorator implements Command {

    private final Command nextCommand;
    private final String message;

    public PostponedDecorator(Command nextCommand, String message) {
        this.nextCommand = nextCommand;
        this.message = message;
    }

    @Override
    public String perform(String[] args, Update update, State state) {
        String chatId = update.getMessage().getChatId().toString();
        Commands.postponedMap.put(chatId, nextCommand);

        return message;
    }
}