# Notes about the project

## The team

- Oliver Donovan: donovan1.oliver@gmail.com

(je suis tout seul car le nombre d'�l�ves est impair)

what did you do ? 

J'ai fait les flags naive, antiAffinity, balance, noViolations et statEnergy.
Je n'est pas fait les bonus par manque de temps.

what failed, what succeeded ?

J'ai pas r�ussi le flag noViolations, les p�nalit� reste positive.
Sinon antiAffinity marche avec succes, les VMs sont bien � l'emplacement indiqu�.
Le Balancing qui compare le MIPS de chaque host a moins de p�nalit� que celle de la naive.
Et le statEnergy � moins d'utilisation d'�nergy que les pr�c�dents.

how did you do ? why ?

Le antiAffinity est fait gr�ce au HashMap, car la compl�xit� de l'algorithme est plus rapide que celle faite avec une liste.
Le balancing a aussi un HashMap mais repr�sentant le taux de MIPS par host, pour trouver le maximum des MIPS j'ai du utiliser 
un Entry.
Le statEnergy est utiliser avec la m�me technique que antiAffinity mais avec un interval plus r�duit, ainsi on minimise l'espace 
RAM perdu par chaque host.

Interesting project or not ?

Le projet est interressant car il montre plus en d�tail l'univers du cloud et de ses probl�mes complexes(comme les algorithmes).
Cependant j'ai eu des difficult�s � comprendre le code, car il y avait beaucoup de classes cach� (comme Host), et les probl�mes
�noncerne sont pas tr�s trivial.



## Comments
