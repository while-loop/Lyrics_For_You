package com.lyics4me.lyrics4u;

import java.lang.reflect.Method;
import java.util.List;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.speech.RecognizerIntent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainScreen extends Activity {
    private static final int REQUEST_CODE = 1234;
    private ListView wordsList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        try {
            Class<?> strictModeClass = Class.forName("android.os.StrictMode");
            Class<?> strictModeThreadPolicyClass = Class
                    .forName("android.os.StrictMode$ThreadPolicy");
            Object laxPolicy = strictModeThreadPolicyClass.getField("LAX").get(
                    null);
            Method method_setThreadPolicy = strictModeClass.getMethod(
                    "setThreadPolicy", strictModeThreadPolicyClass);
            method_setThreadPolicy.invoke(null, laxPolicy);
        } catch (Exception e) {

        }
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_screen);

        final EditText editText = (EditText) findViewById(R.id.editText1);

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId,
                    KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    dialog = ProgressDialog.show(MainScreen.this,
                            "Searching for lyrics", "Please wait...");
                    Thread thread = new Thread(new Runnable() {
                        public void run() {
                            String search_entry = ((EditText) findViewById(R.id.editText1))
                                    .getText().toString();
                            if (search_entry.toLowerCase().equals("fuck you"))
                                handler2.sendEmptyMessage(0);
                            else {
                                search();
                                handler.sendEmptyMessage(0);
                            }
                        }
                    });
                    thread.start();
                    return true;
                } else
                    return false;

            }
        });
        

        final Drawable x = getResources().getDrawable(R.drawable.image4);
        x.setBounds(0, 0, x.getIntrinsicWidth(), x.getIntrinsicHeight());

        Button Button1 = (Button) findViewById(R.id.button1);

        editText.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent arg1) {
                if (editText.getCompoundDrawables()[2] == null) {
                    editText.setCompoundDrawables(null, null, x, null);
                    return false;
                }
                if (arg1.getAction() != MotionEvent.ACTION_DOWN) {
                    // only respond to the down type
                    return false;
                }
                if (arg1.getX() > editText.getMeasuredWidth()
                        - editText.getPaddingRight() - x.getIntrinsicWidth()) {
                    editText.setText("");
                    return true;
                } else {
                    return false;
                }
            }

        });

        /* Voice input
         * Button speakButton = (Button) findViewById(R.id.speakButton);
         * speakButton.setOnClickListener(new View.OnClickListener() {
         * public void onClick(View v) {
         * Intent intent = new Intent(
         * RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
         * intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
         * RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
         * intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Listening...");
         * startActivityForResult(intent, REQUEST_CODE);
         * }
         * });
         * wordsList = (ListView) findViewById(R.id.listView1);
         */

        Button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog = ProgressDialog.show(MainScreen.this,
                        "Searching for lyrics", "Please wait...");
                Thread thread = new Thread(new Runnable() {
                    public void run() {
                        String search_entry = ((EditText) findViewById(R.id.editText1))
                                .getText().toString();
                        if (search_entry.toLowerCase().equals("fuck you"))
                            handler2.sendEmptyMessage(0);
                        else {
                            search();
                            handler.sendEmptyMessage(0);
                        }
                    }
                });
                thread.start();
            }
        });

    }

    private void showOneButtonDialog(String message, String Title,
            final boolean exit, final String toastMessage) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle(Title);
        dialogBuilder.setMessage(message);
        dialogBuilder.setPositiveButton("Close",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), toastMessage,
                                Toast.LENGTH_LONG).show();
                        if (exit) {
                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.addCategory(Intent.CATEGORY_HOME);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }
                });
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    public void search() {
        EditText editText1 = (EditText) findViewById(R.id.editText1);
        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(editText1.getWindowToken(), 0);

        Intent i = new Intent(getApplicationContext(), Lyrics.class);

        String lyrics = "";

        try {
            String search_entry = ((EditText) findViewById(R.id.editText1))
                    .getText().toString();

            // loader
            lyrics = Parser.parseLyrics(Parser.getURL(Parser.getSearchURL(search_entry)));
            String artist = Parser.ARTIST;
            String songName = Parser.SONG;
            i.putExtra("song", lyrics);
            i.putExtra("artist", artist);
            i.putExtra("songName", songName);
            startActivity(i);

        } catch (CustomException e) {
            handler1.sendEmptyMessage(0);
        } catch (StringIndexOutOfBoundsException s) {
            handler1.sendEmptyMessage(0);
        } catch (RuntimeException rt) {
            handler1.sendEmptyMessage(0);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_screen, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.about:
            showAbout();
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

    private void showChangeLog() {
        Intent i = new Intent(getApplicationContext(), ChangeLog.class);
        startActivity(i);
    }

    private void showAbout() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                MainScreen.this);
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
                MainScreen.this);

        alertDialogBuilder.setTitle("Exiting Application");

        alertDialogBuilder
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                MainScreen.this.finish();
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

    private Dialog dialog = null;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            dialog.dismiss();
        }
    };

    private Handler handler1 = new Handler() {
        public void handleMessage(android.os.Message msg) {
            String err = ("No lyrics were found");
            showOneButtonDialog(err, "Uh oh", false, "Sorry =/");
        }
    };

    // Easter egg =D
    private Handler handler2 = new Handler() {
        public void handleMessage(android.os.Message msg) {
            showOneButtonDialog("fuck you too", "Challenge Accepted", true,
                    "Bitch");
            Thread Timer = new Thread() {
                public void run() {
                    try {
                        sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        System.exit(0);
                    }
                }
            };
            Timer.start();
        }
    };
}
