package ru.nb.mish.regionalnews

import android.content.Intent
import android.opengl.Visibility
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_all_article.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import ru.nb.mish.regionalnews.api.ServiceGenerator
import ru.nb.mish.regionalnews.api.adapter.ArticleAdapter
import ru.nb.mish.regionalnews.components.IntentHelper
import java.net.UnknownHostException

class AllArticleActivity : AppCompatActivity() {

    val myAdapter = ArticleAdapter({
        startActivity(Intent(this, ArticleActivity::class.java)
                .putExtra(IntentHelper.EXTRA_ARTICLE, it))
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_article)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(getString(R.string.fresh_news))

        rvArticle.layoutManager = LinearLayoutManager(this)
        rvArticle.adapter = myAdapter

        loadNews()

        swMainSwipe.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorswipe))
        swMainSwipe.setOnRefreshListener {
            loadNews()
        }

        bRefresh.setOnClickListener {
            loadNews()
        }
    }

    private fun loadNews() {
        launch(UI) {
            try {
                val articles = ServiceGenerator.serverApi.loadNews(
                        intent.getStringExtra(IntentHelper.EXTRA_URL)).await()

                myAdapter.mData = articles

            } catch (ex: UnknownHostException) {
                ex.printStackTrace()
                Toast.makeText(this@AllArticleActivity, R.string.error_no_connection_text, Toast.LENGTH_LONG).show()

            } catch (ex: Exception) {
                ex.printStackTrace()
                Toast.makeText(this@AllArticleActivity, R.string.error_no_connection_button, Toast.LENGTH_LONG).show()
            }

            swMainSwipe.isRefreshing = false
            llNoData.visibility = if (rvArticle.adapter.itemCount == 0) View.VISIBLE else View.GONE
            rvArticle.visibility = if (rvArticle.adapter.itemCount == 0) View.GONE else View.VISIBLE

        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}