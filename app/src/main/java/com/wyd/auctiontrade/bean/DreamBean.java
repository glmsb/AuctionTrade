package com.wyd.auctiontrade.bean;

import cn.bmob.v3.BmobObject;

/**
 * Description :梦想表
 * Created by wyd on 2016/5/28.
 */
public class DreamBean extends BmobObject {
    private String name;             //商品名称
    private String type;             //类型
    private String description;      //描述
    private UserInfoBean userName;     //竞买人，一对一的关系

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UserInfoBean getUserName() {
        return userName;
    }

    public void setUserName(UserInfoBean userName) {
        this.userName = userName;
    }
}
