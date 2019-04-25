package jk.android.fragmentintercommunicationcallback;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

// import jk.android.ii.MainActivityInterCom;

public class SecondFragment extends Fragment {

    // MainActivityInterCom interCom;

    public static SecondFragment newInstance() {
        return new SecondFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // interCom = (MainActivityInterCom) getArguments().getSerializable(MainActivityInterCom.KEY_COM_CALLBACK);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment2_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button btn = view.findViewById(R.id.btnFrag2To1);
        btn.setOnClickListener(v -> {
            try {
                // interCom.switchFragment(FirstFragment.newInstance(), true, true, true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
