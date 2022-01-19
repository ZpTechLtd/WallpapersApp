package com.example.wallpapersapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.wallpapersapp.R;
import com.example.wallpapersapp.model.Result;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class PreferenceManager {

    private static final String ADMOB_INTERSTITIAL = "admob_interstitial";
    private static final String ADMOB_native = "admob_native";
    private static final String ADMOB_native_banner = "admob_banner";
    private static final String ADMOB_OPEN = "admob_open";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Gson gson;
    private Context context;

    private static final String PRIVACY_POLICY = "privacy_policy";

    public PreferenceManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("wallpaper", Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
        gson = new Gson();
    }

    public void save(Result result) {
        List<Result> list = getList();
        if (list == null) {
            list = new ArrayList<>();
        }
        boolean isFound = false;
        Result res = null;
        for (Result result1 : list) {
            if (result1.getImageUrl().getUrl().equals(result.getImageUrl().getUrl())) {
                isFound = true;
                res = result1;
            }
        }
        if (isFound) {
            list.remove(res);
        } else {
            list.add(result);
        }
        String json = gson.toJson(list);
        sharedPreferences.edit().putString("list", json).commit();
    }

    public List<Result> getList() {

        return gson.fromJson(sharedPreferences.getString("list", null),
                new TypeToken<List<Result>>() {
                }.getType());
    }

    public boolean isFavourite(Result result) {
        List<Result> list = getList();
        if (list == null) {
            list = new ArrayList<>();
        }
        for (Result result1 : list) {
            if (result1.getImageUrl().getUrl().equals(result.getImageUrl().getUrl())) {
                return true;
            }
        }
        return false;
    }

    public void setAdmobInterstitial(String admobInterstitial) {
        editor.putString(ADMOB_INTERSTITIAL, admobInterstitial).commit();
    }

    public String getAdmobInterstitial() {
        return sharedPreferences.getString(ADMOB_INTERSTITIAL, context.getString(R.string.admob_interstitial));
    }

    public void setADMOB_native(String admob_native) {
        editor.putString(ADMOB_native, admob_native).commit();
    }

    public String getADMOB_native() {
        return sharedPreferences.getString(ADMOB_native, context.getString(R.string.admob_native));
    }

    public void setAdmobOpen(String admobOpen) {
        editor.putString(ADMOB_OPEN, admobOpen).commit();
    }

    public String getAdmobOpen() {
        return sharedPreferences.getString(ADMOB_OPEN, context.getString(R.string.admob_open));
    }


    public void setADMOB_native_banner(String admob_native) {
        editor.putString(ADMOB_native_banner, admob_native).commit();
    }

    public String getADMOB_native_banner() {
        return sharedPreferences.getString(ADMOB_native_banner, context.getString(R.string.admob_native_banner));
    }

    public void setPrivacyPolicy(boolean privacyPolicy) {
        editor.putBoolean(PRIVACY_POLICY, privacyPolicy).commit();
    }

    public boolean getPrivacyPolicy() {
        return sharedPreferences.getBoolean(PRIVACY_POLICY, false);
    }
}
