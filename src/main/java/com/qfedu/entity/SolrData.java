package com.qfedu.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @auther Zhangbo
 * @date 2020/5/9 15:29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolrData {

    private long id;
    private String title;
    private String sellPoint;
    private long price;
    private  String image;
    private  String catName;
    private String itemDesc;

}
