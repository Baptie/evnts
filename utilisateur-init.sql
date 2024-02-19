USE utilisateur;

CREATE TABLE User (
                      idUser INT AUTO_INCREMENT PRIMARY KEY,
                      email VARCHAR(255) NOT NULL,
                      pseudo VARCHAR(255) NOT NULL,
                      description TEXT,
                      status VARCHAR(50),
                      photo VARCHAR(255)
);

CREATE TABLE Message (
                         idMessage INT AUTO_INCREMENT PRIMARY KEY,
                         contenu TEXT NOT NULL,
                         dateEnvoi DATETIME NOT NULL,
                         idSender INT,
                         idReceiver INT,
                         FOREIGN KEY (idSender) REFERENCES User(idUser),
                         FOREIGN KEY (idReceiver) REFERENCES User(idUser)
);


-- Créer les utilisateurs Vince et Sid
INSERT INTO User (email, pseudo, description, status, photo) VALUES ('vince@bg.com', 'Vince', 'Description de Vince', 'Actif', 'nerd.png');
INSERT INTO User (email, pseudo, description, status, photo) VALUES ('sid@gital.com', 'Sid', 'Description de Sid', 'Actif', 'chauve.png');

-- Ajouter deux messages entre Vince et Sid
INSERT INTO Message (contenu, dateEnvoi, idSender, idReceiver) VALUES ('Salut Sid, comment ça va ?', NOW(), 1, 2);
INSERT INTO Message (contenu, dateEnvoi, idSender, idReceiver) VALUES ('Salut Vince, ça va bien, merci !', NOW(), 2, 1);

