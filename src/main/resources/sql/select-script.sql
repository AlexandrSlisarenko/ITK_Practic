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
HAVING AVG(pr.price * od.quantity) > 100.00 AND SUM(od.quantity) > 2;
/*В задаче ошибка не больше 20 а больше 2 по количеству, или я что-то не понял*/

SELECT student.studentName, course.courseName, AVG(grade.grade)
FROM Grades AS grades
         LEFT JOIN Student AS student ON grades.studentId = student.studentId
         LEFT JOIN Course AS course ON grades.courseId = course.courseId
         LEFT JOIN Grade AS grade ON grades.gradeId = grade.gradeId
GROUP BY grades.studentId, student.studentName, course.courseName
HAVING AVG(grade.grade) > 70;

WITH getTotalHours AS (
    SELECT taskA.employeeid, taskA.projectid, taskA.taskid, SUM(taskA.hoursworked) AS totalHours
    FROM TaskAssignments taskA
    group by taskA.employeeid, taskA.projectid, taskA.taskid
),
getTotalHoursInProject AS (
    SELECT taskA.projectid, SUM(taskA.hoursworked) AS totalHoursInProject
    FROM TaskAssignments taskA
    GROUP BY taskA.projectid
),
getAVGTotalHoursInProject AS (
    SELECT taskA.projectid,taskA.taskid, AVG(taskA.hoursworked) AS avgHoursInProject
    FROM TaskAssignments taskA
    GROUP BY taskA.projectid, taskid
)
SELECT employee.employeename,
       project.projectname,
       task.taskname,
       totalHoursSpendOnTask.totalHours,
       totalHoursInProject.totalHoursInProject
FROM getTotalHours totalHoursSpendOnTask
LEFT JOIN Employee employee ON totalHoursSpendOnTask.employeeid = employee.employeeid
LEFT JOIN Project project ON totalHoursSpendOnTask.projectid = project.projectid
LEFT JOIN Task task ON totalHoursSpendOnTask.taskid = task.taskid
LEFT JOIN getTotalHoursInProject totalHoursInProject ON totalHoursSpendOnTask.projectid = totalHoursInProject.projectid
LEFT JOIN getAVGTotalHoursInProject avgTotalHouse ON avgTotalHouse.taskid = totalHoursSpendOnTask.taskid AND
                                                        avgTotalHouse.projectid = totalHoursSpendOnTask.projectid
WHERE totalHoursSpendOnTask.totalHours > avgTotalHouse.avgHoursInProject
GROUP BY employee.employeename, project.projectname, task.taskname, totalHoursSpendOnTask.totalHours,
         totalHoursInProject.totalHoursInProject;

/* Не понимаю почему отработало
WITH TaskStats AS (
    -- Сначала агрегируем часы по задачам
    SELECT
        ta.EmployeeID,
        ta.ProjectID,
        ta.TaskID,
        SUM(ta.HoursWorked) AS TotalHours
    FROM TaskAssignments ta
    GROUP BY ta.EmployeeID, ta.ProjectID, ta.TaskID
),
     ProjectStats AS (
         -- Считаем общее количество часов по каждому проекту
         SELECT
             ProjectID,
             SUM(HoursWorked) AS TotalHoursInProject
         FROM TaskAssignments
         GROUP BY ProjectID
     ),
     TaskAvg AS (
         -- Считаем среднее количество часов по задаче в рамках проекта
         SELECT
             ta.ProjectID,
             ta.TaskID,
             AVG(ta.HoursWorked) AS AvgHoursForTask
         FROM TaskAssignments ta
         GROUP BY ta.ProjectID, ta.TaskID
     )
SELECT
    e.EmployeeName,
    p.ProjectName,
    t.TaskName,
    ts.TotalHours,
    ps.TotalHoursInProject
FROM TaskStats ts
         JOIN Employee e ON ts.EmployeeID = e.EmployeeID
         JOIN Project p ON ts.ProjectID = p.ProjectID
         JOIN Task t ON ts.TaskID = t.TaskID
         JOIN ProjectStats ps ON ts.ProjectID = ps.ProjectID
         JOIN TaskAvg ta ON ts.ProjectID = ta.ProjectID AND ts.TaskID = ta.TaskID
WHERE ts.TotalHours > ta.AvgHoursForTask
ORDER BY e.EmployeeName, p.ProjectName, t.TaskName;*/

