package ir.tdaapp.mms.View.Activitys;

import androidx.appcompat.app.AppCompatActivity;
import ir.tdaapp.mms.Model.Services.S_Splash;
import ir.tdaapp.mms.Presenter.P_Splash;
import ir.tdaapp.mms.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements S_Splash {

    P_Splash p_splash;
    ImageView Logo;
    TextView Title;
    ProgressBar ProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FindItem();
        Implements();

        p_splash.Start();
    }

    void FindItem() {
        Logo = findViewById(R.id.Logo);
        Title = findViewById(R.id.Title);
        ProgressBar = findViewById(R.id.ProgressBar);
    }

    void Implements() {
        p_splash = new P_Splash(getApplicationContext(), this);
    }

    //برای نمایش لوگو فراخوانی می شود
    @Override
    public void ShowLogo() {
        Animation aniFade = AnimationUtils.loadAnimation(this, R.anim.fadein);
        Logo.startAnimation(aniFade);
        Logo.setVisibility(View.VISIBLE);
    }

    //برای نمایش سرتیتر فراخوانی می شود
    @Override
    public void ShowTitle() {
        Animation aniSlide = AnimationUtils.loadAnimation(this, R.anim.slide_in_right);
        Title.setAnimation(aniSlide);
        Title.setVisibility(View.VISIBLE);
    }

    //برای نمایش پروگرس بار فراخوانی می شود
    @Override
    public void ShowProgressBar() {
        ProgressBar.setVisibility(View.VISIBLE);
    }

    //برای مخفی شدن کل آیتم ها فراخوانی می شود
    @Override
    public void HideAll() {
        Logo.setVisibility(View.INVISIBLE);
        Title.setVisibility(View.INVISIBLE);
        ProgressBar.setVisibility(View.INVISIBLE);
    }

    //در اینجا نتیجه عملیات که کاربر حساب کاربری دارد یا خیر چک می شود
    @Override
    public void HasAccount(boolean b) {
        if (b) {
            startActivity(new Intent(this,CentralActivity.class));
            finish();
        } else{
            startActivity(new Intent(this,LoginActivity.class));
            finish();
        }
    }
}
