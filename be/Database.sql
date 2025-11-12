CREATE TABLE roles (
    id INT IDENTITY PRIMARY KEY,
    name VARCHAR(100),
    description VARCHAR(255)
);

CREATE TABLE users (
    id UNIQUEIDENTIFIER PRIMARY KEY DEFAULT NEWID(),
    full_name NVARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role_id INT FOREIGN KEY REFERENCES roles(id),
    phone VARCHAR(20) UNIQUE,
    status VARCHAR(50),
    created_at DATETIME DEFAULT GETDATE(),
    updated_at DATETIME DEFAULT GETDATE()
);

CREATE TABLE user_addresses (
    id UNIQUEIDENTIFIER PRIMARY KEY DEFAULT NEWID(),
    user_id UNIQUEIDENTIFIER FOREIGN KEY REFERENCES users(id),
    full_name NVARCHAR(100),
    phone VARCHAR(20),
    province NVARCHAR(100),
    district NVARCHAR(100),
    ward NVARCHAR(100),
    address_detail NVARCHAR(255),
    is_default BIT,
    created_at DATETIME DEFAULT GETDATE(),
    updated_at DATETIME DEFAULT GETDATE()
);

CREATE TABLE categories (
    id UNIQUEIDENTIFIER PRIMARY KEY DEFAULT NEWID(),
    name NVARCHAR(100),
    description NVARCHAR(255),
    parent_id UNIQUEIDENTIFIER NULL FOREIGN KEY REFERENCES categories(id)
);

CREATE TABLE products (
    id UNIQUEIDENTIFIER PRIMARY KEY DEFAULT NEWID(),
    category_id UNIQUEIDENTIFIER FOREIGN KEY REFERENCES categories(id),
    name NVARCHAR(255),
    description NVARCHAR(MAX),
    sold INT DEFAULT 0,
    created_at DATETIME DEFAULT GETDATE(),
    updated_at DATETIME DEFAULT GETDATE()
);



CREATE TABLE product_variants (
    id UNIQUEIDENTIFIER PRIMARY KEY DEFAULT NEWID(),
    product_id UNIQUEIDENTIFIER FOREIGN KEY REFERENCES products(id),
    sku VARCHAR(100),
    price DECIMAL(12,2),
    stock INT,
    thumbnail_url NVARCHAR(MAX),
    created_at DATETIME DEFAULT GETDATE(),
    updated_at DATETIME DEFAULT GETDATE()
);

CREATE TABLE variant_images (
    id UNIQUEIDENTIFIER PRIMARY KEY DEFAULT NEWID(),
    variant_id UNIQUEIDENTIFIER FOREIGN KEY REFERENCES product_variants(id),
    image_url NVARCHAR(MAX)
);

CREATE TABLE attributes (
    id UNIQUEIDENTIFIER PRIMARY KEY DEFAULT NEWID(),
    name NVARCHAR(100),          -- Tên kỹ thuật, ví dụ: "color"
);

CREATE TABLE variant_attributes (
    id UNIQUEIDENTIFIER PRIMARY KEY DEFAULT NEWID(),
    variant_id UNIQUEIDENTIFIER FOREIGN KEY REFERENCES product_variants(id),
    attribute_id UNIQUEIDENTIFIER FOREIGN KEY REFERENCES attributes(id),
    attribute_value NVARCHAR(100)  -- Ví dụ: "Đỏ", "Size M"
);


CREATE TABLE suppliers (
    id UNIQUEIDENTIFIER PRIMARY KEY DEFAULT NEWID(),
    name NVARCHAR(255) NOT NULL,
    email VARCHAR(100),
    phone VARCHAR(20),
    address NVARCHAR(255),
    contact_person NVARCHAR(100),
    note NVARCHAR(255),
    created_at DATETIME DEFAULT GETDATE(),
    updated_at DATETIME DEFAULT GETDATE()
);


CREATE TABLE warehouses (
    id UNIQUEIDENTIFIER PRIMARY KEY DEFAULT NEWID(),
    name NVARCHAR(100) NOT NULL,
    location NVARCHAR(255),
    manager_id UNIQUEIDENTIFIER NULL FOREIGN KEY REFERENCES users(id),
    created_at DATETIME DEFAULT GETDATE()
);


