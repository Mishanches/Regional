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

// класс одной новости, когда из списка кликаем и попадаем на отдельную активность с определенной новостью

class ArticleActivity : AppCompatActivity() {

    private lateinit var mInterstitialAd: InterstitialAd  // переменная рекламы, InterstitialAd - клас отвечающий за рекламу

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(getString(R.string.one_news))


                // реклама
        mInterstitialAd = InterstitialAd(this) // создали банер
        mInterstitialAd.adUnitId = "ca-app-pub-6071257354885611/7215242946" // выставляляем ID

        mInterstitialAd.adListener = object : AdListener() { // присвоили обработчик, который следит за изменениями статуса рекламы
            override fun onAdLoaded() { // onAdLoaded() - загрузил рекламу, но пока не отобразил
                val handler = Handler()
                handler.postDelayed(Runnable {
                    this@ArticleActivity.mInterstitialAd.show()
                }, 30000) // реклама каждые 30 секунд

            }
        }
        mInterstitialAd.loadAd(AdRequest.Builder().build()) // загружаем рекламу из Google


        // получаем intent со статьей, на которую нажали
        val article = intent.getParcelableExtra<Article>(IntentHelper.EXTRA_ARTICLE)
        // выше getParcelableExtra<Article> - вызываем вторичный конструктор из Article (получаем распарсеные данные)

        // и выводим на кативность:
        tvTitle.text = Html.fromHtml(article.title.toString()) // заголовок
        tvDate.text = SimpleDateFormat("dd MM yyyy").format(article.date) // дата

        webView.settings.javaScriptEnabled = true // поддержка javaScript
        webView.webChromeClient = WebChromeClient() // взаимодействие со страницей после того, как она была загружена
        webView.loadData("<style>img{display: inline;height: auto;max-width: 100%;}iframe{display: inline;height: auto;max-width: 100%;}</style>" + article.content.toString(), "text/html; charset=UTF-8", null) // передаем в LoadData - HTML
        // UTF-8 - передаем кодировку
        // "<style>img{display: inline;height: auto;max-width: 100%;}</style>" - стиль CSS для HTML


    }

    override fun onSupportNavigateUp(): Boolean { // функция кнопки назад
        onBackPressed()
        return true
    }
}
