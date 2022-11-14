-- table creation
DROP TABLE IF EXISTS Reviews;
DROP TABLE IF EXISTS Saves;
DROP TABLE IF EXISTS Likes;
DROP TABLE IF EXISTS Content;
DROP TABLE IF EXISTS StudySet;
DROP TABLE IF EXISTS Subjects; 
DROP TABLE IF EXISTS User;

-- DROP TABLE IF EXISTS 

CREATE TABLE User (
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
    created_by CHAR(20),
    set_name TINYTEXT NOT NULL,
    set_subject CHAR(2) NOT NULL,
    PRIMARY KEY(set_id),
    FOREIGN KEY(created_by) REFERENCES User(username),
    FOREIGN KEY(set_subject) REFERENCES Subjects(subject_code)
);
CREATE TABLE Saves (
    set_id INT UNSIGNED,
    username CHAR(20),
    FOREIGN KEY(set_id) REFERENCES StudySet(set_id),
    FOREIGN KEY(username) REFERENCES User(username),
    PRIMARY KEY(set_id, username)
);
CREATE TABLE Likes (
    set_id INT UNSIGNED,
    username CHAR(20),
    FOREIGN KEY(set_id) REFERENCES StudySet(set_id),
    FOREIGN KEY(username) REFERENCES User(username),
    PRIMARY KEY(set_id, username)
);
CREATE TABLE Content (
    question_id INT UNSIGNED AUTO_INCREMENT,
    question TINYTEXT NOT NULL,
    answer TINYTEXT NOT NULL,
    username CHAR(20),
    study_set INT UNSIGNED,
    FOREIGN KEY(username) REFERENCES User(username),
    FOREIGN KEY(study_set) REFERENCES StudySet(set_id),
    PRIMARY KEY(question_id)
);



-- populating table
INSERT INTO User VALUES("gcalvert", "1234", "George", "Calvert"),
                        ("pAbrahm", "password", "Peri", "Abrahm"),
                        ("dGiacobbi", "p123", "David", "Giacobbi"),
                        ("jDoe", "userpass", "Jane", "Doe"),
                        ("jSmith", "4321", "John", "Smith");
INSERT INTO Subjects VALUES("MA", "Math"),
                    ("SC", "Science"),
                    ("LI", "Literature"),
                    ("LA", "Language");
INSERT INTO StudySet VALUES(1, "gcalvert", "Database", "SC"),
                            (2, "jSmith", "BiologySem1 Final", "SC"),
                            (3, "gcalvert", "Math 231", "MA"),
                            (4, "jSmith", "French1", "LA"),
                            (5, "jSmith", "To kill a mocking bird", "LI"),
                            (6, "jSmith", "The Giver", "LI"),
                            (7, "jSmith", "MidsummerNights", "LI"),
                            (8, "jSmith", "Magpie Murders", "LI"),
                            (9, "gcalvert", "Discrete", "SC"),
                            (10, "gcalvert", "Software", "SC"),
                            (11, "gcalvert", "Algorithms", "SC"),
                            (12, "gcalvert", "Comm101", "LA"),
                            (13, "gcalvert", "CPSC121", "SC"),
                            (14, "gcalvert", "CPSC122", "SC"),
                            (15, "gcalvert", "PHYS101", "LA"),
                            (16, "pAbrahm", "Meaning Making", "LI"),
                            (17, "pAbrahm", "Sensations", "SC"),
                            (18, "pAbrahm", "Practices/Habits", "LI"),
                            (19, "pAbrahm", "Public Text", "LI"),
                            (20, "pAbrahm", "Writing", "LA"),
                            (21, "pAbrahm", "BioSci", "SC"),
                            (22, "dGiacobbi", "PHIL201", "SC"),
                            (23, "dGiacobbi", "Religion101", "LI"),
                            (24, "dGiacobbi", "CPSC222", "SC"),
                            (25, "dGiacobbi", "CPSC221", "SC"),
                            (26, "dGiacobbi", "Chem101L", "SC"),
                            (27, "gcalvert", "MATH259", "SC");
INSERT INTO Likes VALUES(1, "jSmith"),
                        (2, "jSmith"),
                        (3, "jSmith"),
                        (1, "dGiacobbi"),
                        (1, "jDoe"),
                        (1, "pAbrahm"),
                        (4, "jSmith"),
                        (4, "pAbrahm"),
                        (2, "pAbrahm"),
                        (23, "gcalvert"),
                        (24, "gcalvert"),
                        (26, "gcalvert");
INSERT INTO Content VALUES(1, "question", "answer", "gcalvert", 1),
                          (2, "question2", "answer2", "gcalvert", 1),
                          (3, "This is a question", "this is an answer", "gcalvert", 1),
                          (4, "How many blanks are in a blank", "17", "gcalvert", 1);

                        

                        