CREATE TABLE purchase_orders (
    id UNIQUEIDENTIFIER PRIMARY KEY DEFAULT NEWID(),
    supplier_id UNIQUEIDENTIFIER NOT NULL FOREIGN KEY REFERENCES suppliers(id),
    total_cost DECIMAL(18,2) DEFAULT 0,
    status NVARCHAR(50) DEFAULT 'pending',
    created_by UNIQUEIDENTIFIER FOREIGN KEY REFERENCES users(id),
    created_at DATETIME DEFAULT GETDATE()
);

CREATE TABLE purchase_order_details (
    id UNIQUEIDENTIFIER PRIMARY KEY DEFAULT NEWID(),
    purchase_order_id UNIQUEIDENTIFIER NOT NULL 
        FOREIGN KEY REFERENCES purchase_orders(id),
    variant_id UNIQUEIDENTIFIER NOT NULL 
        FOREIGN KEY REFERENCES product_variants(id),
    quantity INT NOT NULL,
    unit_cost DECIMAL(18,2) NOT NULL, 
    subtotal AS (quantity * unit_cost) PERSISTED 
);


CREATE TABLE inventory (
    id UNIQUEIDENTIFIER PRIMARY KEY DEFAULT NEWID(),
    warehouse_id UNIQUEIDENTIFIER NOT NULL FOREIGN KEY REFERENCES warehouses(id),
    variant_id UNIQUEIDENTIFIER NOT NULL FOREIGN KEY REFERENCES product_variants(id),
    quantity INT DEFAULT 0,
    last_updated DATETIME DEFAULT GETDATE(),
    CONSTRAINT uq_inventory UNIQUE (warehouse_id, variant_id)
);

CREATE TABLE inventories (
    id UNIQUEIDENTIFIER PRIMARY KEY DEFAULT NEWID(),
    variant_id UNIQUEIDENTIFIER FOREIGN KEY REFERENCES product_variants(id),
    stock_in INT DEFAULT 0,
    stock_out INT DEFAULT 0,
    current_stock AS (stock_in - stock_out) PERSISTED,
    last_updated DATETIME DEFAULT GETDATE()
);


CREATE TABLE carts (
    id UNIQUEIDENTIFIER PRIMARY KEY DEFAULT NEWID(),
    user_id UNIQUEIDENTIFIER FOREIGN KEY REFERENCES users(id),
    created_at DATETIME DEFAULT GETDATE(),
    updated_at DATETIME DEFAULT GETDATE()
);

CREATE TABLE cart_details (
    id UNIQUEIDENTIFIER PRIMARY KEY DEFAULT NEWID(),
    cart_id UNIQUEIDENTIFIER FOREIGN KEY REFERENCES carts(id),
    variant_id UNIQUEIDENTIFIER FOREIGN KEY REFERENCES product_variants(id),
    quantity INT,
    price DECIMAL(12,2),
    voucher_id UNIQUEIDENTIFIER NULL FOREIGN KEY REFERENCES vouchers(id)
);

CREATE TABLE orders (
    id UNIQUEIDENTIFIER PRIMARY KEY DEFAULT NEWID(),
    user_id UNIQUEIDENTIFIER FOREIGN KEY REFERENCES users(id),
    address_id UNIQUEIDENTIFIER FOREIGN KEY REFERENCES user_addresses(id),
    total_price DECIMAL(12,2),
    payment_method VARCHAR(50),
    payment_status VARCHAR(50),
    shipping_status VARCHAR(50),
    shipping_provider VARCHAR(100),
    tracking_code VARCHAR(100),
    order_status VARCHAR(50),
    created_at DATETIME DEFAULT GETDATE(),
    updated_at DATETIME DEFAULT GETDATE()
);

CREATE TABLE order_details (
    id UNIQUEIDENTIFIER PRIMARY KEY DEFAULT NEWID(),
    order_id UNIQUEIDENTIFIER NOT NULL CONSTRAINT FK_orderdetails_order REFERENCES orders(id),
    variant_id UNIQUEIDENTIFIER NULL CONSTRAINT FK_orderdetails_variant REFERENCES product_variants(id),
    quantity INT NOT NULL,
    price DECIMAL(18,2) NOT NULL,         
   
);

CREATE TABLE user_tokens (
    id UNIQUEIDENTIFIER PRIMARY KEY DEFAULT NEWID(),
    user_id UNIQUEIDENTIFIER NOT NULL FOREIGN KEY REFERENCES users(id),
    token NVARCHAR(MAX) NOT NULL,
    token_type VARCHAR(50) NOT NULL,       
    expires_at DATETIME,
    created_at DATETIME DEFAULT GETDATE(),
    is_revoked BIT DEFAULT 0
);
