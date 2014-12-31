package org.nhnnext.android.selftest;


import java.util.ArrayList;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.devspark.sidenavigation.ISideNavigationCallback;
import com.devspark.sidenavigation.SideNavigationView;
import com.devspark.sidenavigation.SideNavigationView.Mode;

public class MainActivity extends ActionBarActivity implements OnClickListener, OnItemClickListener{

	private ArrayList<Article> articleList;
	private ListView listView;
	private SideNavigationView sideNavigationView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		sideNavigationView = (SideNavigationView) findViewById(R.id.side_navigation_view);
		sideNavigationView.setMenuItems(R.menu.side_menu);
		sideNavigationView.setMenuClickCallback(sideNavigationCallback);
		sideNavigationView.setMode(Mode.LEFT);
		
		
		
		Button mButtonWrite =  (Button)findViewById(R.id.main_button_write);
		Button mButtonRefresh = (Button)findViewById(R.id.main_button_refresh);
		
		mButtonWrite.setOnClickListener(this);
		mButtonRefresh.setOnClickListener(this);

		listView = (ListView)findViewById(R.id.custom_list_listView);
	}
		@Override
		public boolean onCreateOptionsMenu(Menu menu){
			
			getMenuInflater().inflate(R.menu.main, menu);
			return true;
	}

	

	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		
		String text ="";
		
		switch(item.getItemId()){
		case android.R.id.home:
			text = "Side Navigation toggle";
			sideNavigationView.toggleMenu();
			break;
		case R.id.action_item_add:
			text = "Action item, with text, displayed if room exists";
			break;
		case R.id.action_item_search:
			text = "Action item, icon only, always displayed";
			break;
		case R.id.action_item_normal:
			text = "Normal menu item";
			break;
		default:
			return false;
			
		}
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
		return true;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		refreshData();
		//listView();
		
	}
		private void listView(){
			
			
			//ListView listView = (ListView)findViewById(R.id.custom_list_listView);
			Dao dao = new Dao(getApplicationContext());
			articleList = dao.getArticleList();
			CustomAdapter customAdapter = new CustomAdapter(this, R.layout.custom_list_row, articleList);
			/* 
			 * context를 넘기는 자리에 activity의 this를 넘길 수 있는 이유는 
			 * activity가 context를 상속받았기 때문.
			 */
			listView.setAdapter(customAdapter);
			listView.setOnItemClickListener(this);

		}
	private final Handler handler = new Handler();
	private void refreshData(){
		new Thread(){
			public void run(){
				Proxy proxy = new Proxy();
				String jsonData = proxy.getJson();
				
				/* 
				 * Dao생성시 해당 context에 DB 생성
				 * DB 연동 Json데이터를 getJsonData()로 불러 String 변수에 지정
				 * 지정한 변수를 insertJsonData()로 레코드 생성 
				 */
				Dao dao = new Dao(getApplicationContext());
				//String testJsonData = dao.getJsonTestData();
				//dao.insertJsonData(testJsonData);
				dao.insertJsonData(jsonData);
				
				handler.post(new Runnable(){
					public void run(){
						listView();
					}
				});
			}
		}.start();
	}
	
	@Override
	public void onClick(View arg0) {
		
		switch(arg0.getId()){
		case R.id.main_button_write:
			Intent intentWrite = new Intent(this, ArticleWriter.class);
			startActivity(intentWrite);
			//startActivity(intentWrite);
		case R.id.main_button_refresh:
			refreshData();
			break;
		}
		}
	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
	
		/*
		 * intent는 component(Activity)간의 메시지   
		 * putExtra() intent에 데이터를 저장할 때 사
		 * getExtra() intent에서 데이터를 가져올 때 사용 
		 */
		Intent intent = new Intent(this, ViewActivity.class);
		
		intent.putExtra("ArticleNumber", articleList.get(position).getArticleNumber() + "");
		startActivity(intent);
		
	}

	ISideNavigationCallback sideNavigationCallback = new ISideNavigationCallback() {
		
		@Override
		public void onSideNavigationItemClick(int itemId) {
			String text = "";
			switch (itemId) {
			case R.id.side_navigation_menu_add:
				text = "add";
				break;
			case R.id.side_navigation_menu_call:
				text = "call";
				break;
			case R.id.side_navigation_menu_delete:
				text = "delete";
				break;
			default:
				text = "";
			}
			Toast.makeText(getApplicationContext(), "side menu:" + text,
					Toast.LENGTH_SHORT).show();
		}
	};

}
