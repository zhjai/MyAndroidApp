package com.example.exam;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BottomSheetDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BottomSheetDialogFragment extends com.google.android.material.bottomsheet.BottomSheetDialogFragment {

    public interface BottomSheetListener {
        void onOptionClicked(String text);
    }

    private BottomSheetListener mListener;

    public BottomSheetDialogFragment() {
        // Required empty public constructor
    }

    public static BottomSheetDialogFragment newInstance() {
        BottomSheetDialogFragment fragment = new BottomSheetDialogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_bottom_sheet_dialog, container, false);

        TextView nameSort = rootView.findViewById(R.id.name_sort);
        TextView dateSort = rootView.findViewById(R.id.date_sort);
        TextView importanceSort = rootView.findViewById(R.id.importance_sort);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onOptionClicked(((TextView)v).getText().toString());
                dismiss();
            }
        };

        nameSort.setOnClickListener(listener);
        dateSort.setOnClickListener(listener);
        importanceSort.setOnClickListener(listener);

        return rootView;
    }

    public void setBottomSheetListener(BottomSheetListener listener) {
        mListener = listener;
    }
}