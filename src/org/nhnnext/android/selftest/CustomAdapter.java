package org.nhnnext.android.selftest;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdapter extends ArrayAdapter<Article>{
	private Context context;
	/*
	 * context는 application실행을 위해 밑바탕에 깔려있는 환경으로
	 *  안드로이드시스템이 제공해주는 유용한 정보를 getSystemService로 불러낼 수 있음 
	 */
	private int layoutResourceId;
	private ArrayList<Article> articleData;
	
	public CustomAdapter(Context context, int layoutResourceId, ArrayList<Article> articleData){
			super(context, layoutResourceId, articleData);
			this.context = context;
			this.layoutResourceId = layoutResourceId;
			this.articleData = articleData;
	} 

	public View getView(int position, View convertView, ViewGroup parent){
		
		View row = convertView;
		
		if(row == null){
			LayoutInflater inflater = ((Activity)context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);
			
		}
		ImageView imageView = (ImageView) row.findViewById(R.id.custom_row_imageView1);
		TextView tvTitle = (TextView) row.findViewById(R.id.custom_row_textView);
		TextView tvContent = (TextView) row.findViewById(R.id.custom_row_textView2);
		
		tvTitle.setText(articleData.get(position).getTitle());
		tvContent.setText(articleData.get(position).getContent());		
		
		String img_path = context.getFilesDir().getPath() + "/" + articleData.get(position).getImgName();
		File img_load_path = new File(img_path);
		
		if(img_load_path.exists()){
			Bitmap bitmap = BitmapFactory.decodeFile(img_path);
			imageView.setImageBitmap(bitmap);
		}
		
		/*
		try{

		//	Log.i("info", articleData.get(position).getImgName());
			InputStream is = context.getAssets().open(articleData.get(position).getImgName());
			Drawable d = Drawable.createFromStream(is, null);
			
			imageView.setImageDrawable(d);
			
		}catch(IOException e){
			Log.e("ERROR", "ERROR: " +e);
		}
		*/
		return row;
	}
	
}
