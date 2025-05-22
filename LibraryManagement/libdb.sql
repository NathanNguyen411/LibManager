CREATE DATABASE qlthuvien;
USE qlthuvien;

CREATE TABLE Book (
                      BookID INT AUTO_INCREMENT PRIMARY KEY,
                      BookName VARCHAR(255) NOT NULL,
                      pageNo VARCHAR(255),
                      LANGUAGE VARCHAR(255),
                      price INT,
                      amount INT,
                      publishYear VARCHAR(255),
                      type VARCHAR(255),
                      author VARCHAR(255),
                      publisher VARCHAR(255)
);

CREATE TABLE Reader(
                       readerID INT AUTO_INCREMENT PRIMARY KEY,
                       surname VARCHAR(255),
                       name VARCHAR(255),
                       identityCard VARCHAR(255),
                       phoneNO VARCHAR(255),
                       cardIssueDate DATE,
                       job VARCHAR(255)
);

CREATE TABLE Loan(
                     loanID INT AUTO_INCREMENT PRIMARY KEY,
                     readerID INT,
                     FOREIGN KEY (readerID) REFERENCES Reader(readerID) ON DELETE CASCADE,
                     BookID INT,
                     FOREIGN KEY(BookID) REFERENCES Book(BookID) ON DELETE CASCADE,
                     loanNo INT,
                     loanDate DATE,	
                     bookReturnAppointmentDate DATE,
                     bookReturnDate DATE,
                     STATUS VARCHAR(255)
);

CREATE TABLE Statistic(
                          totalBook INT,
                          totalLoan INT,
                          loan INT,
                          punish INT
);

CREATE TABLE Punishment(
                           loanID INT AUTO_INCREMENT PRIMARY KEY,
                           FOREIGN KEY (loanID) REFERENCES Loan(loanID) ON DELETE CASCADE,
                           readerID INT,
                           FOREIGN KEY (readerID) REFERENCES Reader(readerID) ON DELETE CASCADE,
                           fullname VARCHAR(255),
                           bookID INT,
                           FOREIGN KEY (bookID) REFERENCES Book(BookID) ON DELETE CASCADE,
                           book VARCHAR(255),
                           loanNo INT,
                           daysLate INT,
                           total INT,
                           STATUS VARCHAR(255)
);


/*quản lý sách*/
DELIMITER $$
CREATE PROCEDURE sp_findAllBook()
BEGIN 
 SELECT *FROM book;
END $$ 
DELIMITER ;
/*CALL sp_findAllBook();*/

DELIMITER $$
CREATE PROCEDURE sp_insertBook(
  IN U1 VARCHAR(255),
  IN U2 VARCHAR(255),
  IN U3 VARCHAR(255),
  IN U4 INT,
  IN U5 INT,
  IN U6 VARCHAR(255),
  IN U7 VARCHAR(255),
  IN U8 VARCHAR(255),
  IN U9 VARCHAR(255)
)
BEGIN
 INSERT INTO book(BookName,pageNo,LANGUAGE,price,amount,publishYear,TYPE,author,publisher)
 VALUES (U1,U2,U3,U4,U5, U6,U7 ,U8,U9);
END $$
DELIMITER ;
/*CALL sp_insertBook(U1,U2,U3,U4,U5,U6,U7,U8,U9);*/


DELIMITER $$
CREATE PROCEDURE sp_deleteBook(
IN U1 INT
)
BEGIN
DELETE FROM Book
WHERE BookID = U1;
END $$
DELIMITER ;
/*Call sp_deleteBook(U1);*/


DELIMITER $$
CREATE PROCEDURE sp_updateBook(
IN U1 VARCHAR(255),
  IN U2 VARCHAR(255),
  IN U3 VARCHAR(255),
  IN U4 INT,
  IN U5 INT,
  IN U6 VARCHAR(255),
  IN U7 VARCHAR(255),
  IN U8 VARCHAR(255),
  IN U9 VARCHAR(255),
  IN U10 INT
)
BEGIN
UPDATE Book
SET BookName = U1,
pageNO = U2,
LANGUAGE=U3,
price=U4,
amount=U5,
publishYear=U6,
TYPE=U7,
author=U8,
publisher=U9
WHERE BookID=U10;
END $$
DELIMITER ;
/*CALL sp_updateBook(U1,U2,U3,U4,U5,U6,U7,U8,U9,U10);*/



