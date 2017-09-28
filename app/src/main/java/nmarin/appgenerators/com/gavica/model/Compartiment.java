package nmarin.appgenerators.com.gavica.model;


public class Compartiment {
    private int id;
    private String title;


    public Compartiment(){
    }

    public Compartiment(String title) {
        this.title = title;
    }

    public Compartiment(int idCompartiment, String titleComp) {
        this.id = idCompartiment;
        this.title = titleComp;
    }

    public int getId() {
        return id;
    }

    public void setId(int idCompartiment) {
        this.id = idCompartiment;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String titleComp) {
        this.title = titleComp;
    }
}
