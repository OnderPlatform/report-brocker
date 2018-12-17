package tech.onder.meters.models;


public class Meter implements IEntity<String> {
    
    private String uuid;
    
    private String name;
    
    private String comment;
    
    /**
     * Factually, persistent relations consumer-supplier are against of the Onder concept.
     * This field has to be removed afterward
     */
    private String supplierId;
    
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
    
    public String getSupplierId() {
        return supplierId;
    }
    
    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }
    
    public Meter() {
    }
    
    @Override
    public String getId() {
        return this.uuid;
    }
    
    public static Meter generate(String id) {
        Meter m = new Meter(id, "name");
        m.comment = "comment_" + id;
        return m;
    }
    
}
