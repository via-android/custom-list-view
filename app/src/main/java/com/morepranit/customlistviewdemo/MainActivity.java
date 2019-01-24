package com.morepranit.customlistviewdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.morepranit.customlistviewdemo.adapters.BookAdapter;
import com.morepranit.customlistviewdemo.models.BookModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    /*TextView tvData;
    NetworkImageView imageView;*/

    ArrayList<BookModel> bookModels;
    ListView lvBooks;
    BookAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bookModels = new ArrayList<>();
        lvBooks = findViewById(R.id.lv_books);
        adapter = new BookAdapter(this, bookModels);
        lvBooks.setAdapter(adapter);

        lvBooks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                BookModel model = bookModels.get(i);
                Intent intent = new Intent(MainActivity.this, BookPreviewActivity.class);
                intent.putExtra("preview_link", model.getPreviewLink());
                startActivity(intent);
            }
        });

        /*tvData = findViewById(R.id.tv_data);
        imageView = findViewById(R.id.imageView);

        ImageLoader loader = AppController.getInstance().getImageLoader();

        imageView.setImageUrl("https://i.pinimg.com/originals/0c/48/76/0c4876e490e1e4dc925cc09be057a5a5.jpg", loader);*/

        getData();
    }

    private void getData() {
        String url = "https://www.googleapis.com/books/v1/volumes?q=inspirational";

        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Log.e("onResponse: ", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            parseJSON(jsonObject);
                            // tvData.setText(bookModels.get(9).getTitle());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("onErrorResponse: ", error.getMessage());
            }
        });

        AppController.getInstance().addToRequestQueue(request, "req_tag");
    }

    private void parseJSON(JSONObject jsonObject) throws JSONException {
        JSONArray jsonArray = jsonObject.getJSONArray("items");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject bookObject = jsonArray.getJSONObject(i);

            String id = bookObject.getString("id");
            JSONObject volInfo = bookObject.getJSONObject("volumeInfo");
            String title = volInfo.getString("title");

            JSONArray authors = volInfo.getJSONArray("authors");
            ArrayList<String> authorsList = new ArrayList<>();
            for (int j = 0; j < authors.length(); j++) {
                String author = authors.getString(j);
                authorsList.add(author);
            }

            String publisher = volInfo.has("publisher") ? volInfo.getString("publisher") : "";
            String publishedDate = volInfo.has("publishedDate") ? volInfo.getString("publishedDate") : "";
            String desc = volInfo.has("description") ? volInfo.getString("description") : "";

            String thumbnail = volInfo.getJSONObject("imageLinks").getString("thumbnail");
            String previewLink = volInfo.getString("previewLink");

            BookModel bookModel = new BookModel();
            bookModel.setId(id);
            bookModel.setDescription(desc);
            bookModel.setPublisher(publisher);
            bookModel.setPublishedDate(publishedDate);
            bookModel.setThumbnail(thumbnail);
            bookModel.setTitle(title);
            bookModel.setAuthors(authorsList);
            bookModel.setPreviewLink(previewLink);

            bookModels.add(bookModel);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onStop() {
        super.onStop();
        AppController.getInstance().cancelPendingRequests("req_tag");
    }
}
