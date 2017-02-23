package com.wyd.auctiontrade.bean;

import cn.bmob.v3.BmobUser;

/**
 * Description:用户信息实体
 * Created by wyd on 2016/4/24.
 */
public class UserInfoBean extends BmobUser{//父类中提供了，手机号，密码，邮箱，用户名等属性

    private String headImg;//头像
    private String sex;// 性别(0-男，1-女)
    private String age; //年龄

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }
}
