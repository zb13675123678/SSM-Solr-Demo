package com.qfedu.dao;

import com.qfedu.entity.SolrData;

import java.util.List;

/**
 * @auther: Zhangbo
 * @date: 2020/5/9 15:46
 * @Description:    Solr和mysql的数据传输
 */
public interface SolrDataDao {

    /**
     * 获取mysql数据库数据
     * @return
     */
    List<SolrData> getAllData();
    
}
