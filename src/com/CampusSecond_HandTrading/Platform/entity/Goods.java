package com.CampusSecond_HandTrading.Platform.entity;

import java.util.Date;

public class Goods {
    private int id;             // 商品ID
    private String name;        // 商品名称
    private double price;       // 商品价格
    private String description; // 商品描述
    private int sellerId;       // 卖家ID
    private String status;      // 状态（在售/已售出）
    private Date createTime;    // 发布时间

    public Goods(){};
    // 带参构造
    public Goods(int id, String name, double price, String description, int sellerId, String status, Date createTime) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.sellerId = sellerId;
        this.status = status;
        this.createTime = createTime;
    }

    // Getter & Setter

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public int getSellerId() {
        return sellerId;
    }

    public String getStatus() {
        return status;
    }

    public Date getCreateTime() {
        return createTime;
    }
}