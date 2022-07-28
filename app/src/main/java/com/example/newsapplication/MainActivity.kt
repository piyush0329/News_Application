package com.example.newsapplication

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest


class MainActivity : AppCompatActivity(), NewsItemClicked {
    public lateinit var mAdapter: NewsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
         val recyclerView= findViewById<RecyclerView>(R.id.recyclerView);
        recyclerView.layoutManager= LinearLayoutManager(this)
        fetchData()
        mAdapter = NewsAdapter(this)
        recyclerView.adapter=mAdapter

    }
    private fun fetchData(){
        val url ="https://newsapi.org/v2/top-headlines?country=in&apiKey=8ac7c5307f5c42d78357c23455e83655"
        val jsonObjectRequest = object:JsonObjectRequest(Request.Method.GET,url,null,
            Response.Listener { val newsJsonArray=it.getJSONArray("articles")
                              val newsArray= ArrayList<News>()
                              for(i in 0 until newsJsonArray.length()){
                                  val newsJsonobject = newsJsonArray.getJSONObject(i)
                                  val news = News(
                                      newsJsonobject.getString("title"),
                                      newsJsonobject.getString("author"),
                                      newsJsonobject.getString("url"),
                                      newsJsonobject.getString("urlToImage")
                                  )
                                  newsArray.add(news)
                              }
                           mAdapter.upadteNews(newsArray)
                              },
            Response.ErrorListener {

            })
        {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["User-Agent"] = "Mozilla/5.0"
                return headers
            }
        }
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }
    

    override fun onItemClicked(item: News) {
        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(this, Uri.parse(item.url))
        val colorInt: Int = Color.parseColor("#FF0000") //red



    }

}