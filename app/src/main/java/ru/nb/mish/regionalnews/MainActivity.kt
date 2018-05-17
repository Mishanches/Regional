package ru.nb.mish.regionalnews

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.activity_main.*
import ru.nb.mish.regionalnews.components.IntentHelper

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MobileAds.initialize(this, "ca-app-pub-6071257354885611~7310611711") // реклама
    }

    fun onClick (view: View) {
        when(view.id) {

            R.id.llKaliningrad -> {
                val intent = Intent(this, AllArticleActivity::class.java)
                        .putExtra(IntentHelper.EXTRA_TITLE, getString(R.string.title_article_kaliningrad))
                        .putExtra(IntentHelper.EXTRA_URL, "http://knia.ru/wp-json/wp/v2/posts/")
                startActivity(intent)
            }
            R.id.llYaroslavl-> {
                val intent2 = Intent(this, AllArticleActivity::class.java)
                        .putExtra(IntentHelper.EXTRA_TITLE, getString(R.string.title_article_yaroslavl))
                        .putExtra(IntentHelper.EXTRA_URL, "http://imenno.ru/wp-json/wp/v2/posts")
                startActivity(intent2)
            }

            R.id.llNovosibirsk-> {
                val intent3 = Intent(this, AllArticleActivity::class.java)
                        .putExtra(IntentHelper.EXTRA_TITLE, getString(R.string.title_article_novosibirsk))
                        .putExtra(IntentHelper.EXTRA_URL, "http://newsib.ru/wp-json/wp/v2/posts")
                startActivity(intent3)
            }

            R.id.lllYakutsk-> {
                val intent4 = Intent(this, AllArticleActivity::class.java)
                        .putExtra(IntentHelper.EXTRA_TITLE, getString(R.string.title_article_yakutsk))
                        .putExtra(IntentHelper.EXTRA_URL, "http://sakhalife.ru/wp-json/wp/v2/posts")
                startActivity(intent4)
            }

        }
    }
}
