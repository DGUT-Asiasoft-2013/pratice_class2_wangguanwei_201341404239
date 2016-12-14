package com.example.helloworld;

import java.io.IOException;
import java.util.List;

import com.example.helloworld.api.Server;
import com.example.helloworld.entity.Article;
import com.example.helloworld.entity.Comment;
import com.example.helloworld.entity.Page;
import com.example.helloworld.fragments.pages.FeedListFragment;
import com.example.helloworld.fragments.widgets.AvatarView;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.R.color;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.Response;

public class FeedContentActivity extends Activity {
	Article article;
	ListView listView;
	View btnLoadMore;
	TextView textLoadMore;
	Button btn_like;

	List<Comment> data;
	int page =0;

	@SuppressWarnings("null")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_feedcontent);

		btnLoadMore=LayoutInflater.from(this).inflate(R.layout.load_more_button, null);
		textLoadMore= (TextView)btnLoadMore.findViewById(R.id.text);

		listView = (ListView)findViewById(R.id.list);
		listView.addFooterView(btnLoadMore);
		listView.setAdapter(listAdapter);

		String title=getIntent().getStringExtra("Title");
		String text=getIntent().getStringExtra("Text");
		String authorName=getIntent().getStringExtra("AuthorName");
		String date=getIntent().getStringExtra("Date");

		article = (Article)getIntent().getSerializableExtra("Data");

		TextView textview1=(TextView)findViewById(R.id.textView1);
		TextView textview2=(TextView)findViewById(R.id.textView2);
		TextView textview3=(TextView)findViewById(R.id.textView3);
		TextView textview4=(TextView)findViewById(R.id.textView4);
		AvatarView avatar = (AvatarView)findViewById(R.id.avatar);
		avatar.load(Server.ServerAdress+getIntent().getStringExtra("AuthorAvatar"));

		btn_like=(Button)findViewById(R.id.btn_zan);

		textview1.setText("标题:"+title);  
		textview2.setText("作者:"+authorName); 
		textview3.setText("文章内容:"+text); 
		textview4.setText("发表时间:"+date); 

		findViewById(R.id.btn_comment).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				makeComment();
			}
		});

		btn_like.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				toggleLikes();
			}
		}); 

		btnLoadMore.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				loadmore();
			}
		});

	}

	@Override
	protected void onResume() {
		super.onResume();
		reload();
	}
	BaseAdapter listAdapter = new BaseAdapter() {

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;
			if(convertView==null){
				LayoutInflater inflater= LayoutInflater.from(parent.getContext());
				view =inflater.inflate(R.layout.feed_list_item_2, null);
			}else{
				view = convertView;
			}
			TextView text1 = (TextView)view.findViewById(R.id.text1);
			TextView text2 = (TextView)view.findViewById(R.id.text2);
			AvatarView avatar = (AvatarView)view.findViewById(R.id.avatar);

			Comment comment = data.get(position);
			text1.setText("评论内容:" + comment.getText());
			String dateStr = DateFormat.format("yyyy-MM-dd hh:mm", comment.getCreateDate()).toString();
			text2.setText(dateStr);

			avatar.load(Server.ServerAdress + article.getAuthorAvatar());

			return view;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return data.get(position);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return data==null?0:data.size();
		}
	};

	private boolean isLiked;

	//检查赞按钮是否被选中
	void checkLiked(){
		Request request = Server.requestBuilderWithApi("article/"+article.getId()+"/isliked").get().build();
		Server.getSharedClient().newCall(request).enqueue(new Callback() {
			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				try{
					final String responseString = arg1.body().string();
					final Boolean result = new ObjectMapper().readValue(responseString, Boolean.class);

					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							onCheckLikedResult(result);
						}
					});
				}catch(final Exception e){
					e.printStackTrace();
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							onCheckLikedResult(false);
						}
					});
				}
			}

			@Override
			public void onFailure(Call arg0, IOException e) {
				e.printStackTrace();
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						onCheckLikedResult(false);
					}
				});				
			}
		});
	}
	
	//检查赞按钮是否被选中返回的结果
	void onCheckLikedResult(boolean result){
		isLiked =result;
		btn_like.setTextColor(result? Color.BLUE:Color.BLACK);
	}

	//更新显示赞的数量
	void reloadLikes(){
		Request request =Server.requestBuilderWithApi("article/"+article.getId()+"/likes").get().build();
		Server.getSharedClient().newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				try{
					String responseString = arg1.body().string();
					final Integer count = new ObjectMapper().readValue(responseString, Integer.class);

					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							onReloadLikesResult(count);
						}
					});
				}catch (Exception e) {
					e.printStackTrace();
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							onReloadLikesResult(0);
						}
					});
				}

			}

			@Override
			public void onFailure(Call arg0, IOException e) {
				e.printStackTrace();
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						onReloadLikesResult(0);
					}
				});

			}
		});
	}

	void onReloadLikesResult(int count){
		if(count>0){
			btn_like.setText("赞("+count+")");
		}else{
			btn_like.setText("赞");
		}
	}
	
	void toggleLikes(){
		MultipartBody body = new MultipartBody.Builder()
				.addFormDataPart("likes", String.valueOf(!isLiked))
				.build(); 
		
		Request request = Server.requestBuilderWithApi("article/"+article.getId()+"/likes")
				.post(body).build();
		
		Server.getSharedClient().newCall(request).enqueue(new Callback() {
			
			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				runOnUiThread(new Runnable() {
					public void run() {
						reload();
					}
				});
			}
			
			@Override
			public void onFailure(Call arg0, IOException arg1) {
				runOnUiThread(new Runnable() {
					public void run() {
						reload();
					}
				});
			}
});
	}
	void reload(){
		reloadLikes();
		checkLiked();
		Request request = Server.requestBuilderWithApi("/article/"+article.getId()+"/comments")
				.method("GET", null)
				.build();

		Server.getSharedClient().newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(Call arg0,final Response arg1) throws IOException {
				try{
					final Page<Comment> data = new ObjectMapper()
							.readValue(arg1.body().string(), new TypeReference<Page<Comment>>(){});
					runOnUiThread(new Runnable() {
						public void run() {
							FeedContentActivity.this.page = data.getNumber();
							FeedContentActivity.this.data = data.getContent();
							listAdapter.notifyDataSetInvalidated();
						}
					});
				}catch (final Exception e) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							new AlertDialog.Builder(null)
							.setMessage(e.getMessage())
							.show();
						}
					});  
				}
			}


			@Override
			public void onFailure(Call arg0,final IOException e) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						new AlertDialog.Builder(null)
						.setMessage(e.getMessage())
						.show();
					}
				}); 
			}
		});
	}

	void loadmore(){

		btnLoadMore.setEnabled(false);
		textLoadMore.setText("载入中…");

		Request request = Server.requestBuilderWithApi("/article/"+article.getId()+"/comments/"+(page+1)).get().build();
		Server.getSharedClient().newCall(request).enqueue(new Callback() {
			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				runOnUiThread(new Runnable() {
					public void run() {
						btnLoadMore.setEnabled(true);
						textLoadMore.setText("加载更多");
					}
				});

				try{
					final Page<Comment> comments = new ObjectMapper().readValue(arg1.body().string(), new TypeReference<Page<Comment>>() {});
					if(comments.getNumber()>page){

						runOnUiThread(new Runnable() {
							public void run() {
								if(data==null){
									data = comments.getContent();
								}else{
									data.addAll(comments.getContent());
								}
								page = comments.getNumber();
								listAdapter.notifyDataSetChanged();
							}
						});
					}
				}catch(Exception ex){
					ex.printStackTrace();
				}
			}

			@Override
			public void onFailure(Call arg0, IOException arg1) {
				runOnUiThread(new Runnable() {
					public void run() {
						btnLoadMore.setEnabled(true);
						textLoadMore.setText("加载更多");
					}
				});
			}
		});


	}

	void makeComment(){
		Intent itnt =new Intent(FeedContentActivity.this,AddCommentActivity.class);
		itnt.putExtra("data",article);
		startActivity(itnt);
	}


}
