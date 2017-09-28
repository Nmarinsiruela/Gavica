package nmarin.appgenerators.com.gavica.database_helper;


public class Cosa {
        int id;
        String title;

        public Cosa() {
        }

        public Cosa(String title) {
                this.title = title;
        }


        public int getId() {
                return id;
        }

        public void setId(int id) {
                this.id = id;
        }

        public String getTitle() {
                return title;
        }

        public void setTitle(String title) {
                this.title = title;
        }
}
