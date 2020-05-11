package com.qfedu.controller;

import com.qfedu.entity.SolrData;
import com.qfedu.service.ISolrDataService;
import javafx.geometry.Pos;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @auther: Zhangbo
 * @date: 2020/5/9 20:27
 * @Description:
 */
@Controller
public class SolrDataController {

    @Resource
    private ISolrDataService solrDataService;

    /**
     * 获取数据库数据（多表查询，返回指定字段数据）
     * @return  数据库数据
     */
    @GetMapping("/solrDatas")
    @ResponseBody
    public List<SolrData> getAllData(){
        return solrDataService.getAllData();
    }

    /**
     * 从数据库获取数据 到 solr中
     * @return   获取数据是否成功
     */
    @GetMapping("/getDataFromDB2Solr")
    @ResponseBody
    public boolean getDataFromDB2Solr() {
        return solrDataService.getDataFromDB2Solr();
    }


    /**
     * 从solr获取数据到应用程序,根据关键词查询，并分页
     * @param keyword  关键词
     * @param pageNum  页数
     * @param pageSize  每页记录数
     * @return  查询的数据
     */
    @GetMapping("/getDataFromSolrByKeyWord")
    @ResponseBody
    public List<SolrData>  getDataFromSolrByKeyWord(@RequestParam(value = "keyword") String keyword,
                                                    @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                                                    @RequestParam(value = "pageSize",defaultValue = "5") int pageSize){
        return solrDataService.getDataFromSolrByKeyWord(keyword,pageNum,pageSize);
    }

    //测试上面获取solr数据方法在页面的展示;
    @PostMapping("/solrData2Page")
    public String searchPage(@RequestParam(value = "keyword",defaultValue = "") String keyword,
                             @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                             @RequestParam(value = "pageSize",defaultValue = "5") int pageSize,
                             Model model){
        System.out.println(keyword);

        List<SolrData> datas = solrDataService.getDataFromSolrByKeyWord(keyword, pageNum, pageSize);

        model.addAttribute("datas",datas);

        return "search.jsp";
    }

}
