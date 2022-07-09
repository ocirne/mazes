---
layout: page
title: Mazes
---

Ausgangspunkt war das Buch [Mazes for Programmers](https://pragprog.com/titles/jbmaze/mazes-for-programmers/) von Jamis Buck.

## Raster

Derzeit sind Raster mit kartesischen und Polarkoordinaten sowie welche auf der Basis von Dreiecken, Sechsecken und einer Mischung aus Vier- und Achtecken (Upsilon) implementiert.

![different grids without connected cells](images/all_grids_plain.png)

Die folgenden Algorithmen machen aus diesen Rastern Irrgärten durch Entfernen von Wänden zwischen den Zellen.


## Algorithmen

### Binary Tree

Der erste Algorithmus im Buch ist *BinaryTree*.
Eine Besonderheit ist, dass jede Zelle unabhängig von anderen entscheidet, welche Wand sie entfernt.
Da immer die Richtung Norden oder Osten entfernt werden, entstehen charakteristische durchgehende Wege am oberen und rechten Rand des Irrgartens:

![algorithm BinaryTree on all grids](images/all_grids_BinaryTree.png)

Auch die Textur der Irrgärten ist immer ähnlich:

![textures of binarytree](images/cartesian_BinaryTree_colorized.png)


### Aldous Broder

Der Algorithmus *Aldous Broder* kann alle möglichen Irrgärten erzeugen:

![algorithm Aldous Broder on all grids](images/all_grids_AldousBroder.png)

Dadurch gibt es kein wiedererkennbares Muster in den Irrgärten:

![textures of Adlous Broder](images/cartesian_AldousBroder_colorized.png)


### Wilsons

Der Algorithmus *Wilsons* kann ebenfalls alle möglichen Irrgärten erzeugen:

![algorithm Wilsons on all grids](images/all_grids_Wilsons.png)

Sie sollten ähnlich wie die von *Aldous Broder* erzeugten Irrgärten aussehen:

![textures of wilsons](images/cartesian_Wilsons_colorized.png)

Sowohl *Aldous Broder* als auch *Wilsons* können zwar alle Irrgärten erzeugen, sind aber relativ teuer und durch statistische Normalverteilung haben die Irrgärten immer ähnliche Texturen.
Die folgenden Algorithmen versuchen beide Probleme zu lösen.


### Hunt And Kill

Über *Hunt And Kill* habe ich grad nichts Besonderes zu erzählen:

![algorithm hunt and kill on all grids](images/all_grids_HuntAndKill.png)

Die Textur zeichnet sich durch lange Wege und wenige Sackgassen aus:

![textures of hunt and kill](images/cartesian_HuntAndKill_colorized.png)


### Recursive Backtracker

Der Algorithmus *Recursive Backtracker* hat eine sehr schöne Implementierung.
Zudem ist er effizient, weil jede Zelle exakt zweimal betrachtet wird:

![algorithm recursive backtracker on all grids](images/all_grids_RecursiveBacktracker.png)

Dadurch ergibt sich eine Textur mit langen, mäandernden Wegen:

![textures of recursive backtracker](images/cartesian_RecursiveBacktracker_colorized.png)


### Growing Tree

Der Algorithmus *Growing Tree* ist eine parameterisierte Implementierung anderer Algorithmen.
Als Parameter wird eine Funktion genutzt, die die nächste Zelle angibt, von der aus weiter gesucht wird.
Zunächst die Variante, wo die nächste Zelle *random* ausgewählt wird:

![algorithm growing tree with variant random](images/all_grids_GrowingTree_random_colorized.png)

Mit der Variante *last* hat man den *Recursive Backtracker* nochmal implementiert:

![algorithm growing tree with variant last](images/all_grids_GrowingTree_last_colorized.png)

Durch Mischung der Funktionen kann man die Charakteristiken der einzelnen Algorithmen mischen, hier eine Mischung aus *last* und *random*: 

![algorithm growing tree with variant mix](images/all_grids_GrowingTree_mix_colorized.png)
