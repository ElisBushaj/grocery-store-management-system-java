create table Store (
    id int auto_increment primary key, name varchar(50) not null, pointsPerEuro decimal(10, 2) not null
);

create table Category (
    id int auto_increment primary key, name varchar(100) not null, storeId int not null, constraint Category_ibfk_1 foreign key (storeId) references Store (id)
);

create index storeId on Category (storeId);

create table Customer (
    id int auto_increment primary key, name varchar(50) not null, lastname varchar(50) not null, phoneNumber varchar(20) null, gender enum('male', 'female', 'other') null, points int default 0 null, storeId int not null, constraint Customer_ibfk_1 foreign key (storeId) references Store (id)
);

create index storeId on Customer (storeId);

create table Product (
    id int auto_increment primary key, categoryId int not null, name varchar(100) not null, description text null, price decimal(10, 2) not null, stock int not null, supplierInfo varchar(255) null, storeId int not null, constraint Product_ibfk_1 foreign key (storeId) references Store (id), constraint Product_ibfk_2 foreign key (categoryId) references Category (id)
);

create table Discount (
    id int auto_increment primary key, productId int not null, percentage decimal(5, 2) not null, storeId int not null, constraint productId unique (productId), constraint Discount__fk foreign key (productId) references Product (id), constraint Discount_ibfk_1 foreign key (storeId) references Store (id)
);

create index storeId on Discount (storeId);

create index categoryId on Product (categoryId);

create index storeId on Product (storeId);

create table ProductPoints (
    id int auto_increment primary key, productId int not null, priceInPoints int not null, storeId int not null, constraint productId unique (productId), constraint ProductPoints__fk foreign key (productId) references Product (id), constraint ProductPoints_ibfk_1 foreign key (storeId) references Store (id)
);

create index storeId on ProductPoints (storeId);

create table User (
    id int auto_increment primary key, name varchar(50) not null, lastname varchar(50) not null, email varchar(100) not null, password varchar(100) not null, role enum('admin', 'cashier') not null, storeId int not null, constraint email unique (email), constraint User_ibfk_1 foreign key (storeId) references Store (id)
);

create table Receipt (
    id int auto_increment primary key, customerId int null, userId int not null, date date not null, price decimal(10, 2) not null, storeId int not null, constraint Receipt_ibfk_1 foreign key (storeId) references Store (id), constraint Receipt_ibfk_2 foreign key (customerId) references Customer (id), constraint Receipt_ibfk_3 foreign key (userId) references User (id)
);

create index customerId on Receipt (customerId);

create index storeId on Receipt (storeId);

create index userId on Receipt (userId);

create table SoldProduct (
    id int auto_increment primary key, productId int not null, receiptId int not null, price decimal(10, 2) not null, paidMoney decimal(10, 2) not null, paidPoints int not null, discount decimal(5, 2) null, storeId int not null, constraint SoldProduct_ibfk_1 foreign key (storeId) references Store (id), constraint SoldProduct_ibfk_2 foreign key (productId) references Product (id), constraint SoldProduct_ibfk_3 foreign key (receiptId) references Receipt (id)
);

create index productId on SoldProduct (productId);

create index receiptId on SoldProduct (receiptId);

create index storeId on SoldProduct (storeId);

create index storeId on User (storeId);