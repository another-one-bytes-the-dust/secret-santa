package commands;

import core.Database;
import core.Participant;
import core.State;
import org.telegram.telegrambots.meta.api.objects.Update;

public class RemoveUser implements Command {

    @Override
    public String perform(String[] args, Update update, State state) {
        if (state != State.REGISTRATION) return "Данные запечатаны";

        try {
            int id = Integer.parseInt(args[1]);
            Database db = new Database();
            Participant participant = db.getParticipantById(id);
            if (participant == null) return "Никого не нашлось";

            db.deleteParticipant(id);
            return String.format("Непрошенный гость, %s, удалён!",
                    participant.getName());
        } catch (NumberFormatException e) {
            return "Ошибка в айди";
        }
    }
}