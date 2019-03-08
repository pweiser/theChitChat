package edu.fandm.pweiser.chitchat;

/** Code is not ours and was taken from a wordly.apk */

import android.content.Context;
import android.content.DialogInterface;
//import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
//import android.view.View.OnSystemUiVisibilityChangeListener;
import android.view.animation.Animation;
//import android.view.animation.Animation.AnimationListener;
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
import android.content.pm.ActivityInfo;
import android.support.v7.app.ActionBar;
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
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;


public class GameActivity extends AppCompatActivity {
    public static final String TAG = "GameActivityTAG";
    ImageView iv;
    ImageView iv2;
    ImageView iv3;
    ImageView iv4;
    private String Url;
    private ArrayList<String> urls;
    private RequestQueue rq;
    private String search;
    private RequestQueue mRequestQueue;

    private View all;
    private Bitmap[] bitmaps = new Bitmap[3];
    private Context ctx;
    protected ImageView hintIV;
    private Thread isst;
    protected final int numImages = 3;
    private GridView puzzle;
    private ArrayList<String> shown;
    private ArrayList<String> soln;
    private String imageUrl;


    class Game1 implements View.OnSystemUiVisibilityChangeListener {

        /* renamed from:C03011 */
        class Game_2 implements Runnable {
            Game_2() {
            }

            public void run() {
                GameActivity.this.hide(null);
            }
        }

        Game1() {
        }

        public void onSystemUiVisibilityChange(int visibility) {
            if ((visibility & 4) == 0) {
                new Handler().postDelayed(new Game_2(), 2500);
            }
        }
    }

