package core;

import java.util.Arrays;

public class Participant {

    private final int id;
    private final String name;
    private final String department;
    private final String chat_id;
    private final boolean ready;

    public Participant(int id, String name, String department, String chat_id, boolean ready) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.chat_id = chat_id;
        this.ready = ready;
    }

    public Participant(int id, String name, String department, String chat_id) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.chat_id = chat_id;
        this.ready = false;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    public String getChatId() {
        return chat_id;
    }

    public boolean getReady() { return ready; }

    @Override
    public String toString() {
        return String.format("%s (%s)", name, department) + (ready ? " verified" : "");
    }
}
