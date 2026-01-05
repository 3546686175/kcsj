package com.CampusSecond_HandTrading.Platform.util;

import com.CampusSecond_HandTrading.Platform.entity.Goods;
import com.CampusSecond_HandTrading.Platform.entity.Order;
import com.CampusSecond_HandTrading.Platform.entity.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataService {
    public static User login(String name, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DBUtil.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);//预编译SQL语句
            ps.setString(1, name);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setUsername(rs.getString("username"));
                    user.setPassword(rs.getString("password"));
                    user.setRole(rs.getString("role"));
                    return user;
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();//打印异常信息
        }
        return null;
    }

    // 发布商品
    public static void addGoods(Goods goods) {
        String sql = "INSERT INTO goods (name, price, description, seller_id, status, create_time) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);//预编译SQL语句
            ps.setString(1, goods.getName());
            ps.setDouble(2, goods.getPrice());
            ps.setString(3, goods.getDescription());
            ps.setInt(4, goods.getSellerId());
            ps.setString(5, goods.getStatus());
            ps.setDate(6, new java.sql.Date(goods.getCreateTime().getTime()));
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();//打印异常信息
        }
    }

    //获取所有商品
    public static List<Goods> getAllGoods() {
        String sql = "SELECT * FROM goods";
        List<Goods> goodsList = new ArrayList<>();  // 初始化一个空列表，而不是null
        try (Connection conn = DBUtil.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Goods goods = new Goods();
                    goods.setId(rs.getInt("id"));
                    goods.setName(rs.getString("name"));
                    goods.setPrice(rs.getDouble("price"));
                    goods.setDescription(rs.getString("description"));
                    goods.setSellerId(rs.getInt("seller_id"));
                    goods.setStatus(rs.getString("status"));
                    goods.setCreateTime(rs.getDate("create_time"));
                    goodsList.add(goods);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return goodsList;  // 始终返回列表，可能为空但不会是null
    }

    // 根据ID获取商品
    public static Goods getGoodsById(int id) {
        String sql = "SELECT * FROM goods WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Goods goods = new Goods();
                    goods.setId(rs.getInt("id"));
                    goods.setName(rs.getString("name"));
                    goods.setPrice(rs.getDouble("price"));
                    goods.setDescription(rs.getString("description"));
                    goods.setSellerId(rs.getInt("seller_id"));
                    goods.setStatus(rs.getString("status"));
                    goods.setCreateTime(rs.getTimestamp("create_time"));
                    return goods;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 更新商品状态
    public static void updateGoodsStatus(int goodsId, String status) {
        String sql = "UPDATE goods SET status = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, goodsId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 新增订单
    public static void addOrder(Order order) {
        String sql = "INSERT INTO orders(goods_id, buyer_id, seller_id, price, status, create_time) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, order.getGoodsId());
            ps.setInt(2, order.getBuyerId());
            ps.setInt(3, order.getSellerId());
            ps.setDouble(4, order.getPrice());
            ps.setString(5, order.getStatus());
            ps.setTimestamp(6, new Timestamp(order.getCreateTime().getTime()));
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 获取用户订单
    public static List<Order> getOrdersByUserId(int userId) {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE buyer_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Order order = new Order();
                    order.setId(rs.getInt("id"));
                    order.setGoodsId(rs.getInt("goods_id"));
                    order.setBuyerId(rs.getInt("buyer_id"));
                    order.setSellerId(rs.getInt("seller_id"));
                    order.setPrice(rs.getDouble("price"));
                    order.setStatus(rs.getString("status"));
                    order.setCreateTime(rs.getTimestamp("create_time"));
                    list.add(order);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 注册用户
    public static void register(User newUser) {
        String sql = "INSERT INTO users(username, password, role) VALUES (?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newUser.getUsername());
            ps.setString(2, newUser.getPassword());
            ps.setString(3, newUser.getRole());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 查询所有用户
    public static List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                User u = new User();
                u.setId(rs.getInt("id"));
                u.setUsername(rs.getString("username"));
                u.setPassword(rs.getString("password"));
                u.setRole(rs.getString("role"));
                list.add(u);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 查询所有订单
    public static List<Order> getAllOrders() {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT * FROM orders";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Order o = new Order();
                o.setId(rs.getInt("id"));
                o.setGoodsId(rs.getInt("goods_id"));
                o.setBuyerId(rs.getInt("buyer_id"));
                o.setSellerId(rs.getInt("seller_id"));
                o.setPrice(rs.getDouble("price"));
                o.setStatus(rs.getString("status"));
                o.setCreateTime(rs.getTimestamp("create_time"));
                list.add(o);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 根据ID获取用户
    public static User getUserById(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setUsername(rs.getString("username"));
                    user.setPassword(rs.getString("password"));
                    user.setRole(rs.getString("role"));
                    return user;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 根据商品ID获取订单
    public static Order getOrderByGoodsId(int goodsId) {
        String sql = "SELECT * FROM orders WHERE goods_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, goodsId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Order order = new Order();
                    order.setId(rs.getInt("id"));
                    order.setGoodsId(rs.getInt("goods_id"));
                    order.setBuyerId(rs.getInt("buyer_id"));
                    order.setSellerId(rs.getInt("seller_id"));
                    order.setPrice(rs.getDouble("price"));
                    order.setStatus(rs.getString("status"));
                    order.setCreateTime(rs.getTimestamp("create_time"));
                    return order;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}