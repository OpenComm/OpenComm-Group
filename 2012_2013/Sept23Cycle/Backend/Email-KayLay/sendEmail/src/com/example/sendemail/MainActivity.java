package com.example.sendemail;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
   
        
        Button send = (Button) findViewById(R.id.button1); 
        send.setOnClickListener(new View.OnClickListener() { 
          public void onClick(View view) { 
            GMailerClass m = new GMailerClass("lei.kevin.n@gmail.com", "elcajon12"); 

            String[] toArr = {"knl35@cornell.com", "cuair.business@gmail.com"}; 
            m.setTo(toArr); 
            m.setFrom("lei.kevin.n@gmail.com"); 
            m.setSubject("This is an email sent using my Mail JavaMail wrapper from an Android device."); 
            m.setBody("Email body."); 

            try { 
              //m.addAttachment("/sdcard/filelocation"); 

              if(m.send()) { 
                //Toast.makeText(MailApp.this, "Email was sent successfully.", Toast.LENGTH_LONG).show(); 
              } else { 
                //Toast.makeText(MailApp.this, "Email was not sent.", Toast.LENGTH_LONG).show(); 
              } 
            } catch(Exception e) { 
              //Toast.makeText(MailApp.this, "There was a problem sending the email.", Toast.LENGTH_LONG).show(); 
              Log.e("MailApp", "Could not send email", e); 
            } 
          } 
        }); 
        
        /**   
        final Button send = (Button) this.findViewById(R.id.button1);
        send.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub

                try {   
                    Mail sender = new Mail("lei.kevin.n@gmail.com", "elcajon12");
                    sender.sendMail("This is Subject",   
                            "This is Body",   
                            "cuair.business@gmail.com",   
                            "knl35@yahoo.com");
                    Log.v("MainActivity", "send successfully!");
                    
                } catch (Exception e) {   
                    Log.v("MainActivity", e.getMessage(), e);   
                } 

            }
        });
*/
        
        
    }
        
    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}
