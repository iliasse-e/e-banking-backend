# e-Banking application backend

Ce fichier est destiné à résumer l'ensemble des éléments d'apprentissage présent dans ce projet.

## Getting Started

![Capture d'écran 2025-09-08 221840.png](./src/main/resources/Capture%20d'écran%202025-09-08%20221840.png)

## Mapping d'une relation d'héritage

Dans le diagramme, on a une relation d'héritage entre `BankAccount` et les deux class `CurrentAccount` et `SavingAccount`.

L'héritage se fait côté JAVA.
Côté SGBD, l'héritage n'existe pas, et pour le mapping d'une relation d'héritage, JPA nous donne 3 choix différents, qui sont les suivants.

Les 3 stratégies :

* Single Table
* Table per class
* Joined Table

- `Single Table` est suffisant pour la plupart des cas.

- Dès lors que qu'on a plus de colonne `null` que de colonnes renseignées, `Table per class` devient la stratégie appropriée.

- `Joined Table` créé 3 tables, et créé des jointures à chaque invocation, ce qui peut etre coûteux.

### Les Annotations JPA :

`@Inheritence()` : Concerne la classe parent

On lui renseigne la stratégie choisie.

```java
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class BankAccount {}
```

`@DiscriminitatorColomn()` : Concerne la colone de la classe parent

Dans le cas d'une stratégie `SINGLE TABLE`,
On renseigne ensuite le nom et la taille de la colonne qui va permettre de discriminer les classes dérivées.

```java
@DiscriminatorColumn(name = "TYPE",length = 4)
public class BankAccount {}
```

`@DiscriminatorValue()` : Concerne la/les classe dérivée

Toujours dans le cas de la stratégie `SINGLE TABLE`,
On renseigne dans la classe dérivée, la valeur que celle ci doit prendre dans la table.

```java
@DiscriminatorValue("SA") // Dans la colonne TYPE, la valeur sera "SA"
public class SavingAccount extends BankAccount { }
```

### Annotation `mappedBy`

Dans une relation bidirectionnelle (ici OneToMany), si on ne renseigne pas mappedBy, JPA va créer deux foreign keys,
alors qu'une seule FK est necessaire pour représenter la relation.
```java
public class Customer {
    ...
    @OneToMany(mappedBy = "customer")
    private List<BankAccount> bankAccounts;
}
```

## Enregistrer un enumerateur en base de données


Par défault, un énumérateur devient un chiffre en base de données :

| Status |
|:------:|
|   0    |
|   1    |
|   0    |

Pour persister une valeur (string), il faut utiliser l'annotation `@Enumerated()` :

```java
@Enumerated(EnumType.STRING)
private AccountStatus status;
```

Résultat en base de données :


|    Status    |
|:------------:|
|   CREATED    |
|  ACTIVATED   |
|   CREATED    |

## Les logs

## Gestion des erreurs

Erreurs surveillées
Try catch et signature