CREATE TABLE IF NOT EXISTS Company
(
    companyId         int PRIMARY KEY,
    companyName       varchar(255) NOT NULL,
    address           varchar(255),
    numberOfEmployees int          not null
);

INSERT INTO Company(companyId, companyName, address, numberOfEmployees)
VALUES (1, 'Company A', 'Address A', 150),
       (2, 'Company B', 'Address B', 80),
       (3, 'Company C', 'Address C', 200);

CREATE TABLE IF NOT EXISTS Employee
(
    employeeId   int PRIMARY KEY,
    employeeName varchar(255) NOT NULL,
    salary       decimal(7, 2),
    companyId    int,
    CONSTRAINT companyConstrain FOREIGN KEY (companyId) REFERENCES Company (companyId)
);

insert into Employee(employeeId, employeeName, salary, companyId)
VALUES (1, '	John Doe', 60000.00, 1),
       (2, 'Jane Smith', 55000.00, 1),
       (3, '	Bob Johnson', 48000.00, 2),
       (4, '	Alice Brown', 75000.00, 3),
       (5, '	Charlie White', 42000.00, 2);


CREATE TABLE IF NOT EXISTS Customer
(
    customerId   int primary key,
    customerName varchar(255),
    city         varchar(255) not null
);

CREATE TABLE IF NOT EXISTS MOrder
(
    orderId     int primary key,
    orderDate   timestamp not null,
    customerId  int,
    totalAmount decimal(7, 2),
    CONSTRAINT customerConstraint FOREIGN KEY (customerId) REFERENCES Customer (customerId)
);

INSERT INTO customer(customerId, customerName, city)
VALUES (101, 'John Doe', 'New York'),
       (102, 'Jane Smith', 'Los Angeles'),
       (103, 'Bob Johnson', 'New York'),
       (104, 'Alice Brown', 'Chicago'),
       (105, 'Charlie White', 'New York');

INSERT INTO morder(orderId, orderDate, customerId, totalAmount)
VALUES (1, '2023-01-10', 101, 120.00),
       (2, '2023-02-15', 102, 75.50),
       (3, '2023-03-20', 103, 200.00),
       (4, '2023-04-05', 104, 350.00),
       (5, '2023-05-12', 105, 80.00),
       (6, '2023-06-18', 104, 500.00),
       (7, '2023-07-25', 101, 180.00),
       (8, '2023-08-30', 105, 90.00),
       (9, '2023-09-05', 103, 120.00),
       (10, '2023-10-12', 104, 300.00);


CREATE TABLE IF NOT EXISTS Student
(
    studentId   int primary key,
    studentName varchar(255),
    age         int,
    gender      varchar(10)
);
CREATE TABLE IF NOT EXISTS Course
(
    courseId   int primary key,
    courseName varchar(255)
);
CREATE TABLE IF NOT EXISTS Registration
(
    registrationId int primary key,
    courseId       int,
    studentId      int,
    CONSTRAINT courseId FOREIGN KEY (courseId) REFERENCES Course (courseId),
    CONSTRAINT studentId FOREIGN KEY (studentId) REFERENCES Student (studentId)
);

INSERT INTO Student(studentId, studentName, age, gender)
VALUES (1, 'John Doe', 20, 'Male'),
       (2, 'Jane Smith', 22, 'Female'),
       (3, 'Bob Johnson', 21, 'Male'),
       (4, 'Alice Brown', 23, 'Female'),
       (5, 'Charlie White', 20, 'Male');
INSERT INTO Course(courseId, courseName)
VALUES (1, 'Mathematics'),
       (2, 'Computer Science'),
       (3, 'English Literature'),
       (4, 'History'),
       (5, 'Chemistry');
INSERT INTO Registration(registrationId, courseId, studentId)
VALUES (1, 1, 1),
       (2, 1, 2),
       (3, 2, 2),
       (4, 3, 1),
       (5, 3, 3),
       (6, 4, 4),
       (7, 5, 2),
       (8, 5, 3);

CREATE TABLE IF NOT EXISTS Author
(
    authorId   int primary key,
    authorName varchar(255),
    birthYear  int
);
CREATE TABLE IF NOT EXISTS Book
(
    bookId    int primary key,
    bookTitle varchar(255),
    authorId  int,
    CONSTRAINT authorId FOREIGN KEY (authorId) REFERENCES Author (authorId)
);

