package nmarin.appgenerators.com.gavica.database_helper;


public class Comp_Cosa {
    long id;
    long cosa;
    long comp;

    public Comp_Cosa() {
    }

    public Comp_Cosa(long id, long cosa, long comp) {
        this.id = id;
        this.cosa = cosa;
        this.comp = comp;

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCosa() {
        return cosa;
    }

    public void setCosa(long cosa) {
        this.cosa = cosa;
    }

    public long getComp() {
        return comp;
    }

    public void setComp(long comp) {
        this.comp = comp;
    }
}
