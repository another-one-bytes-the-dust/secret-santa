package commands;

import core.Database;
import core.State;
import org.telegram.telegrambots.meta.api.objects.Update;

public class SetDepartment implements Command {

    @Override
    public String perform(String[] args, Update update, State state) {
        if (state != State.REGISTRATION) return "Уже никак";

        String chatId = update.getMessage().getChatId().toString();
        String department = update.getMessage().getText();
        String departmentTrim = (department.length() > 280) ? department.substring(0, 280) : department;

        Database db = new Database();
        if (db.getParticipantByChatId(chatId) == null) {
            db.addParticipant(null, departmentTrim, chatId);
        } else {
            db.changeParticipantDepartment(departmentTrim, chatId);
        }

        return String.format("Твой отдел: %s", departmentTrim);
    }
}