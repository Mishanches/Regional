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


// клас для отобраения активности - списка статей (10 новостей + кнопки в случае ошибки сервера)

class AllArticleActivity : AppCompatActivity() {

    val myAdapter = ArticleAdapter({startActivity(Intent(this, ArticleActivity::class.java)
            .putExtra(IntentHelper.EXTRA_ARTICLE, it))}) // создаем экземпляр класса ArticleAdapter (myAdapter)
    // it - это есть Article.kt, т.е. мы ее передаем
    // в конструкторе - наподобие onClickListener: происходит запуск ArticleActivity при нажатии на той
    // новости, на которую кликнули

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_article)

        supportActionBar?.setDisplayHomeAsUpEnabled(true) // кнопка назад
        supportActionBar?.setTitle(getString(R.string.fresh_news))

        rvArticle.layoutManager = LinearLayoutManager(this)
        // РесайклВью присвоили менеджер Лайаутов для отображения, один из них - LinearLayoutManager

        rvArticle.adapter = myAdapter // РесайклВью присвоили наш myAdapter, который есть ArticleAdapter вместе с интентом

        loadNews() // при создании Активити загружаем новости

        swMainSwipe.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorswipe)) //установка цвета swipe

        swMainSwipe.setOnRefreshListener {
            loadNews() // при свайпе загружаем новости
        }

        bRefresh.setOnClickListener {
            loadNews() // при нажатии на кнопку запускаем загрузку новостей
        }

    }
    private fun  loadNews() {
        launch(UI) { // для корутинов (для асинхронного запроса)
            try { // ловим исключение, если нет сети
                // тут получаем данные и присваиваем в алаптер

                val articles = ServiceGenerator.serverApi.loadNews(intent.getStringExtra(IntentHelper.EXTRA_URL)).await()
                // в перменную articles засунули ответ с сайта
                // тут serverApi - перменная типа ServerApi из ServiceGenerator, а ServerApi - интерфейс, в котором
                // делается запрос
                // intent.getStringExtra(IntentHelper.EXTRA_URL) - тут получаем ссылку с MainActivity.kt(на какой сайт заходим)
                // await() - запрос делается асинхронно

                myAdapter.mData = articles // получили ответ в articles, этот ответ присвоили адаптеру - myAdapter,
                        // mData- это список статей

            } catch (ex: UnknownHostException) {
                ex.printStackTrace() // метод диагностики исключения - показывает что и где произошло
                Toast.makeText(this@AllArticleActivity, R.string.error_no_connection_text, Toast.LENGTH_LONG).show()

            } catch (ex:Exception) { // исключение ошибки сервера
                ex.printStackTrace()
                Toast.makeText(this@AllArticleActivity, R.string.error_no_connection_button, Toast.LENGTH_LONG).show()
            }

            // !!! - тут попробывать поиграться с
            // 1. swMainSwipe.isRefreshing = false
            // 2. и rvArticle.visibility = if (rvArticle.adapter.itemCount == 0 )View.GONE else View.VISIBLE
            swMainSwipe.isRefreshing = false // скрываем swipe, когда новость загружена
            // isRefreshing - это метод setRefreshing, который скрывет swipe при удачной загрузке контента

            // llNoData - лайоут с кнопой и текстом ошибок
            // если статей нет в адаптере (пришло 0 статей с сервера), то делаем это лайоут видимым
            // else View.GONE - если есть хоть одна новость, то делаем этот лайоут невидимым
            llNoData.visibility = if(rvArticle.adapter.itemCount == 0) View.VISIBLE else View.GONE

            rvArticle.visibility = if (rvArticle.adapter.itemCount == 0 )View.GONE else View.VISIBLE

        }
    }

    override fun onSupportNavigateUp(): Boolean {
    onBackPressed()
        return true
    }

}
