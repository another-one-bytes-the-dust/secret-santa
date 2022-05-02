package commands;

import core.Database;
import core.State;
import org.telegram.telegrambots.meta.api.objects.Update;

public class SetName implements Command {

    @Override
    public String perform(String[] args, Update update, State state) {
        if (state != State.REGISTRATION) return "Данные запечатаны почтовой службой санты";

        String chatId = update.getMessage().getChatId().toString();
        String name = update.getMessage().getText();
        String nameTrim = (name.length() > 100) ? name.substring(0, 100) : name;

        Database db = new Database();
        if (db.getParticipantByChatId(chatId) == null) {
            db.addParticipant(nameTrim, null, chatId);
        } else {
            db.changeParticipantName(nameTrim, chatId);
        }

        return String.format("Имя участника: %s", nameTrim);
    }
}
