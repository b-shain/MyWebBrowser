package edu.temple.mywebbrowser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;

//Brandon Shain

@SuppressLint("HandlerLeak")
public class MainActivity extends Activity {

	WebView display;
	Button button_go;
	EditText editText_url;
	String urlString;
	String htmlString;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		button_go = (Button) findViewById(R.id.button_go);
		display = (WebView) findViewById(R.id.display_webView);
		editText_url = (EditText) findViewById(R.id.url_editText);
		urlString = getResources().getString(R.string.default_url);
		
		View.OnClickListener listener = new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				urlString = editText_url.getText().toString();
				final Handler webHandle = new Handler(){
					@Override
					public void handleMessage(Message msg) {		 
						//display.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
						
						//to enable javascript
						//display.getSettings().setJavaScriptEnabled(true);
						
						//to display the HTML code in the webView
						display.loadData(htmlString, "text/html", "UTF-8");					
				    }				
				};
				
				//Create a new thread that takes a runnable object, so you can 
				//start the thread whenever you choose
				Thread webThread = new Thread(new Runnable(){
					
					public void run(){
						URL url;
						try 
						{
							url = new URL(urlString);
							InputStreamReader streamReader = new InputStreamReader(url.openStream()); 
							BufferedReader reader = new BufferedReader(streamReader);
							for(String line = reader.readLine(); line != null; line = reader.readLine())
							{
								//concatenates the retrieved html code from the buffered reader
								htmlString = htmlString + line;
								
								//for testing
								//System.out.println("*"+htmlString);
							}
							//create a new message object and retrieves the message from the handler
							Message msg = webHandle.obtainMessage();
							msg.obj = htmlString;
							webHandle.sendMessage(msg);
							
						} 
						catch (MalformedURLException e) 
						{
							e.printStackTrace();
						}
						catch (IOException e) 
						{
							e.printStackTrace();
						}					
					}		
				});//end webThread
			
				//start the thread
				webThread.start();

			}//end OnClick
		};//end listener
		
		
		button_go.setOnClickListener(listener);
		
	}
}
