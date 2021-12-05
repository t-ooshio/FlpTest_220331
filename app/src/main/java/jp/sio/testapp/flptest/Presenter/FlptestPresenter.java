package jp.sio.testapp.flptest.Presenter;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

import java.text.SimpleDateFormat;
import java.util.List;

import jp.sio.testapp.flptest.Activity.FlpTestActivity;
import jp.sio.testapp.flptest.Activity.SettingActivity;
import jp.sio.testapp.flptest.L;
import jp.sio.testapp.flptest.R;
import jp.sio.testapp.flptest.Service.FlpRequestLocationUpdates;
import jp.sio.testapp.flptest.Service.FlpgetCurrentLocationService;
import jp.sio.testapp.flptest.Usecase.FlptestUsecase;
import jp.sio.testapp.flptest.Usecase.SettingUsecase ;
import jp.sio.testapp.flptest.Repository.LocationLog;

/**
 * Created by NTT docomo on 2017/05/23.
 * ActivityとServiceの橋渡し
 * Activityはなるべく描画だけに専念させたいから分けるため
 */

public class FlptestPresenter {
    private FlpTestActivity activity;
    private SettingUsecase settingUsecase;
    private FlptestUsecase flptestUsecase;
    private Intent settingIntent;
    private Intent locationserviceIntent;
    private ServiceConnection runService;
    private LocationLog locationLog;

    private String receiveCategory;
    private String categoryLocation;
    private String categoryColdStart;
    private String categoryColdStop;
    private String categoryServiceStop;

    private FlpRequestLocationUpdates flpRequestLocationUpdates;
    private FlpgetCurrentLocationService flpgetCurrentLocationService;

    private String locationApi;
    private String locationPriority;
    private boolean isSetInterval;
    private int setInterval;
    private int count;
    private long timeout;
    private long interval;
    private boolean isCold;
    private int suplendwaittime;
    private int delassisttime;

    private String settingHeader;
    private String locationHeader;


