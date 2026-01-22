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

## Les boucles infinies (dans les relations bi-directionnelles)

Dans un controller, si on envoie une donné ayant une relation bi-directionnelle (ex: one to many),
On va se retrouver avec une erreur étant donné que `jackson` va sérialiser cette relation en une boucle infinie.

Solutions à ce problème :

```java
import com.fasterxml.jackson.annotation.JsonProperty;

@OneToMany(mappedBy = "customer")
@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
private List<BankAccount> bankAccounts;
```

Néanmoins, la solution la plus viable restent le `DTO` :

L'idée est de transformer une `Entité` à un `DTO`, et de laisser les entitées dans le serveur seulement.

## Streams

`.stream()` & `.collect()`

```java
public List<CustomerDTO> listCustomer() {
    List<Customer> customers = customerRepository.findAll();
    List<CustomerDTO> customersDTO = customers
            .stream().map(customer -> customerMapper.toDto(customer))
            .collect(Collectors.toList());
    return customersDTO;
}
```

## Validateurs & DTO

Les DTO permettent de gérer la validation des données :

```java
public class CustomerDTO {
    @NotNull(message = "Name cannot be null")
    @Size(min = 2, message = "Name must have at least 2 characters")
    private String name;
}
```

Installer `<artifactId>spring-boot-starter-validation</artifactId>`

Mettre en place l'annotation `@Valid` dans le controller :

```java
@PostMapping
public CustomerDTO saveCustomer(@Valid @RequestBody CustomerDTO dto) {}
```

On peut aussi créer un validateur personnalisé.

## Tests


|               Élément               |                	Utilisation principale             |
|:-----------------------------------:|:------------------------------------------------------:|
|           @SpringBootTest           |      	Test d’intégration global (pas pour TU pur)      |
|             @WebMvcTest             |     	Test unitaire du contrôleur (mock du service)    |
|              @MockBean              |      	Injection d’un mock dans le contexte Spring      |
| @ExtendWith(MockitoExtension.class) |      	Pour les tests unitaires purs avec Mockito       |
|        @Mock / @InjectMocks         |     	Pour mocker les dépendances dans les services    |
|      MockMvc	                       |   Pour simuler des requêtes HTTP vers le contrôleur    |

