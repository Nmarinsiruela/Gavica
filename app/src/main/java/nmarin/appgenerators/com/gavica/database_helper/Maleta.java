package nmarin.appgenerators.com.gavica.database_helper;


public class Maleta {
    private String title;
    private int id;
    private String type;
    private String dateStart;
    private String dateReturn;


    public Maleta(){
    }

    public Maleta(String title, String type, String dateStart, String dateReturn) {
        this.title = title;
        this.type = type;
        this.dateStart = dateStart;
        this.dateReturn = dateReturn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDateStart() {
        return dateStart;
    }

    public void setDateStart(String dateStart) {
        this.dateStart = dateStart;
    }

    public String getDateReturn() {
        return dateReturn;
    }

    public void setDateReturn(String dateReturn) {
        this.dateReturn = dateReturn;
    }

    @Override
    public String toString() {
        return getTitle();
    }

}
