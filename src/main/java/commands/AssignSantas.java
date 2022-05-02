package commands;

import core.Database;
import core.Participant;
import core.State;
import org.telegram.telegrambots.meta.api.objects.Update;
import utils.PermutationUtils;

import java.util.*;
import java.util.stream.Collectors;

public class AssignSantas implements Command {

    @Override
    public String perform(String[] args, Update update, State state) {
        Database db = new Database();

        List<Participant> participants = db.getAllParticipants();
        List<Integer> santaIds = participants.stream().map(Participant::getId).collect(Collectors.toList());
        List<Integer> giftedPersons = PermutationUtils.derangementFrom(santaIds);
        Map<Participant, Participant> pairs = new LinkedHashMap<>();

        for (int i = 0; i < participants.size(); i++) {
            Participant santa = db.getParticipantById(santaIds.get(i));
            Participant giftedPerson = db.getParticipantById(giftedPersons.get(i));
            pairs.put(santa, giftedPerson);
        }

        db.assignSantas(pairs);

        return null;
    }
}
