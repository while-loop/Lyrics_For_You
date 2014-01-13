package com.lyics4me.lyrics4u;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class Splash extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        Intent openStartingPoint = new Intent( "com.lyrics4me.lyrics4u.STARTINGPOINT");
        startActivity(openStartingPoint);
    }

    protected void onPause() {
        super.onPause();
        finish();
    }
}
