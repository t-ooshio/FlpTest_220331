package jp.sio.testapp.flptest.Presenter;

import android.app.Activity;

import jp.sio.testapp.flptest.Activity.SettingActivity;
import jp.sio.testapp.flptest.L;
import jp.sio.testapp.flptest.R;
import jp.sio.testapp.flptest.Usecase.SettingUsecase;

/**
 * Created by NTT docomo on 2017/05/24.
 * SettingActivityとSettingUsecaseの橋渡し
 */

public class SettingPresenter {
    SettingActivity activity;
    SettingUsecase settingusecase;

    public SettingPresenter(SettingActivity activity){
        this.activity = activity;
        settingusecase = new SettingUsecase(activity);
    }

    /**
     * 現在Activityに入力されている値を保存する
     */
    public void commitSetting(){
        String locationApi = activity.getResources().getString(R.string.api_requestLocationUpdates);
        if(activity.isRadioButtonRequestLocationUpdates()){
            locationApi = activity.getResources().getString(R.string.api_requestLocationUpdates);
        }else if(activity.isRadioButtonGetCurrentLocation()){
            locationApi = activity.getResources().getString(R.string.api_getCurrentLocation);
        }

        String locationTyep = activity.getResources().getString(R.string.locationFlpBalancedPowerAccuracy);
        if(activity.isRadioButtonFlpBalancedPower()){
            locationTyep = activity.getResources().getString(R.string.locationFlpBalancedPowerAccuracy);
        }else if(activity.isRadioButtonFlpHighAccuracy()) {
            locationTyep = activity.getResources().getString(R.string.locationFlpHighAccuracy);
        }else if(activity.isRadioButtonFlpLowPower()) {
            locationTyep = activity.getResources().getString(R.string.locationFlpLowPower);
        }
        else if(activity.isRadioButtonFlpNoPower()) {
            locationTyep = activity.getResources().getString(R.string.locationFlpNoPower);
        }
        settingusecase.setIsSetInterval(activity.isSetInterval());
        settingusecase.setSetInteravl(activity.getSetInterval());
        settingusecase.setCount(activity.getCount());
        settingusecase.setInterval(activity.getInterval());
        settingusecase.setTimeout(activity.getTimeout());
        settingusecase.setIsCold(activity.isColdCheck());
        settingusecase.setDelAssistDataTime(activity.getDelAssistDataTime());
        settingusecase.setSuplEndWaitTIme(activity.getSuplEndWaitTime());
        settingusecase.commitSetting();
    }
    /**
     * 現在保存されている値をActivityに表示する
     */
    public void loadSetting(){
        String locationApi = settingusecase.getLocationApi();
        if(locationApi.equals(activity.getResources().getString(R.string.api_requestLocationUpdates))) {
            activity.enableRadioButtonRequestLocationUpdates();
        }else if(locationApi.equals(activity.getResources().getString(R.string.api_getCurrentLocation))){
            activity.enableRadioButtonGetCurrentLocation();
        }

        String locationPriority = settingusecase.getLocationPriority();
        if(locationPriority.equals(activity.getResources().getString(R.string.locationFlpBalancedPowerAccuracy))) {
            activity.enableRadioButtonFlpBalancedPowerAccuracy();
        }else if(locationPriority.equals(activity.getResources().getString(R.string.locationFlpHighAccuracy))) {
            activity.enableRadioButtonFlpHighAccuracy();
        }else if(locationPriority.equals(activity.getResources().getString(R.string.locationFlpLowPower))){
            activity.enableRadioButtonFlpLowPower();
        }else if(locationPriority.equals(activity.getResources().getString(R.string.locationFlpNoPower))) {
            activity.enableRadioButtonFlpNoPower();
        }
        if(settingusecase.getIsSetInterval()) {
            activity.enableIsSetInterval();
        }else {
            activity.disableIsSetInterval();
        }
        activity.setSetInterval(settingusecase.getSetInterval());
        activity.setCount(settingusecase.getCount());
        activity.setInterval(settingusecase.getInterval());
        activity.setTimeout(settingusecase.getTimeout());
        if(settingusecase.getIsCold()) {
            activity.enableIsCold();
        }else {
            activity.disableIsCold();
        }
        activity.setDelAssistDataTime(settingusecase.getDelAssistDataTime());
        activity.setSuplEndWaitTime(settingusecase.getSuplEndWaitTime());
    }
}
