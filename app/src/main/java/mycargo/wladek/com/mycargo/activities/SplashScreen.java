package mycargo.wladek.com.mycargo.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import mycargo.wladek.com.mycargo.R;

public class SplashScreen extends Activity {
    private boolean isRunning;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        this.isRunning = true;
        startSplash();
    }

    private void startSplash() {
        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    SplashScreen.this.runOnUiThread(new Runnable() {
                        public void run() {
                            SplashScreen.this.doFinish();
                        }
                    });
                }
            }
        }).start();
    }

    private synchronized void doFinish() {
        if (this.isRunning) {
            this.isRunning = false;
            Intent i = new Intent(this, LoginActivity.class);
            i.addFlags(67108864);
            startActivity(i);
            finish();
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4) {
            return super.onKeyDown(keyCode, event);
        }
        this.isRunning = false;
        finish();
        return true;
    }
}
