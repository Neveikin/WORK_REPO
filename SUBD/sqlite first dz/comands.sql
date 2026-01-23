CREATE TABLE Student (
    id INTEGER PRIMARY KEY,
    fam TEXT NOT NULL,
    name TEXT NOT NULL,
    year_b INTEGER,
    gr INTEGER,
    FOREIGN KEY (gr) REFERENCES Gruppa(gr)
);

CREATE TABLE Gruppa (
    gr INTEGER PRIMARY KEY,
    sp TEXT,
    year_n INTEGER
);


CREATE TABLE Specialnost (
    id INTEGER PRIMARY KEY,
    shifr TEXT NOT NULL,
    name TEXT NOT NULL
);

ALTER TABLE Gruppa ADD COLUMN shifr TEXT;

INSERT INTO Gruppa (gr, year_n, shifr)
VALUES (1994, 2023, '09.03.01');

INSERT INTO Student (id, fam, name, year_b, gr)
VALUES (265, 'Невейкин', 'Илья', 2008, 1994);

INSERT INTO Specialnost (shifr, name)
VALUES ('09.03.01', 'Информатика');

UPDATE Student
SET year_b = 2004
WHERE id = 265;

DELETE FROM Student
WHERE id = 265;

SELECT s.*
FROM Student s
JOIN Gruppa g ON s.gr = g.gr
WHERE g.shifr LIKE '09%';