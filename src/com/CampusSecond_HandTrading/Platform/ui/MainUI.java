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
        scanner.nextLine();
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
        System.out.print("请输入真实姓名: ");
        String name = scanner.nextLine();
        System.out.print("请输入用户名: ");
        String username = scanner.nextLine();
        System.out.print("请输入密码: ");
        String password = scanner.nextLine();
        System.out.print("请确认密码: ");
        String confirmPassword = scanner.nextLine();
        System.out.print("请输入联系方式: ");
        String contact = scanner.nextLine();
        
        if (!password.equals(confirmPassword)) {
            System.out.println("两次密码输入不一致，请重新注册！");
            return;
        }

        // 调用数据服务层注册用户
        DataService dataService = new DataService();
        User newUser = new User(0, username, password, "user", name, contact);
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
            System.out.println("3. 搜索商品");
            System.out.println("4. 返回主菜单");
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
                    searchGoods();
                    break;
                case 4:
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
        while (true) {
            System.out.println("\n===== 管理员菜单 =====");
            System.out.println("1. 展示所有用户");
            System.out.println("2. 展示所有商品");
            System.out.println("3. 展示所有订单");
            System.out.println("4. 商品管理");
            System.out.println("5. 返回主菜单");
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
                    manageGoods();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("无效选择，请重新输入！");
            }
        }
    }

    // 商品管理功能
    private void manageGoods() {
        while (true) {
            System.out.println("\n===== 商品管理 =====");
            System.out.println("1. 删除商品");
            System.out.println("2. 修改商品信息");
            System.out.println("3. 查询用户交易记录");
            System.out.println("4. 返回管理员菜单");
            System.out.print("请选择功能: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    deleteGoods();
                    break;
                case 2:
                    updateGoods();
                    break;
                case 3:
                    queryUserTransactionRecords();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("无效选择，请重新输入！");
            }
        }
    }

    // 删除商品
    private void deleteGoods() {
        System.out.println("\n===== 删除商品 =====");
        showAllGoods();
        
        System.out.print("请输入要删除的商品ID: ");
        int goodsId = scanner.nextInt();
        scanner.nextLine();
        
        System.out.print("确认删除商品ID为 " + goodsId + " 的商品吗？(y/n): ");
        String confirm = scanner.nextLine();
        
        if ("y".equalsIgnoreCase(confirm)) {
            boolean success = DataService.deleteGoods(goodsId);
            if (success) {
                System.out.println("商品删除成功！");
            } else {
                System.out.println("商品删除失败，请检查商品ID是否正确！");
            }
        } else {
            System.out.println("取消删除操作。");
        }
    }

    // 修改商品信息
    private void updateGoods() {
        System.out.println("\n===== 修改商品信息 =====");
        showAllGoods();
        
        System.out.print("请输入要修改的商品ID: ");
        int goodsId = scanner.nextInt();
        scanner.nextLine();
        
        Goods goods = DataService.getGoodsById(goodsId);
        if (goods == null) {
            System.out.println("商品不存在！");
            return;
        }
        
        System.out.println("当前商品信息：");
        System.out.printf("名称: %s | 价格: %.2f | 状态: %s | 描述: %s\n",
                goods.getName(), goods.getPrice(), goods.getStatus(), goods.getDescription());
        
        System.out.print("请输入新商品名称（直接回车保持原值）: ");
        String name = scanner.nextLine();
        if (!name.isEmpty()) {
            goods.setName(name);
        }
        
        System.out.print("请输入新商品价格（输入0保持原值）: ");
        double price = scanner.nextDouble();
        scanner.nextLine();
        if (price > 0) {
            goods.setPrice(price);
        }
        
        System.out.print("请输入新商品描述（直接回车保持原值）: ");
        String description = scanner.nextLine();
        if (!description.isEmpty()) {
            goods.setDescription(description);
        }
        
        System.out.print("请输入新商品状态（在售/已售出，直接回车保持原值）: ");
        String status = scanner.nextLine();
        if (!status.isEmpty()) {
            goods.setStatus(status);
        }
        
        boolean success = DataService.updateGoods(goods);
        if (success) {
            System.out.println("商品信息修改成功！");
        } else {
            System.out.println("商品信息修改失败！");
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
            System.out.printf("ID: %d | 姓名: %s | 用户名: %s | 联系方式: %s | 角色: %s\n",
                    u.getId(), u.getName(), u.getUsername(), u.getContact(), u.getRole());
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

    // 搜索商品
    private void searchGoods() {
        System.out.println("\n===== 搜索商品 =====");
        System.out.print("请输入搜索关键词: ");
        String keyword = scanner.nextLine();
        
        if (keyword == null || keyword.trim().isEmpty()) {
            System.out.println("搜索关键词不能为空！");
            return;
        }
        
        List<Goods> searchResults = DataService.searchGoods(keyword);
        if (searchResults.isEmpty()) {
            System.out.println("未找到包含关键词 '" + keyword + "' 的商品！");
            return;
        }
        
        System.out.println("\n===== 搜索结果 =====");
        System.out.println("找到 " + searchResults.size() + " 个相关商品：");
        for (Goods g : searchResults) {
            System.out.printf("ID: %d | 名称: %s | 价格: %.2f | 状态: %s | 描述: %s\n",
                    g.getId(), g.getName(), g.getPrice(), g.getStatus(), g.getDescription());
        }
        
        // 提供查看商品详情的选项
        System.out.print("\n是否查看商品详情？(y/n): ");
        String viewDetail = scanner.nextLine();
        if ("y".equalsIgnoreCase(viewDetail)) {
            viewGoodsDetail(searchResults);
        }
    }
    
    // 查看商品详情
    private void viewGoodsDetail(List<Goods> goodsList) {
        System.out.print("请输入要查看详情的商品ID: ");
        int goodsId = scanner.nextInt();
        scanner.nextLine();
        
        Goods goods = DataService.getGoodsById(goodsId);
        if (goods == null) {
            System.out.println("商品不存在！");
            return;
        }
        
        System.out.println("\n===== 商品详情 =====");
        System.out.printf("商品ID: %d\n", goods.getId());
        System.out.printf("商品名称: %s\n", goods.getName());
        System.out.printf("商品价格: %.2f\n", goods.getPrice());
        System.out.printf("商品状态: %s\n", goods.getStatus());
        System.out.printf("商品描述: %s\n", goods.getDescription());
        System.out.printf("发布时间: %s\n", goods.getCreateTime());
        
        // 如果是已售出商品，显示购买方信息
        if ("已售出".equals(goods.getStatus())) {
            Order order = DataService.getOrderByGoodsId(goodsId);
            if (order != null) {
                User buyer = DataService.getUserById(order.getBuyerId());
                if (buyer != null) {
                    System.out.printf("购买方: %s (ID: %d)\n", buyer.getUsername(), buyer.getId());
                }
            }
        }
    }
    
    // 查询用户交易记录
    private void queryUserTransactionRecords() {
        System.out.println("\n===== 查询用户交易记录 =====");
        showAllUsers();
        
        System.out.print("请输入要查询的用户ID: ");
        int userId = scanner.nextInt();
        scanner.nextLine();
        
        User user = DataService.getUserById(userId);
        if (user == null) {
            System.out.println("用户不存在！");
            return;
        }
        
        System.out.println("\n===== 用户信息 =====");
        System.out.printf("用户ID: %d\n", user.getId());
        System.out.printf("姓名: %s\n", user.getName());
        System.out.printf("用户名: %s\n", user.getUsername());
        System.out.printf("联系方式: %s\n", user.getContact());
        System.out.printf("角色: %s\n", user.getRole());
        
        // 获取用户的交易记录（作为买家和卖家的订单）
        List<Order> transactionRecords = DataService.getUserTransactionRecords(userId);
        if (transactionRecords.isEmpty()) {
            System.out.println("\n该用户暂无交易记录！");
            return;
        }
        
        System.out.println("\n===== 交易记录 =====");
        System.out.println("共找到 " + transactionRecords.size() + " 条交易记录：");
        
        for (Order order : transactionRecords) {
            Goods goods = DataService.getGoodsById(order.getGoodsId());
            User buyer = DataService.getUserById(order.getBuyerId());
            User seller = DataService.getUserById(order.getSellerId());
            
            String transactionType = (order.getBuyerId() == userId) ? "购买" : "出售";
            String counterparty = (order.getBuyerId() == userId) ? 
                (seller != null ? seller.getName() : "未知卖家") : 
                (buyer != null ? buyer.getName() : "未知买家");
            
            System.out.println("\n--- 交易记录 ---");
            System.out.printf("订单ID: %d\n", order.getId());
            System.out.printf("交易类型: %s\n", transactionType);
            System.out.printf("交易对方: %s\n", counterparty);
            System.out.printf("商品名称: %s\n", goods != null ? goods.getName() : "未知商品");
            System.out.printf("交易金额: %.2f\n", order.getPrice());
            System.out.printf("交易状态: %s\n", order.getStatus());
            System.out.printf("交易时间: %s\n", order.getCreateTime());
        }
        
        // 统计交易信息
        double totalBuyAmount = 0;
        double totalSellAmount = 0;
        int buyCount = 0;
        int sellCount = 0;
        
        for (Order order : transactionRecords) {
            if (order.getBuyerId() == userId) {
                totalBuyAmount += order.getPrice();
                buyCount++;
            } else {
                totalSellAmount += order.getPrice();
                sellCount++;
            }
        }
        
        System.out.println("\n===== 交易统计 =====");
        System.out.printf("购买次数: %d 次\n", buyCount);
        System.out.printf("出售次数: %d 次\n", sellCount);
        System.out.printf("总购买金额: %.2f\n", totalBuyAmount);
        System.out.printf("总出售金额: %.2f\n", totalSellAmount);
        System.out.printf("交易总额: %.2f\n", totalBuyAmount + totalSellAmount);
    }
}