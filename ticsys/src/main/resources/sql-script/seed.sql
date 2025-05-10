insert into Role (name) values ('USER');
insert into Role (name) values ('ADMIN');
insert into Role (name) values ('ORGANIZER');
insert into Category values ('Sport');
insert into Category values ('Music');
insert into Category values ('Other');
insert into Category values ('Comedy');
insert into PromotionType(name) values ('Flash'), ('Combo'), ('Voucher');

insert into Users (userName, email, fullName, passWord, phoneNumber, birthday,gender) values ('admin', 'admin@gmail.com', 'Nguyen Van An', 'admin', '0123456789', '2000-01-01','Male')
insert into RoleOfUser(roleName,userId) values ('ADMIN','admin')
insert into RoleOfUser(roleName,userId) values ('USER','admin')
insert into RoleOfUser(roleName,userId) values ('ORGANIZER','admin')
update Users set passWord = '$2a$12$wRLx0rjeVNcI.Tt9CzmVPue72ER0Z3sHtYpEee/ECJ72GF0xZOSJa' where userName = 'admin';


insert into Users (userName, email, fullName, passWord, phoneNumber, birthday,gender) values ('admin2', 'admin2@gmail.com', 'Nguyen Van Binh', '$2a$12$M96m7iAcm/GAKKr631r1JulXIiGkF54HQZMIvOXT33snBRqTUUwme', '0123456789', '2000-01-01','Male')
insert into RoleOfUser(roleName,userId) values ('ADMIN','admin')
insert into RoleOfUser(roleName,userId) values ('USER','admin')
insert into RoleOfUser(roleName,userId) values ('ORGANIZER','admin')

insert into PaymentMethod(userId, bankAccountNumber, bankName) VALUES('pqkiet854','012345678912345','tsbank');

select * from Category;
select * from users;
select * from role;
select * from RoleOfUser;
select * from [event];
select * from Ticket;
select * from TicketOfOrder;
select * from [Order];
select * from Comment;
select * from PromotionType;
select * from Promotion;
select * from VoucherOfUser;
select * from Organizer_infor;

alter table event alter column description nvarchar(max);

alter table ticket drop constraint [FK__Ticket__type__571DF1D5];
drop table TicketType;
alter table ticket drop column type;
alter table ticket add minQtyInOrder int;
alter table ticket add maxQtyInOrder int;



alter table [order] drop column quantity;
alter table [order] add voucherOfUserId int default 0;

alter table comment add dateCreatedAt date default '2025-02-18';
alter table comment add timeCreatedAt time default '16:00:00';

 INSERT INTO [comment] (content, senderId, eventId, parentId,dateCreatedAt, timeCreatedAt)
values ('Is this event interesting?', 'pqkiet854', 18, null, GETDATE(), GETDATE());


update PromotionType set name = 'Combo Sale' where name = 'Combo';
update PromotionType set name = 'Flash Sale' where name = 'Flash';
update PromotionType set name = 'Voucher Gift' where name = 'Voucher';

alter table voucherOfUser add status nvarchar(20);

update Promotion set status = 'active', endDate = '2025-03-07' where id = 3;

-- Count orders which apply selected promotion
select p.*, count(o.id) as orderCount
from Promotion p join [Order] o on p.ID = o.promotionId
group by p.ID,p.eventId,p.endDate,p.MinPriceToReach,p.promoPercent,p.startDate,p.status,p.type,p.voucherValue;



-- Count revenue of event by date
DECLARE @startDate DATE = '2025-02-05';
DECLARE @endDate DATE = '2025-03-06';

WITH date_series AS (
    SELECT DATEADD(DAY, number, @startDate) AS ngay
    FROM master.dbo.spt_values
    WHERE type = 'P'
    AND DATEADD(DAY, number, @startDate) <= @endDate
)
SELECT 
    ds.ngay as label,
    ISNULL(SUM(o.price), 0) AS revenue
FROM 
    date_series ds
LEFT JOIN 
    [Order] o ON CAST(o.DateCreatedAt AS DATE) = ds.ngay and o.eventId = 2
GROUP BY 
    ds.ngay
ORDER BY 
    ds.ngay DESC;

-- Count ticket amount of event by date
DECLARE @startDate2 DATE = '2025-02-05';
DECLARE @endDate2 DATE = '2025-03-06';

WITH date_series AS (
    SELECT DATEADD(DAY, number, @startDate2) AS ngay
    FROM master.dbo.spt_values
    WHERE type = 'P'
    AND DATEADD(DAY, number, @startDate2) <= @endDate2
)
SELECT 
    ds.ngay as label,
    ISNULL(SUM(too.quantity), 0) AS ticket_amount
FROM 
    date_series ds
LEFT JOIN 
    [Order] o ON CAST(o.DateCreatedAt AS DATE) = ds.ngay and o.eventId = 2
LEFT JOIN [TicketOfOrder] too on o.ID = too.orderId
GROUP BY 
    ds.ngay
ORDER BY 
    ds.ngay DESC;

-- Count ticket amount of event by date with specific ticket type
DECLARE @startDate3 DATE = '2025-02-05';
DECLARE @endDate3 DATE = '2025-03-06';

WITH date_series AS (
    SELECT DATEADD(DAY, number, @startDate3) AS ngay
    FROM master.dbo.spt_values
    WHERE type = 'P'
    AND DATEADD(DAY, number, @startDate3) <= @endDate3
)
SELECT 
    ds.ngay as label,
    ISNULL(SUM(too.quantity), 0) AS ticket_amount
FROM 
    date_series ds
LEFT JOIN 
    [Order] o ON CAST(o.DateCreatedAt AS DATE) = ds.ngay and o.eventId = 2
LEFT JOIN [TicketOfOrder] too on o.ID = too.orderId
LEFT JOIN [Ticket] t on too.ticketId = t.ID and t.eventId = 2 and t.name = 'VIP STANDING'
GROUP BY 
    ds.ngay
ORDER BY 
    ds.ngay DESC;



DECLARE @startDate4 DATE = '2025-02-15';
DECLARE @endDate4 DATE = '2025-06-06';

WITH month_series AS (
            SELECT DATEADD(MONTH, number, @startDate4 ) AS month_start
            FROM master.dbo.spt_values
            WHERE type = 'P'
            AND DATEADD(MONTH, number, @startDate4) < @endDate4
            )
            SELECT 
                ms.month_start as label,
                ISNULL(SUM(too.quantity), 0) AS value
            FROM 
                month_series ms
            LEFT JOIN 
                [Order] o ON DATEPART(MONTH, o.DateCreatedAt) = DATEPART(MONTH, ms.month_start) and o.eventId = 2
            LEFT JOIN [TicketOfOrder] too on o.ID = too.orderId
            GROUP BY 
                ms.month_start
            ORDER BY 
                ms.month_start ASC;

		
SELECT o.*, SUM(too.quantity) as ticketCount
FROM [order] o JOIN TicketOfOrder too ON o.ID = too.orderId
GROUP BY o.ID, o.createdBy, o.DateCreatedAt, o.eventId, o.price, o.promotionId, o.status, o.TimeCreatedAt

