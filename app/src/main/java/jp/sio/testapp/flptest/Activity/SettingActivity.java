package jp.sio.testapp.flptest.Activity;

import android.content.pm.ActivityInfo;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import jp.sio.testapp.flptest.Presenter.SettingPresenter;
import jp.sio.testapp.flptest.R;

/**
 * Settingの画面
 * 処理はSettingUsecaseへ渡す
 */
public class SettingActivity extends AppCompatActivity {

    SettingPresenter settingPresenter;

    private EditText editTextSetInterval;
    private CheckBox checkBoxisSetInterval;
    private EditText editTextCount;
    private EditText editTextTimeout;
    private EditText editTextInterval;
    private EditText editTextSuplEndWaitTime;
    private EditText editTextDelAssistDataTime;
    private RadioButton radioButtonRequestLocationUpdates;
    private RadioButton radioButtonGetCurrentLocation;
    private RadioButton radioButtonFlpBalancedPower;
    private RadioButton radioButtonFlpHighAccuracy;
    private RadioButton radioButtonFlpLowPower;
    private RadioButton radioButtonFlpNoPower;
    private CheckBox checkBoxisCold;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        //縦方向に固定
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        settingPresenter = new SettingPresenter(this);
        editTextSetInterval = (EditText)findViewById(R.id.editTextSetInterval);
        checkBoxisSetInterval = (CheckBox)findViewById(R.id.checkboxIsSetInterval);
        editTextCount = (EditText)findViewById(R.id.editTextCount);
        editTextTimeout = (EditText)findViewById(R.id.editTextTimeout);
        editTextInterval = (EditText)findViewById(R.id.editTextInterval);
        editTextSuplEndWaitTime = (EditText)findViewById(R.id.editTextSuplEndWaitTime);
        editTextDelAssistDataTime = (EditText)findViewById(R.id.editTextDelAssistDataTime);
        radioButtonRequestLocationUpdates = (RadioButton)findViewById(R.id.rbRequestLocationUpdates);
        radioButtonGetCurrentLocation = (RadioButton)findViewById(R.id.rbGetCurrentLocation);
        radioButtonFlpBalancedPower = (RadioButton)findViewById(R.id.rbFlpBalancedPower);
        radioButtonFlpHighAccuracy = (RadioButton)findViewById(R.id.rbFlpHighAccuracy);
        radioButtonFlpLowPower = (RadioButton)findViewById(R.id.rbFlpLowPower);
        radioButtonFlpNoPower = (RadioButton)findViewById(R.id.rbFlpNoPower);
        checkBoxisCold = (CheckBox)findViewById(R.id.checkboxIsCold);
    }

    @Override
    protected void onStart(){
        super.onStart();
        settingPresenter.loadSetting();
    }
    @Override
    protected void onResume(){
        settingPresenter.loadSetting();
        super.onResume();
    }

    public void onButtonSet(){

    }
    public void enableIsSetInterval(){
        checkBoxisSetInterval.setChecked(true);
    }
    public void disableIsSetInterval(){
        checkBoxisSetInterval.setChecked(false);
    }
    public void setSetInterval(int setinterval){
        editTextSetInterval.setText(Integer.toString(setinterval));
    }
    public void setCount(int count){
        editTextCount.setText(Integer.toString(count));
    }
    public void setTimeout(long timeout){
        editTextTimeout.setText(Long.toString(timeout));
    }
    public void setInterval(long interval){
        editTextInterval.setText(Long.toString(interval));
    }
    public void setSuplEndWaitTime(int suplEndWaitTime){
        editTextSuplEndWaitTime.setText(Integer.toString(suplEndWaitTime));
    }
    public void setDelAssistDataTime(int delAssistDataTime){
        editTextDelAssistDataTime.setText(Integer.toString(delAssistDataTime));
    }
    public void setRadioButtonRequestLocationUpdates(){
        radioButtonRequestLocationUpdates.setChecked(true);
    }
    public void setRadioButtonGetCurrentLocation(){
        radioButtonGetCurrentLocation.setChecked(true);
    }
    public void enableRadioButtonRequestLocationUpdates(){
        radioButtonRequestLocationUpdates.setChecked(true);
    }
    public void enableRadioButtonGetCurrentLocation(){
        radioButtonGetCurrentLocation.setChecked(true);
    }
    public void enableRadioButtonFlpBalancedPowerAccuracy(){
        radioButtonFlpBalancedPower.setChecked(true);
    }
    public void enableRadioButtonFlpHighAccuracy(){
        radioButtonFlpHighAccuracy.setChecked(true);
    }
    public void enableRadioButtonFlpLowPower(){
        radioButtonFlpLowPower.setChecked(true);
    }
    public void enableRadioButtonFlpNoPower(){
        radioButtonFlpNoPower.setChecked(true);
    }
    public void enableIsCold(){
        checkBoxisCold.setChecked(true);
    }
    public void disableIsCold(){
        checkBoxisCold.setChecked(false);
    }
    public int getSetInterval(){
        String interval;
        interval = editTextSetInterval.getText().toString();
        return Integer.parseInt(interval);
    }
    public boolean isSetInterval(){
        return checkBoxisSetInterval.isChecked();
    }
    public int getCount(){
        String count;
        count = editTextCount.getText().toString();
        return Integer.parseInt(count);
    }
    public long getTimeout(){
        String timeout;
        timeout = editTextTimeout.getText().toString();
        return Long.parseLong(timeout);
    }
    public long getInterval(){
        String interval;
        interval = editTextInterval.getText().toString();
        return Long.parseLong(interval);
    }
    public int getSuplEndWaitTime(){
        String suplendwaittime;
        suplendwaittime = editTextSuplEndWaitTime.getText().toString();
        return Integer.parseInt(suplendwaittime);
    }
    public int getDelAssistDataTime(){
        String delassistdatatime;
        delassistdatatime = editTextDelAssistDataTime.getText().toString();
        return Integer.parseInt(delassistdatatime);
    }
    public boolean isRadioButtonFlpBalancedPower(){
        return radioButtonFlpBalancedPower.isChecked();
    }
    public boolean isRadioButtonRequestLocationUpdates(){
        return radioButtonRequestLocationUpdates.isChecked();
    }
    public boolean isRadioButtonGetCurrentLocation(){
        return radioButtonGetCurrentLocation.isChecked();
    }
    public boolean isRadioButtonFlpHighAccuracy(){
        return radioButtonFlpHighAccuracy.isChecked();
    }
    public boolean isRadioButtonFlpLowPower(){
        return radioButtonFlpLowPower.isChecked();
    }
    public boolean isRadioButtonFlpNoPower(){
        return radioButtonFlpNoPower.isChecked();
    }
    public boolean isColdCheck(){
        return checkBoxisCold.isChecked();
    }
    @Override
    protected void onDestroy(){
        //TODO: 戻るボタンを押されたときにSetting
        settingPresenter.commitSetting();
        super.onDestroy();
    }
    @Override
    protected void onPause(){
        settingPresenter.commitSetting();
        super.onPause();
    }
}