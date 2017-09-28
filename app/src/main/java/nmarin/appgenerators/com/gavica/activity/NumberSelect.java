//package nmarin.appgenerators.com.gavica;
//
//import android.content.Intent;
//import android.graphics.Typeface;
//import android.os.Bundle;
//import android.support.v4.widget.DrawerLayout;
//import android.support.v7.app.ActionBarDrawerToggle;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
//import android.util.Log;
//import android.view.KeyEvent;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.inputmethod.EditorInfo;
//import android.widget.AdapterView;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ListView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.google.android.gms.analytics.HitBuilders;
//import com.google.android.gms.analytics.Tracker;
//
//import java.util.ArrayList;
//
//public class NumberSelect extends AppCompatActivity implements View.OnClickListener {
//    private EditText textoexportar;
//    // Variable para onBackPressed.
//    private boolean ifBack = false;
//
//    // Variables para DrawerLayout.
//    private ListView mDrawerList;
//    private RelativeLayout mDrawerPane;
//    private ActionBarDrawerToggle mDrawerToggle;
//    private DrawerLayout mDrawerLayout;
//    private final ArrayList<NavItem> mNavItems = new ArrayList<>();
//
//
//    //Analytics
//    /**
//     * The {@link Tracker} used to record screen views.
//     */
//    private Tracker mTracker;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.number_select);
//
//        // [START shared_tracker]
//        // Obtain the shared Tracker instance.
//        AnalyticsApplication application = (AnalyticsApplication) getApplication();
//        mTracker = application.getDefaultTracker();
//        // [END shared_tracker]
//
//        textoexportar = (EditText) findViewById(R.id.numeroJugadores);
//
//        Button btn = (Button) findViewById(R.id.next_button);
//        Typeface typefaceAmaranth = Typeface.createFromAsset(getAssets(), "fonts/Amaranth-Bold.ttf");
//        Typeface typefaceActor = Typeface.createFromAsset(getAssets(), "fonts/Actor-Regular.ttf");
//        btn.setTypeface(typefaceAmaranth);
//        btn.setOnClickListener(this);
//
//
//        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
//        setSupportActionBar(toolbar);
//        TextView myTextView = (TextView) findViewById(R.id.toolbar_title);
//        myTextView.setTypeface(typefaceAmaranth);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
//
//        // Cambiar fuente texto.
//        TextView baraja = (TextView) findViewById(R.id.textbaraja);
//        baraja.setTypeface(typefaceActor);
//
//        TextView hoy = (TextView) findViewById(R.id.texthoy);
//        hoy.setTypeface(typefaceActor);
//
//        TextView jug = (TextView) findViewById(R.id.textnumjugadores);
//        jug.setTypeface(typefaceActor);
//
//        textoexportar.setTypeface(typefaceActor);
//
//        // Permite usar el Done del botón del teclado virtual.
//        textoexportar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_DONE) {
//                    onClick(v);
//                }
//                return false;
//            }
//        });
//
//        // DrawerLayout
//        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
//        // mDrawerLayout.setStatusBarBackgroundColor(Color.BLACK);
//        // Populate the Navigtion Drawer with options
//        mDrawerPane = (RelativeLayout) findViewById(R.id.drawerPane);
//        mDrawerList = (ListView) findViewById(R.id.navList);
//        mNavItems.add(new NavItem("El Juego", "¿Cómo jugar?", R.drawable.ic_info_black_24px));
//        mNavItems.add(new NavItem("Contacto", "¿Quieres contarnos algo?", R.drawable.ic_email_black_24px));
//        DrawerListAdapter adapter = new DrawerListAdapter(this, mNavItems);
//        mDrawerList.setAdapter(adapter);
//
//        // Drawer Item click listeners
//        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
//
//        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.fin, R.string.app_name) {
//            @Override
//            public void onDrawerOpened(View drawerView) {
//                super.onDrawerOpened(drawerView);
//                invalidateOptionsMenu();
//            }
//
//            @Override
//            public void onDrawerClosed(View drawerView) {
//                super.onDrawerClosed(drawerView);
//                Log.d("LoadWindow", "onDrawerClosed: " + getTitle());
//
//                invalidateOptionsMenu();
//            }
//        };
//        mDrawerLayout.addDrawerListener(mDrawerToggle);
//
//    }        // FIN DE onCREATE
//
//    /* The click listner for ListView in the navigation drawer */
//    private class DrawerItemClickListener implements ListView.OnItemClickListener {
//        @Override
//        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            selectItemFromDrawer(position);
//        }
//    }
//
//
//    private boolean verifyNumber() {
//        String test = textoexportar.getText().toString();
//        if (test.equals("")){
//            Toast.makeText(getApplicationContext(), "¡Por favor, indica un número válido!", Toast.LENGTH_SHORT).show();
//            return false;
//        }
//        int max = Integer.parseInt(test);
//        if (test.matches("[0-9]+")) {
//            if (max > 7 || max < 2){
//                Toast.makeText(getApplicationContext(), "Continental: De 2 a 7 jugadores.", Toast.LENGTH_SHORT).show();
//                return false;
//            }
//            return true;
//        }
//        Toast.makeText(getApplicationContext(), "¡Por favor, indica un número válido!", Toast.LENGTH_SHORT).show();
//        return false;
//    }
//
//
//    @Override
//    public void onClick(View v) {
//        // [START custom_event]
//        mTracker.send(new HitBuilders.EventBuilder()
//                .setCategory("Click")
//                .setAction("NumberSelect-to-PlayerNames")
//                .build());
//
//        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
//        // [END custom_event]
//
//        if (verifyNumber()) {
//            Intent intent = new Intent(v.getContext(), PlayerNames.class);
//            intent.putExtra("numero", ((EditText) findViewById(R.id.numeroJugadores)).getText().toString());
//            startActivity(intent);
//            finish();
//        }
//    }
//
//
//    @Override
//    protected void onResume(){
//        super.onResume();
//        // [START shared_tracker]
//        // Obtain the shared Tracker instance.
//        mTracker.setScreenName(this.getClass().getSimpleName());
//        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
//        // [END shared_tracker]
//    }
//
//    @Override
//    public void onBackPressed() {
//        if (ifBack) {
//            // Otherwise defer to system default behavior.
//            super.onBackPressed();
//        }
//        ifBack = true;
//        Toast.makeText(getApplicationContext(), "Vuelve a pulsar el botón para salir.", Toast.LENGTH_SHORT).show();
//    }
//
//    /*
//* Called when a particular item from the navigation drawer
//* is selected.
//* */
//    private void selectItemFromDrawer(int position) {
//        switch (position){
//            case 0: //Salta el Dialog con el ViewPager
//
//                // [START custom_event]
//                mTracker.send(new HitBuilders.EventBuilder()
//                        .setCategory("Action")
//                        .setAction("TheGame")
//                        .build());
//                // [END custom_event]
//
//
//                mDrawerList.setItemChecked(position, true);
//                mDrawerLayout.closeDrawer(mDrawerPane);
//
//                Intent intentG = new Intent(getApplicationContext(), TheGame.class);
//                startActivity(intentG);
//                // No se mata la activity para que al volver, estén los datos anteriores.
//
//                break;
//            case 1: //Nueva Activity, About.
//                // [START custom_event]
//                mTracker.send(new HitBuilders.EventBuilder()
//                        .setCategory("Action")
//                        .setAction("About")
//                        .build());
//                // [END custom_event]
//                mDrawerList.setItemChecked(position, true);
//                mDrawerLayout.closeDrawer(mDrawerPane);
//
//                Intent intent = new Intent(getApplicationContext(), About.class);
//                startActivity(intent);
//                // No se mata la activity para que al volver, estén los datos anteriores.
//                break;
//            default:
//                break;
//
//        }
//        // Close the drawer
//
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Pass the event to ActionBarDrawerToggle
//        // If it returns true, then it has handled
//        // the nav drawer indicator touch event
//        if (mDrawerToggle.onOptionsItemSelected(item)) {
//            return true;
//        }
//        // Handle your other action bar items...
//        return super.onOptionsItemSelected(item);
//    }
//    @Override
//    protected void onPostCreate(Bundle savedInstanceState) {
//        super.onPostCreate(savedInstanceState);
//        mDrawerToggle.syncState();
//    }
//
////    public void forceCrash(View view) {
////        throw new RuntimeException("This is a crash");
////    }
//
//}