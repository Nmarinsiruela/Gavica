package nmarin.appgenerators.com.gavica.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.gordonwong.materialsheetfab.MaterialSheetFab;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nmarin.appgenerators.com.gavica.R;
import nmarin.appgenerators.com.gavica.database_helper.OpenHelper;
import nmarin.appgenerators.com.gavica.database_helper.Comp_Cosa;
import nmarin.appgenerators.com.gavica.model.Compartiment;
import nmarin.appgenerators.com.gavica.database_helper.Cosa;
import nmarin.appgenerators.com.gavica.model.ElementCheckedCritical;
import nmarin.appgenerators.com.gavica.model.ExpandableListAdapter;
import nmarin.appgenerators.com.gavica.database_helper.Maleta;
import nmarin.appgenerators.com.gavica.database_helper.Relation;

// TODO: Estudiar Menús para cosas y compartimentos.
// Añadir botón de Editar para Cosa, incluye atributos. Nueva activity. Listview que según pulsas salen los otros attr. Creas un nuevo Atributo, guardas y actualizas Relations.
// Añadir botón de Editar para Compartimiento. Solo nombre.
// Añadir botón de Borrar para ambas cosas.

public class MaletaActivity extends AppCompatActivity {
    OpenHelper mydb;

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<ElementCheckedCritical>> listDataChild;

    Maleta maleta;
    List<Relation> listRelations;


    private MaterialSheetFab materialSheetFab;
    private int statusBarColor;

    FloatingActionMenu materialDesignFAM;
    FloatingActionButton fabComp;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maleta_activity);


        // Llamo la BD, y saco la Maleta apropiada usando el Bundle.
        mydb = new OpenHelper(getApplicationContext());

        Bundle bundle = getIntent().getExtras();
        int maleta_id = bundle.getInt("maleta");

        maleta = mydb.getSuitcase(maleta_id);


        Toolbar toolbar = findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        // Erasing standard title
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        TextView myTextView = findViewById(R.id.toolbar_title);
        String textTitle = myTextView.getText().toString();
        myTextView.setText(textTitle + " - " + maleta.getTitle());

        // Si MALETA no está referenciada en Relations, no existe, por lo que se le debe crear los
        // relations base.

        listRelations = mydb.getAllBySuitcase(maleta);

        if (listRelations.size() == 0){
            mydb.createRelationsInitial(maleta);
            listRelations = mydb.getAllBySuitcase(maleta);
        }

        // get the listview
        expListView = findViewById(R.id.lvExp);

        // preparing list data
        prepareListData(listRelations);

        listAdapter = new ExpandableListAdapter(getApplicationContext(), listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);


        // Listview Group click listener
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                // Toast.makeText(getApplicationContext(),
                // "Group Clicked " + listDataHeader.get(groupPosition),
                // Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Expanded",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Collapsed",
                        Toast.LENGTH_SHORT).show();

            }
        });

        // Listview on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {

                // Al tocar, si el checked vale 0, se pone en 1. Si está en 1, se pone en 0.

                String cosaString = listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getElement();
                long cosa = mydb.getCosaIdFromTitle(cosaString);
                long compcosa = mydb.getCompCosaByIds(mydb.getCompIdFromTitle(listDataHeader.get(groupPosition)),cosa);

                Relation r = getRelationFromList(compcosa);

                int checked = r.getChecked();
                switch (checked) {
                    case 0: // Ahora sí se habilita.
                        r.setChecked(1);
                        mydb.updateRelation(r);
                        listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).setChecked(1);
                        break;
                    case 1: // Se deshabilita.
                        r.setChecked(0);
                        mydb.updateRelation(r);
                        listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).setChecked(0);
                        break;
                }
                listAdapter.notifyDataSetChanged();
                return false;
            }

        });

        // FAB Setup

        materialDesignFAM = findViewById(R.id.material_design_android_floating_action_menu);

        materialDesignFAM.getMenuIconView().setColorFilter(Color.parseColor("#000000"));

        fabComp = findViewById(R.id.material_design_floating_action_menu_comp);
