package org.nhnnext.android.selftest;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.provider.Settings;
import android.provider.Settings.Secure;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;


public class Write_article extends Activity implements OnClickListener{
	
	private EditText etWriter;
	private EditText etTitle;
	private EditText etContent;
	private ImageButton ibPhoto;
	private Button beUpload;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_write_article);
		
		etWriter = (EditText)findViewById(R.id.editText1);
		etTitle = (EditText)findViewById(R.id.editText2);
		etContent = (EditText)findViewById(R.id.editText3);
		ibPhoto = (ImageButton)findViewById(R.id.custom_row_imageView1);
		beUpload = (Button)findViewById(R.id.write_button_write);
		ibPhoto.setOnClickListener(this);
		beUpload.setOnClickListener(this);
	}

	private String filePath;
	private String fileName;
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);

		try{
			if(requestCode == REQUEST_PHOTO_ALBUM){
				
			Uri uri = getRealPathUri(data.getData() );
			filePath = uri.toString();
			fileName = uri.getLastPathSegment();
			
			Bitmap bitmap = BitmapFactory.decodeFile(filePath);
			ibPhoto.setImageBitmap(bitmap);
			}
		}catch(Exception e){
			Log.e("test", "onActivityResult ERROR: " + e);
		}
		
	}
	
  	private Uri getRealPathUri(Uri uri) {
		Uri filePathUri = uri;
		if (uri.getScheme().toString().compareTo("content") == 0) {
			Cursor cursor = getApplicationContext().getContentResolver().query(uri, null, null,null, null);
			if (cursor.moveToFirst()) {
				int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				filePathUri = Uri.parse(cursor.getString(column_index));
			}
		}
		return filePathUri;
	}
	private static final int REQUEST_PHOTO_ALBUM = 1;
	private ProgressDialog progressDialog;
	
	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()){
		case R.id.custom_row_imageView1:
			Intent intent = new Intent(Intent.ACTION_PICK);
			intent.setType(Images.Media.CONTENT_TYPE);
			intent.setData(Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(intent, REQUEST_PHOTO_ALBUM);
			
			break;
		case R.id.write_button_write:
			
			final Handler handler = new Handler();
			new Thread(){
				public void run(){
					
					handler.post(new Runnable() {
						public void run(){
							
							progressDialog = ProgressDialog.show(Write_article.this, "", "업로드 중임!");
						}
					});
					String ID = Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
					String DATE = new SimpleDateFormat("yyyy-MM-dd HH", Locale.KOREA).format(new Date());
					Article article = new Article(
							0,
							etTitle.getText().toString(),
							etWriter.getText().toString(),
							ID,
							etContent.getText().toString(),
							DATE,
							fileName);
					
					ProxyUP proxyUP = new ProxyUP();
					proxyUP.uploadArticle(article, filePath);
					
					handler.post(new Runnable() {
						public void run() { 
							progressDialog.cancel();
							
							finish();
						}
					});
				}
			}.start();
			
			break;
		}
	}
	
}