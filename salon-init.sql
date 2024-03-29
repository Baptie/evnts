USE salon;


CREATE TABLE Membre (
                        idMembre INT AUTO_INCREMENT PRIMARY KEY,
                        nomMembre VARCHAR(255) NOT NULL
);

CREATE TABLE Salon (
                       idSalon INT AUTO_INCREMENT PRIMARY KEY,
                       nomSalon VARCHAR(255) NOT NULL,
                       nomCreateur VARCHAR(255) NOT NULL,
                       logo VARCHAR(255)
);

CREATE TABLE SalonMembre (
                             idSalon INT,
                             idMembre INT,
                             FOREIGN KEY (idSalon) REFERENCES Salon(idSalon),
                             FOREIGN KEY (idMembre) REFERENCES Membre(idMembre),
                             PRIMARY KEY (idSalon, idMembre)
);

CREATE TABLE Evenement (
                           idEvenement INT AUTO_INCREMENT PRIMARY KEY,
                           nomEvenement VARCHAR(255) NOT NULL,
                           nombrePersonneMax INT,
                           details TEXT,
                           dateEvenement DATE,
                           lieu VARCHAR(255),
                           isValide BOOLEAN,
                           nomCreateur VARCHAR(255) NOT NULL,
                           idSalon INT,
                           FOREIGN KEY (idSalon) REFERENCES Salon(idSalon)
);



-- Insérer un salon
INSERT INTO Salon (nomSalon, nomCreateur, logo) VALUES ('Mon Premier Salon', 'Vince', 'https://upload.wikimedia.org/wikipedia/commons/4/47/PNG_transparency_demonstration_1.png');

-- Insérer des membres
INSERT INTO Membre (nomMembre) VALUES ('Vince');
INSERT INTO Membre (nomMembre) VALUES ('Camacho');

-- Associer des membres à un salon
INSERT INTO SalonMembre (idSalon, idMembre) VALUES (1, 1); -- Associer Vince à Mon Premier Salon
INSERT INTO SalonMembre (idSalon, idMembre) VALUES (1, 2); -- Associer Camacho à Mon Premier Salon

-- Ajouter un événement au salon
INSERT INTO Evenement (nomEvenement, nombrePersonneMax, details, dateEvenement, lieu, isValide, nomCreateur, idSalon)
VALUES ('Jeux de société', 100, 'Super jeux in coming !', '2024-02-20', 'Chez oim', false, 'Vince', 1);

