package com.btpj.wanandroid.data

import com.btpj.lib_base.bean.ApiResponse
import com.btpj.lib_base.bean.PageResponse
import com.btpj.lib_base.http.BaseRepository
import com.btpj.wanandroid.data.bean.Article
import com.btpj.wanandroid.data.bean.Banner
import com.btpj.wanandroid.data.http.Api
import com.btpj.lib_base.http.RetrofitManager

/**
 * 数据仓库
 *
 * @author LTP  2022/3/23
 */
object DataRepository : BaseRepository(), Api {

    private val service by lazy { RetrofitManager.getService(Api::class.java) }

    override suspend fun getBanner(): ApiResponse<List<Banner>> {
        return apiCall { service.getBanner() }
    }

    override suspend fun getArticleTopList(): ApiResponse<List<Article>> {
        return apiCall { service.getArticleTopList() }
    }

    override suspend fun getArticlePageList(
        pageNo: Int,
        pageSize: Int
    ): ApiResponse<PageResponse<Article>> {
        return apiCall { service.getArticlePageList(pageNo, pageSize) }
    }

    override suspend fun getTreeList(): ApiResponse<List<String>> {
        return apiCall { service.getTreeList() }
    }
}