//        fabComp.setColorNormal(R.color.theme_primary_dark2);
//        fabComp.setColorPressed(Color.parseColor("#024346"));
        fabComp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                materialDesignFAM.close(true);
                newComp();
            }
        });

        materialDesignFAM.setClosedOnTouchOutside(true);

        // Cambiarlo al primer elemento.
        materialDesignFAM.setOnMenuButtonClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (materialDesignFAM.isOpened()){
                    materialDesignFAM.close(true);
                    newCosa();

                }
                else {
                    materialDesignFAM.open(true);
                    materialDesignFAM.setMenuButtonLabelText("Nueva Cosa");

                }
            }
        });
        //  materialDesignFAM.setOnMenuButtonClickListener(this);


    } // FIN OnCreate


    private void prepareListData( List<Relation> listRelations) {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<ElementCheckedCritical>>();

        ArrayList<ArrayList<ElementCheckedCritical>> arrayElements = new ArrayList<ArrayList<ElementCheckedCritical>>();

        // Adding child data
        for (int x=0; x<listRelations.size(); x++){
            Comp_Cosa compCosa;
            compCosa = mydb.getCompCosa(listRelations.get(x).getComp_cosa());

            Compartiment comp = mydb.getComp(compCosa.getComp());
            Cosa cosa =  mydb.getCosa(compCosa.getCosa());
            Relation relation = listRelations.get(x);
            if (listDataHeader.contains(comp.getTitle())){
                int y = listDataHeader.lastIndexOf(comp.getTitle());
                arrayElements.get(y).add(new ElementCheckedCritical(cosa.getTitle(), relation.getChecked(), relation.getCritical()));
            }else{
                listDataHeader.add(comp.getTitle());
                ArrayList<ElementCheckedCritical> arrayNuevo = new ArrayList<>();
                arrayNuevo.add(new ElementCheckedCritical(cosa.getTitle(), relation.getChecked(), relation.getCritical()));
                arrayElements.add(arrayNuevo);
            }
        }

        // ListDataHeader contiene todos los elementos Comp, falta coger los Cosa.


        for (int x=0; x<arrayElements.size(); x++){
            listDataChild.put(listDataHeader.get(x), arrayElements.get(x));
        }
    }




    public void newCosa(){
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
        builderSingle.setTitle("Selecciona un Compartimiento:");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_singlechoice);
        for (int x=0; x<listDataHeader.size(); x++){
            arrayAdapter.add(listDataHeader.get(x));
        }

        builderSingle.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String strName = arrayAdapter.getItem(which);
                AlertDialog.Builder builderInner = new AlertDialog.Builder(MaletaActivity.this, R.style.myDialog);
                builderInner.setMessage("Compartimento: " +strName);
                builderInner.setTitle("Escribe la nueva Cosa");
                final EditText edittext = new EditText(getApplicationContext());
                //   edittext.setInputType(InputType.TYPE_CLASS_TEXT);
                edittext.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
                builderInner.setView(edittext);
                builderInner.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,int which) {
                        String YouEditTextValue = edittext.getText().toString();

                        long compcosa_id = mydb.createCosaComp(mydb.createThing(new Cosa(YouEditTextValue)), mydb.getCompIdFromTitle(strName));
                        mydb.createRelations(compcosa_id, maleta.getId(), 0, 0, 1);
                        listDataChild.get(strName).add(new  ElementCheckedCritical(YouEditTextValue, 0, 0)) ;
                        listAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });
                builderInner.show();
            }
        });
        builderSingle.show();
    }

    public void newComp(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final EditText edittext = new EditText(this);
     //   edittext.setInputType(InputType.TYPE_CLASS_TEXT);
        edittext.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        alert.setMessage("Insertar Compartimiento");
        alert.setTitle("¿Quieres añadir un nuevo compartimiento?");

        alert.setView(edittext);

        alert.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //What ever you want to do with the value
                String YouEditTextValue = edittext.getText().toString();
                listDataHeader.add(YouEditTextValue);
                mydb.createComp(new Compartiment(YouEditTextValue));
                listDataChild.put(YouEditTextValue, (new  ArrayList<ElementCheckedCritical>()));
                listAdapter.notifyDataSetChanged();
            }
        });

        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // if this button is clicked, just close
                // the dialog box and do nothing
                dialog.cancel();
            }
        });
        alert.show();
    }

    private int getStatusBarColor() {
        return getWindow().getStatusBarColor();
    }

    private void setStatusBarColor(int color) {
        getWindow().setStatusBarColor(color);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();

    }


    public Relation getRelationFromList(long compcosa_id){
        Relation r = new Relation();
        for (int x =0; x< listRelations.size(); x++){
            r = listRelations.get(x);
            if (r.getComp_cosa() == (compcosa_id)){
                return r;
            }
        }
        return r;
    }


}
