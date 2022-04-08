package com.btpj.wanandroid.ui.main.wechat

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.btpj.lib_base.base.BaseVMBFragment
import com.btpj.lib_base.bean.PageResponse
import com.btpj.lib_base.ext.getEmptyView
import com.btpj.lib_base.ext.initColors
import com.btpj.lib_base.utils.LogUtil
import com.btpj.wanandroid.R
import com.btpj.wanandroid.data.bean.Article
import com.btpj.wanandroid.databinding.IncludeSwiperefreshRecyclerviewBinding
import com.btpj.wanandroid.ui.main.home.ArticleAdapter
import com.btpj.wanandroid.ui.main.home.HomeViewModel

/**
 * 公众号Tab下的子Fragment
 *
 * @author LTP 2022/3/10
 */
class WechatChildFragment :
    BaseVMBFragment<WechatChildViewModel, IncludeSwiperefreshRecyclerviewBinding>(R.layout.include_swiperefresh_recyclerview) {

    /** 列表总数 */
    private var mTotalCount: Int = 0

    /** 页数 */
    private var mPageNo: Int = 0

    /** 当前列表的数量 */
    private var mCurrentCount: Int = 0

    private val mAdapter by lazy { ArticleAdapter() }

    companion object {
        private const val AUTHOR_ID = "authorId"

        /**
         * 创建实例
         *
         * @param authorId 公众号作者Id
         */
        fun newInstance(authorId: Int) =
            WechatChildFragment().apply {
                arguments = Bundle().apply {
                    putInt(AUTHOR_ID, authorId)
                }
            }
    }

    override fun initView() {
        mBinding.apply {
            recyclerView.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = mAdapter.apply {
                    loadMoreModule.setOnLoadMoreListener { loadMoreData() }
                }

                swipeRefreshLayout.apply {
                    initColors()
                    setOnRefreshListener { onRefresh() }
                }
            }
        }

        onRefresh()
    }

    override fun createObserve() {
        super.createObserve()
        mViewModel.apply {
            articlePageListLiveData.observe(viewLifecycleOwner) {
                it?.let { handleArticleData(it) }
            }
        }
    }

    /**
     * 文章分页数据处理
     *
     * @param pageResponse PageResponse
     */
    private fun handleArticleData(pageResponse: PageResponse<Article>) {
        mPageNo = pageResponse.curPage
        mTotalCount = pageResponse.pageCount
        val list = pageResponse.datas
        mAdapter.apply {
            if (mPageNo == 1) {
                if (list.isEmpty()) {
                    setEmptyView(getEmptyView(recyclerView))
                }
                // 如果是加载的第一页数据，用setList()
                setList(list)
            } else {
                // 不是第一页，则用add
                addData(list)
            }
            mCurrentCount = data.size
            loadMoreModule.apply {
                isEnableLoadMore = true
                if (pageResponse.over) {
                    loadMoreEnd()
                } else {
                    loadMoreComplete()
                }
            }
            mBinding.swipeRefreshLayout.isEnabled = true
        }
        mBinding.swipeRefreshLayout.isRefreshing = false
    }

    /**
     * 获取公众号作者文章分页列表
     */
    private fun fetchArticlePageList(pageNo: Int = 1) {
        arguments?.getInt(AUTHOR_ID)?.let { mViewModel.fetchAuthorArticlePageList(it, pageNo) }
    }

    /**下拉刷新 */
    private fun onRefresh() {
        mBinding.swipeRefreshLayout.isRefreshing = true
        // 这里的作用是防止下拉刷新的时候还可以上拉加载
        mAdapter.loadMoreModule.isEnableLoadMore = false
        fetchArticlePageList()
    }

    /** 下拉加载更多 */
    private fun loadMoreData() {
        // 上拉加载时禁止下拉刷新
        mBinding.swipeRefreshLayout.isEnabled = false
        fetchArticlePageList(++mPageNo)
    }
}