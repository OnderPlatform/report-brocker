package tech.onder.core.models;

public class Meter {

    private String uuid;

    private String name;

    private String comment;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Meter(String uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }

    public static Meter generate(String id) {
        Meter m = new Meter();
        m.uuid = id;
        m.name = "name_" + id;
        m.comment = "comment_" + id;
        return m;
    }
}