DELIMITER $$
CREATE PROCEDURE sp_findByAllBook
(
 IN id VARCHAR(255)
)
BEGIN
 SELECT * FROM Book
 WHERE BookName LIKE CONCAT("%",id,"%")
 OR pageNO LIKE CONCAT("%",id,"%")
 OR LANGUAGE LIKE CONCAT("%",id,"%")
 OR price LIKE CONCAT("%",id,"%")
 OR amount LIKE CONCAT("%",id,"%")
 OR publishYear LIKE CONCAT("%",id,"%")
 OR TYPE LIKE CONCAT("%",id,"%")
 OR author LIKE CONCAT("%",id,"%")
 OR publisher LIKE CONCAT("%",id,"%");
 
END $$
DELIMITER ;
/*CALL sp_findByAllBook(id);*/
 
 
DELIMITER $$
CREATE PROCEDURE sp_findBookByName
(
 IN id VARCHAR(255)
)
BEGIN
 SELECT * FROM Book
 WHERE BookName LIKE CONCAT("%",id,"%");
 END $$
 DELIMITER ;
 /*CALL sp_findBookByName(id);*/
 
 
 
DELIMITER $$
CREATE PROCEDURE sp_findBookByAuthor
(
 IN id VARCHAR(255)
)
BEGIN
 SELECT * FROM Book
 WHERE author LIKE CONCAT("%",id,"%");
 END $$
 DELIMITER ;
 /*CALL sp_findBookByAuthor(id);*/
 
DELIMITER $$
CREATE PROCEDURE sp_findBookByLanguage
(
 IN id VARCHAR(255)
)
BEGIN
 SELECT * FROM Book
 WHERE LANGUAGE LIKE CONCAT("%",id,"%");
 END $$
 DELIMITER ;
 /*CALL sp_findBookByLanguage(id);*/
 
 DELIMITER $$
CREATE PROCEDURE sp_findBookByPublishYear
(
 IN id VARCHAR(255)
)
BEGIN
 SELECT * FROM Book
 WHERE publishYear LIKE CONCAT("%",id,"%");
 END $$
 DELIMITER ;
/* CALL sp_findBookBypublishYear(id);*/
 
 DELIMITER $$
CREATE PROCEDURE sp_findBookByType
(
 IN id VARCHAR(255)
)
BEGIN
 SELECT * FROM Book
 WHERE TYPE LIKE CONCAT("%",id,"%");
 END $$
 DELIMITER ;
 /*CALL sp_findBookByType(id);*/

DELIMITER $$javacrud
CREATE PROCEDURE sp_findBookByPublisher
(
 IN id VARCHAR(255)
)
BEGIN
 SELECT * FROM Book
 WHERE publisher LIKE CONCAT("%",id,"%");
 END $$
 DELIMITER ;
/* CALL sp_findBookByPublisher(id);*/


DELIMITER $$
CREATE PROCEDURE  sp_sortAZPageNO()
BEGIN
 SELECT * FROM book ORDER BY pageNO ASC;
 END $$
DELIMITER ;
/*CALL sp_sortAZPageNO();*/


DELIMITER $$
CREATE PROCEDURE  sp_sortZAPageNO()
BEGIN
 SELECT * FROM book ORDER BY pageNO DESC;
 END $$
DELIMITER ;
/*CALL sp_sortZAPageNO(); */


/* quản lý độc giả*/
DELIMITER $$ 
CREATE PROCEDURE sp_findAllReader()
BEGIN
SELECT * FROM reader;
END $$
DELIMITER ;
/*CALL sp_findAllReader();*/


DELIMITER $$
CREATE PROCEDURE sp_addReader(
    IN u1 VARCHAR(255),
    IN u2 VARCHAR(255),
    IN u3 VARCHAR(255),
    IN u4 VARCHAR(255),
    IN u5 VARCHAR(255)
)
BEGIN
	DECLARE rid INT;
   INSERT INTO reader(surname, NAME, identityCard, phoneNO, job)
   VALUES (u1, u2, u3, u4, u5);
   
   SELECT readerID INTO rid
   FROM reader
   WHERE identityCard = u3
   ORDER BY readerID DESC
   LIMIT 1;
   INSERT INTO users(username, password, readerid)
   VALUES (u4, u3, rid);
END $$
DELIMITER ;


