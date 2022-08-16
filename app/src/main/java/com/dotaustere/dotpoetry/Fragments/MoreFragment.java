package com.dotaustere.dotpoetry.Fragments;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.dotaustere.dotpoetry.Activities.DevActivity;
import com.dotaustere.dotpoetry.R;
import com.dotaustere.dotpoetry.databinding.FragmentMoreBinding;
import com.google.firebase.appcheck.interop.BuildConfig;

import es.dmoral.toasty.Toasty;

public class MoreFragment extends Fragment {

    Button textView;
    FragmentMoreBinding binding;
    Intent intent;
    String profileId = "Hasnain.62777/";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMoreBinding.inflate(inflater, container, false);
//        View view = inflater.inflate(R.layout.fragment_more, container, false);

        binding.exitBtn.setOnClickListener(view -> System.exit(0));


        binding.addPoetry.setOnClickListener(view -> Toast.makeText(getContext(), "coming soon", Toast.LENGTH_SHORT).show());
        binding.feedBackBtn.setOnClickListener(view -> {
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getContext().getPackageName())));
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=" + getContext().getPackageName())));
            }


        });
        binding.shareBtn.setOnClickListener(view -> {

            try {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Share");
                String shareMessage = "https://play.google.com/store/apps/details?=" + BuildConfig.APPLICATION_ID + "\n\n";
                intent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(intent, "Share by Dot Austere"));
            } catch (Exception e) {
                Toast.makeText(getContext(), "Error occurred", Toast.LENGTH_SHORT).show();

            }


        });
        binding.addPoetry.setOnClickListener(view -> {
            Toasty.error(getContext(), "Not Available yet", Toasty.LENGTH_SHORT, true).show();
            binding.addPoetry.setEnabled(false);
        });
        binding.aboutDevelopers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getContext(), DevActivity.class);
                startActivity(intent);

            }
        });

//        binding.aboutDevelopers.setOnClickListener(view -> {
//
//            View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dev_dialoglayout, null);
//            AlertDialog alertDialog = new AlertDialog.Builder(getContext()).setView(dialogView).create();
//            alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//
//            TextView devTv = dialogView.findViewById(R.id.devTextView);
//            ImageView whatsappBtn = dialogView.findViewById(R.id.whatsappBtn);
//            ImageView facebookBtn = dialogView.findViewById(R.id.facebookBtn);
//
//            whatsappBtn.setOnClickListener(view1 -> {
//
//                intent = new Intent(Intent.ACTION_DIAL);
//                intent.setData(Uri.parse("tel:+923499749018"));
//                startActivity(intent);
//                alertDialog.dismiss();
//
//                Toasty.success(getContext(), "You Clicked The TextView", Toasty.LENGTH_SHORT, true).show();
//            });
//            facebookBtn.setOnClickListener(view2 -> {
//
//                try {
//                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/" + profileId));
//                    startActivity(intent);
//                    alertDialog.dismiss();
//                } catch (Exception e) {
//                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/" + profileId));
//                    startActivity(intent);
//                    alertDialog.dismiss();
//                }
//
//
//                Toasty.success(getContext(), "You Clicked The TextView", Toasty.LENGTH_SHORT, true).show();
//            });
//
//            alertDialog.show();
//
//
//        });


        return binding.getRoot();

    }

}