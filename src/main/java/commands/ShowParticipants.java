package commands;

import core.Database;
import core.Participant;
import core.State;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public class ShowParticipants implements Command {

    @Override
    public String perform(String[] args, Update update, State state) {
        List<Participant> people = new Database().getAllParticipants();
        StringBuilder sb = new StringBuilder();

        for (Participant p : people) {
            sb.append(String.format("%d) %s\n", p.getId(), p.toString()));
        }

        return sb.toString();
    }
}
