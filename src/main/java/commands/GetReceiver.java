package commands;

import core.Database;
import core.Participant;
import core.State;
import org.telegram.telegrambots.meta.api.objects.Update;

public class GetReceiver implements Command {

    @Override
    public String perform(String[] args, Update update, State state) {
        if (state == State.REGISTRATION) return null;

        Database db = new Database();
        String chatId = update.getMessage().getChatId() + "";

        Participant santa = db.getParticipantByChatId(chatId);
        Participant receiver = db.getReceiverBySantaId(santa.getId());

        return String.format("Ваша пара на игру: %s\n", receiver.getName());
    }
}
