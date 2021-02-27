package com.example.zkt.ui.fragment;

import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.zkt.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExplainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExplainFragment extends Fragment {

    ArrayList<Integer> starts = new ArrayList<>();
    ArrayList<Integer> ends = new ArrayList<>();
    TextView txt_test;
    int type;
    String str;

    public ExplainFragment(int type) {
        this.type = type;
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_explain, container, false);
        txt_test = view.findViewById(R.id.txt_test);
        if (type==1) {
            str = getString(R.string.message_jiehsao);
        }else {
             str = getString(R.string.message_baozhang);
        }

            SpannableString string = new SpannableString(str);
            for (int i = 0; i < str.length(); i++) {
                String subStr = str.substring(i, i + 1);
                if (subStr.equals("【")) {
                    starts.add(i);
                } else if (subStr.equals("】")) {
                    ends.add(i);
                }
            }

            if (starts.size() == ends.size()) {
                for (int i = 0; i < starts.size(); i++) {
                    StyleSpan span = new StyleSpan(Typeface.BOLD);
                    string.setSpan(span, starts.get(i), ends.get(i) + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    string.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.gray0)), starts.get(i), ends.get(i) + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }

            txt_test.setText(string);


        return view;

    }
}