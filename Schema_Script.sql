use bd_colocviu_partial;

CREATE TABLE Zboruri (
    nrz INT PRIMARY KEY,
    de_la VARCHAR(50),
    la VARCHAR(50),
    distanta INT,
    plecare TIME,
    sosire TIME
    
   
);


CREATE TABLE Aeronave (
    idav INT PRIMARY KEY,
    numeav VARCHAR(50),
    gama_croaziera INT
);


CREATE TABLE Certificare (
    idan INT,
    idav INT,
    FOREIGN KEY (idav) REFERENCES Aeronave(idav),
    FOREIGN KEY (idan) REFERENCES Angajati(idan)
);

CREATE TABLE Angajati (
    idan INT PRIMARY KEY,
    numean VARCHAR(50),
    functie VARCHAR(50),
    salariu INT
    
);

 show tables;
 
 ALTER TABLE Zboruri
ADD COLUMN zi VARCHAR(10);
 
ALTER TABLE Zboruri ADD CONSTRAINT check_zi
    CHECK (zi IN ('Lu', 'Ma', 'Mi', 'Jo', 'Vi', 'Sa', 'Du'));
    
ALTER TABLE Angajati ADD CONSTRAINT check_salariu_director
    CHECK (NOT (LOWER(functie) LIKE '%director%' AND salariu <= 10000));
    

INSERT INTO Angajati(idan,numean,functie,salariu)
VALUES
(1,'George Iulian','Pilot',12000),
(2,'Ioan Alexandru','Pilot',8000),
(27,'Bogdan Constantinescu','Director General',20000),
(103,'Sonia Todica','Control Trafic Aerian',4500),
(3,'Laurențiu Iancu','Pilot',10000),
(404,'Florin Stănescu','Agent de securitate',3500);

INSERT INTO Zboruri(nrz,de_la,la,distanta,plecare,sosire,zi)
VALUES
(1,'Munchen','Madrid',1497,'08:00','10:45','Mi'),
(2,'Singapore','Houston',15997,'01:00','23:05','Lu'),
(3,'Taipei','Macau',874,'09:15','10:43','Vi'),
(4,'Bacau','Budapesta',880,'14:30','16:05','Ma'),
(5,'Berlin','Amsterdam',594,'21:30','22:50','Vi'),
(6,'Paris','Barcelona',723,'04:30','06:00','Ma'),
(7,'Varsovia','Larnaca',2168,'07:20','11:05','Vi'),
(8,'Tokyo','Seoul',950,'17:35','19:40','Jo');    

INSERT INTO Aeronave(idav,numeav,gama_croaziera)
VALUES
(1,'BOEING 737-800',5420),
(11,'BOEING 737-700',6000),
(13,'BOEING 737-300',4600),
(2,'AIRBUS 318-111',5750),
(3,'ATR 42-500',1345),
(25,'Comac C919',5555),
(7,'Embraer E-Jet E2 family',4815),
(8,'Sukhoi Superjet SSJ100',4150);

INSERT INTO Certificare(idan,idav)
VALUES
(2,1),
(2,13),
(2,25),
(3,1),
(3,2),
(3,3);
  
-- 13.03.a)Să se găsească detaliile zborurilor cu distanța între 500 și 1000.

Select*
from zboruri
WHERE(distanta>=500 AND distanta<=1000);

-- 13.03.b)Să se găsească zborurile din zilele ’Ma’ și ’Vi’, ordonat crescător după plecare.

SELECT *
FROM Zboruri
WHERE zi IN ('Ma', 'Vi')
ORDER BY plecare ASC;

-- 13.04.a)Să se găsească zborurile operate de aeronave ‘BOEING’.

Select* 
From Aeronave Join Zboruri ON Aeronave.gama_croaziera>Zboruri.distanta
WHERE Aeronave.numeav LIKE 'BOEING%';

-- 13.04.b)Să se găsească perechi de aeronave (idav1, idav2) cu condiția să existe pilot certificat pentru ambele. O pereche este unică în rezultat.

SELECT DISTINCT C1.idav AS idav1, C2.idav AS idav2
FROM Certificare C1
JOIN Certificare C2 ON C1.idan = C2.idan AND C1.idav < C2.idav;

-- 13.05.a)Să se găsească salariul maxim al piloților certificați pe aeronave ‘AIRBUS’.

SELECT MAX(salariu)
FROM Angajati
WHERE idan IN (SELECT idan FROM Certificare WHERE idav IN (SELECT idav FROM Aeronave WHERE numeav LIKE 'AIRBUS%'));

-- 13.05.b)Să se găsească detaliile aeronavelor pentru care pilotul ‚Ioan Alexandru’ are certificare.

SELECT *
FROM Aeronave
WHERE idav IN (SELECT idav FROM Certificare WHERE idan IN (SELECT idan FROM Angajati WHERE numean = 'Ioan Alexandru'));

-- 13.06.a)Să se găsească pentru fiecare aeronavă numărul de piloți certificați (numeav, număr_piloți).

