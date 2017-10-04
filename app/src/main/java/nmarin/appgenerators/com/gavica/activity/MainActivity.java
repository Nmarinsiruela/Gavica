package nmarin.appgenerators.com.gavica.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import nmarin.appgenerators.com.gavica.database_helper.AndroidDatabaseManager;
import nmarin.appgenerators.com.gavica.database_helper.OpenHelper;
import nmarin.appgenerators.com.gavica.model.JourneyHeader;
import nmarin.appgenerators.com.gavica.model.JourneyListAdapter;
import nmarin.appgenerators.com.gavica.database_helper.Maleta;
import nmarin.appgenerators.com.gavica.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  {

    private final ArrayList<JourneyHeader> mNavItems = new ArrayList<>();
    OpenHelper mydb;
    List<Maleta> listMaleta;
    JourneyListAdapter listAdapter;
    int idMaletaRemove;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Fabric.with(this, new Crashlytics());


        Toolbar toolbar = findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);
        TextView myTextView = findViewById(R.id.toolbar_title);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
//

        mydb = new OpenHelper(getApplicationContext());

        listMaleta = mydb.getAllSuitcases();

        if (listMaleta.size() != 0) {
            for (int x = 0; x < listMaleta.size(); x++) {
                mNavItems.add(new JourneyHeader(listMaleta.get(x).getTitle(), listMaleta.get(x).getDateStart(), listMaleta.get(x).getDateReturn()));
            }
        }

        ListView lv = findViewById(R.id.navList);


        listAdapter =new JourneyListAdapter(this, mNavItems);
        lv.setAdapter(listAdapter);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (listMaleta.size() == 0) {
                    Intent intent = new Intent(getApplicationContext(), InsertDataDialog.class);
                    startActivity(intent);
                    finish();
                }else {


                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            MainActivity.this);

                    // set title
                    alertDialogBuilder.setTitle("Creación de nuevo viaje");

                    // set dialog message
                    alertDialogBuilder
                            .setMessage("¿Quieres crear un nuevo viaje?")
                            .setCancelable(false)
                            .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // if this button is clicked, close
                                    // current activity
                                    Intent intent = new Intent(getApplicationContext(), InsertDataDialog.class);
                                    startActivity(intent);
                                    finish();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // if this button is clicked, just close
                                    // the dialog box and do nothing
                                    dialog.cancel();
                                }
                            });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();
                }
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
                int numberSuitcase = listMaleta.get(position).getId();
                Intent intent = new Intent(getApplicationContext(), MaletaActivity.class);
                intent.putExtra("maleta", numberSuitcase );
                startActivity(intent);
                finish();
            }
        });

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                idMaletaRemove = i;
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        MainActivity.this);

                // set title
                alertDialogBuilder.setTitle("Borrar viaje");

                // set dialog message
                alertDialogBuilder
                        .setMessage("¿Quieres borrar este viaje?")
                        .setCancelable(false)
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, close
                                // current activity
                                mydb.deleteMaleta(listMaleta.get(idMaletaRemove).getId());
                                mNavItems.remove(idMaletaRemove);
                                listMaleta.remove(idMaletaRemove);
                                listAdapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                dialog.cancel();
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
                return true;
            }

        });


        // TEST DB
        TextView tv = findViewById(R.id.yourtextviewid);

        tv.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent dbmanager = new Intent(getApplicationContext(),AndroidDatabaseManager.class);
                startActivity(dbmanager);
            }
        });
    } // FIN onCREATE


}
