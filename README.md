# AlgorithmeGenetique
# Projet d'Optimisation par Algorithme Génétique

Ce projet vise à maximiser la fonction objectif $$f(x) = -x^2+4x$$ dans l'intervalle [1, 30] en utilisant un algorithme génétique. Le problème est modélisé avec une population initiale de 4 individus (chromosomes) codés sur 5 bits (gènes). 

L'algorithme génétique applique un croisement selon la méthode uniforme et génère de nouvelles solutions par mutation et croisement. Les nouvelles solutions sont acceptées si leur fitness est amélioré. Les meilleures solutions actuelles sont sélectionnées pour la nouvelle génération.

Quatre scénarios sont expérimentés avec différentes valeurs de probabilité de croisement (Pc), probabilité de mutation (Pm) et nombre maximum de générations (max-generation). 

Des statistiques sont collectées pour chaque scénario, y compris le temps d'exécution de l'algorithme, le vecteur des fitness, la fitness moyenne et maximale, et le nombre de croisements et de mutations effectués. 

Des graphiques sont tracés pour comparer les quatre scénarios et analyser l'impact des valeurs de Pc, Pm, et max-generation sur la maximisation de la fitness, ainsi que l'impact du nombre de croisements et de mutations sur la maximisation de la solution. 

Le projet comprend le code source en Java.
