# evnts

# Depuis la racine du projet, lancer les commandes suivantes :

cd authentification

docker build -t mon-application-authentification .

cd ../gestionSalon


docker build -t mon-application-salon .


cd ../gestionUtilisateur


docker build -t mon-application-utilisateur .

cd ..

docker compose up -d
