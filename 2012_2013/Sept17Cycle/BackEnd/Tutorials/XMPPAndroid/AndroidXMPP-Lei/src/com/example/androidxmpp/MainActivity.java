package com.example.androidxmpp;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.util.StringUtils;


public class MainActivity extends Activity {
	
	private AndroidMuc androidMuc;
	

    @Override
    public void onCreate(Bundle savedInstanceState) {
       
   
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
   
        
        
        
        
      //  androidMuc.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    public void connect(View v){
    	androidMuc= new AndroidMuc(this);
    	androidMuc.connect(v);
    }


		
	}
    
    
    

