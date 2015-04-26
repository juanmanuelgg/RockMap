package moviles.uniandes.com.rockmapv2;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.List;

import mundo.DBManager;
import mundo.RockMap;

/**
 * Created by Pablo Mesa on 07/03/2015.
 */
public class FragmentRutas extends Fragment {

    private ListView rutasPorHacer;
    private Cursor cursor;
    private RockMap mundo;
    private SimpleCursorAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.tab_rutas, null);
        rutasPorHacer = (ListView)rootView.findViewById(R.id.rutasPorHacer);
        mundo = new RockMap(getActivity());
        cursor = mundo.darRutasPorHacer();
        String[] from = new String[]{DBManager.CN_RUTA_NOMBRE, DBManager.CN_RUTA_DIFICULTAD};
        int[] to = new int[]{android.R.id.text1,android.R.id.text2};
        adapter = new SimpleCursorAdapter(getActivity(),android.R.layout.two_line_list_item,cursor,from,to,0);
        rutasPorHacer.setAdapter(adapter);
        rutasPorHacer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor c = (Cursor)parent.getItemAtPosition(position);
                String item = parent.getItemAtPosition(position).toString();
                Log.i("IMPORTANTE", item);

                String nombre = c.getString(c.getColumnIndex("nombre"));
                String dificultad = c.getString(c.getColumnIndex("dificultad"));
                Log.i("IMPORTANTE","Desplegando intent de ruta individual");
                Intent intent = new Intent(view.getContext(), rutaIndividualActivity.class);
                intent.putExtra("nombre",nombre);
                intent.putExtra("dificultad",dificultad);
                intent.putExtra("altura","50");
                intent.putExtra("agregar","agregar");
                startActivity(intent);
            }
        });

        return rootView;
    }
}
