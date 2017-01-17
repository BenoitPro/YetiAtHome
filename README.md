Le schema Fig. X Page X, donne une vue d'ensemble de la communication entre
les diff�rentes classes Java.

Chaque classe java sera impl�ment�es par un fichier java qui portera le meme nom.

Afin de mettre en avant les ports d'�coutes nous leurs avons attribu�s un num�ro.

D�finition des classes selon les programmes :

Client :

Client.java : Permet la communication entre un client et le serveur central
Interface.java : D�crit l'interface graphique et contiendra dans un premier temps la m�thode static "main" afin de faciliter le passage de l'application en une applet Java.


Serveur Central : 
ServeurEcouteClient.java : Thread attendant la connexion des clients sur un port donn�.
ThreadClient : Thread de dialogue avec un client connect� au serveur.

ServeurEcoutePart.java : Thread attendant la connexion des participants sur un port donn�.
ThreadPart.java : Thread de dialogue avec un participant connect� au serveur.

Participant.java : D�crit un participant avec notamment, son ip, sa disponibilit�, sa liste d'avis et s'il est banis ou non.
LesPartipants.java : Contient la liste des participants connect�s au serveur.

Interface.java : D�crit l'interface graphique permettant la visualisation des clients et des participants.

Main.java : Contient la fonction static "main" qui instanci les classes d'�coute et l'interface.

Participant :
ServeurParticipant.java : Serveur d'attente de connexion d'un autre participant 
pouvant lui demander de r�aliser un calcul.

TheadServeurConnect.java : Thread maintenant la connexion et le dialogue avec le serveur central.

Client.java : Permet de se connecter � un autre participant pour lui demander de r�aliser un calcul.

Interface.java : D�crit une interface graphique permettant de visualiser le d�roulement d'un calcul.