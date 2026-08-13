package akhilanmart.util;

import akhilanmart.config.DatabaseConfig;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseInitializer {
    private static final Logger LOGGER = Logger.getLogger(DatabaseInitializer.class.getName());

    public static void initialize() {
        LOGGER.info("Starting Akhilan Mart database initialization...");
        try (Connection conn = DatabaseConfig.getConnection()) {
            executeSchemaScript(conn);
            seedDemoData(conn);
            LOGGER.info("Akhilan Mart database initialization completed successfully.");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error initializing database", e);
            throw new RuntimeException("Database initialization failed", e);
        }
    }

    private static void executeSchemaScript(Connection conn) throws Exception {
        InputStream is = DatabaseInitializer.class.getClassLoader().getResourceAsStream("schema.sql");
        if (is == null) {
            LOGGER.warning("schema.sql not found on classpath!");
            return;
        }

        StringBuilder sqlBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().startsWith("--") || line.trim().isEmpty()) {
                    continue;
                }
                sqlBuilder.append(line).append("\n");
            }
        }

        String[] statements = sqlBuilder.toString().split(";");
        try (Statement stmt = conn.createStatement()) {
            for (String sql : statements) {
                if (!sql.trim().isEmpty()) {
                    stmt.execute(sql.trim());
                }
            }
        }
        LOGGER.info("Executed database schema script.");
    }

    private static void seedDemoData(Connection conn) throws Exception {
        // Check if users exist
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM users")) {
            if (rs.next() && rs.getInt(1) > 0) {
                LOGGER.info("Database already contains user data. Skipping demo seed.");
                return;
            }
        }

        LOGGER.info("Seeding initial demo data for Akhilan Mart...");

        // Passwords hashed with BCrypt
        String adminHash = PasswordUtil.hashPassword("Admin@123");
        String buyerHash = PasswordUtil.hashPassword("Buyer@123");
        String sellerHash = PasswordUtil.hashPassword("Seller@123");

        String insertUserSql = "INSERT INTO users (name, email, password_hash, role) VALUES (?, ?, ?, ?)";
        
        long adminId, buyerId, sellerId, buyer2Id, seller2Id;

        // Insert Admin
        try (PreparedStatement pstmt = conn.prepareStatement(insertUserSql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, "System Administrator");
            pstmt.setString(2, "admin@akhilanmart.com");
            pstmt.setString(3, adminHash);
            pstmt.setString(4, "ADMIN");
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            rs.next();
            adminId = rs.getLong(1);
        }

        // Insert Primary Buyer
        try (PreparedStatement pstmt = conn.prepareStatement(insertUserSql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, "Akhilan Standard Buyer");
            pstmt.setString(2, "buyer@akhilanmart.com");
            pstmt.setString(3, buyerHash);
            pstmt.setString(4, "BUYER");
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            rs.next();
            buyerId = rs.getLong(1);
        }

        // Insert Primary Seller
        try (PreparedStatement pstmt = conn.prepareStatement(insertUserSql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, "TechZone & Apparel Store");
            pstmt.setString(2, "seller@akhilanmart.com");
            pstmt.setString(3, sellerHash);
            pstmt.setString(4, "SELLER");
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            rs.next();
            sellerId = rs.getLong(1);
        }

        // Insert Buyer 2
        try (PreparedStatement pstmt = conn.prepareStatement(insertUserSql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, "Priya Sharma");
            pstmt.setString(2, "buyer2@akhilanmart.com");
            pstmt.setString(3, buyerHash);
            pstmt.setString(4, "BUYER");
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            rs.next();
            buyer2Id = rs.getLong(1);
        }

        // Insert Seller 2
        try (PreparedStatement pstmt = conn.prepareStatement(insertUserSql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, "Crafts & Living Sellers");
            pstmt.setString(2, "seller2@akhilanmart.com");
            pstmt.setString(3, sellerHash);
            pstmt.setString(4, "SELLER");
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            rs.next();
            seller2Id = rs.getLong(1);
        }

        // Insert Products
        String insertProductSql = "INSERT INTO products (seller_id, name, description, price, stock, category, image_url) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        Object[][] productsData = {
            // Electronics (Seller 1)
            {sellerId, "Wireless Bluetooth Headphones", "Active noise cancelling over-ear headphones with 40h battery life and deep bass response.", 3499.00, 25, "Electronics", "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=600&auto=format&fit=crop&q=80"},
            {sellerId, "Mechanical Gaming Keyboard", "RGB backlit tactile mechanical keyboard with hot-swappable switches and durable aluminum frame.", 2999.00, 18, "Electronics", "https://images.unsplash.com/photo-1587829741301-dc798b83add3?w=600&auto=format&fit=crop&q=80"},
            {sellerId, "USB-C Fast Charger 65W", "GaN technology ultra-compact dual port fast wall charger for laptops and smartphones.", 1299.00, 50, "Electronics", "https://images.unsplash.com/photo-1583863788434-e58a36330cf0?w=600&auto=format&fit=crop&q=80"},
            {sellerId, "Portable Bluetooth Speaker", "IPX7 waterproof wireless speaker with crisp stereo audio and 12-hour playtime.", 1899.00, 30, "Electronics", "https://images.unsplash.com/photo-1608043152269-423dbba4e7e1?w=600&auto=format&fit=crop&q=80"},

            // Books (Seller 2)
            {seller2Id, "Clean Code: A Handbook of Agile Software Craftsmanship", "Essential software engineering guide for writing readable, maintainable, and robust code by Robert C. Martin.", 799.00, 40, "Books", "https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c?w=600&auto=format&fit=crop&q=80"},
            {seller2Id, "Java Programming Fundamentals", "Comprehensive guide to modern Java 17+, object-oriented principles, design patterns, and multithreading.", 650.00, 35, "Books", "https://images.unsplash.com/photo-1532012197267-da84d127e765?w=600&auto=format&fit=crop&q=80"},
            {seller2Id, "Database Management Systems", "In-depth reference covering relational database design, SQL optimization, transactions, and indexing.", 890.00, 20, "Books", "https://images.unsplash.com/photo-1512820790803-83ca734da794?w=600&auto=format&fit=crop&q=80"},
            {seller2Id, "Computer Networks Essentials", "A top-down approach covering TCP/IP protocol stack, network security, and modern routing architectures.", 749.00, 15, "Books", "https://images.unsplash.com/photo-1524995997946-a1c2e315a42f?w=600&auto=format&fit=crop&q=80"},

            // Clothing (Seller 1)
            {sellerId, "Classic Cotton T-Shirt", "100% combed organic cotton breathable t-shirt with premium pre-shrunk finish.", 499.00, 60, "Clothing", "https://images.unsplash.com/photo-1521572267360-ee0c2909d518?w=600&auto=format&fit=crop&q=80"},
            {sellerId, "Casual Fleece Hoodie", "Warm, ultra-soft fleece pullover hoodie with front kangaroo pocket and adjustable drawstrings.", 1499.00, 25, "Clothing", "https://images.unsplash.com/photo-1556905055-8f358a7a47b2?w=600&auto=format&fit=crop&q=80"},
            {sellerId, "Denim Jacket Classic Fit", "Timeless rugged denim jacket with button flap chest pockets and reinforced stitching.", 2299.00, 15, "Clothing", "https://images.unsplash.com/photo-1576995853123-5a10305d93c0?w=600&auto=format&fit=crop&q=80"},
            {sellerId, "Sports Track Pants", "Moisture-wicking stretchable athletic pants with zippered side pockets.", 999.00, 45, "Clothing", "https://images.unsplash.com/photo-1515886657613-9f3515b0c78f?w=600&auto=format&fit=crop&q=80"},

            // Accessories (Seller 2)
            {seller2Id, "Laptop Backpack 15.6 Inch", "Water-resistant commuter backpack with padded laptop sleeve and hidden anti-theft pocket.", 1599.00, 30, "Accessories", "https://images.unsplash.com/photo-1553062407-98eeb64c6a62?w=600&auto=format&fit=crop&q=80"},
            {seller2Id, "Stainless Steel Water Bottle 1L", "Double-wall vacuum insulated flask keeping drinks cold for 24h or hot for 12h.", 699.00, 50, "Accessories", "https://images.unsplash.com/photo-1602143407151-7111542de6e8?w=600&auto=format&fit=crop&q=80"},
            {seller2Id, "Minimalist Leather Wallet", "Slim RFID-blocking genuine leather wallet with quick card ejector mechanism.", 899.00, 40, "Accessories", "https://images.unsplash.com/photo-1627123424574-724758594e93?w=600&auto=format&fit=crop&q=80"},
            {seller2Id, "Analog Stainless Wrist Watch", "Sleek quartz analog watch with scratch-resistant mineral glass and 3ATM water resistance.", 2499.00, 20, "Accessories", "https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=600&auto=format&fit=crop&q=80"},

            // Home (Seller 2)
            {seller2Id, "LED Eye-Care Desk Lamp", "Touch-controlled dimmable LED table lamp with 5 color temperatures and USB charging port.", 1199.00, 35, "Home", "https://images.unsplash.com/photo-1534073828943-f801091bb18c?w=600&auto=format&fit=crop&q=80"},
            {seller2Id, "Ceramic Coffee Mug 400ml", "Artisanal ceramic matte-finished coffee mug with comfortable heat-resistant handle.", 399.00, 60, "Home", "https://images.unsplash.com/photo-1514432324607-a09d9b4aefdd?w=600&auto=format&fit=crop&q=80"},
            {seller2Id, "Desktop Storage Organizer", "Multi-compartment wooden desk organizer for stationary, notes, and accessories.", 799.00, 25, "Home", "https://images.unsplash.com/photo-1584100936595-c0654b55a2e2?w=600&auto=format&fit=crop&q=80"},
            {seller2Id, "Cotton Cushion Set of 2", "Soft velvet decorative throw pillow covers with premium microfiber inserts.", 899.00, 30, "Home", "https://images.unsplash.com/photo-1584100936595-c0654b55a2e2?w=600&auto=format&fit=crop&q=80"}
        };

        long headPhonesId = 0, cleanCodeBookId = 0, laptopBagId = 0;

        for (int i = 0; i < productsData.length; i++) {
            Object[] row = productsData[i];
            try (PreparedStatement pstmt = conn.prepareStatement(insertProductSql, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setLong(1, (Long) row[0]);
                pstmt.setString(2, (String) row[1]);
                pstmt.setString(3, (String) row[2]);
                pstmt.setBigDecimal(4, java.math.BigDecimal.valueOf((Double) row[3]));
                pstmt.setInt(5, (Integer) row[4]);
                pstmt.setString(6, (String) row[5]);
                pstmt.setString(7, (String) row[6]);
                pstmt.executeUpdate();
                ResultSet rs = pstmt.getGeneratedKeys();
                rs.next();
                long prodId = rs.getLong(1);
                if (i == 0) headPhonesId = prodId;
                if (i == 4) cleanCodeBookId = prodId;
                if (i == 12) laptopBagId = prodId;
            }
        }

        // Seed initial Cart Items for buyer
        if (headPhonesId > 0 && cleanCodeBookId > 0) {
            String insertCartSql = "INSERT INTO cart_items (user_id, product_id, quantity) VALUES (?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(insertCartSql)) {
                pstmt.setLong(1, buyerId);
                pstmt.setLong(2, headPhonesId);
                pstmt.setInt(3, 1);
                pstmt.executeUpdate();

                pstmt.setLong(1, buyerId);
                pstmt.setLong(2, cleanCodeBookId);
                pstmt.setInt(3, 2);
                pstmt.executeUpdate();
            }
        }

        // Seed initial Orders and Order Items
        if (headPhonesId > 0 && laptopBagId > 0) {
            String insertOrderSql = "INSERT INTO orders (buyer_id, total_amount, status) VALUES (?, ?, ?)";
            long orderId1, orderId2;

            try (PreparedStatement pstmt = conn.prepareStatement(insertOrderSql, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setLong(1, buyerId);
                pstmt.setBigDecimal(2, java.math.BigDecimal.valueOf(4298.00));
                pstmt.setString(3, "DELIVERED");
                pstmt.executeUpdate();
                ResultSet rs = pstmt.getGeneratedKeys();
                rs.next();
                orderId1 = rs.getLong(1);

                pstmt.setLong(1, buyer2Id);
                pstmt.setBigDecimal(2, java.math.BigDecimal.valueOf(1599.00));
                pstmt.setString(3, "SHIPPED");
                pstmt.executeUpdate();
                rs = pstmt.getGeneratedKeys();
                rs.next();
                orderId2 = rs.getLong(1);
            }

            String insertOrderItemSql = "INSERT INTO order_items (order_id, product_id, quantity, unit_price) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(insertOrderItemSql)) {
                // Order 1 items
                pstmt.setLong(1, orderId1);
                pstmt.setLong(2, headPhonesId);
                pstmt.setInt(3, 1);
                pstmt.setBigDecimal(4, java.math.BigDecimal.valueOf(3499.00));
                pstmt.executeUpdate();

                pstmt.setLong(1, orderId1);
                pstmt.setLong(2, cleanCodeBookId);
                pstmt.setInt(3, 1);
                pstmt.setBigDecimal(4, java.math.BigDecimal.valueOf(799.00));
                pstmt.executeUpdate();

                // Order 2 item
                pstmt.setLong(1, orderId2);
                pstmt.setLong(2, laptopBagId);
                pstmt.setInt(3, 1);
                pstmt.setBigDecimal(4, java.math.BigDecimal.valueOf(1599.00));
                pstmt.executeUpdate();
            }

            // Seed Reviews for Delivered products
            String insertReviewSql = "INSERT INTO reviews (user_id, product_id, rating, comment) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(insertReviewSql)) {
                pstmt.setLong(1, buyerId);
                pstmt.setLong(2, headPhonesId);
                pstmt.setInt(3, 5);
                pstmt.setString(4, "Fantastic audio clarity and superb ANC! Battery lasts all week for my study sessions.");
                pstmt.executeUpdate();

                pstmt.setLong(1, buyerId);
                pstmt.setLong(2, cleanCodeBookId);
                pstmt.setInt(3, 5);
                pstmt.setString(4, "A must-read book for every computer science capstone student!");
                pstmt.executeUpdate();
            }
        }

        LOGGER.info("Seeding completed successfully.");
    }
}
