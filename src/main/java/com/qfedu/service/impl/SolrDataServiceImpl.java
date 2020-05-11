package com.qfedu.service.impl;

import com.qfedu.dao.SolrDataDao;
import com.qfedu.entity.SolrData;
import com.qfedu.service.ISolrDataService;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @auther: Zhangbo
 * @date: 2020/5/9 15:59
 * @Description:
 */
@Service
public class SolrDataServiceImpl implements ISolrDataService {

    @Resource
    private SolrDataDao solrDataDao;


    @Value("${solr_server_url}")
    private String baseURL;

    /**
     * 获取数据库数据（多表查询，返回指定字段数据）
     * @return  数据库数据
     */
    @Override
    public List<SolrData> getAllData() {
        return solrDataDao.getAllData();
    }

    /**
     * 从数据库获取数据 到 solr中
     * @return   获取数据是否成功
     */
    @Override
    public boolean getDataFromDB2Solr() {
        //获取数据库数据
        List<SolrData> list = solrDataDao.getAllData();

        //创建solr服务器连接对象
        HttpSolrServer server = new HttpSolrServer(baseURL);

        //创建solr文档传输对象（pojo对象--》dacument对象）
        SolrInputDocument document = null;

        try {
            for (SolrData data: list) {
                document = new SolrInputDocument();

                //将solrData的值，通过scame.xml的field转换，set到solr中
                document.setField("id", data.getId());
                document.setField("item_title", data.getTitle());
                document.setField("item_sell_point", data.getSellPoint());
                document.setField("item_price", data.getPrice());
                document.setField("item_image", data.getImage());
                document.setField("item_category_name", data.getCatName());
                document.setField("item_desc", data.getItemDesc());

                //添加solr文档对象
                server.add(document);
            }
            //提交到solr
            server.commit();

            return  true;
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }


    /**
     * 从solr获取数据到应用程序,根据关键词查询，并分页
     * @param keyword  关键词
     * @param pageNum  页数
     * @param pageSize  每页记录数
     * @return  查询的数据/或者为null
     */
    @Override
    public List<SolrData> getDataFromSolrByKeyWord(String keyword, int pageNum, int pageSize) {

        System.out.println(keyword);
        //keyword = "诺基亚"

        //创建solr服务器连接对象
        HttpSolrServer server = new HttpSolrServer(baseURL);

        //创建查询对象，设置solr查询条件设置
        SolrQuery query = new SolrQuery();
        try {
            //设置solr查询关键字
            if (StringUtils.isEmpty(keyword)) {
                query.setQuery("*:*");
            } else {
                query.setQuery(keyword);
            }

            //设置默认查询字段,在item_tile上检索
            query.set("df", "item_title");

            //设置start和rows,实现分页查询
            int start = (pageNum - 1) * pageSize;
            int rows = pageSize;
            // 设置start的值，返回结果的第几条记录开始，一般分页用，默认0开始
            query.set("start", start);
            //设置rows的值，指定返回结果最多有多少条记录，默认值为 10，配合start实现分页
            query.set("rows", rows);

            //设置查询高亮
            //通过查询对象.setHighlight(boolean):设置是否开启高亮或用query.setParam("hl","true");
            query.setHighlight(true);
            //设置高亮显示的域
            query.addHighlightField("item_title");
            //设置高亮的前缀和后缀::自动添加标签
            query.setHighlightSimplePre("<font color='red'>");
            query.setHighlightSimplePost("</font>");

            //测试打印query查询对象的各项查询设置
            System.out.println(query);

         //将查询对象，添加到solr服务器连接对象进行.query()查询
         //并返回solr了查询结果response域
         QueryResponse response = server.query(query);

         //获取solr的查询结果集，（document对象）
         SolrDocumentList resultList = response.getResults();

        //处理高亮数据进行结果返回并在前端显示:map:存储(封装了)高亮结果的信息
         Map<String, Map<String, List<String>>> map = response.getHighlighting();
         //测试查询map里面的信息
         System.out.println(map);

         //将solr的结果集转变为java结果集,并额外处理高亮的结果返回
         /**
          *  转换的二个注意点:
          *     1,dto和schema.xml中的对象类型转换(强转等处理);
          *       因为SolrDocument对象的get("field名称"),默认值返回的Object,
          *       所以要将SolrDocument对象类型转换为dto对象的属性类型
          *       如果只是用来展示,全用string类型即可.
          *     2,高亮信息结果的处理;
          *         在response返回域中,获取高亮的结果信息,
          *         并将需要展示的高亮的数据提取出来返回到前端解析处理
         */
         //datas用于存储返回的结果集
         List<SolrData> datas = new ArrayList<>();
         SolrData dto = null;
         for (SolrDocument data: resultList) {
             //创建dto对象
             dto = new SolrData();

             Object oid = data.getFieldValue("id");
             String sid = (String) oid;
             long id = oid == null ? 0 : Long.parseLong((String) oid);
             dto.setId(id);

             //  通过高亮数据集,提取item_title字段数据,装换为SolrData的Title的字段数据
             dto.setTitle(map.get(data.getFieldValue("id")).get("item_title").get(0));

             Object oprice = data.getFieldValue("item_price");
             long price = oprice == null ? 0 : ((long)oprice);
             //  设置价格
             dto.setPrice(price);
             //  设置卖点
             dto.setSellPoint((String)data.getFieldValue("item_sell_point"));
             //  设置图片
             dto.setImage((String)data.getFieldValue("item_image"));
             //  设置商品种类名
             dto.setCatName((String)data.getFieldValue("item_category_name"));
             //  设置条目描述
             dto.setItemDesc((String)data.getFieldValue("item_desc"));

             //组装每个dto数据到数据集datas
             datas.add(dto);
         }

         return datas;

        }catch (SolrServerException e){
            e.printStackTrace();
        }
        return null;
    }


}
