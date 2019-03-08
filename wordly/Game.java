package edu.fandm.enovak.wordly;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnSystemUiVisibilityChangeListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Game extends AppCompatActivity {
    public static final String TAG = "enovak.Game";
    private View all;
    private Bitmap[] bitmaps = new Bitmap[3];
    private Context ctx;
    protected ImageView hintIV;
    private Thread isst;
    protected final int numImages = 3;
    private GridView puzzle;
    private ArrayList<String> shown;
    private ArrayList<String> soln;

    /* renamed from: edu.fandm.enovak.wordly.Game$1 */
    class C03021 implements OnSystemUiVisibilityChangeListener {

        /* renamed from: edu.fandm.enovak.wordly.Game$1$1 */
        class C03011 implements Runnable {
            C03011() {
            }

            public void run() {
                Game.this.hide(null);
            }
        }

        C03021() {
        }

        public void onSystemUiVisibilityChange(int visibility) {
            if ((visibility & 4) == 0) {
                new Handler().postDelayed(new C03011(), 2500);
            }
        }
    }

    /* renamed from: edu.fandm.enovak.wordly.Game$3 */
    class C03053 implements OnClickListener {
        C03053() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
        }
    }

    class ImageDownloader extends AsyncTask<String, Void, Void> {
        ImageDownloader() {
        }

        protected void onPreExecute() {
            Game.this.endSlideShow();
        }

        private void downloadImageFromURL(String URL, int idx) {
            try {
                InputStream in = new BufferedInputStream(new URL(URL).openStream());
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                byte[] buf = new byte[1024];
                while (true) {
                    int read = in.read(buf);
                    int n = read;
                    if (-1 != read) {
                        out.write(buf, 0, n);
                    } else {
                        out.close();
                        in.close();
                        byte[] response = out.toByteArray();
                        Game.this.bitmaps[idx] = BitmapFactory.decodeByteArray(response, 0, response.length);
                        return;
                    }
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }

        protected Void doInBackground(String... params) {
            BufferedReader reader = null;
            int i = 0;
            String keyword = params[0];
            String str = Game.TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Downloading image for: ");
            stringBuilder.append(keyword);
            Log.d(str, stringBuilder.toString());
            try {
                String readLine;
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("https://pixabay.com/api/?key=10898531-392d5bb44bdda418e54650675&q=");
                stringBuilder2.append(keyword);
                HttpURLConnection con = (HttpURLConnection) new URL(stringBuilder2.toString()).openConnection();
                con.setRequestMethod("GET");
                con.setDoOutput(true);
                con.connect();
                reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = null;
                while (true) {
                    readLine = reader.readLine();
                    line = readLine;
                    if (readLine == null) {
                        break;
                    }
                    StringBuilder stringBuilder3 = new StringBuilder();
                    stringBuilder3.append(line);
                    stringBuilder3.append("\n");
                    sb.append(stringBuilder3.toString());
                }
                readLine = null;
                try {
                    JSONArray arr = new JSONObject(sb.toString()).getJSONArray("hits");
                    if (arr.length() == 0) {
                        String str2 = Game.TAG;
                        StringBuilder stringBuilder4 = new StringBuilder();
                        stringBuilder4.append("No images for: ");
                        stringBuilder4.append(keyword);
                        Log.d(str2, stringBuilder4.toString());
                        Game.this.endSlideShow();
                        try {
                            reader.close();
                        } catch (int i2) {
                            i2.printStackTrace();
                        }
                        return null;
                    }
                    while (i2 < 3) {
                        downloadImageFromURL(arr.getJSONObject(i2).getString("webformatURL"), i2);
                        i2++;
                    }
                    try {
                        reader.close();
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                    Log.d(Game.TAG, "done downloading images.");
                    return null;
                } catch (JSONException je) {
                    je.printStackTrace();
                }
            } catch (MalformedURLException mue) {
                mue.printStackTrace();
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException ioe2) {
                ioe2.printStackTrace();
                if (reader != null) {
                    reader.close();
                }
            } catch (Throwable th) {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException ioe3) {
                        ioe3.printStackTrace();
                    }
                }
            }
        }

        protected void onPostExecute(Void v) {
            Game.this.isst = new ImageSlideShowThread();
            Game.this.isst.start();
        }
    }

    class ImageSlideShowThread extends Thread {
        private final long SLEEP_TIME_MS = 8000;
        private int cur = 0;

        /* renamed from: edu.fandm.enovak.wordly.Game$ImageSlideShowThread$1 */
        class C03061 implements Runnable {
            C03061() {
            }

            public void run() {
                ImageSlideShowThread.this.animatedImageSwitch();
            }
        }

        ImageSlideShowThread() {
        }

        public void run() {
            while (true) {
                Game.this.runOnUiThread(new C03061());
                this.cur = (this.cur + 1) % 3;
                try {
                    Thread.sleep(8000);
                } catch (InterruptedException e) {
                    return;
                }
            }
        }

        private void animatedImageSwitch() {
            Animation anim_out = AnimationUtils.loadAnimation(Game.this.ctx, 17432577);
            final Animation anim_in = AnimationUtils.loadAnimation(Game.this.ctx, 17432576);
            anim_out.setDuration(1500);
            anim_out.setAnimationListener(new AnimationListener() {

                /* renamed from: edu.fandm.enovak.wordly.Game$ImageSlideShowThread$2$1 */
                class C03071 implements AnimationListener {
                    C03071() {
                    }

                    public void onAnimationStart(Animation animation) {
                        Game.this.hintIV.setVisibility(0);
                    }

                    public void onAnimationRepeat(Animation animation) {
                    }

                    public void onAnimationEnd(Animation animation) {
                    }
                }

                public void onAnimationStart(Animation animation) {
                }

                public void onAnimationRepeat(Animation animation) {
                }

                public void onAnimationEnd(Animation animation) {
                    Game.this.hintIV.setImageBitmap(Game.this.bitmaps[ImageSlideShowThread.this.cur]);
                    anim_in.setAnimationListener(new C03071());
                    Game.this.hintIV.startAnimation(anim_in);
                }
            });
            Game.this.hintIV.startAnimation(anim_out);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0310R.layout.activity_game);
        setRequestedOrientation(5);
        this.all = findViewById(C0310R.id.all);
        this.hintIV = (ImageView) findViewById(C0310R.id.game_iv_hint);
        this.ctx = getApplicationContext();
        this.all.setOnSystemUiVisibilityChangeListener(new C03021());
        Intent launchIntent = getIntent();
        String start = launchIntent.getStringExtra("start_word");
        String str = TAG;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(start);
        stringBuilder.append("\n");
        Log.d(str, stringBuilder.toString());
        str = launchIntent.getStringExtra("end_word");
        String str2 = TAG;
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append(str);
        stringBuilder2.append("\n");
        Log.d(str2, stringBuilder2.toString());
        this.soln = launchIntent.getStringArrayListExtra("solution");
        str2 = TAG;
        stringBuilder2 = new StringBuilder();
        stringBuilder2.append("solution: ");
        stringBuilder2.append(this.soln);
        stringBuilder2.append("\n");
        Log.d(str2, stringBuilder2.toString());
        this.shown = new ArrayList(this.soln.size());
        while (this.shown.size() < this.soln.size()) {
            this.shown.add("    ");
        }
        this.shown.set(0, this.soln.get(0));
        this.shown.set(this.soln.size() - 1, this.soln.get(this.soln.size() - 1));
        this.puzzle = (GridView) findViewById(C0310R.id.game_words_gv);
        this.puzzle.setNumColumns(this.shown.size());
        this.puzzle.setAdapter(new ArrayAdapter(this, C0310R.layout.cell, this.shown.toArray(new String[this.shown.size()])));
    }

    private int getCurrentEmptySpot() {
        int idx = -1;
        String text = "blahblahblah";
        while (idx < this.puzzle.getChildCount() && !text.equals("    ")) {
            idx++;
            text = ((TextView) this.puzzle.getChildAt(idx)).getText().toString();
        }
        return idx;
    }

    public void guess(View v) {
        final TextView tv = (TextView) v;
        final int curEmptySpot = getCurrentEmptySpot();
        Builder builder = new Builder(this);
        builder.setTitle("Guess a word");
        final EditText input = new EditText(this);
        input.setInputType(1);
        builder.setView(input);
        builder.setPositiveButton("OK", new OnClickListener() {

            /* renamed from: edu.fandm.enovak.wordly.Game$2$1 */
            class C03031 implements View.OnClickListener {
                C03031() {
                }

                public void onClick(View v) {
                    Game.this.finish();
                }
            }

            public void onClick(DialogInterface dialog, int which) {
                String guess = input.getText().toString();
                if (guess.length() != 4) {
                    Toast.makeText(Game.this.ctx, "That word is not four letters long!", 0).show();
                } else if (!WordGraph.oneLetterDiff((String) Game.this.soln.get(curEmptySpot - 1), guess)) {
                    Context access$000 = Game.this.ctx;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("That word is not one letter different from ");
                    stringBuilder.append((String) Game.this.soln.get(curEmptySpot - 1));
                    stringBuilder.append("!");
                    Toast.makeText(access$000, stringBuilder.toString(), 0).show();
                } else if (guess.equals(Game.this.soln.get(curEmptySpot))) {
                    Game.this.shown.set(curEmptySpot, guess);
                    tv.setText(guess);
                    String str = Game.TAG;
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("shown: ");
                    stringBuilder2.append(Game.this.shown);
                    stringBuilder2.append("   soln: ");
                    stringBuilder2.append(Game.this.soln);
                    Log.d(str, stringBuilder2.toString());
                    if (Game.this.shown.equals(Game.this.soln)) {
                        Log.d(Game.TAG, "WHOA!");
                        Toast.makeText(Game.this.ctx, "CORRECT!!", 1).show();
                        Game.this.endSlideShow();
                        ((Button) Game.this.findViewById(C0310R.id.game_butt_hint)).setVisibility(4);
                        Game.this.hintIV.setVisibility(0);
                        Game.this.hintIV.setImageResource(C0310R.drawable.star);
                        Game.this.hintIV.setBackground(null);
                        Game.this.hintIV.setAnimation(AnimationUtils.loadAnimation(Game.this.ctx, C0310R.anim.spin));
                        Game.this.hintIV.animate();
                        Game.this.hintIV.setOnClickListener(new C03031());
                        return;
                    }
                    new ImageDownloader().execute(new String[]{(String) Game.this.soln.get(curEmptySpot + 1)});
                } else {
                    Toast.makeText(Game.this.ctx, "That's not the word I'm thinking of!", 0).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", new C03053());
        builder.show();
    }

    protected void onResume() {
        super.onResume();
        hide(null);
        new ImageDownloader().execute(new String[]{(String) this.soln.get(1)});
    }

    public void hint(View v) {
        int idx = getCurrentEmptySpot();
        String answer = (String) this.soln.get(idx);
        int i = 0;
        while (((String) this.soln.get(idx - 1)).charAt(i) == answer.charAt(i)) {
            i++;
        }
        char newLetter = answer.charAt(i);
        Context context = this.ctx;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(BuildConfig.FLAVOR);
        stringBuilder.append(newLetter);
        Toast.makeText(context, stringBuilder.toString(), 1).show();
    }

    public void hide(View v) {
        this.all.setSystemUiVisibility(2822);
        getSupportActionBar().hide();
    }

    private void endSlideShow() {
        if (this.isst != null) {
            this.isst.interrupt();
            this.isst = null;
        }
    }
}
