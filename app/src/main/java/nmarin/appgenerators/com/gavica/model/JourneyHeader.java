package nmarin.appgenerators.com.gavica.model;

public class JourneyHeader {
    final String mTitle;
    final String mdateStart;
    final String mdateReturn;

    public JourneyHeader(String title, String dateStart, String dateReturn) {
        mTitle = title;
        mdateStart = dateStart;
        mdateReturn= dateReturn;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getdateStart() {
        return mdateStart;
    }

    public String getdateReturn() {
        return mdateReturn;
    }



}