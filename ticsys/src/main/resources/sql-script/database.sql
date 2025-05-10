ALTER DATABASE [TicSys] SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
GO
ALTER DATABASE [TicSys] SET MULTI_USER;
GO
Drop database TicSys;

CREATE DATABASE TicSys;
GO

USE TicSys;
GO


CREATE TABLE Role (
    name NVARCHAR(50) PRIMARY KEY
);

CREATE TABLE Users (
	userName NVARCHAR(50) NOT NULL primary key,
    email NVARCHAR(255) NOT NULL unique,
    fullName NVARCHAR(100),
    passWord NVARCHAR(255) NOT NULL,
    phoneNumber NVARCHAR(15),
    birthday DATE,
    gender NVARCHAR(10),
    avatarPath NVARCHAR(255)
);

CREATE TABLE RoleOfUser (
    ID INT PRIMARY KEY IDENTITY(1,1),
    roleName NVARCHAR(50) NOT NULL,
    userId NVARCHAR(50) NOT NULL
);

CREATE TABLE Organizer_infor (
    ID INT PRIMARY KEY IDENTITY(1,1),
    name NVARCHAR(100),
    description NVARCHAR(500),
    userId nvarchar(50) NOT NULL
);

CREATE TABLE Category (
    name NVARCHAR(50) PRIMARY KEY
);

CREATE TABLE Event (
    ID INT PRIMARY KEY IDENTITY(1,1),
    organizerId nvarchar(50) NOT NULL,
    location NVARCHAR(255),
    description NVARCHAR(max),
    bannerPath NVARCHAR(255),
    seatMapPath NVARCHAR(255),
    name NVARCHAR(100),
    status NVARCHAR(50),
    category NVARCHAR(50),
    date DATE,
    time TIME
);

CREATE TABLE Ticket (
    ID INT IDENTITY(1,1) PRIMARY KEY,
    eventId INT NOT NULL,
    name NVARCHAR(100),
    service NVARCHAR(255),
    price INT NOT NULL,
    quantity INT NOT NULL,
    minQtyInOrder INT default 1,
    maxQtyInOrder INT default 5
);

CREATE TABLE PromotionType (
    name NVARCHAR(50) PRIMARY KEY
);

CREATE TABLE Promotion (
    ID INT PRIMARY KEY IDENTITY(1,1),
    eventId INT NOT NULL,
    MinPriceToReach INT NOT NULL,
    promoPercent INT,
    voucherValue INT,
    status NVARCHAR(50),
    type NVARCHAR(50),
    startDate DATE,
    endDate DATE
);
CREATE TABLE VoucherOfUser (
	id int primary key identity(1,1),
	voucherValue int,
	userId nvarchar(50),
	promotionId int,
)
CREATE TABLE [Order] (
    ID INT PRIMARY KEY IDENTITY(1,1),
    price INT NOT NULL,
    createdBy nvarchar(50) NOT NULL,
    eventId INT NOT NULL,
    DateCreatedAt DATE,
    TimeCreatedAt TIME,
    status NVARCHAR(50),
    promotionId INT
);

CREATE TABLE TicketOfOrder (
    ID INT PRIMARY KEY IDENTITY(1,1),
    ticketId INT NOT NULL,
    orderId INT NOT NULL,
    quantity INT NOT NULL
);

CREATE TABLE Notification (
    ID INT PRIMARY KEY IDENTITY(1,1),
    receiverId nvarchar(50) NOT NULL,
    eventId INT NOT NULL,
    seen BIT
);

CREATE TABLE Comment (
    ID INT PRIMARY KEY IDENTITY(1,1),
    content NVARCHAR(1000),
    senderId nvarchar(50) NOT NULL,
    eventId INT NOT NULL,
    parentId INT NULL,
    dateCreatedAt DATE default '2025-02-25',
    timeCreatedAt TIME default '16:00:00'
);
CREATE TABLE PaymentMethod(
	ID INT PRIMARY KEY IDENTITY(1,1),
	userId nvarchar(50),
	bankAccountNumber nvarchar(15),
	bankName nvarchar(20)
);

alter TABLE RoleOfUser add FOREIGN KEY (userId) REFERENCES Users(userName);
alter TABLE RoleOfUser add FOREIGN KEY (roleName) REFERENCES Role(name);
alter TABLE Organizer_infor add FOREIGN KEY (userId) REFERENCES Users(userName);
alter TABLE [Event] add FOREIGN KEY (organizerId) REFERENCES Users(userName);
alter TABLE [Event] add FOREIGN KEY (category) REFERENCES Category(name);
alter TABLE Ticket add FOREIGN KEY (eventId) REFERENCES Event(ID);
alter TABLE Promotion add FOREIGN KEY (eventId) REFERENCES Event(ID);
alter TABLE Promotion add FOREIGN KEY (type) REFERENCES PromotionType(name);
alter TABLE [Order] add FOREIGN KEY (createdBy) REFERENCES Users(userName);
alter TABLE [Order] add FOREIGN KEY (eventId) REFERENCES Event(ID);
alter TABLE [Order] add FOREIGN KEY (promotionId) REFERENCES Promotion(ID);
alter TABLE TicketOfOrder add FOREIGN KEY (ticketId) REFERENCES Ticket(ID);
alter TABLE TicketOfOrder add FOREIGN KEY (orderId) REFERENCES [Order](ID);
alter TABLE [Notification] add FOREIGN KEY (receiverId) REFERENCES Users(userName);
alter TABLE [Notification] add FOREIGN KEY (eventId) REFERENCES Event(ID);
alter TABLE Comment add FOREIGN KEY (senderId) REFERENCES Users(userName);
alter TABLE Comment add FOREIGN KEY (eventId) REFERENCES Event(ID);
alter TABLE Comment add FOREIGN KEY (parentId) REFERENCES Comment(ID);
alter TABLE VoucherOfUser add FOREIGN KEY (userId) REFERENCES Users(userName);
alter TABLE VoucherOfUser add FOREIGN KEY (promotionId) REFERENCES Promotion(id);
alter TABLE PaymentMethod add FOREIGN KEY (userId) REFERENCES Users(userName);