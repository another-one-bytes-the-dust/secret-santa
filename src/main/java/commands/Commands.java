package commands;

import commands.decorators.AdminDecorator;
import commands.decorators.PostponedDecorator;

import java.util.*;

import static utils.ConstantStrings.*;

public class Commands {
    public static final Map<String, Command> commandMap = new HashMap<>();
    public static final HashMap<String, Command> postponedMap = new HashMap<>();

    static {
        commandMap.put("/generate", new AdminDecorator(new AssignSantas()));
        commandMap.put("/remove", new AdminDecorator(new RemoveUser()));
        commandMap.put("/show", new AdminDecorator(new ShowParticipants()));

        commandMap.put(REGISTER_BUTTON, new Register());
        commandMap.put(GET_RECEIVER_BUTTON, new GetReceiver());
        commandMap.put("/start", new Start());

        commandMap.put(SET_NAME_BUTTON,
                new PostponedDecorator(new SetName(), "Твоё ФИО:"));
        commandMap.put(SET_DEPARTMENT_BUTTON,
                new PostponedDecorator(new SetDepartment(), "Твой отдел:"));
    }
}
