package com.CampusSecond_HandTrading.Platform.entity;

import java.util.Date;

public class Order {
    private int id;             // 订单ID
    private int buyerId;        // 买家ID
    private int sellerId;       // 卖家ID
    private int goodsId;        // 商品ID
    private double price;       // 订单价格
    private String status;      // 订单状态（待付款/已付款/已取消）
    private Date createTime;    // 订单创建时间

    public Order(){};

    public Order(int id, int goodsId, int buyerId, int sellerId, double price, String status, Date createTime) {
        this.id = id;
        this.goodsId = goodsId;
        this.buyerId = buyerId;
        this.sellerId = sellerId;
        this.price = price;
        this.status = status;
        this.createTime = createTime;
    }
        // Getter & Setter

    public void setBuyerId(int buyerId) {
        this.buyerId = buyerId;
    }

    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getBuyerId() {
        return buyerId;
    }

    public int getSellerId() {
        return sellerId;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public double getPrice() {
        return price;
    }

    public String getStatus() {
        return status;
    }

    public Date getCreateTime() {
        return createTime;
    }
}