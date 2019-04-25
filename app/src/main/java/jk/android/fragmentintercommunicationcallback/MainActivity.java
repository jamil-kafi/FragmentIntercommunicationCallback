package jk.android.fragmentintercommunicationcallback;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import jk.android.intercominterface.ComInterface;

public class MainActivity extends AppCompatActivity {

    @ComInterface(containerId = R.id.mainContainer,
            enterAnim = R.anim.frag_slide_in_left,
            exitAnim = R.anim.frag_slide_in_right,
            popEnterAnim = R.anim.frag_slide_out_left,
            popExitAnim = R.anim.frag_slide_out_right)
    private MainActivityInterCom interCom;

    private Button btnFrag1, btnFrag2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*interCom = new MainActivityInterCom(this);

        btnFrag1 = (Button) findViewById(R.id.btnFrag1);
        btnFrag2 = (Button) findViewById(R.id.btnFrag2);

        btnFrag1.setOnClickListener(v -> {
            try {
                interCom.switchFragment(FirstFragment.newInstance(), true, true, true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        btnFrag2.setOnClickListener(v -> {
            try {
                interCom.switchFragment(SecondFragment.newInstance(), true, true, true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });*/

    }

}
