package com.customevent.example;


//Cauly 전면광고 헤더
//Cauly 웹사이트에서 전면광고 구현 가이드를 참고하여 파일 등을 프로젝트에 추가하여야 함
import com.fsn.cauly.*;

import android.app.Activity;
import android.util.Log;
import com.google.ads.mediation.MediationAdRequest;

//AdMob 전면광고 Custom Event 구현 헤더
import com.google.ads.mediation.customevent.CustomEventInterstitial;
import com.google.ads.mediation.customevent.CustomEventInterstitialListener;

public class CustomEventCaulyInterstitial implements CustomEventInterstitial,
    CaulyInterstitialAdListener {

    private static final String LOGTAG = "CaulyInterstitial_LOG";

    private CustomEventInterstitialListener interstitialListener;
    private Activity InterstitialActivity = null;
    private CaulyInterstitialAd interstial;

    // AdMob custom event callback. Cauly 전면광고를 요청하기 위해 AdMob이 호출해줌.
    @Override
    public void requestInterstitialAd(
            final CustomEventInterstitialListener listener,
            final Activity activity, String label, String serverParameter,
            MediationAdRequest mediationAdRequest, Object extra) {

        Log.d(LOGTAG, "Cauly Interstitial");

        this.interstitialListener = listener;
        this.InterstitialActivity = activity;

        // CaulyAdInfo 생성
        // AdMob mediation UI상에 입력한 값이 serverParameter 인자로 전달됨
        CaulyAdInfo adInfo = new CaulyAdInfoBuilder(serverParameter).build();

        // Cauly 전면 광고 생성
        interstial = new CaulyInterstitialAd();
        interstial.setAdInfo(adInfo);
        interstial.setInterstialAdListener(this);

        // Cauly 전면 광고 요청
        interstial.requestInterstitialAd(InterstitialActivity);

    }

    @Override
    public void onReceiveInterstitialAd(CaulyInterstitialAd ad,
                                        boolean isChargeableAd) {

        if (isChargeableAd == false) {
            Log.d(LOGTAG, "free interstitial AD received.");
        } else {
            Log.d(LOGTAG, "normal interstitial AD received.");
        }
        // Cauly 전면 광고 노출. show()를 호출하지 않으면 전면광고가 보여지지 않음
        //ad.show();

        // AdMob custom event에 전면광고가 성공했음을 알림
        this.interstitialListener.onReceivedAd();

    }

    // 전면 광고 수신 실패할 경우 호출됨.
    @Override
    public void onFailedToReceiveInterstitialAd(CaulyInterstitialAd ad,
                                                int errorCode, String errorMsg) {
        Log.d(LOGTAG, "failed to receive interstitial AD.");
        this.interstitialListener.onFailedToReceiveAd();

    }

    // 전면 광고가 닫힌 경우 호출됨.
    @Override
    public void onClosedInterstitialAd(CaulyInterstitialAd ad) {

        Log.d(LOGTAG, "interstitial AD closed.");
        // AdMob custom event에 전면광고가 닫힘을 알림
        this.interstitialListener.onDismissScreen();

    }

    @Override
    public void onLeaveInterstitialAd(CaulyInterstitialAd ad) {}

    @Override
    public void destroy() {}

    @Override
    public void showInterstitial() {

        Log.i(LOGTAG, " showInterstitial() ");
        // Cauly 전면 광고 노출. show()를 호출하지 않으면 전면광고가 보여지지 않음
        interstial.show();

    }
}