# Notes about the project

## The team

- Oliver Donovan: donovan1.oliver@gmail.com

(je suis tout seul car le nombre d'élèves est impair)

what did you do ? 

J'ai fait les flags naive, antiAffinity, balance, noViolations et statEnergy.
Je n'est pas fait les bonus par manque de temps.

what failed, what succeeded ?

J'ai pas réussi le flag noViolations, les pénalité reste positive.
Sinon antiAffinity marche avec succes, les VMs sont bien à l'emplacement indiqué.
Le Balancing qui compare le MIPS de chaque host a moins de pénalité que celle de la naive.
Et le statEnergy à moins d'utilisation d'énergy que les précédents.

how did you do ? why ?

Le antiAffinity est fait grâce au HashMap, car la compléxité de l'algorithme est plus rapide que celle faite avec une liste.
Le balancing a aussi un HashMap mais représentant le taux de MIPS par host, pour trouver le maximum des MIPS j'ai du utiliser 
un Entry.
Le statEnergy est utiliser avec la même technique que antiAffinity mais avec un interval plus réduit, ainsi on minimise l'espace 
RAM perdu par chaque host.

Interesting project or not ?

Le projet est interressant car il montre plus en détail l'univers du cloud et de ses problèmes complexes(comme les algorithmes).
Cependant j'ai eu des difficultés à comprendre le code, car il y avait beaucoup de classes caché (comme Host), et les problèmes
énoncerne sont pas très trivial.



## Comments
