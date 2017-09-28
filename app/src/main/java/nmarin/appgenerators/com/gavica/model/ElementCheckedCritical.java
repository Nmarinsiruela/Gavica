package nmarin.appgenerators.com.gavica.model;


public class ElementCheckedCritical {
    private String element;
    private int checked;
    private int critical;

    public ElementCheckedCritical(String element, int checked, int critical) {
        this.element = element;
        this.checked = checked;
        this.critical = critical;
    }

    public String getElement() {
        return element;
    }

    public void setElement(String element) {
        this.element = element;
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
}
