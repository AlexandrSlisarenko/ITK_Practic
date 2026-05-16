--Практическое задание!!!
EXPLAIN ANALYSE SELECT SUM(products.price * cart_items.quantity) AS total_cost
                FROM products
                         JOIN cart_items ON products.id = cart_items.product_id
                         JOIN orders ON orders.user_id = cart_items.user_id
                WHERE orders.status = 'active'
                  AND orders.user_id = 100;
-- Было:
-- Planning Time: 0.235 ms
-- Execution Time: 26.324 ms

CREATE INDEX idx_cart_items_products ON cart_items(product_id);
CREATE INDEX idx_cart_items_users ON cart_items(user_id);
CREATE INDEX idx_orders_user_id ON orders (user_id);
CREATE INDEX idx_orders_status_active ON orders(status) WHERE status = 'active';


-- Стало:
-- Planning Time: 0.197 ms
-- Execution Time: 0.187 ms

--Практическое задание!!!


-- SQL livecoding
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

SELECT pr.productname, AVG(pr.price * od.quantity) AS AveragePrice, SUM(od.quantity) AS TotalQuantity
FROM Product AS pr
INNER JOIN Orderdetail AS od ON pr.productid = od.productid
GROUP BY pr.productid, pr.productname
HAVING AVG(pr.price * od.quantity) > 100.00 AND SUM(od.quantity) > 2
/*В задаче ошибка не больше 20 а больше 2 по количеству, или я что-то не понял*/

