package ours.team20.com.groupay.groupfragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ours.team20.com.groupay.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PaymentDetails extends Fragment {


    public PaymentDetails() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_payment_details, container, false);
    }


}
