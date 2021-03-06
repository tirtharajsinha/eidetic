package com.example.todo_app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

public class fourthFragment extends Fragment {

    GoogleSignInClient mGoogleSignInClient;
    SignInButton signInButton;
    Button signoutButton;
    Button helpButton;
    Button dashboardButton;
    Button activityButton;
    Button NotiButton;
    Context thiscontext;
    View view;
    TextView textView3;
    TextView textView4;
    TextView textView5;
    ImageView usrdp;


    public fourthFragment() {
        // require a empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_fourth, container, false);
        thiscontext = view.getContext();
        TextView head = (TextView) view.findViewById(R.id.textView3);
//        head.setText("hello tirtha!");
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(view.getContext(), gso);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(view.getContext());
        updateUI(account);


        FirstFragment hometab = new FirstFragment();
        SecondFragment todostab = new SecondFragment();
        ThirdFragment notificationtab = new ThirdFragment();
        fourthFragment profiletab = new fourthFragment();

        View.OnClickListener page_linker = v -> {
            Fragment fragment;
            System.out.println(v.getId() + " : " + R.id.dashboard_btn);
            switch (v.getId()) {

                case R.id.dashboard_btn:
                    loadFragment(hometab);
                    System.out.println("home tab");
                    break;
                case R.id.activity_btn:
                    loadFragment(todostab);
                    break;
                case R.id.notification_btn:
                    loadFragment(notificationtab);
                    break;
                case R.id.help_btn:
                    loadFragment(profiletab);
                    break;
                default:
                    System.out.println("nothing to do");

            }
        };
        helpButton = view.findViewById(R.id.help_btn);
        helpButton.setOnClickListener(page_linker);
        dashboardButton = view.findViewById(R.id.dashboard_btn);
        dashboardButton.setOnClickListener(page_linker);
        activityButton = view.findViewById(R.id.activity_btn);
        activityButton.setOnClickListener(page_linker);
        NotiButton = view.findViewById(R.id.notification_btn);
        NotiButton.setOnClickListener(page_linker);


//        sign out button setup

        signoutButton = view.findViewById(R.id.sign_out_button);
        signoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

        return view;
    }

    public void updateUI(GoogleSignInAccount account) {

        if (account != null) {
            String personName = account.getDisplayName();
            String personGivenName = account.getGivenName();
            String personFamilyName = account.getFamilyName();
            String personEmail = account.getEmail();
            String personId = account.getId();
            Uri personPhoto = account.getPhotoUrl();
            textView3 = view.findViewById(R.id.textView3);
            textView4 = view.findViewById(R.id.textView4);

            usrdp = view.findViewById(R.id.UserImage);
            textView3.setText(personName);
            textView4.setText("@" + personEmail);

            textView3.setTextSize(20);
            System.out.println(personPhoto+"url of user");

            if(personPhoto==null){
                Picasso.get().load(R.drawable.nulluserimage).into(usrdp);
            }
            else{
                Picasso.get().load(personPhoto).into(usrdp);
            }



        } else {
            redirectAuthPage();

        }
    }


    public void redirectAuthPage() {
        final Intent i = new Intent(thiscontext, LoginActivity.class);
        getActivity().finish();
        startActivity(i);
    }


    ActivityResultLauncher<Intent> startactivityresult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                        handleSignInResult(task);
                    }
                }
            }
    );

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            Toast.makeText(thiscontext, "Congratulations ! successfully signed in.", Toast.LENGTH_SHORT).show();

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.d("GOOGLE ERROR", e.getMessage());
        }
    }


    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        redirectAuthPage();
                        Toast.makeText(thiscontext, "See you later! successfully signed out.", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.flFragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
