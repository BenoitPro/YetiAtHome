> Destiné aux débutants et à but pédagogique, j'ai réalisé ce projet il y a plusieurs années à mes débuts en informatique. Il est donc loin de respecter toutes les règles de l'art en développement et en programmation. Je l'ai conçu avec l'aide de Mathieu Bonin et Anthony Maréchal. N'hésitez pas à corriger, compléter, modifier, ou d'ajouter vos remarques et bonnes pratiques soit par l'intermédiaire de Pull requests ou d'issues, c'est aussi pour ça que je le partage avec la communauté Github 💪 😀.

Nous avons ici réalisé un système de calculs distribué entres différentes machines. Le calcul choisi, qui sera donc à réaliser sur différentes machines est celui d'un produit de matrices.

Le schema ci-dessous donne une vue d'ensemble de la communication entre les différents services Java.
![alt text](calcul-distribué.png?raw=true "schema de calcul distribué")

Chaque classe Java est implémentées par un fichier java qui porte le même nom.

Afin de mettre en avant les ports d'écoutes nous leurs avons attribués un numéro.

Définition des classes selon les programmes :

# Client

- Client.java : Permet la communication entre un client et le serveur central.
- Interface.java : Décrit l'interface graphique et contiendra dans un premier temps la méthode statique "main" afin de faciliter le passage de l'application en une applet Java.


# Serveur Central

- ServeurEcouteClient.java : Thread attendant la connexion des clients sur un port donné.
- ThreadClient : Thread de dialogue avec un client connecté au serveur. 
- ServeurEcoutePart.java : Thread attendant la connexion des participants sur un port donné.
- ThreadPart.java : Thread de dialogue avec un participant connecté au serveur.
- Participant.java : Décrit un participant avec notamment, son ip, sa disponibilité, sa liste d'avis et s'il est banni ou non.
- LesPartipants.java : Contient la liste des participants connectés au serveur.
- Interface.java : Décrit l'interface graphique permettant la visualisation des clients et des participants.
- Main.java : Contient la fonction statique "main" qui instancie les classes d'écoute et l'interface.

# Participant

- ServeurParticipant.java : Serveur d'attente de connexion d'un autre participant pouvant lui demander de réaliser un calcul.
- TheadServeurConnect.java : Thread maintenant la connexion et le dialogue avec le serveur central.
- Client.java : Permet de se connecter à un autre participant pour lui demander de réaliser un calcul.
- Interface.java : Décrit une interface graphique permettant de visualiser le déroulement d'un calcul.
