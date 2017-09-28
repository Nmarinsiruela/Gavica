package nmarin.appgenerators.com.gavica.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import nmarin.appgenerators.com.gavica.R;
import nmarin.appgenerators.com.gavica.database_helper.OpenHelper;
import nmarin.appgenerators.com.gavica.database_helper.Maleta;


public class InsertDataDialog extends AppCompatActivity implements View.OnClickListener{
    OpenHelper mydb;
    int dateJourney[]; // 0 es día, 1 es mes, 2 es año, 3 es longSalida, 4 es longReturn
    boolean alreadyPressed = false;
    EditText dateStartET, dateReturnET;
    DatePickerDialog datePickerDialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.insert_data_calendar);
        //setContentView(R.layout.insert_data);

        //     mydb = new OpenHelper(this);
        mydb = new OpenHelper(getApplicationContext());

        Button btn = findViewById(R.id.buttonInsertData);
        dateStartET = findViewById(R.id.editStartJourney);
        dateReturnET = findViewById(R.id.editReturnJourney);

		//todo Probar que tira.
		EditText edittext = findViewById(R.id.titleJourneyET);
		edittext.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        btn.setOnClickListener(this);
        dateStartET.setOnClickListener(this);
        dateReturnET.setOnClickListener(this);
        dateReturnET.setEnabled(false);

        dateJourney = new int[3];
    }

    @Override
    public void onClick(View v) {

        int element = v.getId();

        switch(element) {
            case R.id.buttonInsertData:
                EditText nameJourney = findViewById(R.id.titleJourneyET);

                ArrayList<EditText> arrayET = new ArrayList<>();

                arrayET.add(nameJourney);
                arrayET.add(dateStartET);
                arrayET.add(dateReturnET);

                if (probarETs(arrayET)) {
                    Spinner spn = findViewById(R.id.spinner);
                    String JourneyType = spn.getSelectedItem().toString();
                    String textStart = dateStartET.getText().toString();
                    String textReturn = dateReturnET.getText().toString();

                    long maleta_id = mydb.createSuitCase(new Maleta(nameJourney.getText().toString(), JourneyType, textStart, textReturn));

                    Integer i = (int) maleta_id;

                    Intent intent = new Intent(getApplicationContext(), MaletaActivity.class);
                    intent.putExtra("maleta", i);
                    startActivity(intent);
                    finish();

                }
                break;
            case R.id.editReturnJourney:
                final Dialog dialogReturn = new Dialog(this);
                final CalendarView calendarReturn = new CalendarView(this);
                dialogReturn.setTitle("Introduce la Fecha de Llegada");

                String dateStartString = dateStartET.getText().toString();
                String parts[] = dateStartString.split("/");

                int day = Integer.parseInt(parts[0]);
                int month = Integer.parseInt(parts[1]);
                int year = Integer.parseInt(parts[2]);

                Calendar calendarR = Calendar.getInstance();
                calendarR.set(Calendar.YEAR, year);
                calendarR.set(Calendar.MONTH, month-1);
                calendarR.set(Calendar.DAY_OF_MONTH, day);

                long milliTime = calendarR.getTimeInMillis();

                calendarReturn.setDate (milliTime, true, true);

                calendarReturn.setMinDate(milliTime - 1000);

                dialogReturn.setContentView(calendarReturn);
                calendarReturn.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                    @Override
                    public void onSelectedDayChange(CalendarView view, int year, int month,
                                                    int dayOfMonth) {
                        dateJourney[0] =(dayOfMonth);
                        dateJourney[1] = (month)+1;
                        dateJourney[2] = (year);
                        String dateReturnString = String.valueOf(dayOfMonth) + "/" + String.valueOf(month+1) + "/" +String.valueOf(year);
                        dateReturnET.setText(dateReturnString);
                        dateReturnET.setEnabled(false);
                        dateStartET.setEnabled(false);
                        dialogReturn.dismiss();
                    }
                });
                dialogReturn.show();
                break;
            case R.id.editStartJourney:
                final Dialog dialogStart = new Dialog(this);
                dialogStart.setTitle("Introduce la Fecha de Salida");
                final CalendarView calendar = new CalendarView(this);

                calendar.setMinDate(System.currentTimeMillis() - 1000);
                dialogStart.setContentView(calendar);
                calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

                    @Override
                    public void onSelectedDayChange(CalendarView view, int year, int month,
                                                    int dayOfMonth) {
                        dateJourney[0] =(dayOfMonth);
                        dateJourney[1] = (month)+1;
                        dateJourney[2] = (year);
                        String dateStartString = String.valueOf(dayOfMonth) + "/" + String.valueOf(month+1) + "/" +String.valueOf(year);
                        dateStartET.setText(dateStartString);
                        dateReturnET.setEnabled(true);
                        dialogStart.dismiss();
                    }
                });
                dialogStart.show();
                break;
            default:
                break;
        }



//        // [START custom_event]
//        mTracker.send(new HitBuilders.EventBuilder()
//                .setCategory("Click")
//                .setAction("LoadWindow-to-NumberSelect")
//                .build());
//        // [END custom_event]

//        Intent intent = new Intent(v.getContext(), NumberSelect.class);
//        startActivity(intent);
//        finish();

    }

    private boolean probarTVs(ArrayList<TextView> et) {
        String test;
        for (int x = 0; x < et.size(); x++) {
            test = et.get(x).getText().toString();
            if (test.equals("")) {
                Toast.makeText(getApplicationContext(), "¡Por favor, indica la fecha del viaje!", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    private boolean probarETs(ArrayList<EditText> et) {
        String test;
        for (int x = 0; x < et.size(); x++) {
            test = et.get(x).getText().toString();
            if (test.equals("")) {
                Toast.makeText(getApplicationContext(), "¡Por favor, rellena todos los espacios!", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }
}