DELIMITER $$
CREATE PROCEDURE sp_findByAllReader
(
 IN id VARCHAR(255)
)
BEGIN
 SELECT * FROM Reader
 WHERE surname LIKE CONCAT("%",id,"%")
 OR name LIKE CONCAT("%",id,"%")
 OR identityCard LIKE CONCAT("%",id,"%")
 OR phoneNO LIKE CONCAT("%",id,"%");
END $$
DELIMITER ;
/*CALL sp_findByAllReader(id);*/


DELIMITER $$
CREATE PROCEDURE sp_updateReader(
IN u1 VARCHAR(255),
IN u2 VARCHAR(255),
IN u3 VARCHAR(255),
IN u4 VARCHAR(255),
IN u5 VARCHAR(255),
IN u6 INT	
)
BEGIN
 UPDATE Reader
 SET surname = u1,
 NAME = u2,
 identityCard = u3,
 phoneNO = u4,
 job = u5
 WHERE ReaderID = u6;
END $$
DELIMITER ;
/*CALL sp_updateReader(u1,u2,u3,u4,u5,u6);*/

DELIMITER $$
CREATE PROCEDURE sp_deleteReader(
IN id INT
)
BEGIN
  DELETE  FROM Reader
  WHERE ReaderId=ID;
END $$
DELIMITER ;
/*CALL sp_deleteReader(ID);*/


/* quản lý loan */
DELIMITER $$
CREATE PROCEDURE sp_findAllLoan()
BEGIN
 SELECT * FROM loan;
END $$
DELIMITER ;
/*CALL sp_findAllLoan();*/


DELIMITER $$
CREATE PROCEDURE sp_addLoan(
    IN p_reader_id INT,
    IN p_book_id INT,
    IN p_book_return_appointment_date VARCHAR(20)
)
BEGIN
    DECLARE book_available INT;
    
    -- Check if book is available
    SELECT amount INTO book_available FROM book WHERE bookid = p_book_id;
    
    IF book_available > 0 THEN
        -- Insert the loan record
        INSERT INTO loan (readerID,bookID ,LoanNO,LoanDate,bookreturnappointmentdate,status)
        VALUES (p_reader_id,p_book_id,1,CURDATE(),p_book_return_appointment_date,"chưa trả");
        
        -- Update book amount
        UPDATE book SET amount = amount - 1 WHERE bookid = p_book_id;
        
        SELECT 1; -- Success
    ELSE
        SELECT 0; -- Book not available
    END IF;
END$$
DELIMITER ;


DELIMITER $$
CREATE PROCEDURE sp_testReaderId(
IN id INT
)
BEGIN
 SELECT NAME FROM reader
 WHERE ReaderId=id;
END $$
DELIMITER ;


DELIMITER $$
CREATE PROCEDURE sp_testBookId(
IN id INT
)
BEGIN
  SELECT BookName FROM Book
  where BookId=id;
END $$
DELIMITER ;

DELIMITER $$
CREATE FUNCTION fc_time_space(p_reader_id INT) RETURNS INT
DETERMINISTIC
BEGIN
    DECLARE loan_count INT;
    
    SELECT COUNT(*) INTO loan_count
    FROM loan
    WHERE 
        readerid = p_reader_id AND 
        loandate >= DATE_SUB(CURRENT_DATE, INTERVAL 7 DAY);
    
    RETURN loan_count;
END$$
DELIMITER ;


DELIMITER $$
CREATE PROCEDURE sp_returnBook(
IN p_loan_id INT
)

BEGIN

    DECLARE b_id INT;
    DECLARE days_overdue INT;
    DECLARE fine_amount DECIMAL(10,2);
    
    -- Get the book ID from the loan
    SELECT bookID INTO b_id 
    FROM loan 
    WHERE loanID = p_loan_id;
    
    -- Calculate days overdue
    SELECT DATEDIFF(CURDATE(), bookreturnappointmentdate) INTO days_overdue
    FROM loan
    WHERE loanid = p_loan_id AND bookreturndate IS NULL;

    UPDATE loan 
    SET bookreturndate = CURDATE(), 
        status = 'Đã trả' 
    WHERE loanid = p_loan_id;
    
    UPDATE book 
    SET amount = amount + 1 
    WHERE bookid = b_id;
    
    IF days_overdue > 0 THEN
        SET fine_amount = days_overdue * 5000;
        
        INSERT INTO punishment (loanID,daysLate, total)
        VALUES (p_loan_id, days_overdue, fine_amount);
    END IF;
    
