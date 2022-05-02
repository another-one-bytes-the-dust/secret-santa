package commands.decorators;

import commands.Command;
import core.State;
import org.telegram.telegrambots.meta.api.objects.Update;

public class AdminDecorator implements Command {

    private final Command command;

    public AdminDecorator(Command command) {
        this.command = command;
    }

    private boolean isOpChatId(String chatId) {
        return chatId.equals(System.getenv("ADMIN_CHAT_ID")) ||
               chatId.equals(System.getenv("OWNER_CHAT_ID"));
    }

    @Override
    public String perform(String[] args, Update update, State state) {
        if (!isOpChatId(update.getMessage().getChatId().toString())) {
            return "Недостаточно прав для выполнения команды";
        }

        return this.command.perform(args, update, state);
    }
}
