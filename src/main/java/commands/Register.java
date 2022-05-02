package commands;

import core.Database;
import core.Participant;
import core.State;
import org.telegram.telegrambots.meta.api.objects.Update;

public class Register implements Command{

    @Override
    public String perform(String[] args, Update update, State state) {
        if (state != State.REGISTRATION) return "Сбор заявок окончен, увы";

        String chatId = update.getMessage().getChatId() + "";
        Database db = new Database();
        Participant participant = db.getParticipantByChatId(chatId);

        if (participant == null || participant.getName() == null) {
            return "Санта пока не знает, как к тебе обращаться. Не забудь кликнуть" +
                "на кнопу 'Ввести ФИО' и заявить о себе!";
        } else if (participant.getDepartment() == null) {
            return "Осталось только указать отдел!";
        }

        db.applyParticipant(chatId);
        return "Поздравляю! Ты в списке участников. Жди дальнейших инструкций";
    }
}
