
select * from information_schema.tables where table_schema = 'juba5766';

CREATE TABLE juba5766.Camera (
    Brand           VARCHAR(20) NOT NULL,
    Model           VARCHAR(10) NOT NULL,
    Name            VARCHAR(30) NOT NULL,
    Lens_mount_type VARCHAR(8)     NOT NULL,
    MP_count        DECIMAL,
    MemCard_slot    VARCHAR(10),
    PRIMARY KEY (Name)
);

insert into camera(Brand, Model, Name, Lens_mount_type, MP_count, MemCard_slot)
VALUES  ('NIKON', 'D40', 'NIKON D40', 'F', 6, 'SD'),
		('NIKON', 'D40x', 'NIKON D40x', 'F', 10, 'SD'),
		('NIKON', 'D90', 'NIKON D90', 'F', 12.3, 'SD'),
		('NIKON', 'D200', 'NIKON D200', 'F', 10.2, 'SD'),
		('CANON', '5D', 'CANON 5D', 'EF', 12.8, 'SD'),
		('CANON', '6D', 'CANON 6D', 'EF', 20.2, 'SD'),
		('SONY', 'A6600', 'SONY A6600', 'E', 24.2, 'SD')
		ON CONFLICT DO NOTHING;
		
CREATE TABLE juba5766.Lens (
    Manufacturer        VARCHAR(20)     NOT NULL,
    Mount_type          CHAR(8)         NOT NULL,
    Focal_length_MIN    SMALLINT        NOT NULL    CONSTRAINT minFocalLength CHECK(Focal_length_MIN > 4),
    Focal_length_MAX    SMALLINT        NOT NULL    CONSTRAINT maxFocalLength CHECK(Focal_length_MIN > 4),
    Aperture_MIN        DECIMAL         NOT NULL    CONSTRAINT minAperture CHECK(Aperture_MIN > 0.7),
    Aperture_MAX        DECIMAL         NOT NULL    CONSTRAINT maxAperture CHECK(Aperture_MIN BETWEEN 0.7 AND 64),
    Name                VARCHAR(100)    NOT NULL,
        
    CONSTRAINT biggerMaxAperture CHECK(Aperture_MAX >= Aperture_MIN),
    CONSTRAINT biggerMaxFocal CHECK(Focal_length_MAX >= Focal_length_MIN),
    PRIMARY KEY (Name)
);

insert into Lens(Manufacturer, Mount_type, Name, Focal_length_MIN, Focal_length_MAX, Aperture_MIN, Aperture_MAX)
VALUES  ('NIKON', 'F', 'Nikkor 18-55mm VR', 18, 55, 3.5, 5.6),
		('NIKON', 'F', 'Nikkor 18-105mm VR', 18, 105, 3.5, 5.6),
		('NIKON', 'F', 'Nikkor 12-24mm IF-ED', 12, 24, 4, 4),
		('CANON', 'EF', 'Canon 20-35mm VR', 20, 35, 3.5, 4.5),
		('SONY', 'E', 'Sony 16-50mm', 16, 50, 3.5, 5.6)
		ON CONFLICT DO NOTHING;

CREATE TABLE juba5766.Customer (
    Id              SERIAL,
    First_name      VARCHAR(1000)   NOT NULL,
    Last_name       VARCHAR(1000),
    Email           VARCHAR(320)    NOT NULL    UNIQUE,
    Phone_number    CHAR(12)        NOT NULL    UNIQUE,
    
    CONSTRAINT validEmail CHECK(Email LIKE('%_@__%.__%')),
    PRIMARY KEY (Id)
);


CREATE UNIQUE INDEX customerNumber
ON juba5766.Customer (Phone_number); 

CREATE UNIQUE INDEX customerEmail
ON juba5766.Customer (Email);

insert into Customer(First_name, Last_name, Email, Phone_number)
VALUES  ('Julius', 'Baranauskas', 'j@mail.com', 866262525),
		('Jonas', 'Arlauskas', 'arlius@mail.com', 866282521),
		('Jošis', 'As', 'joške@mail.com', 866282522),
		('Kazimieras', 'Bešamel', 'bšml@mail.com', 866282523),
		('Kelmas', 'Samana', 'samanos@mail.com', 866282524),
		('Jojo', 'Arklys', 'arklinis@mail.com', 866282526),
		('Algirdas', 'Nesakysiu', 'nemano@mail.com', 866282520)
		ON CONFLICT DO NOTHING;


