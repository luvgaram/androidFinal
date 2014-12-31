package org.nhnnext.android.selftest;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Dao {
	private Context context;
	private SQLiteDatabase database;
	
	public Dao(Context context) {
		this.context = context;
		
		database = context.openOrCreateDatabase("LocalDATA.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);
	
		
		try{
			String sql = "CREATE TABLE IF NOT EXISTS Articles(ID integer primary key autoincrement,"
					+ " ArticleNumber integer UNIQUE not null,"
					+ " Title test not null, "
					+ "WriterName text not null,"
					+ " WriterID text not null,"
					+ " Context text not null, "
					+ "WriteDate text not null,"
					+ "ImgName text UNIQUE not null);";
		
			database.execSQL(sql);
		}catch(Exception e){
			Log.e("test", "CREATE TABLE FAILED! - " + e);
			e.printStackTrace();
		}
	}

	
    public String getJsonTestData() {
           
            StringBuilder sb = new StringBuilder();
            /* 복잡한 문자열 처리에 유용. 비슷한 클래스중 StringBuffer는 동기화를 지원함 
             * 예전에 string을 + 를 사용해 합치는 것이 매번 String인스턴스를 생성하는 방식이라 성능한의 이슈가 있었으나 
             * JDK1.5 이후부터는 컴파일 단계에서 StringBuilder로 컴파일되도록 변경되어 +사용과 비교시 성능상의 큰 이슈는 없음 */
          
            sb.append("");
            sb.append("[");
            sb.append("      {");
            sb.append("         'ArticleNumber':'1',");
            sb.append("         'Title':'오늘도 좋은 하루',");
            sb.append("         'Writer':'학생1',");
            sb.append("         'Id':'6613d02f3e2153283f23bf621145f877',");
            sb.append("         'Content':'하지만 곧 기말고사지...',");
            sb.append("         'WriteDate':'2013-09-23-10-10',");
            sb.append("         'ImgName':'photo1.jpg'");
            sb.append("      },");
            sb.append("      {");
            sb.append("         'ArticleNumber':'2',");
            sb.append("         'Title':'대출 최고 3000만원',");
            sb.append("         'Writer':'김미영 팀장',");
            sb.append("         'Id':'6326d02f3e2153266f23bf621145f734',");
            sb.append("         'Content':'김미영팀장입니다. 고갱님께서는 최저이율로 최고 3000만원까지 30분 이내 통장입금가능합니다.',");
            sb.append("         'WriteDate':'2013-09-24-11-22',");
            sb.append("         'ImgName':'photo2.jpg'");
            sb.append("      },");
            sb.append("      {");
            sb.append("         'ArticleNumber':'3',");
            sb.append("         'Title':'MAC등록신청',");
            sb.append("         'Writer':'학생2',");
            sb.append("         'Id':'8426d02f3e2153283246bf6211454262',");
            sb.append("         'Content':'1a:2b:3c:4d:5e:6f',");
            sb.append("         'WriteDate':'2013-09-25-12-33',");
            sb.append("         'ImgName':'photo3.jpg'");
            sb.append("      }");
            sb.append("]");
             
             return sb.toString();
    }
	
	public void insertJsonData(String jsonData) {
		int articleNumber;
		String title;
		String writer;
		String id;
		String content;
		String writeDate;
		String imgName;
		
		FileDownloader fileDownloader = new FileDownloader(context);
		
		try{
			JSONArray jArr = new JSONArray(jsonData);
			
			for(int i=0; i <jArr.length(); ++i){
				JSONObject jObj = jArr.getJSONObject(i);
				
				articleNumber = jObj.getInt("ArticleNumber");
				title =jObj.getString("Title");
				writer = jObj.getString("Writer");
				id = jObj.getString("Id");
				content = jObj.getString("Content");
				writeDate = jObj.getString("WriteDate");
				imgName = jObj.getString("ImgName");
				
				Log.i("test", "ArticleNumber: " + articleNumber + ", Title: " + title);
					
				String sql = "INSERT INTO Articles(ArticleNumber, Title, WriterName, WriterID, Context, WriteDate, ImgName)"
						+ "VALUES(" + articleNumber + ",'" + title + "','" + writer + "','" + id + "','"
						+ content + "','" + writeDate + "','" + imgName + "');";
				
				try{
					database.execSQL(sql);
				}catch(Exception e){
					Log.e("test", "DB error! - "+e);
					e.printStackTrace();
				}
				
				fileDownloader.downFile("http://54.64.250.239:5009/image/"+ imgName, imgName);
				
			}
		}catch(JSONException e){
			Log.e("test", "JSON ERROR! - " + e);
			e.printStackTrace();
		}
	}

	public ArrayList<Article> getArticleList(){
		
		ArrayList<Article>articleList = new ArrayList<Article>();
		int articleNumber;
		String title;
		String writer;
		String id;
		String content;
		String writeDate;
		String imgName;
		
		String sql = "SELECT*FROM Articles;";
		Cursor cursor = database.rawQuery(sql, null);
		
		
		while(cursor.moveToNext()){
			articleNumber = cursor.getInt(1);
			title = cursor.getString(2);
			writer = cursor.getString(3);
			id = cursor.getString(4);
			content = cursor.getString(5);
			writeDate = cursor.getString(6);
			imgName = cursor.getString(7);
			
			articleList.add(new Article(articleNumber, title, writer, id, content, writeDate, imgName));
		}
		cursor.close();
		
		return articleList;
	}
	
public Article getArticleByArticleNumber(int articleNumber){
		
		Article article = null;
		String title;
		String writer;
		String id;
		String content;
		String writeDate;
		String imgName;
		
		String sql = "SELECT*FROM Articles where ArticleNumber = " + articleNumber + ";";
		Cursor cursor = database.rawQuery(sql, null);
	
		cursor.moveToNext();//하나의 레코드만 불렀느데도 커서가 필요한지 확인! 
		
		articleNumber = cursor.getInt(1);
		title = cursor.getString(2);
		writer = cursor.getString(3);
		id = cursor.getString(4);
		content = cursor.getString(5);
		writeDate = cursor.getString(6);
		imgName = cursor.getString(7);
		
		article = new Article(articleNumber, title, writer, id, content, writeDate, imgName);
		
		cursor.close();
		
		return article;
	}
}
