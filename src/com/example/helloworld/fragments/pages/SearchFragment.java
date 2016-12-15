package com.example.helloworld.fragments.pages;

import java.io.IOException;
import java.util.List;

import com.example.helloworld.FeedContentActivity;
import com.example.helloworld.R;
import com.example.helloworld.api.Server;
import com.example.helloworld.entity.Article;
import com.example.helloworld.entity.Page;
import com.example.helloworld.fragments.widgets.AvatarView;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class SearchFragment extends Fragment {

	EditText searchText;
	Button btn_search;
	ListView listView;
	View view;

	View btnLoadMore;
	TextView textLoadMore;

	int page= 0;
	List<Article>find;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		if(view==null){
			view= inflater.inflate(R.layout.fragment_page_search_page, null);
			btnLoadMore= inflater.inflate(R.layout.load_more_button, null);
			textLoadMore= (TextView)btnLoadMore.findViewById(R.id.text);
			
			listView = (ListView)view.findViewById(R.id.list);
			listView.addFooterView(btnLoadMore);
			listView.setAdapter(listAdapter);
			
			btn_search=(Button)view.findViewById(R.id.btn_search);
			searchText=(EditText)view.findViewById(R.id.keyword_txt);
			
		    btn_search.setOnClickListener(new OnClickListener() {		
				@Override
				public void onClick(View v) {
					searchKeyword();
				}
			});
			
			btnLoadMore.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					loadmore();
				}
			});
			
			listView.setOnItemClickListener(new  AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					onItemClicked(position);
				}
			});
		}
		return view;
	}

	BaseAdapter listAdapter = new BaseAdapter() {

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;
			if(convertView==null){
				LayoutInflater inflater= LayoutInflater.from(parent.getContext());
				view =inflater.inflate(R.layout.feed_list_item_1, null);
			}else{
				view = convertView;
			}
			TextView text1 = (TextView)view.findViewById(R.id.text1);
			TextView text2 = (TextView)view.findViewById(R.id.text2);
			AvatarView avatar = (AvatarView)view.findViewById(R.id.avatar);

			Article article = find.get(position);
			text1.setText("作者:"+article.getAuthorName() + "文章内容: " + article.getText());
			String dateStr = DateFormat.format("发表时间:yyyy-MM-dd hh:mm", article.getCreateDate()).toString();
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
			return find.get(position);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return find==null?0:find.size();
		}
	};

	public void onItemClicked( int position){
		String title = find.get(position).getTitle();
		String text = find.get(position).getText();
		String authorName = find.get(position).getAuthorName();
		String date = DateFormat.format("yyyy-MM-dd hh:mm", find.get(position).getCreateDate()).toString();
		String authorAvatar = find.get(position).getAuthorAvatar();
		Article content = find.get(position);

		Intent itnt =new Intent(getActivity(),FeedContentActivity.class);
		itnt.putExtra("Text", text);
		itnt.putExtra("Title", title);
		itnt.putExtra("AuthorName", authorName);
		itnt.putExtra("Date", date);
		itnt.putExtra("AuthorAvatar", authorAvatar);
		itnt.putExtra("Data",content);

		startActivity(itnt);
	}
	
	void searchKeyword(){
	    String keywords = searchText.getText().toString();	
	    //强制隐藏自带软键盘
	    InputMethodManager inputMethodManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(searchText.getWindowToken(), 0);
	    
		Request request = Server.requestBuilderWithApi("article/s/"+keywords)
				.get()
				.build();

		Server.getSharedClient().newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(Call arg0,final Response arg1) throws IOException {
				try{
					final Page<Article> data = new ObjectMapper()
							.readValue(arg1.body().string(), new TypeReference<Page<Article>>(){});
					getActivity().runOnUiThread(new Runnable() {
						public void run() {
							SearchFragment.this.page = data.getNumber();
							SearchFragment.this.find = data.getContent();
							listAdapter.notifyDataSetInvalidated();
						}
					});
				}catch (final Exception e) {
					getActivity().runOnUiThread(new Runnable() {
						@Override
						public void run() {
							new AlertDialog.Builder(getActivity())
							.setMessage(e.getMessage())
							.show();
						}
					});  
				}
			}


			@Override
			public void onFailure(Call arg0,final IOException e) {
				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						new AlertDialog.Builder(getActivity())
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

		String keywords = searchText.getText().toString();
		Request request = Server.requestBuilderWithApi("article/s/"+keywords+"?page="+(page+1)).get().build();
		Server.getSharedClient().newCall(request).enqueue(new Callback() {
			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				getActivity().runOnUiThread(new Runnable() {
					public void run() {
						btnLoadMore.setEnabled(true);
						textLoadMore.setText("加载更多");
					}
				});

				try{
					final Page<Article> searchs = new ObjectMapper().readValue(arg1.body().string(), new TypeReference<Page<Article>>() {});
					if(searchs.getNumber()>page){
						
						getActivity().runOnUiThread(new Runnable() {
							public void run() {
								if(find==null){
									find = searchs.getContent();
								}else{
									find.addAll(searchs.getContent());
								}
								page = searchs.getNumber();
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
				getActivity().runOnUiThread(new Runnable() {
					public void run() {
						btnLoadMore.setEnabled(true);
						textLoadMore.setText("加载更多");
					}
				});
			}
		});
	
	}
}
