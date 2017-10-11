package com.cse.jntu.placements;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import java.util.Calendar;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class About extends AppCompatActivity {
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_about);

        Element versionElement = new Element();
        versionElement.setTitle("Version 1.0");
        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setImage(R.drawable.jntuh)
                .setDescription("Placement Portal app is a solution for students to prepare for placements.App contains previous interview questions,Tips. Students can clarity their doubts in Q/A section")
                .addItem(versionElement)

                .addGroup("Connect with us")
                .addEmail("teamwebbiesjntu@gmail.com")
                .addWebsite("http:///")

                .addPlayStore("com.cse.jntu.placements")
               .addItem(getCopyRightsElement())
                .create();
        setContentView(aboutPage);



    }
    Element getCopyRightsElement() {
        Element copyRightsElement = new Element();
        final String copyrights = String.format(getString(R.string.copy_right), Calendar.getInstance().get(Calendar.YEAR));
        copyRightsElement.setTitle(copyrights);
        copyRightsElement.setIconDrawable(R.drawable.copyright);
        copyRightsElement.setIconTint(mehdi.sakout.aboutpage.R.color.about_item_icon_color);
        copyRightsElement.setIconNightTint(android.R.color.white);
        copyRightsElement.setGravity(Gravity.CENTER);
        copyRightsElement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(About.this, copyrights, Toast.LENGTH_SHORT).show();
            }
        });
        return copyRightsElement;
    }
}
