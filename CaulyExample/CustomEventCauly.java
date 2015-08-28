package com.customevent.example;

//Cauly 전면광고 헤더
//Cauly 웹사이트에서 전면광고 구현 가이드를 참고하여 파일 등을 프로젝트에 추가하여야 함
import com.fsn.cauly.CaulyAdInfo;
import com.fsn.cauly.CaulyAdInfoBuilder;
import com.fsn.cauly.CaulyAdView;
import com.fsn.cauly.CaulyAdViewListener;

import com.customevent.example.R;
import android.app.Activity;
import android.util.Log;
import android.widget.LinearLayout;
import com.google.ads.AdSize;
import com.google.ads.mediation.MediationAdRequest;
import com.google.ads.mediation.customevent.CustomEventBanner;
import com.google.ads.mediation.customevent.CustomEventBannerListener;
//AdMob 전면광고 Custom Event 구현 라이브러리  

public class CustomEventCauly implements CustomEventBanner, CaulyAdViewListener {

	private static final String LOGTAG = "CaulyBanner_LOG";

	private CustomEventBannerListener bannerListener;
	private CaulyAdView adView = null;
	private Activity bannerActivity = null;
	private LinearLayout layout = null;

	// AdMob custom event callback. Cauly 전면광고를 요청하기 위해 AdMob이 호출해줌.
	@Override
	public void requestBannerAd(final CustomEventBannerListener listener,
			final Activity activity, String label, String serverParameter,
			AdSize adSize, MediationAdRequest mediationAdRequest, Object extra) {

		Log.i(LOGTAG, "Cauly");

		this.bannerListener = listener;
		this.bannerActivity = activity;

		// android:id="@+id/relativelayout" 속성이
		// 지정된 것으로 가정하여 Relativelayout 찾기
		layout = (LinearLayout) activity.findViewById(R.id.layout);

		// 광고 정보 설정
		// AdMob mediation UI상에 입력한 값이 serverParameter 인자로 전달됨
		CaulyAdInfo adInfo = new CaulyAdInfoBuilder(serverParameter).effect(
				"RightSlide").build();

		// Cauly 배너 광고 View 생성.
		this.adView = new CaulyAdView(bannerActivity);

		// 광고 정보 설정
		this.adView.setAdInfo(adInfo);
		this.adView.setAdViewListener(this);

		// Cauly의 경우 addView를 하면 광고가 요청하여 성공하면 바로 노출함.
		layout.addView(adView);

	}

	// Cauly 광고 수신 성공 & 노출된 경우 호출됨.
	@Override
	public void onReceiveAd(CaulyAdView adView, boolean isChargeableAd) {

		Log.i(LOGTAG, "Cauly banner onReceiveAd");

		// 광고 요청과 동시에 add된 광고 view를 제거함
		layout.removeView(adView);

		
			// 수신된 광고가 무료 광고인 경우 isChargeableAd 값이 false 임.
			if (isChargeableAd == false) {
				Log.i(LOGTAG, "free banner AD received.");

			} else {
				Log.i(LOGTAG, "normal banner AD received.");

			}

			// AdMob custom event에 전면광고가 성공했음을 알림
			this.bannerListener.onReceivedAd(adView);

			// mediaition framework에 광고 전달 후, 초 단위로 발생하는 Cauly request 증지
			adView.pause();
		
	}

	// Cauly 광고 정보 수신 실패
	@Override
	public void onFailedToReceiveAd(CaulyAdView adView, int errorCode,
			String errorMsg) {
		Log.i(LOGTAG, "failed to receive banner AD.");
		// Cauly는 광고 수신 실패시 내장된 Cauly 이미지를 게재함
		// 다음 mediation network의 광고를 노출하기 위해 해당 이미지 삭제 필요
		layout.removeView(adView);
	}

	// 광고 배너를 클릭하여 랜딩 페이지가 열린 경우 호출
	@Override
	public void onShowLandingScreen(CaulyAdView adView) {
		Log.i(LOGTAG, "banner AD landing screen opened.");
	}

	// 광고 배너를 클릭하여 열린 랜딩 페이지가 닫힌 경우 호출
	@Override
	public void onCloseLandingScreen(CaulyAdView adView) {
		Log.i(LOGTAG, "banner AD landing screen closed.");
	}

	@Override
	public void destroy() {
		 if (this.adView != null) {
		      this.adView.destroy();
		    }
	}

}
