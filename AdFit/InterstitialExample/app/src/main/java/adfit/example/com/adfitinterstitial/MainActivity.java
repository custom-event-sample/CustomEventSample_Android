package adfit.example.com.adfitinterstitial;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class MainActivity extends AppCompatActivity {

    private InterstitialAd mInterstitialAd;
    private CountDownTimer mCountDownTimer;
    private Button mShowButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //InterstitialAd 객체 생성 및 ad_unit_id 셋팅
        mInterstitialAd = new InterstitialAd(this);
        //res/values/string.xml에서 ad_unit_id 정의
        mInterstitialAd.setAdUnitId(getString(R.string.ad_unit_id));

        //광고보기 버튼
        mShowButton = (Button)findViewById(R.id.showButton);
        //일정시간이 지난 뒤에 버튼이 보여짐. (미리 광고로딩을 한다.)
        mShowButton.setVisibility(View.INVISIBLE);
        mShowButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                showInterstitial();
            }
        });


        // 버튼이 보이지 않는 동안에 게임이 진행되고 시간이 다해 게임이 끝나면 '게임 다시 시작'버튼이 보인다 가정.
        // '다시 시작'(Retry) 버튼 보여주기
        final TextView textView = ((TextView) findViewById(R.id.timer));
        mCountDownTimer = new CountDownTimer(3000, 50) {
            @Override
            public void onTick(long millisUnitFinished) {
                textView.setText("seconds remaining: " + ((millisUnitFinished / 1000) + 1));
            }

            @Override
            public void onFinish() {
                textView.setText("done!");
                mShowButton.setVisibility(View.VISIBLE);
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onResume() {
        // Initialize the timer if it hasn't been initialized yet.
        // Start the game.
        super.onResume();
        startGame();
    }

    @Override
    public void onPause() {
        // Cancel the timer if the game is paused.
        mCountDownTimer.cancel();
        super.onPause();
    }

    private void startGame() {
        // 버튼은 숨기기, 광고는 요청, 게임 시간 카운트다운.
        mShowButton.setVisibility(View.INVISIBLE);
        AdRequest adRequest = new AdRequest.Builder().build();
        mInterstitialAd.loadAd(adRequest);
        mCountDownTimer.start();
    }


    private void showInterstitial() {
        // 광고가 준비 되었으면 광고를 보여주고, 없으면 광고없이 게임 다시 시작.
        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Toast.makeText(this, "Ad did not load", Toast.LENGTH_SHORT).show();

        }
    }

}
