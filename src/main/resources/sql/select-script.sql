SELECT e.employeename, e.salary, companyname, c.address
FROM employee e
         LEFT JOIN public.company c on c.companyid = e.companyid;


SELECT morder.orderid, morder.orderdate, c.customername
FROM morder
         LEFT JOIN public.customer c on c.customerid = morder.customerid
WHERE c.city LIKE '%New York%';


SELECT s.studentname, STRING_AGG(c.coursename, ', ')
FROM Student AS s,
     course AS c
WHERE 1 < (SELECT count(courseid) FROM registration)
group by s.studentname;


WITH getAVG AS (SELECT AVG(count_book) AS avg
                FROM (SELECT COUNT(book.booktitle) AS count_book
                      from book
                      GROUP BY authorid) as bcb)
SELECT book.booktitle, author.authorname
FROM Book book
         LEFT JOIN Author author on book.authorid = author.authorid
WHERE book.authorid in (SELECT book.authorid
                        FROM Book AS book
                        GROUP BY book.authorid
                        HAVING COUNT(*) > (SELECT avg FROM getAVG));


--ошибка в примере

SELECT student.studentname, AVG(grade.grade)
FROM Grade as grade
         LEFT JOIN Student as student ON grade.studentid = student.studentid
WHERE grade.studentid IN (SELECT grade.studentid
                          FROM Grade as grade
                          GROUP BY grade.studentid
                          HAVING AVG(grade.grade) > 75)
GROUP BY student.studentname;


SELECT employee.employeename, COUNT(project.projectid)
FROM Project as project
         LEFT JOIN Employee as employee ON project.employeeid = employee.employeeid
GROUP BY employee.employeename
HAVING COUNT(project.projectid) > 2;


WITH getAVGTotalAmount AS (SELECT AVG(morder.totalamount) avgTotalAmount
                      FROM Morder AS morder)
SELECT customer.customername, SUM(morder.totalamount)
FROM Morder AS morder
LEFT JOIN Customer AS customer ON morder.customerid = customer.customerid
GROUP BY customer.customername
HAVING SUM(morder.totalamount) > (SELECT avgTotalAmount FROM getAVGTotalAmount);

WITH averagePrice AS (

)
SELECT product.productname
FROM Orderdetail AS detail
LEFT JOIN Product AS product ON detail.productid = product.productid

