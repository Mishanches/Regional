package ru.nb.mish.regionalnews

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Html
import android.webkit.WebChromeClient
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import kotlinx.android.synthetic.main.activity_article.*
import ru.nb.mish.regionalnews.components.IntentHelper
import ru.nb.mish.regionalnews.models.Article
import java.text.SimpleDateFormat


class ArticleActivity : AppCompatActivity() {

    private lateinit var mInterstitialAd: InterstitialAd

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(getString(R.string.one_news))

        mInterstitialAd = InterstitialAd(this)
        mInterstitialAd.adUnitId = "ca-app-pub-6071257354885611/7215242946"

        mInterstitialAd.adListener = object : AdListener() {
            override fun onAdLoaded() {
                val handler = Handler()
                handler.postDelayed(Runnable {
                    this@ArticleActivity.mInterstitialAd.show()
                }, 30000)
            }
        }
        mInterstitialAd.loadAd(AdRequest.Builder().build())

        val article = intent.getParcelableExtra<Article>(IntentHelper.EXTRA_ARTICLE)

        tvTitle.text = Html.fromHtml(article.title.toString())
        tvDate.text = SimpleDateFormat("dd MM yyyy").format(article.date)

        webView.settings.javaScriptEnabled = true
        webView.webChromeClient = WebChromeClient()
        webView.loadData("<style>img{display: inline;height: " +
                "auto;max-width: 100%;}iframe{display: inline;height: auto;max-width: 100%;}</style>"
                + article.content.toString(), "text/html; charset=UTF-8", null)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}