package moviles.uniandes.com.rockmapv2;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

import mundo.RockMap;

/**
 * Created by Pablo Mesa on 07/03/2015.
 */
public class FragmentBuscar extends Fragment implements View.OnClickListener
{
    private RockMap mundo;
    private String txtoMin, txtoMax;
    private Spinner sp;
    SeekBar barraMinimo, barraMaximo;
    private int altura;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.tab_buscar, null);
    mundo = new RockMap(getActivity());
        mundo.setModoSeleccionado(getActivity().getIntent().getStringExtra("seleccion"));
        //Bara de minimo --------------------------------------------------------------------------
        barraMinimo=(SeekBar)rootView.findViewById(R.id.minima);
        final EditText txtmin=(EditText)rootView.findViewById(R.id.txtDifmin);
        txtoMin = mundo.verDificultadSeleccionada(barraMinimo.getProgress());
        txtmin.setText(txtoMin);

        barraMinimo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                txtoMin=mundo.verDificultadSeleccionada(progress);
                txtmin.setText(txtoMin);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        //Bara de maximo----------------------------------------------------------------------------
        barraMaximo=(SeekBar)rootView.findViewById(R.id.maxima);
        final EditText txtmax=(EditText)rootView.findViewById(R.id.txtDifmax);
        txtoMax=mundo.verDificultadSeleccionada(barraMaximo.getProgress());
        txtmax.setText(txtoMax);

        barraMaximo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                txtoMax=mundo.verDificultadSeleccionada(progress);
                txtmax.setText(txtoMax);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        //Spinner Parque seleccionado---------------------------------------------------------------

        sp = (Spinner)rootView.findViewById(R.id.spinParquesBusqueda);
        String[] names = new String[mundo.getParques().size()];
        for(int i=0; i<mundo.getParques().size(); i++) names[i]=mundo.getParques().get(i).toString();

        String[] from = new String[]{"nombre"};
        int[] to = new int[]{android.R.id.text1};
        Cursor cursor = mundo.darParques();
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(getActivity(), android.R.layout.simple_spinner_item,cursor,from,to,0);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adapter);

        //Number Picker altura maxima --------------------------------------------------------------
        final NumberPicker np=(NumberPicker)rootView.findViewById(R.id.numberPicker);
        np.setMinValue(0);
        np.setMaxValue(1000);
        np.setWrapSelectorWheel(false);
        np.setOnScrollListener(new NumberPicker.OnScrollListener() {
            @Override
            public void onScrollStateChange(NumberPicker view, int scrollState) {
                altura=np.getValue();
            }
        });

        //Boton aceptar ----------------------------------------------------------------------------
        Button okButton = (Button) rootView.findViewById(R.id.btnBuscar);
        okButton.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v)
    {
        if(barraMinimo.getProgress()>barraMaximo.getProgress())
        {
            showDialog("Error","EL valor minimo no puede ser mayor al maximo.");
            return;
        }
        Cursor c = (Cursor) sp.getSelectedItem();

        try {
            String est = c.getString(c.getColumnIndex("nombre"));
            //mundo.hacerBusqueda(barraMinimo.getProgress(),barraMaximo.getProgress(),altura,sp.getSelectedItem().toString());
            final NumberPicker np = (NumberPicker) v.findViewById(R.id.numberPicker);

            Cursor resp = mundo.busqueda(barraMinimo.getProgress(), barraMaximo.getProgress(), altura, est);
            Intent intent = new Intent(getActivity(), verRutasActivity.class);
            intent.putExtra("ProgressMin", barraMinimo.getProgress());
            intent.putExtra("ProgressMax", barraMaximo.getProgress());
            intent.putExtra("altura", altura);
            intent.putExtra("sp", est);

            startActivity(intent);
        }
        catch(Exception e)
        {
            showDialog("Alerta","No hay parques en el sistema, descargue los parques de su país");
        }

    }

    private void showDialog(String title, String message)
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle(title);
        alertDialog.setCancelable(false);
        alertDialog.setMessage(message);
        alertDialog.setPositiveButton("OK",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int id) {
            }
        });
        AlertDialog dialog= alertDialog.create();
        dialog.show();
    }
}
