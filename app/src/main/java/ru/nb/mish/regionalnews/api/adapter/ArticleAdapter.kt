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

class ArticleAdapter(val onArticleClick: (Article) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var mData: List<Article>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return object : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_article, parent,
                false)) {}
    }

    override fun getItemCount(): Int {
        return mData?.size ?: 0
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val article = mData!![position]

        holder.itemView.tvTitle.text = Html.fromHtml(article.title.toString())
        holder.itemView.tvExcerpt.text = Html.fromHtml(article.excerpt.toString())
        holder.itemView.tvDate.text = SimpleDateFormat("dd MM yyyy").format(article.date)

        holder.itemView.setOnClickListener { onArticleClick(mData!![holder.adapterPosition]) }

        Glide.with(holder.itemView)
                .load(article.getImageUrl())
                .apply(RequestOptions().centerCrop())
                .into(holder.itemView.imageView)
    }
}