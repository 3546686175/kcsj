package com.CampusSecond_HandTrading.Platform.ui;

import com.CampusSecond_HandTrading.Platform.entity.Goods;
import com.CampusSecond_HandTrading.Platform.entity.Order;
import com.CampusSecond_HandTrading.Platform.entity.User;
import com.CampusSecond_HandTrading.Platform.util.DataService;

import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class MainUI {
    private Scanner scanner = new Scanner(System.in);
    private User currentUser = null;

    // 程序入口
    public void start() {
        System.out.println("===== 欢迎来到校园二手交易平台 =====");
        System.out.println("1. 注册");
        System.out.println("2. 登录");
        int choice = scanner.nextInt();
        scanner.nextLine(); // 清除缓冲区
        switch (choice) {
            case 1:
                register();
                break;
            case 2:
                login();
                break;
            default:
                System.out.println("无效选择，请重新输入！");
        }

        // 主菜单
        showMainMenu();
    }

    // 注册功能
    private void register() {
        System.out.println("\n===== 注册 =====");
        System.out.print("请输入用户名: ");
        String username = scanner.nextLine();
        System.out.print("请输入密码(6-12位): ");
        String password = scanner.nextLine();
        System.out.print("请确认密码: ");
        String confirmPassword = scanner.nextLine();
        if (!password.equals(confirmPassword)) {
            System.out.println("两次密码输入不一致，请重新注册！");
            return;
        }

        // 调用数据服务层注册用户
        DataService dataService = new DataService();
        User newUser = new User(0, username, password, "user");
        dataService.register(newUser);

        System.out.println("注册成功！");
    }

    // 登录功能
    private void login() {
        System.out.println("\n===== 登录 =====");
        System.out.print("请输入用户名: ");
        String username = scanner.nextLine();
        System.out.print("请输入密码: ");
        String password = scanner.nextLine();

        // 调用数据服务层登录用户
        DataService dataService = new DataService();
        User user = dataService.login(username, password);
        if (user != null) {
            currentUser = user;
            System.out.println("登录成功！");
        } else {
            System.out.println("登录失败！用户名或密码错误。");
        }
    }

    // 主菜单
    private void showMainMenu() {
        while (true) {
            System.out.println("\n===== 主菜单 =====");
            
            // 所有用户都能看到的功能
            System.out.println("1. 商品管理");
            System.out.println("2. 订单管理");
            
            // 管理员特有的功能
            if (currentUser != null && User.ROLE_ADMIN.equals(currentUser.getRole())) {
                System.out.println("3. 管理员功能");
            }
            
            System.out.println("4. 退出系统");
            System.out.print("请选择功能: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // 清除缓冲区
    
            switch (choice) {
                case 1:
                    showGoodsMenu();
                    break;
                case 2:
                    showOrderMenu();
                    break;
                case 3:
                    // 检查是否为管理员
                    if (currentUser != null && User.ROLE_ADMIN.equals(currentUser.getRole())) {
                        showAdminMenu();
                    } else {
                        System.out.println("无效选择，请重新输入！");
                    }
                    break;
                case 4:
                    System.out.println("感谢使用，再见！");
                    System.exit(0);
                    break;
                default:
                    System.out.println("无效选择，请重新输入！");
            }
        }
    }

    // 商品管理菜单
    private void showGoodsMenu() {
        while (true) {
            System.out.println("\n===== 商品管理 =====");
            System.out.println("1. 发布商品");
            System.out.println("2. 展示商品列表");
            System.out.println("3. 返回主菜单");
            System.out.print("请选择功能: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    publishGoods();
                    break;
                case 2:
                    showGoodsList();
                    break;
                case 3:
                    return;
                default:
                    System.out.println("无效选择，请重新输入！");
            }
        }
    }

    // 订单管理菜单
    private void showOrderMenu() {
        while (true) {
            System.out.println("\n===== 订单管理 =====");
            System.out.println("1. 下单购买");
            System.out.println("2. 查看订单状态");
            System.out.println("3. 返回主菜单");
            System.out.print("请选择功能: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    placeOrder();
                    break;
                case 2:
                    showOrderStatus();
                    break;
                case 3:
                    return;
                default:
                    System.out.println("无效选择，请重新输入！");
            }
        }
    }

    // 发布商品
    private void publishGoods() {
        System.out.println("\n===== 发布商品 =====");
        System.out.print("商品名称: ");
        String name = scanner.nextLine();
        System.out.print("商品价格: ");
        double price = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("商品描述: ");
        String desc = scanner.nextLine();

        // 生成商品ID
        int goodsId = DataService.getAllGoods().size() + 1;
        Goods goods = new Goods(goodsId, name, price, desc, currentUser.getId(), "在售", new Date());
        DataService.addGoods(goods);

        System.out.println("商品发布成功！商品ID: " + goodsId);
    }

    // 展示商品列表
    private void showGoodsList() {
        System.out.println("\n===== 商品列表 =====");
        List<Goods> goods = DataService.getAllGoods();
        if (goods.isEmpty()) {
            System.out.println("暂无商品！");
            return;
        }
        for (Goods g : goods) {
            System.out.printf("ID: %d | 名称: %s | 价格: %.2f | 状态: %s | 描述: %s\n",
                    g.getId(), g.getName(), g.getPrice(), g.getStatus(), g.getDescription());
        }
    }

    // 下单购买
    private void placeOrder() {
        System.out.println("\n===== 下单购买 =====");
        showGoodsList();

        System.out.print("请输入要购买的商品ID: ");
        int goodsId = scanner.nextInt();
        scanner.nextLine();

        Goods goods = DataService.getGoodsById(goodsId);
        if (goods == null) {
            System.out.println("商品不存在！");
            return;
        }
        if (!"在售".equals(goods.getStatus())) {
            System.out.println("商品已售出！");
            return;
        }

        // 生成订单ID
        int orderId = DataService.getOrdersByUserId(currentUser.getId()).size() + 1;
        Order order = new Order(orderId, goodsId, currentUser.getId(), goods.getSellerId(), goods.getPrice(), "待付款", new Date());
        DataService.addOrder(order);
        // 更新商品状态
        DataService.updateGoodsStatus(goodsId, "已售出");

        System.out.println("下单成功！订单ID: " + orderId);
    }

    // 查看订单状态
    private void showOrderStatus() {
        System.out.println("\n===== 我的订单 =====");
        List<Order> orders = DataService.getOrdersByUserId(currentUser.getId());
        if (orders.isEmpty()) {
            System.out.println("暂无订单！");
            return;
        }
        for (Order o : orders) {
            System.out.printf("订单ID: %d | 商品ID: %d | 价格: %.2f | 状态: %s | 下单时间: %s\n",
                    o.getId(), o.getGoodsId(), o.getPrice(), o.getStatus(), o.getCreateTime());
        }
    }

    // 启动程序
    public static void main(String[] args) {
        new MainUI().start();
    }

    //展示管理员页面
    public void showAdminMenu() {
        System.out.println("\n===== 管理员菜单 =====");
        System.out.println("1. 展示所有用户");
        System.out.println("2. 展示所有商品");
        System.out.println("3. 展示所有订单");
        System.out.println("4. 返回主菜单");
        System.out.print("请选择功能: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                showAllUsers();
                break;
            case 2:
                showAllGoods();
                break;
            case 3:
                showAllOrders();
                break;
            case 4:
                return;
            default:
                System.out.println("无效选择，请重新输入！");
        }
    }

    private void showAllUsers() {
        System.out.println("\n===== 所有用户 =====");
        List<User> users = DataService.getAllUsers();
        if (users.isEmpty()) {
            System.out.println("暂无用户！");
            return;
        }
        for (User u : users) {
            System.out.printf("ID: %d | 用户名: %s | 角色: %s\n",
                    u.getId(), u.getUsername(), u.getRole());
        }
    }

    private void showAllGoods() {
        System.out.println("\n===== 所有商品 =====");
        List<Goods> goods = DataService.getAllGoods();
        if (goods.isEmpty()) {
            System.out.println("暂无商品！");
            return;
        }
        for (Goods g : goods) {
            System.out.printf("ID: %d | 名称: %s | 价格: %.2f | 状态: %s | 描述: %s",
                    g.getId(), g.getName(), g.getPrice(), g.getStatus(), g.getDescription());
            
            // 如果商品已售出，显示购买方信息
            if ("已售出".equals(g.getStatus())) {
                Order order = DataService.getOrderByGoodsId(g.getId());
                if (order != null) {
                    User buyer = DataService.getUserById(order.getBuyerId());
                    if (buyer != null) {
                        System.out.printf(" | 购买方: %s", buyer.getUsername());
                    }
                }
            }
            System.out.println(); // 换行
        }
    }

    private void showAllOrders() {
        System.out.println("\n===== 所有订单 =====");
        List<Order> orders = DataService.getAllOrders();
        if (orders.isEmpty()) {
            System.out.println("暂无订单！");
            return;
        }
        for (Order o : orders) {
            System.out.printf("订单ID: %d | 商品ID: %d | 买家ID: %d | 卖家ID: %d | 价格: %.2f | 状态: %s\n",
                    o.getId(), o.getGoodsId(), o.getBuyerId(), o.getSellerId(), o.getPrice(), o.getStatus());
        }
    }

    
}