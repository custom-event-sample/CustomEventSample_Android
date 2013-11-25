package com.customevent.example;

//Ad@m 전면광고 라이브러리  
//Ad@m 웹사이트에서 전면광고 구현 가이드를 참고하여 파일 등을 프로젝트에 추가하여야 함
import net.daum.adam.publisher.AdView.AnimationType;
import net.daum.adam.publisher.AdView.OnAdClickedListener;
import net.daum.adam.publisher.AdView.OnAdFailedListener;
import net.daum.adam.publisher.AdView.OnAdLoadedListener;
import net.daum.adam.publisher.AdView.OnAdWillLoadListener;
import net.daum.adam.publisher.impl.AdError;

import android.app.Activity;
import android.util.Log;
import android.widget.LinearLayout;

import com.customevent.example.R;
import com.google.ads.AdSize;
import com.google.ads.mediation.MediationAdRequest;

//AdMob 배너광고 Custom Event 구현 라이브러리 
import com.google.ads.mediation.customevent.CustomEventBanner;
import com.google.ads.mediation.customevent.CustomEventBannerListener;


public class CustomEventAdam implements CustomEventBanner,OnAdWillLoadListener,OnAdLoadedListener,OnAdFailedListener,OnAdClickedListener {

	private static final String LOGTAG = "Ad@mBanner_LOG";

	private net.daum.adam.publisher.AdView adView = null;
	private LinearLayout layout = null;
	private CustomEventBannerListener bannerListener;
	private Activity bannerActivity = null;

	// AdMob custom event callback. Adam 배너광고를 요청하기 위해 AdMob이 호출해줌.
	@Override
	public void requestBannerAd(final CustomEventBannerListener listener,
			final Activity activity, String label, String serverParameter,
			AdSize adSize, MediationAdRequest mediationAdRequest, Object extra) {

		Log.i(LOGTAG, "Ad@m");

		this.bannerListener = listener;
		this.bannerActivity = activity;

		AdSize bestAdSize = adSize = adSize.findBestSize(AdSize.BANNER,
				AdSize.IAB_BANNER, AdSize.IAB_LEADERBOARD, AdSize.IAB_MRECT,
				AdSize.IAB_WIDE_SKYSCRAPER);

		if (bestAdSize == null) {
			this.bannerListener.onFailedToReceiveAd();
			return;
		}

		// android:id="@+id/relativelayout" 속성이
		// 지정된 것으로 가정하여 Linearlayout 찾기
		layout = (LinearLayout) activity.findViewById(R.id.layout);

		// Ad@m 배너 광고 view생성
		this.adView = new net.daum.adam.publisher.AdView(this.bannerActivity);

		// 광고 리스너 설정
		// 광고를 불러올때 실행할 리스너
		this.adView.setOnAdWillLoadListener(this);
		// 광고를 정상적으로 내려받았을 경우에 실행할 리스너
		this.adView.setOnAdLoadedListener(this);
		// 광고 내려받기 실패했을 경우에 실행할 리스너
		this.adView.setOnAdFailedListener(this);
		// 광고 클릭시 실행할 리스너
		this.adView.setOnAdClickedListener(this);

		
		// AdMob mediation UI상에 입력한 값이 serverParameter 인자로 전달
		// 할당 받은 clientId 설정
		this.adView.setClientId(serverParameter);
		// 광고 갱신 주기를 120초로 설정
		this.adView.setRequestInterval(120);
		// Animation 효과 : 기본 값은 AnimationType.NONE
		this.adView.setAnimationType(AnimationType.FLIP_HORIZONTAL);
		// Adam의 경우 addView를 하면 광고가 요청하여 성공하면 바로 노출함.
		layout.addView(adView);
	}

	@Override
	public void destroy() {

	}
	
	@Override
	public void OnAdClicked() {
		Log.i(LOGTAG, "광고를 클릭했습니다.");

		// AdMob custom event에 광고가 클릭되어 열리게 될 것임을 알림
		bannerListener.onClick();

	}
	
	@Override
	public void OnAdFailed(AdError arg0, String arg1) {
		Log.i(LOGTAG, arg1);

		// AdMob custom event에 배너광고 요청이 실패했음을 알림
		bannerListener.onFailedToReceiveAd();
	}
	
	@Override
	public void OnAdLoaded() {
		
			// 광고 요청과 동시에 add된 광고 view를 제거함
			layout.removeView(adView);
		
			// 광고 view를 AdMob mediation으로 전달
			bannerListener.onReceivedAd(adView);

			Log.i(LOGTAG, "광고가 정상적으로 로딩되었습니다.");
		
	}
	
	@Override
	public void OnAdWillLoad(String url) {
		Log.i(LOGTAG, "광고를 불러옵니다. : " + url);
	}
}
