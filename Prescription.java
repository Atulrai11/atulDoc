package com.codemaven.xadapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.codemaven.xadapp.R;
import com.squareup.picasso.Picasso;

import uk.co.senab.photoview.PhotoViewAttacher;

public class Prescription extends AppCompatActivity {
ImageView description_imv;
    PhotoViewAttacher pAttacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        toolbar.setTitle(getString(R.string.title_description));
        toolbar.setNavigationIcon(R.drawable.arrow);



        description_imv=findViewById(R.id.description_imv);




        String photo=getIntent().getStringExtra("imvUrl");
       // Log.e("Imageurl",""+photo);
        Picasso.with(this)
                .load(photo)
                .error(R.drawable.noimage)
                .resize(300, 300)
                .centerInside()
                .into(description_imv);

        pAttacher = new PhotoViewAttacher(description_imv);
        pAttacher.update();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Prescription.super.onBackPressed();
            }
        });
    }
}
