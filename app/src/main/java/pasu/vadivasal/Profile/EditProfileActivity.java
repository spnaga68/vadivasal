package pasu.vadivasal.Profile;

/**
 * Created by developer on 14/10/17.
 */

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.jjobes.slidedatetimepicker.SlideDateTimeListener;
import com.github.jjobes.slidedatetimepicker.SlideDateTimePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import pasu.vadivasal.R;
import pasu.vadivasal.android.Utils;
import pasu.vadivasal.globalModle.ProfileData;

/**
 * Created by developer on 26/9/17.
 */

public class EditProfileActivity extends AppCompatActivity {

    TextView sav_txt, dob, chose_img_txt;
    EditText name, phnum, city, email, about;
    String name_str, phnum_str, dob_str, city_str, email_str;
    String name_str_old, phnum_str_old, dob_str_old, city_str_old, email_str_old;
    LinearLayout imglay;
    private static int IMG_RESULT = 1;
    String ImageDecode;
    ImageView imageViewLoad;

    Intent intent;
    private static int RESULT_LOAD_IMG = 1;
    private DatabaseReference myRef;
    private ProfileData profileData;
    private String about_str,about_str_old;
    private SimpleDateFormat dateFormatter;
    private SlideDateTimeListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.raw_edit_profile);
        listener=new DateTimeListner();
        this.dateFormatter = new SimpleDateFormat("dd MMM yyyy");
        sav_txt = (TextView) findViewById(R.id.save_txt);
        chose_img_txt = (TextView) findViewById(R.id.imge_chose_txt);
        name = (EditText) findViewById(R.id.name_txt);
        phnum = (EditText) findViewById(R.id.phnumb_txt);
        dob = (TextView) findViewById(R.id.dob_txt);
        city = (EditText) findViewById(R.id.city_txt);
        email = (EditText) findViewById(R.id.email_txt);
        imglay = (LinearLayout) findViewById(R.id.img_lay);
        imageViewLoad = (ImageView) findViewById(R.id.profile_image);
        about = (EditText) findViewById(R.id.desc_text);
        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setDateTimeField();
//                Calendar now = Calendar.getInstance();
//                DatePickerDialog dpd = DatePickerDialog.newInstance(
//                        ScheduleNewGame.this,
//                        now.get(Calendar.YEAR),
//                        now.get(Calendar.MONTH),
//                        now.get(Calendar.DAY_OF_MONTH)
//                );
//                dpd.setAccentColor(ContextCompat.getColor(getActivity(), R.color.black));
//                dpd.setCancelColor(ContextCompat.getColor(getActivity(), R.color.black));
//                dpd.setOkColor(ContextCompat.getColor(getActivity(), R.color.black));
//                dpd.setMinDate(now);
//                // dpd.setc(ContextCompat.getColor(getActivity(),R.color.black));
//                dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
            }
        });
        if (getIntent() != null)
            if (getIntent().getStringExtra("data") != null) {
                profileData = Utils.fromJson(getIntent().getStringExtra("data"), ProfileData.class);
                name_str_old = profileData.getName();
                phnum_str_old = String.valueOf(profileData.getPhonenumber());
                dob_str_old = String.valueOf(profileData.getDob());
                city_str_old = profileData.getCity();
                email_str_old = profileData.getMail();
                about_str_old = profileData.getDescription();
                setProfileData();
            }
        sav_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                name_str = name.getText().toString();
                phnum_str = phnum.getText().toString();
                dob_str = dob.getText().toString();
                city_str = city.getText().toString();
                email_str = email.getText().toString();
                about_str = about.getText().toString();

                if (name_str.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Enter your  name", Toast.LENGTH_LONG).show();
                } else if (phnum_str.length() == 0) {

                    Toast.makeText(getApplicationContext(), "Enter your phnum", Toast.LENGTH_LONG).show();

//                } else if (dob_str.length() == 0) {
//                    Toast.makeText(getApplicationContext(), "Enter your dob", Toast.LENGTH_LONG).show();

                } else if (city_str.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Enter your city", Toast.LENGTH_LONG).show();

                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email_str).matches()) {
                    Toast.makeText(getApplicationContext(), "Invalid Email", Toast.LENGTH_LONG).show();
                } else {


                    saveProfile();

                }
            }
        });

        imglay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
                System.out.println("llk" + RESULT_LOAD_IMG);

            }

        });


    }

    private void setProfileData() {
        name.setText(name_str_old);
        dob.setText(dob_str_old);
        city.setText(city_str_old);
        phnum.setText(phnum_str_old);
        email.setText(email_str_old);
        about.setText(about_str_old);
    }
    private void setDateTimeField() {
        Calendar newCalendar = Calendar.getInstance();
        new SlideDateTimePicker.Builder(getSupportFragmentManager()).setListener(this.listener).setInitialDate(newCalendar.getTime()).setMaxDate(new Date()).setTheme(R.style.AppTheme).setIndicatorColor(Color.parseColor("#BB4235")).build().show();
    }

    class DateTimeListner extends SlideDateTimeListener {
        DateTimeListner() {
        }

        public void onDateTimeSet(Date date) {

            dob.setText(EditProfileActivity.this.dateFormatter.format(date));
        }

        public void onDateTimeCancel() {
            System.out.println("onDateTimeCancel");
        }
    }

    private void saveProfile() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String profile_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        System.out.println("getDataaaa" + profile_id + "__" + FirebaseAuth.getInstance().getCurrentUser().getUid());
       if(!name_str.equals(name_str_old))
        database.getReference("profile/" + profile_id + "/name").setValue(name_str);
        if(!dob_str.equals(dob_str_old))
        database.getReference("profile/" + profile_id + "/dob").setValue(dob_str);
        if(!phnum_str.equals(phnum_str_old))
        database.getReference("profile/" + profile_id + "/phonenumber").setValue(Long.parseLong(phnum_str));
        if(!email_str.equals(email_str_old))
        database.getReference("profile/" + profile_id + "/mail").setValue(email_str);
        if(!city_str.equals(city_str_old))
        database.getReference("profile/" + profile_id + "/city").setValue(city_str);
        myRef = database.getReference("profile/" + profile_id + "/description");
//        ProfileData profileData = new ProfileData();
//        profileData.setName(name.getText().toString());
//        profileData.setCity(city.getText().toString());
//        profileData.setMail(email.getText().toString());
//        profileData.setDob(12328346L);
//        profileData.setDescription(email.getText().toString());
//        profileData.setPhonenumber(Long.parseLong(phnum.getText().toString()));
        myRef.setValue(about_str);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Toast.makeText(getApplicationContext(), "suceess logging", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);


        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                imageViewLoad.setImageBitmap(selectedImage);
                Toast.makeText(EditProfileActivity.this, "picked n seted", Toast.LENGTH_LONG).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(EditProfileActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(EditProfileActivity.this, "You haven't picked Image", Toast.LENGTH_LONG).show();
        }
    }
}