SELECT Aeronave.numeav, COUNT(Certificare.idan) AS numar_piloti
FROM Aeronave
LEFT JOIN Certificare ON Aeronave.idav = Certificare.idav
GROUP BY Aeronave.numeav;

-- 13.06.b)Să se găsească salariul mediu al angajaților pentru fiecare funcție (funcție, salariu_mediu).
SELECT functie, AVG(salariu) AS salariu_mediu
FROM Angajati
GROUP BY functie;

-- 13.07.a)Să se introducă în BD faptul că pilotul George Iulian (cu idan 1) are certificare pentru trei aeronave de tip ’Boeing’, 737-800 cu gama croazieră 5420, 737-700 cu gama croazieră 6000 și 737-300 cu gama croazieră 4600.
INSERT INTO Certificare (idan, idav)
VALUES 
(1, (SELECT idav FROM Aeronave WHERE numeav = 'Boeing 737-800')),
(1, (SELECT idav FROM Aeronave WHERE numeav = 'Boeing 737-700')),
(1, (SELECT idav FROM Aeronave WHERE numeav = 'Boeing 737-300'));

-- 13.07.b)Să se șteargă aeronavele pentru care nu există cerificare.

DELETE FROM Aeronave
WHERE idav NOT IN (SELECT DISTINCT idav FROM Certificare);

-- 13.07.c)Să se modifice distanța în Zboruri între aeroporturile ’Munchen’ și ’Madrid’ scăzând 4.

UPDATE Zboruri
SET distanta = distanta - 4
WHERE de_la = 'Munchen' AND la = 'Madrid';

-- 13.08.Să se definească o procedură stocată care va introduce în tabela Excepții
-- acele linii din tabela Aeronave cu condiția există certificare pentru aeronavă și
-- salariul angajatului certificat este fie cel mai mic salariu, fie cel mai mare salariu
-- între piloții certificați. Tabela Excepții va avea aceleași coloane ca și tabela
-- Aeronave plus o coloană ce indică natura excepției.

CREATE TABLE Exceptii (
    idav INT  ,
    numeav VARCHAR(50),
    gama_croaziera INT,
    natura_exceptiei VARCHAR(50),
      FOREIGN KEY (idav) REFERENCES Aeronave(idav)
);

DELIMITER //

CREATE PROCEDURE IntroduceExceptii()
BEGIN
    -- Selectăm salariul minim și maxim dintre piloții certificați
    DECLARE min_salariu DECIMAL;
    DECLARE max_salariu DECIMAL;

    SELECT MIN(salariu), MAX(salariu) INTO min_salariu, max_salariu
    FROM Angajati
    WHERE idan IN (SELECT idan FROM Certificare);

    -- Inserăm în Excepții aeronavele cu piloții având salariul minim sau maxim
    INSERT INTO Exceptii (idav, numeav, gama_croaziera, natura_exceptiei)
    SELECT idav, numeav, gama_croaziera,
           CASE 
               WHEN salariu = min_salariu THEN 'Salariu Minim'
               WHEN salariu = max_salariu THEN 'Salariu Maxim'
           END AS natura_exceptiei
    FROM (
        SELECT 
            A.idav,
            A.numeav,
            A.gama_croaziera,
            AN.salariu,
            ROW_NUMBER() OVER (PARTITION BY A.idav ORDER BY AN.salariu) AS rn
        FROM Aeronave A
        JOIN Certificare C ON A.idav = C.idav
        JOIN Angajati AN ON C.idan = AN.idan
        WHERE AN.salariu = min_salariu OR AN.salariu = max_salariu
    ) AS RankedData
    WHERE rn = 1; -- Un Window function care ne asigura faptul ca trecem prin fiecare idav o singura data
END//

DELIMITER ;
Call IntroduceExceptii;
Select* from exceptii;

-- 13.09. Să se definească triggere pentru:
-- a) A asigura că la adăugarea sau ștergerea unei certificări, salariul angajatului
-- crește respectiv scade cu 100.
CREATE TRIGGER trg_adauga_certificare
AFTER INSERT ON Certificare
FOR EACH ROW
UPDATE Angajati
SET salariu = salariu + 100
WHERE idan = NEW.idan;

CREATE TRIGGER trg_sterge_certificare
AFTER DELETE ON Certificare
FOR EACH ROW
UPDATE Angajati
SET salariu = salariu - 100
WHERE idan = OLD.idan;

DELETE FROM certificare where idav=13;
select* from certificare;
select* from angajati;

-- Să se definească un trigger instead-of pentru a permite adăugare prin această vedere. (un
-- pilot poate fi certificat pe mai multe aeronave, o aeronavă poate avea mai mulți piloți
-- certificați)

CREATE VIEW PilotiAeronave AS
SELECT a.idan, numean, salariu, numeav, ae.idav, gama_croaziera
FROM Angajati a, Certificare c, Aeronave ae
WHERE c.idan = a.idan AND
ae.idav = c.idav;


select* from pilotiaeronave;
select* from zboruri;
