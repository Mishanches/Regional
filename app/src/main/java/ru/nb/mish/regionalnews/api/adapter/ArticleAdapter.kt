package ru.nb.mish.regionalnews.api.adapter

import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_article.view.*
import ru.nb.mish.regionalnews.R
import ru.nb.mish.regionalnews.models.Article
import java.text.SimpleDateFormat

// класс для хранения отдельных статей (с ним идет item_article.xml, в котором указываем, что в каждой статье (в общем списке
// всех статей) мы будем отображать - фото, заголовок, описание, дату)


class ArticleAdapter(val onArticleClick:(Article)-> Unit): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // val onArticleClick:(Article) - обработка клика по стате
    // Unit - ничего не возвращает

    var mData: List<Article>? = null // в перемнную mData "засовываем" список статей
    // используем метод set для обновления информации в списвке
    set(value) { // вместо SetData в Java
        field=value // field - это есть mData, присовили значение value
        notifyDataSetChanged() // говорим, что список изменился
        // если этот метод не вызывать, то адаптер не будет знать, что данные поменялись
    }

     // имплементируем 3 обязательных метода  (в консрукторе программа сама указала такие параметры
     // )
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
         return object : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_article,parent,false)) {}

        // return object : RecyclerView.ViewHolder - анонимный клас, наследуемся от RecyclerView
         // LayoutInflater.from(parent.context) - создаем inflate
         // inflate(R.layout.item_article,parent, false) - указываем, "что раздуваем"
         // суть метода - создает пустой элемент списка (типа контейнера), а ниже метод onBindViewHolder
         // заполняет этот контенйер данными
         // если не вызывать этот метод, то может невсегда обновлятся новости (инфрмация в этом контейнере)

    }

    override fun getItemCount(): Int { // возвращает кол-во статей (всего)
        return mData?.size?:0 // если кол-во статей не равно 0 - вернет кол-во, если равно 0 (size?:0), то вернет 0
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        // данный метод вызывается для каждого элемента списка, заполняя его данными
        // ниже указываем какими данными заполнять - позиция(это нужно для внутр информации - запомнить на какую новость
        // (позицию) мы кликнули), описание, дата, картинка

        val article = mData!![position] // получаем из списка данных нужную статью
        // дальше для каждого элемента списка формируем свой заголовок, описание и дату
        // itemView - все элементы
        holder.itemView.tvTitle.text = Html.fromHtml(article.title.toString()) // Html.fromHtml( - поддержка HTML в textView -
        holder.itemView.tvExcerpt.text = Html.fromHtml(article.excerpt.toString()) // чтобы не было коряво
        holder.itemView.tvDate.text = SimpleDateFormat("dd MM yyyy").format(article.date)

        // данный метод будет вызыван столько раз, сколько новостей и каждый раз holder.itemView будет разный

        // реализация клика: кликаем по любому View(заголовок, картинка или описание или дата)
        holder.itemView.setOnClickListener { onArticleClick(mData!![holder.adapterPosition]) }

        // картинка, которая приходит с сервера, подгружаем с помощью Glide (ассинхронно(неодновременно) загружает картинки)
        Glide.with(holder.itemView)
                .load(article.getImageUrl()) // getImageUrl() - метод в Article.kt, который "выдергивает" первую картинку
                .apply(RequestOptions().centerCrop()) // как именно будет отображаться картинка в нашем списке
                .into(holder.itemView.imageView) // куда згаружаем - в imageView в из item_article.xml
    }
}