CREATE TABLE juba5766.Rentable_item (
	Id					SERIAL,
	Serial_number       INTEGER          NOT NULL,
	Price_per_day       DECIMAL(6, 2)   NOT NULL,
	Discount            DECIMAL(4, 2)   DEFAULT 0,
	Item_name           VARCHAR(100)   NOT NULL    DEFAULT 'ITEM REMOVED',
	Item_type           CHAR(1)         NOT NULL    DEFAULT 'ITEM REMOVED',
	
	PRIMARY KEY (Id)
);


CREATE VIEW juba5766.Rentable_camera(Id, Serial_number, Price_per_day, Discount, Name)
AS	SELECT Id, Serial_number, Price_per_day, Discount, Item_name
	FROM juba5766.Rentable_item
	WHERE Item_type = 'c';

CREATE VIEW juba5766.Rentable_lens(Id, Serial_number, Price_per_day, Discount, Name)
AS	SELECT Id, Serial_number, Price_per_day, Discount, Item_name
	FROM juba5766.Rentable_item
	WHERE Item_type = 'l';

CREATE INDEX priceIndex
ON juba5766.Rentable_item(Price_per_day);

insert into rentable_item(Serial_number, Price_per_day, Item_name, Item_type)
values  (864866644, 10, 'NIKON D40', 'c'),
		(164866644, 12, 'NIKON D40x', 'c'),
		(264866644, 15, 'NIKON D200', 'c'),
		(364866644, 40, 'Nikkor 12-24mm IF-ED', 'l'),
		(464866644, 23, 'Sony 16-50mm', 'l'),
		(564866644, 8, 'SONY A6600', 'c')
		ON CONFLICT DO NOTHING;


CREATE TRIGGER existingItem BEFORE UPDATE OF Item_name OR INSERT ON Rentable_item
FOR EACH ROW EXECUTE PROCEDURE checkItemName();
CREATE TABLE juba5766.Item_types(
    type    CHAR(1) NOT NULL,
    PRIMARY KEY(type)
);


CREATE OR REPLACE FUNCTION checkItemName() RETURNS TRIGGER AS $$
   BEGIN
		IF NEW.Item_type = 'c' THEN
			PERFORM checkCameraName(New.Item_name);
		ELSIF Item_type = 'l' THEN
			PERFORM checkLensName(New.Item_name);
		ELSE	
			RAISE EXCEPTION 'Item type does not exist in this database';
		END IF;
      RETURN NEW;
   END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION checkCameraName(nam TEXT) returns void AS
$$
   BEGIN
		IF nam NOT IN (SELECT Name FROM juba5766.Camera) THEN
			RAISE EXCEPTION 'Camera with name provided does not exist in the database';
		END IF;
   END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION checkLensName(nam TEXT) returns void AS
$$
   BEGIN
		IF nam NOT IN (SELECT Name FROM juba5766.Lens) THEN
			RAISE EXCEPTION 'Lens with name provided does not exist in the database';
		END IF;
   END;
$$ LANGUAGE plpgsql;


create table juba5766.Rent(
	Rent_number		SERIAL,
	Date_taken		DATE		NOT NULL,
	Return_date		DATE		NOT NULL,
	Date_returned	DATE,
	Item			INTEGER		DEFAULT NULL,
	Customer		INTEGER		DEFAULT NULL,
	Fine			DECIMAL(4, 2) DEFAULT NULL,
	
	CONSTRAINT Iitem		FOREIGN KEY (Item)		REFERENCES juba5766.Rentable_item(Id)	ON UPDATE CASCADE ON DELETE SET DEFAULT,
	CONSTRAINT Icustomer	FOREIGN KEY (Customer)  REFERENCES juba5766.Customer(Id) 		ON UPDATE CASCADE ON DELETE SET DEFAULT,
	CONSTRAINT laterReturn  CHECK(Return_date >= Date_taken),
	CONSTRAINT returnedLater CHECK(CASE WHEN Date_returned IS NOT NULL AND Date_returned >= Date_taken THEN TRUE
		WHEN Date_returned IS NULL THEN TRUE ELSE FALSE END),
	PRIMARY KEY (Rent_number)
);

