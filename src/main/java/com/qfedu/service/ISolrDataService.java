package com.qfedu.service;

import com.qfedu.entity.SolrData;

import java.util.List;

/**
 * @auther: Zhangbo
 * @date: 2020/5/9 15:59
 * @Description:
 */
public interface ISolrDataService {

    /**
     * 获取数据库数据（多表查询，返回指定字段数据）
     * @return  数据库数据
     */
    List<SolrData> getAllData();

    /**
     * 从数据库获取数据 到 solr中
     * @return   获取数据是否成功
     */
    boolean getDataFromDB2Solr();

    /**
     * 从solr获取数据到应用程序,根据关键词查询，并分页
     * @param keyword  关键词
     * @param pageNum  页数
     * @param pageSize  每页记录数
     * @return  查询的数据
     */
    List<SolrData>  getDataFromSolrByKeyWord(String keyword,int pageNum,int pageSize);
}
