package com.example.acer.attandance_free_feature;


import com.stephentuso.welcome.BasicPage;
import com.stephentuso.welcome.ParallaxPage;
import com.stephentuso.welcome.TitlePage;
import com.stephentuso.welcome.WelcomeConfiguration;
import com.stephentuso.welcome.WelcomeHelper;

public class WelcomeActivity extends com.stephentuso.welcome.WelcomeActivity {

    @Override
    protected WelcomeConfiguration configuration() {
        WelcomeHelper wr = new WelcomeHelper(this, WelcomeActivity.class);
        return new WelcomeConfiguration.Builder(this)
                .defaultTitleTypefacePath("Montserrat-Bold.ttf")
                .defaultHeaderTypefacePath("Montserrat-Bold.ttf")
                .defaultBackgroundColor(R.color.colorPrimary)
                    .page(new TitlePage(R.drawable.icon_date, "Welcome Title Page"))
                    .page(new BasicPage(R.drawable.icon_date,
                        "Welcome1",
                        "Ini page Welcome"))
                    .page(new BasicPage(R.drawable.icon_date,
                            "Welcome2",
                            "ini page welcome 2"))
                    .page(new BasicPage(R.drawable.icon_date,
                            "Welcome3",
                            "ini page welcome 3"))
                    .swipeToDismiss(true)
                    .canSkip(true)
                    .backButtonSkips(true)
                .build();
    }


}