INSERT INTO Author(authorId, authorName, birthYear)
VALUES (1, 'Jane Austen', 1775),
       (2, 'Charles Dickens', 1812),
       (3, 'Mark Twain', 1835),
       (4, 'Emily Bronte', 1818),
       (5, 'Leo Tolstoy', 1828);
INSERT INTO Book(bookId, bookTitle, authorId)
VALUES (1, 'Pride and Prejudice', 1),
       (2, 'Great Expectations', 2),
       (3, 'War and Peace', 5),
       (4, 'Wuthering Heights', 4),
       (5, 'The Adventures of Tom Sawyer', 3),
       (6, 'Sense and Sensibility', 1),
       (7, 'Anna Karenina', 5),
       (8, 'Oliver Twist', 2),
       (9, 'Jane Eyre', 4);


CREATE TABLE IF NOT EXISTS Grade
(
    gradeId   int primary key,
    studentId int,
    subject   varchar(255),
    grade     int,
    CONSTRAINT studentId FOREIGN KEY (studentId) REFERENCES Student (studentId)
);
INSERT INTO Grade(gradeId, studentId, subject, grade)
VALUES (1, 1, 'Mathematics', 85),
       (2, 1, 'Physics', 78),
       (3, 2, 'Mathematics', 92),
       (4, 2, 'Physics', 88),
       (5, 3, 'Mathematics', 70),
       (6, 3, 'Physics', 75),
       (7, 4, 'Mathematics', 95),
       (8, 4, 'Physics', 80),
       (9, 5, 'Mathematics', 88),
       (10, 5, 'Physics', 92);


ALTER TABLE Employee
    ADD COLUMN position varchar(255);

UPDATE Employee
SET position = 'Software Engineer'
WHERE employeeId = 1;

UPDATE Employee
SET position = 'Project Manager'
WHERE employeeId = 2;

UPDATE Employee
SET position = 'UI/UX Designer'
WHERE employeeId = 4;

UPDATE Employee
SET position = 'QA Engineer'
WHERE employeeId = 3;

UPDATE Employee
SET position = 'Software Engineer'
WHERE employeeId = 5;

CREATE TABLE IF NOT EXISTS Project
(
    projectId   int primary key,
    projectName varchar(255),
    employeeId  int,
    CONSTRAINT employeeId FOREIGN KEY (employeeId) REFERENCES Employee (employeeId)
);

INSERT INTO Project(projectId, projectName, employeeId)
VALUES (1, 'Project A', 1),
       (2, 'Project B', 2),
       (3, 'Project C', 1),
       (4, 'Project D', 3),
       (5, 'Project E', 4),
       (6, 'Project F', 2),
       (7, 'Project G', 1),
       (8, 'Project H', 5),
       (9, 'Project I', 3),
       (10, 'Project J', 4);


CREATE TABLE IF NOT EXISTS Product
(
    productId   int primary key,
    productName varchar(255),
    price decimal(7, 2)
);

CREATE TABLE IF NOT EXISTS OrderDetail
(
    orderDetailId int primary key,
    orderId int not null,
    productId  int not null,
    quantity int default 0,
    CONSTRAINT orderId FOREIGN KEY(orderId) REFERENCES MOrder(orderId),
    CONSTRAINT productID FOREIGN KEY(productId) REFERENCES Product(productId)
);

INSERT INTO Product(productId, productName, price)
VALUES (1,	'Laptop',	800.00),
       (2,	'Smartphone',	400.00),
       (3,	'Headphones',	50.00),
       (4,	'Printer',	150.00),
       (5,	'External HDD',	120.00);

INSERT INTO  OrderDetail(orderDetailId, orderId, productId, quantity)
VALUES (1,	1,	1,	2),
       (2,	1,	2,	3),
       (3,	2,	1,	1),
       (4,	2,	3,	5),
       (5,	3,	2,	2),
       (6,	3,	4,	1),
       (7,	4,	1,	3),
       (8,	4,	5,	2),
       (9,	5,	3,	4),
       (10,	5,	4,	1);