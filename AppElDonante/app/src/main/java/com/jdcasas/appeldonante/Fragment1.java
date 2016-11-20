package com.jdcasas.appeldonante;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * Created by Usuario on 10/10/2016.
 */

public class Fragment1 extends Fragment {

    View rootView;
    Spinner sp_distrito;
    public static Fragment1 newInstance() {
        Fragment1 fragment = new Fragment1();
        return fragment;
    }

    public Fragment1() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate (R.layout.fragment_1, container, false);
        this.sp_distrito = (Spinner) rootView.findViewById(R.id.sp_distrito);
        loadSpinnerDistritos();
        return rootView;
    }

    private void loadSpinnerDistritos() {

        // Create an ArrayAdapter using the string array and a default spinner
        // layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this.getActivity(), R.array.distritos_lima, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        this.sp_distrito.setAdapter(adapter);

        // This activity implements the AdapterView.OnItemSelectedListener
       // this.sp_distrito.setOnItemSelectedListener(this.getActivity());
    }

}