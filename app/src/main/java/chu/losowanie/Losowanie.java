package chu.losowanie;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;


public class Losowanie extends Activity {

    LinkedList<String> zadania= new LinkedList<String>();
    TextView tresc;
    Button losuj;
    Random rand = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_losowanie);

        tresc = (TextView) findViewById(R.id.tresc);
        losuj = (Button) findViewById(R.id.losuj);

        DataBaseHelper myDbHelper = new DataBaseHelper(this);

        try
        {
            myDbHelper.createDataBase();
            //tresc.setText("sukces");
        }
        catch (IOException ioe)
        {
            //tresc.setText("blad");
            throw new Error("Unable to create database");
        }

        try
        {
            myDbHelper.openDataBase();
            //tresc.setText("SUCCESS!");

        }
        catch(SQLException sqle)
        {
            try {
                throw sqle;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        /*zadania.addLast("Pokaż termofor");
        zadania.addLast("Udawaj alpakę");
        zadania.addLast("Udawaj kromkę chleba");
        zadania.addLast("Pokaż portfel");*/

        Cursor k = myDbHelper.dajWszystkie();
        while(k.moveToNext()){
            int id=k.getInt(0);
            String tresc=k.getString(1);
            zadania.addLast(tresc);
        }

        Collections.shuffle(zadania);

        losuj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*int i = rand.nextInt(4);

                tresc.setText(zadania.get(i));*/

                //test

                tresc.setText(zadania.getFirst());
                zadania.removeFirst();

                if (zadania.size() == 0) {
                    tresc.setText("Koniec zadań");
                    losuj.setOnClickListener(null);
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.losowanie, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