END$$
DELIMITER ;

 
DELIMITER $$
CREATE PROCEDURE sp_findByAllLoan(IN p_search VARCHAR(255))
BEGIN

    -- Tìm theo loanid trước
    SELECT l.*, b.bookname, r.name
    FROM loan l
    JOIN book b ON l.bookid = b.bookid
    JOIN reader r ON l.readerid = r.readerid
    WHERE CAST(l.loanid AS CHAR) LIKE p_search
     OR b.bookname LIKE p_search
      OR r.name LIKE p_search;

END$$
DELIMITER ;

DELIMITER $$
CREATE FUNCTION fc_getStatisticTotalBook() RETURNS INT 
BEGIN
    DECLARE total_books INT;
    SELECT SUM(amount) INTO total_books FROM book;
    RETURN total_books;
END$$
DELIMITER ;

DELIMITER $$
CREATE FUNCTION fc_getStatisticTotalLoan() RETURNS INT
DETERMINISTIC
BEGIN
    DECLARE total_loans INT;
    SELECT COUNT(*) INTO total_loans FROM loan;
    RETURN total_loans;
END$$
DELIMITER ;

DELIMITER $$
CREATE FUNCTION fc_getStatisticLoan() RETURNS INT
DETERMINISTIC
BEGIN
    DECLARE current_loans INT;
    SELECT COUNT(*) INTO current_loans FROM loan WHERE status = 'Chưa trả';
    RETURN current_loans;
END$$
DELIMITER ;

DELIMITER $$
CREATE FUNCTION fc_getStatisticPunish() RETURNS INT
DETERMINISTIC
BEGIN
    DECLARE overdue_books INT;
    SELECT COUNT(*) INTO overdue_books
    FROM loan 
    WHERE CURRENT_DATE > bookreturnappointmentdate AND status = 'Chưa trả';
    RETURN overdue_books;
END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE sp_punish()
BEGIN
    SELECT 
        l.loanid,
        l.readerid,
        CONCAT(r.surname, ' ', r.name) AS fullname,
        l.bookid,
        b.bookname AS book,
        l.loanno,
        DATEDIFF(l.bookreturndate, l.bookreturnappointmentdate) AS dayslate,
        DATEDIFF(l.bookreturndate, l.bookreturnappointmentdate) * 5000 AS total,
        l.status
    FROM 
        loan l
    JOIN 
        reader r ON l.readerid=r.readerid
    JOIN 
        book b ON l.bookid=b.bookid
    WHERE 
        l.bookreturndate > l.bookreturnappointmentdate AND
        l.status = 'Đã trả';
END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE sp_PunishReturnYet()
BEGIN
    SELECT 
        l.loanid,
        l.readerid,
        CONCAT(r.surname, ' ', r.name) AS fullname,
        l.bookid,
        b.bookname AS book,
        l.loanno,
        DATEDIFF(CURRENT_DATE, l.bookreturnappointmentdate) AS dayslate,
        DATEDIFF(CURRENT_DATE, l.bookreturnappointmentdate) * 5000 AS total,
        l.status
    FROM 
        loan l
    JOIN 
        reader r ON l.readerid = r.readerid
    JOIN 
        book b ON l.bookid = b.bookid
    WHERE 
        CURRENT_DATE > l.bookreturnappointmentdate AND
        l.status = 'Chưa trả';
END$$
DELIMITER ;

CREATE TABLE users (
    userid INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    readerid INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (readerid) REFERENCES reader(readerid) ON DELETE CASCADE
);


DELIMITER //
CREATE PROCEDURE sp_checkUserCredentials(
    IN p_username VARCHAR(50),
    IN p_password VARCHAR(255)
)
BEGIN
    SELECT u.userid, u.username, r.readerid, r.name
    FROM users u
    JOIN readers r ON u.reader_id = r.readerid
    WHERE u.username = p_username AND u.password = p_password;
END //
DELIMITER ;

INSERT INTO reader
VALUES (1, 'admin', 'admin', 12345, 12345, CURRENT_DATE(), 'Giảng viên');
INSERT INTO users
VALUES (1, 'admin', 'admin', 1, CURRENT_DATE());
