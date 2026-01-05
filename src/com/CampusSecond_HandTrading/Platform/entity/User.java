package com.CampusSecond_HandTrading.Platform.entity;

public class User {
    // 定义角色常量
    public static final String ROLE_ADMIN = "admin";
    public static final String ROLE_USER = "user";
    
    private int id;         // 用户ID
    private String username;// 用户名
    private String password;// 密码
    private String role;    // 角色（普通用户/管理员）

    // 无参构造
    public User() {}

    // 带参构造
    public User(int id, String username, String password, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // Getter & Setter

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }
}