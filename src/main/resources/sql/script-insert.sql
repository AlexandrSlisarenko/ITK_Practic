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

insert into customer(customerId, customerName, city)
VALUES (101, 'John Doe', 'New York'),
       (102, 'Jane Smith', 'Los Angeles'),
       (103, 'Bob Johnson', 'New York'),
       (104, 'Alice Brown', 'Chicago'),
       (105, 'Charlie White', 'New York');

insert into morder(orderId, orderDate, customerId, totalAmount)
VALUES (1, '2023-01-10', 101, 120.00),
       (2, '2023-02-15', 102, 75.50),
       (3, '2023-03-20', 103, 200.00),
       (4, '2023-04-05', 104, 350.00),
       (5, '2023-05-12', 105, 80.00);


CREATE TABLE IF NOT EXISTS Student
(
    studentId   int primary key,
    studentName varchar(255),
    age         int,
    gender      varchar(10)
);
CREATE TABLE IF NOT EXISTS Course
(
    courseId int primary key,
    courseName varchar(255)
);
CREATE TABLE IF NOT EXISTS Registration
(
    registrationId int primary key,
    courseId int,
    studentId int,
    CONSTRAINT courseId FOREIGN KEY (courseId) REFERENCES Course(courseId),
    CONSTRAINT studentId FOREIGN KEY (studentId) REFERENCES Student(studentId)
);

INSERT INTO Student(studentId, studentName, age, gender)
VALUES (1,	'John Doe',	20,	'Male'),
       (2,	'Jane Smith',	22,	'Female'),
       (3,	'Bob Johnson',	21,	'Male'),
       (4,	'Alice Brown',	23,	'Female'),
       (5,	'Charlie White',	20,	'Male');
INSERT INTO  Course(courseId, courseName)
VALUES (1,	'Mathematics'),
       (2,	'Computer Science'),
       (3,	'English Literature'),
       (4,	'History'),
       (5,	'Chemistry');
INSERT INTO Registration(registrationId, courseId, studentId)
VALUES (1,	1,	1),
       (2,	1,	2),
       (3,	2,	2),
       (4,	3,	1),
       (5,	3,	3),
       (6,	4,	4),
       (7,	5,	2),
       (8,	5,	3);