[Pour une introduction à J UNIT](https://github.com/iliasse-e/j-unit)

[Pour une introduction à Mockito](https://github.com/iliasse-e/mockito)

Lancer un fichier de test :

```bash
mvn -Dtest=CustomerRepositoryTest test
```

Lancer une seule méthode de test :

```bash
mvn -Dtest=CustomerRepositoryTest#shouldSaveCustomer test
```

### AssertJ

[AssertJ](https://assertj.github.io/doc/) est une bibliothèque Java dédiée aux assertions dans les tests unitaires.
Elle offre une syntaxe fluide, lisible et expressive, bien plus agréable que les assertions classiques de JUnit.

```java
import static org.assertj.core.api.Assertions.*;
```

Avec JUnit classique :

```java
assertEquals("Hello", message);
```

Avec AssertJ classique :

```java
assertThat(actual)
    .isEqualTo(expected)       // égalité
    .isNotEqualTo(unexpected)  // inégalité
    .isNull()                  // null
    .isNotNull()               // non null
    .isInstanceOf(Class.class) // type
    .isSameAs(obj)             // même référence
    .isNotSameAs(obj);         // référence différente
```

Assertion sur les collections :

```java
List<String> names = List.of("Alice", "Bob", "Charlie");

assertThat(names)
    .isNotEmpty()
    .hasSize(3)
    .contains("Alice", "Bob")
    .doesNotContain("David")
    .allMatch(name -> name.length() > 2);

```

Assertion sur les exceptions :

```java
assertThatThrownBy(() -> {
    throw new IllegalArgumentException("Bad argument");
})
    .isInstanceOf(IllegalArgumentException.class)
    .hasMessageContaining("Bad");
```

On peut aussi tester des chaînes de caractères, des nombres, maps, objets.

Voir la documentation.

Intégration dans Spring boot

```java
List<Customer> result = customerRepository.findByName("ass");

assertThat(result)
    .isNotEmpty()
    .extracting(Customer::getName)
    .contains("Yassine");
```

### Tester un Service

```java
@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

  @Mock
  private CustomerRepository customerRepository;

  @Mock
  private CustomerMapperImpl customerMapper;

  @InjectMocks
  private CustomerServiceImpl customerService;
```

### Tester un Repository

#### Test unitaire (TU)

*Objectif* : tester une méthode isolée, sans dépendance réelle.

*Outils* : Mockito pour simuler le repository, JUnit 5 pour orchestrer, AssertJ pour valider.

```java
@ExtendWith(MockitoExtension.class) 
class CustomerRepositoryUnitTest { 
    @Mock 
    private CustomerRepository customerRepository;
```

#### Test d’intégration

*Objectif* : vérifier que le repository fonctionne réellement avec une base de données.

*Outils* : Spring Boot Test avec ``@DataJpaTest``, base embarquée (H2) ou Testcontainers.

```java
@DataJpaTest 
@TestPropertySource("classpath:application-test.properties")
public class CustomerRepositoryTest {

  @Autowired
  private CustomerRepository customerRepository;
```

##### ``@DataJpaTest``
*But* : lancer un contexte Spring minimal pour tester la couche JPA.

*Comportement* :
Configure uniquement les beans liés à JPA (repositories, EntityManager, etc.).

Utilise par défaut une base H2 en mémoire (sauf si tu précises une autre datasource).

Applique automatiquement ``@Transactional`` → chaque test est rollbacké après exécution.


###### ``@TestPropertySource``
*But* : charger un fichier de configuration spécifique aux tests.

Utile pour :
- Définir une datasource différente (Postgres, MySQL, etc.).
- Configurer des propriétés spécifiques (dialecte Hibernate, logging SQL, etc.).

##### ``@AutoConfigureTestDatabase``
*But* : contrôler la base utilisée pendant les tests.

*Options* :

``replace`` = ``AutoConfigureTestDatabase.Replace.NONE`` → utilise la vraie base définie dans application-test.properties.

Par défaut, Spring remplace par une base embarquée (H2).


##### ``@Transactionnal``

Déjà inclus dans @DataJpaTest.

Chaque test est exécuté dans une transaction rollbackée automatiquement → base propre à chaque test.

Tu peux l’ajouter manuellement si besoin dans d’autres contextes

##### Le fichier application-test.properties

On peut utiliser une base H2 en mémoire (comme dans l'exemple "./src/test/resources/application-test.properties") ou bien utiliser une vraie base de données (ici postgresql) :

```bash
spring.datasource.url=jdbc:postgresql://localhost:5432/testdb
spring.datasource.username=test
spring.datasource.password=test
spring.jpa.hibernate.ddl-auto=update
```

### Tester un Controller

`@WebMvcTest(ControllerClass.class)` Lance un contexte Spring minimal pour tester uniquement la couche web (controller + MVC).

`@MockBean` Permet de mocker les dépendances du controller (services).

``MockMvc`` Outil pour simuler des requêtes HTTP (GET, POST, etc.) et vérifier les réponses.

- Simuler les requêtes.

```java
mockMvc.perform(get("/customers/1"))
```

- Check statut HTTP

```java
mockMvc.perform(get("/customers/1")) 
    .andExpect(status().isOk()); // 200
```

- Check contenu JSON

```java
mockMvc.perform(get("/customers/1"))
    .andExpect(jsonPath("$.name").value("Hassan"))
    .andExpect(jsonPath("$.age").value(30));
```

- Check header

```java
mockMvc.perform(post("/customers")
    .contentType(MediaType.APPLICATION_JSON)
    .content("{\"name\":\"Nassim\"}"))
    .andExpect(status().isCreated())
    .andExpect(header().string("Location", "/customers/1"));
```

- Vérification des appels au service

```java
verify(customerService).getCustomer(1L);
```

