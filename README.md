Le schema Fig. X Page X, donne une vue d'ensemble de la communication entre
les différentes classes Java.

Chaque classe java sera implémentées par un fichier java qui portera le meme nom.

Afin de mettre en avant les ports d'écoutes nous leurs avons attribués un numéro.

Définition des classes selon les programmes :

Client :

Client.java : Permet la communication entre un client et le serveur central
Interface.java : Décrit l'interface graphique et contiendra dans un premier temps la méthode static "main" afin de faciliter le passage de l'application en une applet Java.


Serveur Central : 
ServeurEcouteClient.java : Thread attendant la connexion des clients sur un port donné.
ThreadClient : Thread de dialogue avec un client connecté au serveur.

ServeurEcoutePart.java : Thread attendant la connexion des participants sur un port donné.
ThreadPart.java : Thread de dialogue avec un participant connecté au serveur.

Participant.java : Décrit un participant avec notamment, son ip, sa disponibilité, sa liste d'avis et s'il est banis ou non.
LesPartipants.java : Contient la liste des participants connectés au serveur.

Interface.java : Décrit l'interface graphique permettant la visualisation des clients et des participants.

Main.java : Contient la fonction static "main" qui instanci les classes d'écoute et l'interface.

Participant :
ServeurParticipant.java : Serveur d'attente de connexion d'un autre participant 
pouvant lui demander de réaliser un calcul.

TheadServeurConnect.java : Thread maintenant la connexion et le dialogue avec le serveur central.

Client.java : Permet de se connecter à un autre participant pour lui demander de réaliser un calcul.

Interface.java : Décrit une interface graphique permettant de visualiser le déroulement d'un calcul.