WITH AgregateData AS (
    SELECT assignment.employeeid,
           assignment.projectid,
           assignment.taskid,
           SUM(assignment.hoursworked) AS hoursWorked,
           SUM(SUM(assignment.hoursworked)) OVER (PARTITION BY assignment.projectid) AS totalHoursWorkedOnProject,
           AVG(SUM(assignment.hoursworked)) OVER (PARTITION BY assignment.projectid, assignment.taskid) AS avgHoursWorkedOnProject
    FROM TaskAssignments assignment
    GROUP BY assignment.employeeid, assignment.projectid, assignment.taskid)
SELECT employee.employeename, project.projectname, task.taskname, data.hoursWorked, data.totalHoursWorkedOnProject
FROM AgregateData data
LEFT JOIN Employee employee ON data.employeeid = employee.employeeid
LEFT JOIN Project project ON data.projectid = project.projectid
LEFT JOIN Task task ON data.taskid = task.taskid
WHERE data.hoursWorked > data.avgHoursWorkedOnProject
GROUP BY employee.employeename, project.projectname, task.taskname, data.hoursWorked, data.totalHoursWorkedOnProject;




SELECT o.orderId,
       o.totalOrderAmount,
       o.uniqueProductCount,
       (
        SELECT product1.productname
        FROM OrderDetail detail1
        LEFT JOIN Product product1 ON detail1.productid = product1.productid
        WHERE o.orderid = detail1.orderid
        ORDER BY product1.price DESC
        LIMIT 1
        ) AS mostExpensiveProduct
FROM (
    SELECT detail.orderid AS orderId,
        SUM(product.price * detail.quantity) AS totalOrderAmount,
        COUNT(DISTINCT detail.productid) AS uniqueProductCount
    FROM OrderDetail detail
    LEFT JOIN Product product ON detail.productid = product.productid
    GROUP BY detail.orderid
    HAVING SUM(product.price * detail.quantity) > 500
        AND COUNT(DISTINCT detail.productid) > 1
     ) AS o
ORDER BY o.orderId;



SELECT data.studentname,
       data.coursename,
       data.TotalCourseHours,
       data.TotalStudyHours,
       data.AverageGrade
FROM (
    SELECT student.studentname,
           course.coursename,
           course.hours AS TotalCourseHours,
           SUM(course.hours) OVER (PARTITION BY enrollment.studentid) AS TotalStudyHours,
           AVG(grade.grade) OVER (PARTITION BY enrollment.studentid, enrollment.courseid) AS AverageGrade
    FROM Enrollment enrollment
    LEFT JOIN Student student ON enrollment.studentid = student.studentid
    LEFT JOIN Course course ON enrollment.courseid = course.courseid
    LEFT JOIN Grades grade ON enrollment.studentid = grade.studentid AND enrollment.courseid = grade.courseid
    GROUP BY enrollment.studentid, student.studentname,
             course.coursename,
             course.hours,
             grade.grade,
             enrollment.courseid
    HAVING AVG(grade.grade) > 80
) AS data
GROUP BY data.studentname, data.coursename, data.TotalCourseHours,
         data.TotalStudyHours,
         data.AverageGrade;



SELECT
    s.StudentName,
    c.CourseName,
    c.Hours AS TotalCourseHours,
    c.Hours AS TotalStudyHours,
    AVG(g.Grade) AS AverageGrade
FROM Student s
         JOIN Enrollment e ON s.StudentID = e.StudentID
         JOIN Course c ON e.CourseID = c.CourseID
         JOIN Grades g ON s.StudentID = g.StudentID AND c.CourseID = g.CourseID
GROUP BY s.StudentID, s.StudentName, c.CourseID, c.CourseName, c.Hours
HAVING AVG(g.Grade) > 80
ORDER BY s.StudentName, c.CourseName;
