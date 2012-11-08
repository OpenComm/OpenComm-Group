package com.example.sendemail;

import android.os.Bundle;
import android.app.Activity;
//import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
//import android.widget.Toast;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
   
        
        Button send = (Button) findViewById(R.id.button1); 
        send.setOnClickListener(new View.OnClickListener() { 
          public void onClick(View view) { 
        	  
        	  GMailerClass m= new GMailerClass();
        	  //GMailerClass m = new GMailerClass("ocuserservice@gmail.com", "ocuserservice"); 
        	   
        	   //parameters for make account (email, open comm username, open comm password)
        	   m.makeAccount("cuair.business@gmail.com", "cuair.business@gmail.com", "cuairbusiness", "KevinLei"); 
        	   
        	   
        	   
        	   
//               String[] toArr = {m.getCreatedAccountUser()}; 
//               m.setTo(toArr); 
//               m.setFrom(m.getDeveloperEmail()); 
//               m.setSubject("This is a confirmation email"); 
//               m.setBody("http://cuopencomm.no-ip.org:9090/plugins/userService/userservice?type=add&secret=" +
//               		m.getSecret() +"&username=" + m.getCreatedAccountUser() + "&password=" + m.getCreatedAccountPassword()+ "&name=franz&email=" + m.getUserEmail()); 
//               
//               

               	
               
//            try { 
//              //ATTCHEMENT FROM FILE
//
//              if(m.send()) { 
//                //TOAST
//              } else { 
//                //TOAST 
//              } 
//            } catch(Exception e) { 
//              //TOAST
//        
//            } 
          } 
        }); 
        
    }
        
    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}
