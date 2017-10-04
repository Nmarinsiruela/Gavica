package nmarin.appgenerators.com.gavica.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

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

// TODO: Hacer estas tareas.
// Añadir botón de Editar para Cosa, incluye atributos. Nueva activity. Listview que según pulsas salen los otros attr. Creas un nuevo Atributo, guardas y actualizas Relations.

public class MaletaActivity extends AppCompatActivity {
    OpenHelper mydb;

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<ElementCheckedCritical>> listDataChild;

    Maleta maleta;
    List<Relation> listRelations;

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

        registerForContextMenu(expListView);

        // Listview Group click listener
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
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


    } // FIN OnCreate

//    @Override
///**
// * This method will be called everytime a view register itself for floating context menu
// */
//    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//        super.onCreateContextMenu(menu, v, menuInfo);
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.context_menu, menu);
//    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        Log.i("", "Click");
        ExpandableListView.ExpandableListContextMenuInfo info = (ExpandableListView.ExpandableListContextMenuInfo) menuInfo;

        int type = ExpandableListView.getPackedPositionType(info.packedPosition);
        int groupPosition = ExpandableListView.getPackedPositionGroup(info.packedPosition);
        int childPosition = ExpandableListView.getPackedPositionChild(info.packedPosition);

        // Show context menu for groups
        if (type == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
            menu.setHeaderTitle("Categoría");
            menu.add(0, 0, 1,"Borrar");

            // Show context menu for children
        } else if (type == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
            menu.setHeaderTitle("Cosa");
            menu.add(0, 2, 1, "Editar");

            if (listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getCritical() == 0){
                menu.add(0, 1, 2, "Marcar como crítico");
            }else{
                menu.add(0, 1, 2, "Desmarcar como crítico");
            }


            menu.add(0, 0, 3, "Borrar");
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        ExpandableListView.ExpandableListContextMenuInfo info = (ExpandableListView.ExpandableListContextMenuInfo) item
                .getMenuInfo();

        int type = ExpandableListView.getPackedPositionType(info.packedPosition);
        int groupPosition = ExpandableListView.getPackedPositionGroup(info.packedPosition);
        int childPosition = ExpandableListView.getPackedPositionChild(info.packedPosition);

        if (type == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
            switch (item.getItemId()) {
                case 0:
                    deleteComp(groupPosition);
                    break;
            }


        } else if (type == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {

            long comp_id = mydb.getCompIdFromTitle(listAdapter.getGroup(groupPosition).toString());
            long cosa_id = mydb.getCosaIdFromTitle(listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getElement());
            switch (item.getItemId()) {
                case 0: // BORRAR
                    mydb.deleteCosa(cosa_id, comp_id, maleta.getId());
                    listDataChild.get(listDataHeader.get(groupPosition)).remove(childPosition);
                   // listDataChild.remove((listDataHeader.get(groupPosition)));

//                    listDataChild.remove(listDataHeader.get(groupPosition));
//                    listDataHeader.remove(groupPosition);
                    listAdapter.notifyDataSetChanged();
                    break;
                case 1: // CRITICO
                    Relation r = getRelationFromList(mydb.getCompCosaByIds(comp_id,cosa_id));

                    if (r.getCritical() ==0){
                        r.setCritical(1);
                        listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).setCritical(1);
                    }else{
                        r.setCritical(0);
                        listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).setCritical(0);
                    }
                    mydb.updateRelation(r);
                    listAdapter.notifyDataSetChanged();
                    break;
                case 2: // EDITAR
                    Toast.makeText(MaletaActivity.this, "EDITAR " +"Cosa : " + listAdapter.getChild(groupPosition,childPosition), Toast.LENGTH_SHORT).show();
                    break;
            }
            // do someting with child
        }

        return super.onContextItemSelected(item);
    }

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
                        boolean itExist = false;
                        for (int x =0; x<listRelations.size(); x++){

                            if (compcosa_id == listRelations.get(x).getComp_cosa()){
                                Toast.makeText(MaletaActivity.this, "Error. Cosa ya existente.", Toast.LENGTH_SHORT).show();
                                itExist = true;
                                break;
                            }
                        }
                        if (!itExist){
                            mydb.createRelations(compcosa_id, maleta.getId());
                            listDataChild.get(strName).add(new  ElementCheckedCritical(YouEditTextValue, 0, 0)) ;
                            listAdapter.notifyDataSetChanged();
                            dialog.dismiss();
                        }
                    }
                });
                builderInner.show();
            }
        });
        builderSingle.show();
    }

    public void newComp(){
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
        builderSingle.setTitle("Selecciona un Compartimiento:");

        final List<Compartiment> listaAllComp = mydb.getAllComps();

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_singlechoice);
        arrayAdapter.add("Insertar Nueva Categoría");

        // Agrego a la lista los compartimientos que NO están ya en la maleta.
        for (int x=0; x<listaAllComp.size(); x++){
            if (!listDataHeader.contains(listaAllComp.get(x).getTitle())){
                arrayAdapter.add(listaAllComp.get(x).getTitle());
            }
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

                if (strName.equals("Insertar Nueva Categoría")){
                    AlertDialog.Builder builderInner = new AlertDialog.Builder(MaletaActivity.this, R.style.myDialog);
                    builderInner.setMessage("Insertar Compartimiento");
                    final EditText edittext = new EditText(getApplicationContext());
                    //   edittext.setInputType(InputType.TYPE_CLASS_TEXT);
                    edittext.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
                    builderInner.setView(edittext);
                    builderInner.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,int which) {
                            String YouEditTextValue = edittext.getText().toString();
                            boolean itExist = false;
                            for (int x=0; x<listaAllComp.size(); x++){
                                if (YouEditTextValue.equals(listaAllComp.get(x).getTitle())){
                                    Toast.makeText(MaletaActivity.this, "Error. Categoría disponible en el sistema.", Toast.LENGTH_SHORT).show();
                                    itExist = true;
                                    break;
                                }
                            }

                            if (!itExist) {
                                listDataHeader.add(YouEditTextValue);
                                mydb.createComp(new Compartiment(YouEditTextValue));
                                listDataChild.put(YouEditTextValue, (new ArrayList<ElementCheckedCritical>()));
                                listAdapter.notifyDataSetChanged();
                                dialog.dismiss();
                            }else{
                                dialog.dismiss();
                            }

                        }
                    });
                    builderInner.show();
                }else{
                    // Ya existe la comp, solo debo sacarla.
                    listDataHeader.add(strName);
                    ArrayList<Integer> arrayInt = mydb.getCompCosaIDbyComp(mydb.getCompIdFromTitle(strName));
                    ArrayList<ElementCheckedCritical> arrayCosas = new ArrayList<ElementCheckedCritical>();

                    for (int x=0; x< arrayInt.size(); x++){
                        mydb.createRelations(arrayInt.get(x), maleta.getId());
                        Comp_Cosa compcosa = mydb.getCompCosa(arrayInt.get(x));
                        Cosa cosa = mydb.getCosa(compcosa.getCosa());
                        arrayCosas.add(new ElementCheckedCritical(cosa.getTitle(), 0, 0 ));
                    }

                    // Aquí, debo generar el nuevo ElemCheckCrit con los valores de BD.
                    listDataChild.put(strName, arrayCosas);
                    listAdapter.notifyDataSetChanged();
                }
            }
        });
        builderSingle.show();
    }


    public void deleteComp(final int groupPosition){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Borrar Compartimiento");
        alert.setMessage("Borrarás todas las cosas asociadas. ¿Quieres borrar este compartimiento?");

        alert.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //What ever you want to do with the value
                long comp_id = mydb.getCompIdFromTitle(listAdapter.getGroup(groupPosition).toString());
                mydb.deleteCompFromMaleta(comp_id, maleta.getId());
                listDataChild.remove(listDataHeader.get(groupPosition));
                listDataHeader.remove(groupPosition);
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