    /* renamed from:C03053 */
    class Game3 implements DialogInterface.OnClickListener {
        Game3() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
        }
    }

    /*class ImageDownloader extends AsyncTask<String, Void, Void> {
        ImageDownloader() {
        }

        protected void onPreExecute() {
            GameActivity.this.endSlideShow();
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
                        GameActivity.this.bitmaps[idx] = BitmapFactory.decodeByteArray(response, 0, response.length);
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
            String str = GameActivity.TAG;
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
                        String str2 = GameActivity.TAG;
                        StringBuilder stringBuilder4 = new StringBuilder();
                        stringBuilder4.append("No images for: ");
                        stringBuilder4.append(keyword);
                        Log.d(str2, stringBuilder4.toString());
                        GameActivity.this.endSlideShow();
                        reader.close();
                        return null;
                    }
                    while (i < 3) {
                        downloadImageFromURL(arr.getJSONObject(i).getString("webformatURL"), i);
                        i++;
                    }

                    try {
                        reader.close();
                    }
                    catch (IOException ioe) {
                        ioe.printStackTrace();
                    }

                    Log.d(GameActivity.TAG, "done downloading images.");
                    return null;
                } catch (JSONException je) {
                    je.printStackTrace();
                }
            } catch (MalformedURLException mue) {
                mue.printStackTrace();
                if (reader != null) {
                    try {
                        reader.close();
                    }
                    catch(IOException e) {
                        Log.e(TAG, "IO Exception");
                    }
                }
            } catch (IOException ioe2) {
                ioe2.printStackTrace();
                if (reader != null) {
                    try {
                        reader.close();
                    }
                    catch(IOException e) {
                        Log.e(TAG, "IO Exception");
                    }
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
            return null;
        }
        protected void onPostExecute(Void v) {
            GameActivity.this.isst = new ImageSlideShowThread();
            GameActivity.this.isst.start();
        }
    }*/

    class ImageSlideShowThread extends Thread {
        private final long SLEEP_TIME_MS = 8000;
        private int cur = 0;

        /* Was C03061 */
        class GameImageSlideShow implements Runnable
        {
            GameImageSlideShow() { }

            public void run()
            {
                ImageSlideShowThread.this.animatedImageSwitch();
            }
        }

        ImageSlideShowThread() { }

        public void run() {
            while (true) {
                GameActivity.this.runOnUiThread(new GameImageSlideShow());
                this.cur = (this.cur + 1) % 3;
                try {
                    Thread.sleep(8000);
                }
                catch (InterruptedException e) {
                    return;
                }
            }
        }

        private void animatedImageSwitch() {
            Animation anim_out = AnimationUtils.loadAnimation(GameActivity.this.ctx, R.anim.fade_out);//Was random numbers instead of R.anim.blink
            final Animation anim_in = AnimationUtils.loadAnimation(GameActivity.this.ctx, R.anim.fade_in);//Was random numbers
            anim_out.setDuration(1500);
            anim_out.setAnimationListener(new Animation.AnimationListener()
            {

                /* Was C03071 */
                class GameImageSlideShowThread2 implements Animation.AnimationListener
                {
                    GameImageSlideShowThread2()
                    {
                    }

                    public void onAnimationStart(Animation animation)
                    {
                        GameActivity.this.hintIV.setVisibility(View.INVISIBLE);
                        // used to be Game.this.hintIV.setVisibility(0);
                    }

                    public void onAnimationRepeat(Animation animation)
                    {
                    }

                    public void onAnimationEnd(Animation animation) {
                    }
                }

                public void onAnimationStart(Animation animation) {
                }

                public void onAnimationRepeat(Animation animation) {
                }

                public void onAnimationEnd(Animation animation) {
                    GameActivity.this.hintIV.setImageBitmap(GameActivity.this.bitmaps[ImageSlideShowThread.this.cur]);
                    anim_in.setAnimationListener(new GameImageSlideShowThread2());
                    GameActivity.this.hintIV.startAnimation(anim_in);
                }
            });
            GameActivity.this.hintIV.startAnimation(anim_out);
        }
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // setRequestedOrientation(5);
        this.all = findViewById(R.id.all);
        this.hintIV = (ImageView) findViewById(R.id.game_iv_hint);
        this.ctx = getApplicationContext();
        this.all.setOnSystemUiVisibilityChangeListener(new Game1());
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
        this.puzzle = (GridView) findViewById(R.id.words_gv);
        this.puzzle.setNumColumns(this.shown.size());
        this.puzzle.setAdapter(new ArrayAdapter(this, R.layout.cell, this.shown.toArray(new String[this.shown.size()])));
        iv = (ImageView) findViewById(R.id.Pic);
        iv2 = (ImageView) findViewById(R.id.Pic2);
        iv3 = (ImageView) findViewById(R.id.Pic3);
        iv4 = (ImageView) findViewById(R.id.Pic4);
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
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            class C03031 implements View.OnClickListener {
                C03031() {
                }

                public void onClick(View v) {
                    GameActivity.this.finish();
                }
            }

            public void onClick(DialogInterface dialog, int which) {
                String guess = input.getText().toString();
                MediaPlayer mp ;
                if (guess.length() != 4) {
                    Toast.makeText(GameActivity.this.ctx, "That word is not four letters long!", Toast.LENGTH_SHORT).show();
                    mp = MediaPlayer.create(GameActivity.this.ctx,R.raw.loss);
                    mp.start();
                } else if (!WordGraph.oneLetterDiff((String) GameActivity.this.soln.get(curEmptySpot - 1), guess)) {
                    Context access$000 = GameActivity.this.ctx;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("That word is not one letter different from ");
                    stringBuilder.append((String) GameActivity.this.soln.get(curEmptySpot - 1));
                    stringBuilder.append("!");
                    mp = MediaPlayer.create(GameActivity.this.ctx,R.raw.loss);
                    mp.start();
                    Toast.makeText(access$000, stringBuilder.toString(), Toast.LENGTH_SHORT).show();
                } else if (guess.equals(GameActivity.this.soln.get(curEmptySpot))) {
                    GameActivity.this.shown.set(curEmptySpot, guess);
                    tv.setText(guess);
                    String str = GameActivity.TAG;
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("shown: ");
                    stringBuilder2.append(GameActivity.this.shown);
                    stringBuilder2.append("   soln: ");
                    stringBuilder2.append(GameActivity.this.soln);
                    Log.d(str, stringBuilder2.toString());
                    if (GameActivity.this.shown.equals(GameActivity.this.soln)) {
                        Log.d(GameActivity.TAG, "WHOA!");
                        iv.setVisibility(View.INVISIBLE);
                        iv2.setVisibility(View.INVISIBLE);
                        iv3.setVisibility(View.INVISIBLE);
                        iv4.setVisibility(View.INVISIBLE);
                        mp = MediaPlayer.create(GameActivity.this.ctx,R.raw.winning);
                        mp.start();
                        Toast.makeText(GameActivity.this.ctx, "CORRECT!!", Toast.LENGTH_SHORT).show();
                        GameActivity.this.endSlideShow();
                        ((Button) GameActivity.this.findViewById(R.id.game_butt_hint)).setVisibility(View.VISIBLE);
                        GameActivity.this.hintIV.setVisibility(View.INVISIBLE);
                        GameActivity.this.hintIV.setImageResource(R.drawable.star);
                        GameActivity.this.hintIV.setBackground(null);
                        GameActivity.this.hintIV.setAnimation(AnimationUtils.loadAnimation(GameActivity.this.ctx, R.anim.spin));
                        GameActivity.this.hintIV.animate();
                        GameActivity.this.hintIV.setOnClickListener(new C03031());
                        return;
                    }
                    mRequestQueue = Volley.newRequestQueue(GameActivity.this.ctx);
                    parseJSON((String) GameActivity.this.soln.get(curEmptySpot + 1));
                } else {
                    mp = MediaPlayer.create(GameActivity.this.ctx,R.raw.loss);
                    mp.start();
                    Toast.makeText(GameActivity.this.ctx, "That's not the word I'm thinking of!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", new Game3());
        builder.show();
    }

    protected void onResume() {
        super.onResume();
        hide(null);
        mRequestQueue = Volley.newRequestQueue(GameActivity.this.ctx);
        parseJSON((String) this.soln.get(1));
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
        Toast.makeText(context, stringBuilder.toString(), Toast.LENGTH_SHORT).show();
    }

    public void hide(View v) {
        this.all.setSystemUiVisibility(2822);
        ActionBar actionBar = getSupportActionBar();
        //added if statement
        if(actionBar != null){
            getSupportActionBar().hide();
        }
    }

    private void endSlideShow() {
        if (this.isst != null) {
            this.isst.interrupt();
            this.isst = null;
        }
    }

    private void parseJSON(String search){
        urls = new ArrayList<>();
        String url = "https://pixabay.com/api/?key=11808353-f5278657ea770ee748fef170a&q=";
        url += search;
        Log.d(TAG,url);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("hits");
                            if(jsonArray.length()>0) {
                                Log.d(TAG, ""+jsonArray.length());
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    Log.d(TAG, ""+i);
                                    JSONObject hit = jsonArray.getJSONObject(i);

                                    imageUrl = hit.getString("webformatURL");
                                    Log.d(TAG,imageUrl);
                                    Log.d(TAG,""+imageUrl.length());

                                    urls.add(imageUrl);

                                }

                                
                                Picasso.with(getApplicationContext()).load(urls.get(0)).into(iv);
                                Picasso.with(getApplicationContext()).load(urls.get(1)).into(iv2);
                                Picasso.with(getApplicationContext()).load(urls.get(2)).into(iv3);
                                Picasso.with(getApplicationContext()).load(urls.get(3)).into(iv4);

                            }

                            else{
                                Picasso.with(getApplicationContext()).load("https://pixabay.com/get/eb35b70e29f7033ed1584d05fb1d4797eb77ead61cb20c4090f5c379a6efb5bcde_640.png").into(iv);
                                Picasso.with(getApplicationContext()).load("https://pixabay.com/get/eb35b70e29f7033ed1584d05fb1d4797eb77ead61cb20c4090f5c379a6efb5bcde_640.png").into(iv2);
                                Picasso.with(getApplicationContext()).load("https://pixabay.com/get/eb35b70e29f7033ed1584d05fb1d4797eb77ead61cb20c4090f5c379a6efb5bcde_640.png").into(iv3);
                                Picasso.with(getApplicationContext()).load("https://pixabay.com/get/eb35b70e29f7033ed1584d05fb1d4797eb77ead61cb20c4090f5c379a6efb5bcde_640.png").into(iv4);
                            }

                        } catch (JSONException e) {
                            Log.d(TAG, "FIRST CATCH");
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "ERROR");
                error.printStackTrace();
            }
        });
        Log.d(TAG, "AT END");
        mRequestQueue.add(request);

    }
}
