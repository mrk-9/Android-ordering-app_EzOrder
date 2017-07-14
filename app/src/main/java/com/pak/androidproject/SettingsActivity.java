package com.pak.androidproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Build;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;

import com.mirasense.scanditsdk.interfaces.ScanditSDK;
import com.mirasense.scanditsdk.interfaces.ScanditSDKOverlay;

public class SettingsActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener {
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if (Build.VERSION.SDK_INT >= 14) {
			super.setTheme(android.R.style.Theme_DeviceDefault);
		} else if (Build.VERSION.SDK_INT >= 11) {
			super.setTheme(android.R.style.Theme_Holo);
		} else {
			super.setTheme(android.R.style.Theme_Black_NoTitleBar);
		}
		
		addPreferencesFromResource(R.xml.preferences);
		
		// Loop over the categories.
		for (int i = 0; i < getPreferenceScreen().getPreferenceCount(); i++) {
			Preference categoryPref = getPreferenceScreen().getPreference(i);
			if (categoryPref instanceof PreferenceCategory) {
				PreferenceCategory category = (PreferenceCategory) categoryPref;
				// Loop over the preferences inside a category.
				for (int j = 0; j < category.getPreferenceCount(); j++) {
					Preference preference = category.getPreference(j);
				    if (preference instanceof ListPreference) {
				        updateListPreference((ListPreference) preference);
				    } else if (preference instanceof EditTextPreference) {
				    	updateEditTextPreference((EditTextPreference) preference);
				    }
				}
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	void updateListPreference(ListPreference preference) {
        preference.setSummary(preference.getEntry());
        
        if (preference.getKey().equals("camera_switch_visibility")) {
        	boolean enabled = true;
        	if (preference.getValue().equals("0")) {
        		enabled = false;
        	}
        	getPreferenceScreen().findPreference("camera_switch_button_x").setEnabled(enabled);
        	getPreferenceScreen().findPreference("camera_switch_button_y").setEnabled(enabled);
        }
	}
	
	void updateEditTextPreference(EditTextPreference preference) {
        preference.setSummary(preference.getText());
	}
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onResume() {
	    super.onResume();
	    getPreferenceScreen().getSharedPreferences()
	            .registerOnSharedPreferenceChangeListener(this);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onPause() {
	    super.onPause();
	    getPreferenceScreen().getSharedPreferences()
	            .unregisterOnSharedPreferenceChangeListener(this);
	}

	@SuppressWarnings("deprecation")
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		Preference preference = findPreference(key);
	    if (preference instanceof ListPreference) {
	        updateListPreference((ListPreference) preference);
	    } else if (preference instanceof EditTextPreference) {
	    	updateEditTextPreference((EditTextPreference) preference);
	    }
	}
	

	public static void setSettingsForPicker(Context context, ScanditSDK picker) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		
		if (prefs.getBoolean("ignore_preview_aspect_ratio", false)) {
			picker.ignorePreviewAspectRatio();
		}
		picker.setEan13AndUpc12Enabled(prefs.getBoolean("ean13_and_upc12_enabled", true));
		picker.setEan8Enabled(prefs.getBoolean("ean8_enabled", true));
		picker.setUpceEnabled(prefs.getBoolean("upce_enabled", true));
		picker.setCode39Enabled(prefs.getBoolean("code39_enabled", true));
		picker.setCode93Enabled(prefs.getBoolean("code93_enabled", false));
		picker.setCode128Enabled(prefs.getBoolean("code128_enabled", true));
		picker.setItfEnabled(prefs.getBoolean("itf_enabled", true));
		picker.setMsiPlesseyEnabled(prefs.getBoolean("msi_plessey_enabled", false));
		picker.setGS1DataBarEnabled(prefs.getBoolean("databar_enabled", false));
		picker.setGS1DataBarExpandedEnabled(prefs.getBoolean("databar_expanded_enabled", false));
		picker.setCodabarEnabled(prefs.getBoolean("codabar_enabled", false));
		
		int msiPlesseyChecksum = Integer.valueOf(prefs.getString("msi_plessey_checksum", "1"));
		int actualChecksum = ScanditSDK.CHECKSUM_MOD_10;
		if (msiPlesseyChecksum == 0) {
			actualChecksum = ScanditSDK.CHECKSUM_NONE;
		} else if (msiPlesseyChecksum == 2) {
			actualChecksum = ScanditSDK.CHECKSUM_MOD_11;
		} else if (msiPlesseyChecksum == 3) {
			actualChecksum = ScanditSDK.CHECKSUM_MOD_1010;
		} else if (msiPlesseyChecksum == 4) {
			actualChecksum = ScanditSDK.CHECKSUM_MOD_1110;
		}
		picker.setMsiPlesseyChecksumType(actualChecksum);
		
		picker.setQrEnabled(prefs.getBoolean("qr_enabled", true));
		picker.setDataMatrixEnabled(prefs.getBoolean("data_matrix_enabled", true));
		if (prefs.getBoolean("data_matrix_enabled", true)) {
			picker.setMicroDataMatrixEnabled(prefs.getBoolean("micro_data_matrix_enabled", false));
			picker.setInverseRecognitionEnabled(prefs.getBoolean("inverse_recognition", false));
		}
		picker.setPdf417Enabled(prefs.getBoolean("pdf417_enabled", false));
		
		picker.setScanningHotSpot(prefs.getInt("hot_spot_x", 50) / 100.0f, 
				prefs.getInt("hot_spot_y", 50) / 100.0f);
		picker.restrictActiveScanningArea(prefs.getBoolean("restrict_scanning_area", false));
		picker.setScanningHotSpotHeight(prefs.getInt("hot_spot_height", 25) / 100.0f);
		picker.getOverlayView().drawViewfinder(prefs.getBoolean("draw_viewfinder", true));
		picker.getOverlayView().setViewfinderDimension(
				prefs.getInt("viewfinder_width",  70) / 100.0f, 
				prefs.getInt("viewfinder_height",  30) / 100.0f, 
				prefs.getInt("viewfinder_landscape_width",  40) / 100.0f, 
				prefs.getInt("viewfinder_landscape_height",  30) / 100.0f);
		
		picker.getOverlayView().setBeepEnabled(prefs.getBoolean("beep_enabled", true));
		picker.getOverlayView().setVibrateEnabled(prefs.getBoolean("vibrate_enabled", false));
		
		picker.getOverlayView().showSearchBar(prefs.getBoolean("search_bar", false));
		picker.getOverlayView().setSearchBarPlaceholderText(prefs.getString(
				"search_bar_placeholder", "Scan barcode or enter it here"));

		picker.getOverlayView().setTorchEnabled(prefs.getBoolean("torch_enabled", true));
		picker.getOverlayView().setTorchButtonPosition(
				prefs.getInt("torch_button_x", 5) / 100.0f, prefs.getInt("torch_button_y", 1) / 100.0f, 67, 33);
		
		int cameraSwitch = Integer.valueOf(prefs.getString("camera_switch_visibility", "0"));
		int cameraSwitchVisibility = ScanditSDKOverlay.CAMERA_SWITCH_NEVER;
		if (cameraSwitch == 1) {
			cameraSwitchVisibility = ScanditSDKOverlay.CAMERA_SWITCH_ON_TABLET;
		} else if (cameraSwitch == 2) {
			cameraSwitchVisibility = ScanditSDKOverlay.CAMERA_SWITCH_ALWAYS;
		}
		picker.getOverlayView().setCameraSwitchVisibility(cameraSwitchVisibility);
		picker.getOverlayView().setCameraSwitchButtonPosition(
				prefs.getInt("camera_switch_button_x", 5) / 100.0f, prefs.getInt("camera_switch_button_y", 1) / 100.0f, 67, 33); 
	}
}
