-- table creation
DROP TABLE IF EXISTS Reviews;
DROP TABLE IF EXISTS StudySet;
DROP TABLE IF EXISTS Subjects; 
DROP TABLE IF EXISTS User;
-- DROP TABLE IF EXISTS 

CREATE TABLE User(
    username CHAR(20),
    pass CHAR(20) NOT NULL,
    fname TINYTEXT NOT NULL,
    lname TINYTEXT NOT NULL,
    PRIMARY KEY(username)
);
CREATE TABLE Subjects (
    subject_code CHAR(2) NOT NULL,
    subject_name TINYTEXT NOT NULL,
    PRIMARY KEY(subject_code)
);
CREATE TABLE StudySet (
    set_id INT UNSIGNED AUTO_INCREMENT,
    username CHAR(20),
    set_name TINYTEXT NOT NULL,
    set_subject CHAR(2) NOT NULL,
    PRIMARY KEY(set_id, username),
    FOREIGN KEY(username) REFERENCES User(username),
    FOREIGN KEY(set_subject) REFERENCES Subjects(subject_code)
);
CREATE TABLE Reviews (
    review_id INT UNSIGNED AUTO_INCREMENT,
    set_id INT NOT NULL,
    username CHAR(20) NOT NULL,
    created_user CHAR(20) NOT NULL,
    review INT UNSIGNED NOT NULL,
    PRIMARY KEY(review_id),
    FOREIGN KEY(username) REFERENCES User(username),
    FOREIGN KEY(created_user) REFERENCES StudySet(username),
    CHECK(username != created_user),
    CHECK(review <= 5)
);

-- populating table
INSERT INTO User VALUES("gcalvert", "1234", "George", "Calvert");
