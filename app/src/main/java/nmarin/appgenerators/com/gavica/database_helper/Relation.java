package nmarin.appgenerators.com.gavica.database_helper;


public class Relation {
    long id;
    long comp_cosa;
    long suit;
    int checked;
    int critical;
    int attributes;



    public Relation() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getComp_cosa() {
        return comp_cosa;
    }
    public void setComp_cosa(long comp_cosa) {
        this.comp_cosa = comp_cosa;
    }

    public long getSuit() {
        return suit;
    }

    public void setSuit(long suit) {
        this.suit = suit;
    }

    public int getChecked() {
        return checked;
    }

    public void setChecked(int checked) {
        this.checked = checked;
    }

    public int getCritical() {
        return critical;
    }

    public void setCritical(int critical) {
        this.critical = critical;
    }

    public int getAttributes() {
        return attributes;
    }

    public void setAttributes(int attributes) {
        this.attributes = attributes;
    }
}
