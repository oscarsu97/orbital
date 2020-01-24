package com.example.myorbital;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/*import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;*/
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LoginFragment extends Fragment {

    /* List<AuthUI.IdpConfig> providers;
     private static final int My_Request_Code = 7117;
     private static final int REQUEST_OK = -1;
     Button btn_sign_out;*/
    private LoginButton loginButton;
    private ImageView ProfilePic;
    private TextView Name;
    private TextView Email;
    private CallbackManager callbackManager;
    private TextView fbName;
    private TextView testName;
    private NavigationView navigationView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View loginView = inflater.inflate(R.layout.login_fragment,container,false);
        fbName = loginView.findViewById(R.id.fb_name);
        loginButton = loginView.findViewById(R.id.login_button);
        testName = loginView.findViewById(R.id.testName);

        //btn_sign_out = v.findViewById(R.id.btn_sign_out);
        /*View navigationHeaderView = inflater.inflate(R.layout.nav_header_main, null);
        ProfilePic = navigationHeaderView.findViewById(R.id.profile_photo);
        Name = navigationHeaderView.findViewById(R.id.profile_name);
        Email = navigationHeaderView.findViewById(R.id.email);*/

        View activityMainView = inflater.inflate(R.layout.activity_main, null);
        navigationView = activityMainView.findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        Name = headerView.findViewById(R.id.nav_username);
        Email = headerView.findViewById(R.id.nav_mail);
        ProfilePic = headerView.findViewById(R.id.nav_user_photo);

        callbackManager = CallbackManager.Factory.create();

        return loginView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loginButton.setPermissions(Arrays.asList("email, public_profile"));

        loginButton.setFragment(this);

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                fbName.setText("success");
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                fbName.setText("error");
            }
        });

       /* providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.PhoneBuilder().build(),
                new AuthUI.IdpConfig.FacebookBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build()
        );

        btn_sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthUI.getInstance()
                        .signOut(getContext())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                btn_sign_out.setEnabled(false);
                                showSignInOptions();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        showSignInOptions();*/
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    AccessTokenTracker tokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            // user are logged out
            if (currentAccessToken == null) {
                Name.setText("");
                Email.setText("");
              //  ProfilePic.setImageResource(0);
                Toast.makeText(getContext(), "logged out", Toast.LENGTH_SHORT).show();
            } else {
                loadUserProfile(currentAccessToken);
            }
        }
    };


    private void loadUserProfile(AccessToken newAccessToken) {
        GraphRequest request = GraphRequest.newMeRequest(newAccessToken, new GraphRequest.GraphJSONObjectCallback() {
            //response in the form of json object
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    String first_name = object.getString("first_name");
                    String last_name = object.getString("last_name");
                    String email = object.getString("email");
                    String id = object.getString("id");
                    //check if image uploading works as well
                    String image_url = "https://graph.facebook.com/" + id + "/picture?type=normal";

                    fbName.setText(email);
                    Name.setText(first_name + " " + last_name);
                    testName.setText(first_name + " " + last_name);
                    Email.setText(email);

                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions.dontAnimate();
                  //  Glide.with(LoginFragment.this).load(image_url).into(ProfilePic);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "first_name,last_name,email,id");
        request.setParameters(parameters);
        request.executeAsync();
    }

    /*private void passData(String name, String email){
        Intent passdataIntent = new Intent()
    }*/

    /*private void showSignInOptions() {
        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setTheme(R.style.MyTheme)
                        .build(), My_Request_Code
        );
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == My_Request_Code) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            // result ok could be faulty
            if (resultCode == REQUEST_OK) {
                //Get User
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                //show email on Toast
                Toast.makeText(getContext(), "" + user.getEmail(), Toast.LENGTH_SHORT).show();
                //set Button signout
                btn_sign_out.setEnabled(true);
                //set navigation drawer to user profile detail eg. android@gmail.com to oscar@gmail.com
                //
                //
            } else {
                Toast.makeText(getContext(), "" + response.getError().getMessage(), Toast.LENGTH_SHORT).show();

            }
        }
    }*/
}
