package com.example.david.weatherapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.github.amlcurran.showcaseview.OnShowcaseEventListener;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private int intTemp;
    private int intHum;
    private int intLevel;

    private TextView txtTemp, txtHum, txtLevel, txtCondition, txtswitch;
    private Button btnControl;
    private Switch swMode;

    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtswitch = (TextView)findViewById(R.id.txtswitch);
        txtCondition = (TextView)findViewById(R.id.txtWeather);
        txtTemp = (TextView)findViewById(R.id.txtTemperature);
        txtHum = (TextView)findViewById(R.id.txtHumidity);
        txtLevel = (TextView)findViewById(R.id.txtWaterLevel);
        btnControl = (Button)findViewById(R.id.btnSwitch);
        swMode = (Switch)findViewById(R.id.switchMode);

        dbRef = FirebaseDatabase.getInstance().getReference();
        btnControl.setEnabled(false);

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Long mode = (Long) dataSnapshot.child("Weather").child("Mode").child("1").getValue();
                if (mode == 0) {
                    swMode.setChecked(false);
                } else if (mode == 1) {
                    swMode.setChecked(true);
                }

                Double control = (Double) dataSnapshot.child("Weather").child("Status").child("1").getValue();
                if (control == 7.5) {
                    btnControl.setText("OFF");
                } else if (control == 12.5) {
                    btnControl.setText("ON");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        DatabaseReference temperature = FirebaseDatabase.getInstance().getReference().child("Weather");

        Query newTem = temperature.child("Temperature").orderByKey().limitToLast(1);
        newTem.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for( DataSnapshot child : dataSnapshot.getChildren()) {
                    intTemp = child.getValue(Integer.class);
//                    System.out.println(childKey);
                    txtTemp.setText(String.valueOf(intTemp));

                    if (intLevel > 100){
                        txtCondition.setText("Rainning");
                    } else {
                        if (intTemp < 30 && intHum > 60){
                            txtCondition.setText("Cloudy");
                        } else {
                            txtCondition.setText("Sunny");
                        }
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DatabaseReference humidity = FirebaseDatabase.getInstance().getReference().child("Weather");

        Query newHum = humidity.child("Humidity").orderByKey().limitToLast(1);
        newHum.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for( DataSnapshot child : dataSnapshot.getChildren()) {
                    intHum = child.getValue(Integer.class);
//                    System.out.println(childKey);
                    txtHum.setText(String.valueOf(intHum));

                    if (intLevel > 100){
                        txtCondition.setText("Rainning");
                    } else {
                        if (intTemp < 30 && intHum > 60){
                            txtCondition.setText("Cloudy");
                        } else {
                            txtCondition.setText("Sunny");
                        }
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DatabaseReference pressure = FirebaseDatabase.getInstance().getReference().child("Weather");

        Query newPressure = pressure.child("Pressure").orderByKey().limitToLast(1);
        newPressure.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for( DataSnapshot child : dataSnapshot.getChildren()) {
                    intLevel = child.getValue(Integer.class);
//                    System.out.println(childKey);
                    txtLevel.setText(String.valueOf(intLevel));

                    if (intLevel > 100){
                        txtCondition.setText("Rainning");
                    } else {
                        if (intTemp < 30 && intHum > 60){
                            txtCondition.setText("Cloudy");
                        } else {
                            txtCondition.setText("Sunny");
                        }
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

//        txtCondition.setText(String.valueOf(intLevel));

        /*if (txtTemp.getText() != "---") {
            intTemp = Integer.parseInt(txtTemp.getText().toString());
        }

        if (txtHum.getText() != "---") {
            intHum = Integer.parseInt(txtHum.getText().toString());
        }*/

        swMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked == true) {
                    dbRef.child("Weather").child("Mode").child("1").setValue(1);
                    btnControl.setEnabled(true);
                    txtswitch.setTextColor(getResources().getColor(R.color.red));
                }else if (isChecked == false){
                    dbRef.child("Weather").child("Mode").child("1").setValue(0);
                    btnControl.setEnabled(false);
                    txtswitch.setTextColor(getResources().getColor(R.color.gray));
                }
            }
        });

        btnControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btnControl.getText().equals("ON")){
                    dbRef.child("Weather").child("Status").child("1").setValue(7.5);
                    btnControl.setText("OFF");
                }else if(btnControl.getText().equals("OFF")){
                    dbRef.child("Weather").child("Status").child("1").setValue(12.5);
                    btnControl.setText("ON");
                }
            }
        });
    }

    public void onClickImageInfo(View view){
        if (txtCondition.getText().equals("Rainning")) {
            ShowcaseView showcaseView = new ShowcaseView.Builder(this)
                    .withHoloShowcase()
                    .setStyle(R.style.CustomShowcaseTheme2)
                    .setTarget(new ViewTarget(R.id.txtrain, this))
                    .setContentTitle("Weather Info")
                    .setContentText(R.string.rain)
                    .setShowcaseEventListener(new OnShowcaseEventListener() {
                                                  @Override
                                                  public void onShowcaseViewHide(ShowcaseView showcaseView) {
                                                      btnControl.setVisibility(View.VISIBLE);
                                                  }

                                                  @Override
                                                  public void onShowcaseViewDidHide(ShowcaseView showcaseView) {

                                                  }

                                                  @Override
                                                  public void onShowcaseViewShow(ShowcaseView showcaseView) {
                                                      btnControl.setVisibility(View.INVISIBLE);
                                                  }
                                              }
                    )
                    .build();
            showcaseView.setDetailTextAlignment(Layout.Alignment.ALIGN_NORMAL);
            showcaseView.setTitleTextAlignment(Layout.Alignment.ALIGN_NORMAL);
            showcaseView.forceTextPosition(ShowcaseView.BELOW_SHOWCASE);
        } else if (txtCondition.getText().equals("Cloudy")){
            ShowcaseView showcaseView = new ShowcaseView.Builder(this)
                    .withHoloShowcase()
                    .setStyle(R.style.CustomShowcaseTheme2)
                    .setTarget(new ViewTarget(R.id.txtcloud, this))
                    .setContentTitle("Weather Info")
                    .setContentText(R.string.cloud)
                    .setShowcaseEventListener(new OnShowcaseEventListener() {
                                                  @Override
                                                  public void onShowcaseViewHide(ShowcaseView showcaseView) {
                                                      btnControl.setVisibility(View.VISIBLE);
                                                  }

                                                  @Override
                                                  public void onShowcaseViewDidHide(ShowcaseView showcaseView) {

                                                  }

                                                  @Override
                                                  public void onShowcaseViewShow(ShowcaseView showcaseView) {
                                                      btnControl.setVisibility(View.INVISIBLE);
                                                  }
                                              }
                    )
                    .build();
            showcaseView.setDetailTextAlignment(Layout.Alignment.ALIGN_NORMAL);
            showcaseView.setTitleTextAlignment(Layout.Alignment.ALIGN_NORMAL);
            showcaseView.forceTextPosition(ShowcaseView.BELOW_SHOWCASE);
        } else if (txtCondition.getText().equals("Sunny")) {
            ShowcaseView showcaseView = new ShowcaseView.Builder(this)
                    .withHoloShowcase()
                    .setStyle(R.style.CustomShowcaseTheme2)
                    .setTarget(new ViewTarget(R.id.txtsunny, this))
                    .setContentTitle("Weather Info")
                    .setContentText(R.string.sunny)
                    .setShowcaseEventListener(new OnShowcaseEventListener() {
                                                  @Override
                                                  public void onShowcaseViewHide(ShowcaseView showcaseView) {
                                                      btnControl.setVisibility(View.VISIBLE);
                                                  }

                                                  @Override
                                                  public void onShowcaseViewDidHide(ShowcaseView showcaseView) {

                                                  }

                                                  @Override
                                                  public void onShowcaseViewShow(ShowcaseView showcaseView) {
                                                      btnControl.setVisibility(View.INVISIBLE);
                                                  }
                                              }
                    )
                    .build();
            showcaseView.setDetailTextAlignment(Layout.Alignment.ALIGN_NORMAL);
            showcaseView.setTitleTextAlignment(Layout.Alignment.ALIGN_NORMAL);
            showcaseView.forceTextPosition(ShowcaseView.BELOW_SHOWCASE);
        }
    }
}
