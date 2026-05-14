SELECT e.employeename, e.salary, companyname, c.address
FROM employee e LEFT JOIN public.company c on c.companyid = e.companyid;

SELECT morder.orderid, morder.orderdate, c.customername
FROM morder LEFT JOIN public.customer c on c.customerid = morder.customerid
WHERE c.city LIKE '%New York%';

SELECT s.studentname, STRING_AGG(c.coursename, ', ')
FROM Student AS s, course AS c
WHERE 1 < (SELECT count(courseid) FROM registration)
group by s.studentname