CREATE INDEX rentCustomer ON juba5766.Rent(Customer);

INSERT INTO Rent(Date_taken, Return_date, Date_returned, Item, Customer)
VALUES  (to_date('2019/11/28', 'yyyy/mm/dd'), to_date('2019/11/29', 'yyyy/mm/dd'), null, 1, 1),
		(to_date('2019/11/25', 'yyyy/mm/dd'), to_date('2019/11/27', 'yyyy/mm/dd'), null, 2, 1),
		(to_date('2019/12/01', 'yyyy/mm/dd'), to_date('2019/12/02', 'yyyy/mm/dd'), null, 3, 2),
		(to_date('2019/11/12', 'yyyy/mm/dd'), to_date('2019/11/23', 'yyyy/mm/dd'), to_date('2019/11/28', 'yyyy/mm/dd'), 4, 3),
		(to_date('2019/11/13', 'yyyy/mm/dd'), to_date('2019/11/23', 'yyyy/mm/dd'), to_date('2019/11/28', 'yyyy/mm/dd'), 5, 3),
		(to_date('2019/11/29', 'yyyy/mm/dd'), to_date('2019/12/02', 'yyyy/mm/dd'), null, 4, 1)
		ON CONFLICT DO NOTHING;

CREATE TRIGGER tooManyRents BEFORE INSERT ON Rent
FOR EACH ROW EXECUTE PROCEDURE checkRentLimit();

CREATE OR REPLACE FUNCTION checkRentLimit() RETURNS TRIGGER AS $$
   BEGIN
		IF (SELECT count(*)
			FROM Rent
			WHERE Date_returned IS NULL
			AND Customer = NEW.Customer) > 8
			THEN
			RAISE EXCEPTION 'Single customer is only allowed to rent 8 items at a time';
		END IF;
      RETURN NEW;
   END;
$$ LANGUAGE plpgsql;


CREATE VIEW juba5766.Taken_cameras (Serial_number, Name, Date_taken, Return_date, Customer)
	AS 	SELECT Serial_number, Name, Date_taken, Return_date, Customer
		FROM Rent JOIN Rentable_camera ON Item = Id
		WHERE Date_returned IS NULL;

CREATE VIEW juba5766.Taken_lenses (Serial_number, Name, Date_taken, Return_date, Customer)
	AS 	SELECT Serial_number, Name, Date_taken, Return_date, Customer
		FROM Rent JOIN Rentable_lens ON Item = Id
		WHERE Date_returned IS NULL;
		
		
CREATE VIEW juba5766.Available_cameras(Name, "# of available")
AS (SELECT Name, COUNT(*) AS "# of units"
FROM juba5766.Rent JOIN Rentable_camera ON Rentable_camera.Id = Rent.Item
GROUP BY Name
HAVING Count(Date_returned) = Count(Return_date));

CREATE VIEW juba5766.Available_Lenses(Name, "# of available")
AS (SELECT Name, COUNT(*) AS "# of units"  
FROM juba5766.Rent JOIN Rentable_lens ON Rentable_lens.Id = Rent.Item
GROUP BY Name
HAVING Count(Date_returned) = Count(Return_date));

CREATE MATERIALIZED VIEW juba5766.Popular_items_current_month(Name, "# of rents current month")
AS SELECT Item_name, COUNT(*)
FROM Rent JOIN Rentable_item ON Rent.Item = Rentable_item.Id
WHERE Date_taken BETWEEN date_trunc('month', current_date) AND date_trunc('month', current_date) + interval '1 month' - interval '1 day'
GROUP BY Item_name
ORDER BY 2 desc
LIMIT 10;


DELETE:

DROP TABLE Camera CASCADE;
DROP TABLE Lens CASCADE;
DROP TABLE Rentable_item CASCADE;
DROP TABLE Rent CASCADE;
DROP TABLE Customer CASCADE;
DROP VIEW Taken_cameras CASCADE;
DROP VIEW Available_cameras CASCADE;
DROP VIEW Available_Lenses CASCADE;
DROP MATERIALIZED VIEW PopularItems;
