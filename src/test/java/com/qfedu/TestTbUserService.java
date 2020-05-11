package com.qfedu;

import com.qfedu.entity.SolrData;
import com.qfedu.service.ISolrDataService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * @auther Zhangbo
 * @date 2020/5/9 15:09
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-mybatis.xml")
public class TestTbUserService {

    @Resource
    private ISolrDataService solrDataService;

    @Test
    public void testGetAllData(){
        List<SolrData> list = solrDataService.getAllData();
        for (SolrData data: list) {
            System.out.println(data);
        }
    }

    @Test
    public void testgetDataFromSolrByKeyWord(){
        List<SolrData> datas = solrDataService.getDataFromSolrByKeyWord("诺基亚", 1, 5);
    }
}
