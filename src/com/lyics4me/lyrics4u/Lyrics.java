package com.lyics4me.lyrics4u;

import java.util.List;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer.OnInitializedListener;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerView;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

public class Lyrics extends YouTubeBaseActivity implements
        OnInitializedListener {

    public static EditText lyricView;
    public VideoView mVideoView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lyrics);

        lyricView = (EditText) findViewById(R.id.lyricView);

        Bundle extras = getIntent().getExtras();
        String song = extras.getString("song");
        String artist = extras.getString("artist");
        String songName = extras.getString("songName");
        lyricView.setText(song);

       

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM,
                WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

       

        YouTubePlayerView youTubeView = (YouTubePlayerView) findViewById(R.id.youtubeView);
        youTubeView.initialize("AIzaSyARnvog5wasFol4h4XtfUbfRmcPm3BhW5k", this);

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.lyrics_menu, menu);
        return super.onCreateOptionsMenu(menu);

    }

    private void showChangeLog() {
        Intent i = new Intent(getApplicationContext(), ChangeLog.class);
        startActivity(i);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.about:
            showAbout();
            break;
        case R.id.share:
            share();
            break;
        case R.id.changeLog:
            showChangeLog();
            break;
        case R.id.exit:
            ExitApp();
            break;
        }
        return true;
    }

    private void share() {

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "String");
        startActivity(Intent.createChooser(shareIntent, "Share lyrics to..."));
    }

    private void showAbout() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                Lyrics.this);
        alertDialogBuilder.setTitle("About");
        alertDialogBuilder
                .setMessage(
                        "The App:"
                                + "\n"
                                + "This app was made find lyrics to a song."
                                + "\n"
                                + "It is simple and fast."
                                + "\n\n"
                                + "About me:"
                                + "\n"
                                + "I'm a freshman in college studying Computer Science."
                                + "\n"
                                + "I love Android and want to learn as much as possible."
                                + "\n\n" + "Contact me:" + "\n"
                                + "Email me bugs or suggestions :)" + "\n"
                                + "Gmail & Google Talk: Cvballa3g0@gmail.com"
                                + "\n")
                .setCancelable(false)
                .setNegativeButton("Close",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        })
                .setPositiveButton("Email me",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                SendEmail();
                            }
                        })
                .setNeutralButton("Rate my app",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent browserIntent = new Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse("https://play.google.com/store/apps/details?id=com.lyics4me.lyrics4u&utm_source=appcup.me&utm_medium=install-button&utm_campaign=organic-download"));
                                startActivity(browserIntent);
                            }
                        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    private void ExitApp() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                Lyrics.this);

        alertDialogBuilder.setTitle("Exiting Application");

        alertDialogBuilder
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Lyrics.this.finish();
                            }
                        })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    private void SendEmail() {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.putExtra(Intent.EXTRA_EMAIL,
                new String[] { "Cvballa3g0@gmail.com" });
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Lyrics For You");
        emailIntent.setType("plain/text");
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,
                "\n\n\n- Sent from Lyrics For You App");
        final PackageManager pm = getPackageManager();
        final List<ResolveInfo> matches = pm.queryIntentActivities(emailIntent,
                0);
        ResolveInfo best = null;
        for (final ResolveInfo info : matches)
            if (info.activityInfo.packageName.endsWith(".gm")
                    || info.activityInfo.name.toLowerCase().contains("gmail"))
                best = info;
        if (best != null)
            emailIntent.setClassName(best.activityInfo.packageName,
                    best.activityInfo.name);
        startActivity(emailIntent);
    }

    public void onInitializationSuccess(YouTubePlayer.Provider provider,
            YouTubePlayer player, boolean wasRestored) {
        if (!wasRestored) {
            try {
                player.cueVideo(Parser.getVideo());
            } catch (NullPointerException e) {
                player.cueVideo("EMsX1JnGEdE");
            }
        }
    }

    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return (YouTubePlayerView) findViewById(R.id.youtubeView);
    }

    public void onInitializationFailure(Provider arg0,
            YouTubeInitializationResult arg1) {
        Toast.makeText(this, "Initialization Fail", Toast.LENGTH_LONG).show();

    }
}
