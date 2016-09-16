package com.cetpainfotech.facebook;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class BlankFragment extends Fragment implements View.OnClickListener {

CallbackManager mcallbackManager;
LoginButton loginButton;
    TextView textView;
    ProfileTracker profileTracker;
    AccessTokenTracker tokenTracker;
    EditText textView2;
    Profile profile;
    Button button;
    JSONObject object;
    String id;
 private FacebookCallback<LoginResult> mCallBack=new FacebookCallback<LoginResult>() {

     @Override
     public void onSuccess(LoginResult loginResult) {
         AccessToken accessToken=loginResult.getAccessToken();
         profile=Profile.getCurrentProfile();

         if (profile!=null) {
             textView.setText("Welcome "+profile.getName());
             id=profile.getId();}
        textView2.setVisibility(View.VISIBLE);
          button.setVisibility(View.VISIBLE);

     }








     @Override
     public void onCancel() {
     }

     @Override
     public void onError(FacebookException error) {
     }
 };
    public void onClick(View v) {
        try {
            object=new JSONObject("{\"message\":\""+textView2.getText()+"\"}");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        loginButton.clearPermissions();

        loginButton.setPublishPermissions("publish_actions");
      GraphRequest rq= GraphRequest.newPostRequest(
                AccessToken.getCurrentAccessToken(),
                "/"+id+"/feed",object
                ,new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        // Insert your code here
                        Log.d("response",""+response);
                    } });
        rq.executeAsync();
        Toast.makeText(getContext(), "permission granted", Toast.LENGTH_SHORT).show();

    }
    public BlankFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());

 mcallbackManager=CallbackManager.Factory.create();
        tokenTracker=new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
if (currentAccessToken==null) {
    textView.setText("Please Login");
    textView2.setVisibility(View.INVISIBLE);
    button.setVisibility(View.INVISIBLE);
}
            }
        };
        profileTracker=new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                if (currentProfile!=null)
                    textView.setText("Welcome "+currentProfile.getName());
            }
        };
        profileTracker.startTracking();
        tokenTracker.startTracking();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_blank, container, false);
        textView= (TextView) view.findViewById(R.id.tv);
        textView2= (EditText) view.findViewById(R.id.textView);
        textView2.setVisibility(View.INVISIBLE);
        button= (Button) view.findViewById(R.id.button);
        button.setVisibility(View.INVISIBLE);
        button.setOnClickListener(this);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Profile profile=Profile.getCurrentProfile();
        if (profile!=null)
            textView.setText("Welcome "+profile.getName());
        textView2.setVisibility(View.VISIBLE);
        button.setVisibility(View.VISIBLE);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        tokenTracker.stopTracking();
        profileTracker.stopTracking();
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loginButton = (LoginButton) view.findViewById(R.id.login_button);
        loginButton.setReadPermissions("user_likes");
        loginButton.setFragment(this);
        loginButton.registerCallback(mcallbackManager,mCallBack);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mcallbackManager.onActivityResult(requestCode,resultCode,data);
    }



}
