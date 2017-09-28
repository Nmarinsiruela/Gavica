package nmarin.appgenerators.com.gavica.database_helper;


public class Atributo {
    int id;
    int numero;
    String atrib_1, atrib_2, atrib_3, atrib_4;

    public Atributo(){

    }

    public Atributo(int numero, String atrib1, String atrib2, String atrib3, String atrib4){
        this.numero = numero;
        this.atrib_1 = atrib1;
        this.atrib_2 = atrib2;
        this.atrib_3 = atrib3;
        this.atrib_4 = atrib4;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getAtrib_1() {
        return atrib_1;
    }

    public void setAtrib_1(String atrib_1) {
        this.atrib_1 = atrib_1;
    }

    public String getAtrib_2() {
        return atrib_2;
    }

    public void setAtrib_2(String atrib_2) {
        this.atrib_2 = atrib_2;
    }

    public String getAtrib_3() {
        return atrib_3;
    }

    public void setAtrib_3(String atrib_3) {
        this.atrib_3 = atrib_3;
    }

    public String getAtrib_4() {
        return atrib_4;
    }

    public void setAtrib_4(String atrib_4) {
        this.atrib_4 = atrib_4;
    }
}
