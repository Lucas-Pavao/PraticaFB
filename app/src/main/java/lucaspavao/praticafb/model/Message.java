package lucaspavao.praticafb.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Message {
    private final String name;
    private final String text;
    public Message() {
        this.name = null;
        this.text = null;
    }
    public Message(String name, String text) {
        this.name = name;
        this.text = text;
    }
    public String getName() {
        return name;
    }
    public String getText() {
        return text;
    }
}
