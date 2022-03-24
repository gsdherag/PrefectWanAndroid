package com.btpj.wanandroid.ui.main.home

import com.btpj.wanandroid.R
import com.btpj.wanandroid.data.bean.Article
import com.btpj.wanandroid.databinding.ListItemArticleBinding
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder

/**
 * 文章列表的Adapter
 *
 * @author LTP  2022/3/23
 */
class ArticleListAdapter :
    BaseQuickAdapter<Article, BaseDataBindingHolder<ListItemArticleBinding>>(layoutResId = R.layout.list_item_article),
    LoadMoreModule {

    override fun convert(holder: BaseDataBindingHolder<ListItemArticleBinding>, item: Article) {
        holder.dataBinding?.apply {
            article = item
            executePendingBindings()
        }
    }
}