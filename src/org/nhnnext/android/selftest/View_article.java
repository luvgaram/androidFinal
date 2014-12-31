package org.nhnnext.android.selftest;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;


public class View_article extends Activity implements OnClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_article);
		
		
		TextView tvTitle = (TextView)findViewById(R.id.view_article_textView_title);
		TextView tvWriter = (TextView)findViewById(R.id.view_article_textView_writer);
		TextView tvContent = (TextView)findViewById(R.id.view_article_textView_content);
		TextView tvWriteDate = (TextView)findViewById(R.id.view_article_textView_write_time);
	
		ImageView ivImage = (ImageView)findViewById(R.id.view_article_imageView_photo);
		
		String articleNumber = getIntent().getExtras().getString("ArticleNumber");
	
		Dao dao = new Dao( getApplicationContext());
		Article article = dao.getArticleByArticleNumber(Integer.parseInt(articleNumber));
	
		tvTitle.setText(article.getTitle());
		tvWriter.setText(article.getWriter());
		tvContent.setText(article.getContent());
		tvWriteDate.setText(article.getWriteDate());
		
		String img_path = getApplicationContext().getFilesDir().getPath()+"/"+article.getImgName();
		File img_load_path = new File(img_path);
		
		if(img_load_path.exists()){
			Bitmap bitmap = BitmapFactory.decodeFile(img_path);
			ivImage.setImageBitmap(bitmap);
		}
		/*
		try{
			
			InputStream is = getApplicationContext().getAssets().open(article.getImgName());
			Drawable d = Drawable.createFromStream(is, null);
			
			ivImage.setImageDrawable(d);
			
		}catch(IOException e){
			Log.e("ERROR", "ERROR: " +e);
		}
		*/
	
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
}