    private ServiceConnection serviceConnectionFlpRequestLocationUpdates = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            flpRequestLocationUpdates = ((FlpRequestLocationUpdates.FlpRequestLocationUpdatesService_Binder)service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            activity.unbindService(runService);
            flpRequestLocationUpdates = null;
        }
    };

    private ServiceConnection serviceConnectionFlpGetCurrentLocarionService = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            flpgetCurrentLocationService = ((FlpgetCurrentLocationService.FlpGetCurrentLocationService_Binder)service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            activity.unbindService(runService);
            flpgetCurrentLocationService = null;

        }
    };

    private final LocationReceiver locationReceiver = new LocationReceiver();

    public FlptestPresenter(FlpTestActivity activity){
        this.activity = activity;
        flptestUsecase = new FlptestUsecase(activity);
        settingUsecase = new SettingUsecase(activity);

        categoryLocation = activity.getResources().getString(R.string.categoryLocation);
        categoryColdStart = activity.getResources().getString(R.string.categoryColdStart);
        categoryColdStop = activity.getResources().getString(R.string.categoryColdStop);
        categoryServiceStop = activity.getResources().getString(R.string.categoryServiceEnd);
        settingHeader = activity.getResources().getString(R.string.settingHeader) ;
        locationHeader =activity. getResources().getString(R.string.locationHeader);

    }

    public void checkPermission(){
        flptestUsecase.hasPermissions();
    }

    public void mStart(){
        activity.offBtnStop();

        activity.showTextViewState(activity.getResources().getString(R.string.locationStop));
    }

    public void locationStart(){
        IntentFilter filter = null;
        getSetting();
        //ログファイルの生成
        locationLog = new LocationLog(activity);
        L.d("before_makeLogFile");
        locationLog.makeLogFile(settingHeader);
        locationLog.writeLog(
                locationApi + "," + locationPriority + "" + count + "," + timeout
                        + "," + interval + "," + suplendwaittime + ","
                        + delassisttime + "," + isCold);
        locationLog.writeLog(locationHeader);

        activity.showTextViewSetting("測位API:" + locationApi + "測位精度:" + locationPriority + "\n" + "測位回数:" + count + "\n" + "タイムアウト:" + timeout + "\n" +
                "測位間隔:" + interval + "\n" + "Cold:" + isCold + "\n"
                + "suplEndWaitTime:" + suplendwaittime + "\n" + "アシストデータ削除時間:" + delassisttime + "\n");

        if(locationApi.equals(activity.getResources().getString(R.string.api_requestLocationUpdates))) {
            locationserviceIntent = new Intent(activity.getApplicationContext(), FlpRequestLocationUpdates.class);
            setSetting(locationserviceIntent);
            runService = serviceConnectionFlpRequestLocationUpdates;
            filter = new IntentFilter(activity.getResources().getString(R.string.api_requestLocationUpdates));
            L.d("api_requestLocationUpdatesService");

        }else if(locationApi.equals(activity.getResources().getString(R.string.api_getCurrentLocation))) {
            locationserviceIntent = new Intent(activity.getApplicationContext(), FlpgetCurrentLocationService.class);
            setSetting(locationserviceIntent);
            runService = serviceConnectionFlpGetCurrentLocarionService;
            filter = new IntentFilter(activity.getResources().getString(R.string.api_getCurrentLocation));
            L.d("api_getCurrentLocationService");

        }else{
            showToast("予期せぬ測位方式");
            L.d("予期せぬ測位方式");
        }
        activity.startService(locationserviceIntent);
        activity.registerReceiver(locationReceiver,filter);
        activity.bindService(locationserviceIntent,runService ,Context.BIND_AUTO_CREATE);

    }

    /**
     * 測位回数満了などで測位を停止する処理
     */
    public void locationStop(){
        L.d("locationStop");

        L.d("ServiceConnectionの削除");
        if(runService != null) {
            L.d("unbindService");
            try {
                activity.unbindService(runService);
            }catch (IllegalArgumentException e){
                e.printStackTrace();
            }
        }


        //Service1の停止
        L.d("Serviceの停止");
        if(locationserviceIntent != null) {
            try {
                activity.stopService(locationserviceIntent);
            }catch(SecurityException e){
                e.printStackTrace();
            }
        }

        //Receiverの消去
        L.d("Receiverの消去");
        try {
            if (locationReceiver != null) {
                activity.unregisterReceiver(locationReceiver);
            }
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }

        //logファイルの終了
        L.d("logファイルの終了");
        if(locationLog != null) {
            locationLog.endLogFile();
        }
    }

    /**
     * Setting表示開始
     */
    public void settingStart(){
        settingIntent = new Intent(activity.getApplicationContext(), SettingActivity.class);
        activity.startActivity(settingIntent);
    }

    /**
     * activityにToastを表示する
     * @param message
     */
    public void showToast(String message){
        activity.showToast(message);
    }

    /**
     * 測位結果を受けとるためのReceiver
     */
    public class LocationReceiver extends BroadcastReceiver {
        Boolean isFix;
        double lattude, longitude, altitude, ttff;
        long fixtimeEpoch;
        float accuracy,bearing;
        String fixtimeUTC;
        String locationStarttime, locationStoptime;

        String provider;
        int sucCnt;
        int failCnt;

        Location location = new Location(LocationManager.GPS_PROVIDER);
        SimpleDateFormat fixTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZ");
        SimpleDateFormat simpleDateFormatHH = new SimpleDateFormat("HH:mm:ss.SSS");


        @Override
        public void onReceive(Context context, Intent intent) {
            L.d("onReceive");
            Bundle bundle = intent.getExtras();
            receiveCategory = bundle.getString(activity.getResources().getString(R.string.category));

            if (receiveCategory.equals(categoryLocation)) {
                L.d("Location Result onReceive");
                location = bundle.getParcelable(activity.getResources().getString(R.string.TagLocation));
                isFix = bundle.getBoolean(activity.getResources().getString(R.string.TagisFix));
                sucCnt = bundle.getInt(activity.getResources().getString(R.string.TagSuccessCount));
                failCnt = bundle.getInt(activity.getResources().getString(R.string.TagFailCount));
                locationStarttime = simpleDateFormatHH.format(bundle.getLong(activity.getResources().getString(R.string.TagLocationStarttime)));
                locationStoptime = simpleDateFormatHH.format(bundle.getLong(activity.getResources().getString(R.string.TagLocationStoptime)));
                if (isFix) {
                    lattude = location.getLatitude();
                    longitude = location.getLongitude();
                    accuracy = location.getAccuracy();
                    altitude = location.getAltitude();
                    bearing = location.getBearing();
                    provider = location.getProvider();
                    fixtimeEpoch = location.getTime();
                    fixtimeUTC = fixTimeFormat.format(fixtimeEpoch);
                } else {
                    lattude = -1;
                    longitude = -1;
                    altitude = -1;
                    bearing = -1;
                    provider = "-1";
                    fixtimeEpoch = -1;
                    fixtimeUTC = "-1";
                }
                ttff = bundle.getDouble(activity.getResources().getString(R.string.Tagttff));
                L.d("onReceive");
                L.d(locationStarttime + "," + locationStoptime + "," + isFix + "," + lattude + "," + longitude + "," + ttff + ","
                        + sucCnt + "," + failCnt + "," + fixtimeEpoch + "," + fixtimeUTC + "\n");
                locationLog.writeLog(
                        locationStarttime + "," + locationStoptime + "," + isFix + "," + location.getLatitude() + "," + location.getLongitude()
                                + "," + ttff + "," +  accuracy + "," + altitude + "," + bearing + "," + provider + "," + fixtimeEpoch + "," + fixtimeUTC);

                activity.showTextViewResult("測位成否:" + isFix + "\n" + "緯度:" + lattude + "\n" + "経度:" + longitude + "\n" + "Altitude:" + altitude + "\n" + "Provider:" + provider + "\n" + "TTFF：" + ttff + "\n" + "Accuracy:" + accuracy
                        + "\n" + "成功回数:" + sucCnt + "\n"  + "失敗回数:" + failCnt + "\n" + "fixTimeEpoch:" + fixtimeEpoch + "\n" + "fixTimeUTC:" + fixtimeUTC + "\n");

                activity.showTextViewState(activity.getResources().getString(R.string.locationWait));
            } else if (receiveCategory.equals(categoryColdStart)) {
                L.d("ReceiceColdStart");
                activity.showTextViewState(activity.getResources().getString(R.string.locationPositioning));
                showToast("アシストデータ削除中");
            } else if (receiveCategory.equals(categoryColdStop)) {
                L.d("ReceiceColdStop");
                showToast("アシストデータ削除終了");
            } else if (receiveCategory.equals(categoryServiceStop)) {
                L.d("ServiceStop");
                activity.showTextViewState(activity.getResources().getString(R.string.locationStop));
                showToast("測位サービス終了");
                activity.onBtnStart();
                activity.offBtnStop();
                activity.onBtnSetting();
            }
        }

        public void unreggister() {
            activity.unregisterReceiver(this);
        }
    }

    private void setSetting(Intent locationServiceIntent){
        locationServiceIntent.putExtra(activity.getResources().getString(R.string.settingLocationApi),locationApi);
        locationServiceIntent.putExtra(activity.getResources().getString(R.string.settingLocationPriority),locationPriority);
        locationServiceIntent.putExtra(activity.getResources().getString(R.string.settingSetInterval),setInterval);
        locationServiceIntent.putExtra(activity.getResources().getString(R.string.settingIsSetInterval),isSetInterval);
        locationServiceIntent.putExtra(activity.getResources().getString(R.string.settingCount),count);
        locationServiceIntent.putExtra(activity.getResources().getString(R.string.settingTimeout),timeout);
        locationServiceIntent.putExtra(activity.getResources().getString(R.string.settingInterval),interval);
        locationServiceIntent.putExtra(activity.getResources().getString(R.string.settingIsCold),isCold);
        locationServiceIntent.putExtra(activity.getResources().getString(R.string.settingSuplEndWaitTime),suplendwaittime);
        locationServiceIntent.putExtra(activity.getResources().getString(R.string.settingDelAssistdataTime),delassisttime);

    }

    /**
     * 設定画面で設定した値を取得する
     */
    private void getSetting(){
        locationApi = settingUsecase.getLocationApi();
        locationPriority = settingUsecase.getLocationPriority();
        isSetInterval = settingUsecase.getIsSetInterval();
        setInterval = settingUsecase.getSetInterval();
        count = settingUsecase.getCount();
        timeout = settingUsecase.getTimeout();
        interval = settingUsecase.getInterval();
        isCold = settingUsecase.getIsCold();
        suplendwaittime = settingUsecase.getSuplEndWaitTime();
        delassisttime = settingUsecase.getDelAssistDataTime();

    }

    /**
     * clsに渡したServiceが起動中か確認する
     * true:  起動している
     * false: 起動していない
     * @param context
     * @param cls
     * @return
     */
    private boolean isServiceRunning(Context context, Class<?> cls){
        ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningService = am.getRunningServices(Integer.MAX_VALUE);
        for(ActivityManager.RunningServiceInfo i :runningService){
            if(cls.getName().equals(i.service.getClassName())){
                L.d(cls.getName());
                return true;
            }
        }
        return false;
    }
}