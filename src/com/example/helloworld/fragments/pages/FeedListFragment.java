package com.example.helloworld.fragments.pages;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import com.example.helloworld.BootActivity;
import com.example.helloworld.FeedContentActivity;
import com.example.helloworld.LoginActivity;
import com.example.helloworld.R;
import com.example.helloworld.api.Server;
import com.example.helloworld.entity.Article;
import com.example.helloworld.entity.Page;
import com.example.helloworld.entity.User;
import com.example.helloworld.fragments.widgets.AvatarView;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.R.string;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class FeedListFragment extends Fragment {

	View view;
	ListView listView;

	View btnLoadMore;
	TextView textLoadMore;

	List<Article> data;
	int page =0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		if(view==null){
			view= inflater.inflate(R.layout.fragment_page_feed_list, null);
			btnLoadMore= inflater.inflate(R.layout.load_more_button, null);
			textLoadMore= (TextView)btnLoadMore.findViewById(R.id.text);

			listView = (ListView)view.findViewById(R.id.list);
			listView.addFooterView(btnLoadMore);
			listView.setAdapter(listAdapter);



			listView.setOnItemClickListener(new  AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					onItemClicked(position);
				}
			});

			btnLoadMore.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					loadmore();
				}
			});
		}
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		reload();
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

			Article article = data.get(position);
			text1.setText(article.getAuthorName() + ": " + article.getTitle());
			String dateStr = DateFormat.format("yyyy-MM-dd hh:mm", article.getCreateDate()).toString();
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


	void reload(){
		Request request = Server.requestBuilderWithApi("feeds")
				.method("GET", null)
				.build();

		Server.getSharedClient().newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(Call arg0,final Response arg1) throws IOException {
				try{
					Page<Article> data = new ObjectMapper()
							.readValue(arg1.body().string(), new TypeReference<Page<Article>>(){});
					FeedListFragment.this.page = data.getNumber();
					FeedListFragment.this.data = data.getContent();
					getActivity().runOnUiThread(new Runnable() {
						public void run() {

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

		Request request = Server.requestBuilderWithApi("feeds/"+(page+1)).get().build();
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
					Page<Article> feeds = new ObjectMapper().readValue(arg1.body().string(), new TypeReference<Page<Article>>() {});
					if(feeds.getNumber()>page){
						if(data==null){
							data = feeds.getContent();
						}else{
							data.addAll(feeds.getContent());
						}
						page = feeds.getNumber();
						getActivity().runOnUiThread(new Runnable() {
							public void run() {


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

	public void onItemClicked( int position){
		String title = data.get(position).getTitle();
		String text = data.get(position).getText();
		String authorName = data.get(position).getAuthorName();
		String date = DateFormat.format("yyyy-MM-dd hh:mm", data.get(position).getCreateDate()).toString();
		String authorAvatar = data.get(position).getAuthorAvatar();

		Intent itnt =new Intent(getActivity(),FeedContentActivity.class);
		itnt.putExtra("Text", text);
		itnt.putExtra("Title", title);
		itnt.putExtra("AuthorName", authorName);
		itnt.putExtra("Date", date);
		itnt.putExtra("AuthorAvatar", authorAvatar);

		startActivity(itnt);
	}
}
