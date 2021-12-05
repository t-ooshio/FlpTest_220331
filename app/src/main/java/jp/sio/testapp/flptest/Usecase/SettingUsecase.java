package jp.sio.testapp.flptest.Usecase;

import android.content.Context;

import jp.sio.testapp.flptest.L;
import jp.sio.testapp.flptest.Repository.SettingPref;

/**
 * Created by NTT docomo on 2017/05/24.
 * Settingの値を設定したり取得する
 * 設定の保存方法はここで吸収する
 * 今回はSharedPreferenceを使用してる
 */

public class SettingUsecase {
    private SettingPref settingPref;
    private Context context;

    public SettingUsecase(Context context){
        this.context = context;
        settingPref = new SettingPref(context);
        settingPref.createPref();
    }

    /**
     * 設定を初期化する
     */
    public void setDefaultSetting(){
        settingPref.setDefaultSetting();
    }

    /*********************ここからSetter**********************/
    public void setLocationApi(String locationApi){
        L.d("locationAPi:"+locationApi);
        settingPref.setLocationAPi(locationApi);
    }
    public void setLocationPriority(String locationPriority){
        L.d("locationPriority:"+locationPriority);
        settingPref.setLocationPriority(locationPriority);
    }

    public void setIsSetInterval(boolean isSetInterval){
        settingPref.setIsSetInterval(isSetInterval);
    }
    public void setSetInteravl(int setinterval){
        settingPref.setSetInterval(setinterval);
    }
    public void setCount(int count){
        settingPref.setCount(count);
    }
    public void setInterval(long interval){
        settingPref.setInterval(interval);
    }
    public void setTimeout(long timeout){
        settingPref.setTimeout(timeout);
    }
    public void setSuplEndWaitTIme(int suplEndWaitTIme){
        settingPref.setSuplEndWaitTime(suplEndWaitTIme);
    }
    public void setDelAssistDataTime(int delAssistDataTime){
        settingPref.setDelAssistDataTime(delAssistDataTime);
    }
    public void setIsCold(boolean iscold){
        settingPref.setIsCold(iscold);
    }

     /*****************ここからGetter*******************/
    public String getLocationApi(){
        return settingPref.getLocationAPi();
    }
    public String getLocationPriority(){
        return settingPref.getLocationPriority();
    }
    public boolean getIsSetInterval(){
        L.d("getSetInterval:" + settingPref.getIsSetInterval() + "");
        return settingPref.getIsSetInterval();
    }
    public int getSetInterval(){
        return settingPref.getSetInterval();
    }
    public int getCount(){
        return settingPref.getCount();
    }
    public long getTimeout(){
        return settingPref.getTimeout();
    }
    public long getInterval(){
        return settingPref.getInterval();
    }
    public boolean getIsCold(){
        return settingPref.getIsCold();
    }
    public int getSuplEndWaitTime(){
        return settingPref.getSuplEndWaitTime();
    }
    public int getDelAssistDataTime(){
        return settingPref.getDelAssistDataTime();
    }

    public void commitSetting(){
        settingPref.commitSetting();
    }
}
