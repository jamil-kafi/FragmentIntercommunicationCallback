package jk.android.fragmentintercommunicationcallback;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

// import jk.android.ii.SecondActivityInterCom;
import jk.android.intercominterface.ComInterface;

public class SecondActivity extends AppCompatActivity {


    /*@ComInterface(containerId = R.id.mainContainerForSecondActivity)
    private SecondActivityInterCom interCom;*/

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
    }
}
