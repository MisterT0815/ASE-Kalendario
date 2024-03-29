# Programmentwurf
# Kalendario

Name: Debbrecht, Lars
Matrikelnummer: 5714794

Abgabedatum: 

# Kapitel 1: Einführung

## Übersicht über die Applikation
[Was macht die Applikation? Wie funktioniert sie? Welches Problem löst sie/welchen Zweck hat sie?]

## Wie startet man die Applikation
[Wie startet man die Applikation? Welche Voraussetzungen werden benötigt? Schritt-für-Schritt-Anleitung]

## Wie testet man die Applikation?
[Wie testet man die Applikation? Welche Voraussetzungen werden benötigt? Schritt-für-Schritt-Anleitung]



# Kapitel 2: Clean Architecture

## Was ist Clean Architecture?
[allgemeine Beschreibung der Clean Architecture in eigenen Worten]

## Analyse der Dependency Rule
[(1 Klasse, die die Dependency Rule einhält und eine Klasse, die die Dependency Rule verletzt);   jeweils UML der Klasse und Analyse der Abhängigkeiten in beide Richtungen (d.h., von wem hängt die Klasse ab und wer hängt von der Klasse ab) in Bezug auf die Dependency Rule]

### Positiv-Beispiel: Dependency Rule

### Negativ-Beispiel: Dependency Rule

## Analyse der Schichten
[jeweils 1 Klasse zu 2 unterschiedlichen Schichten der Clean-Architecture: jeweils UML der Klasse (ggf. auch zusammenspielenden Klassen), Beschreibung der Aufgabe, Einordnung mit Begründung in die Clean-Architecture]
### Schicht: [Name]
### Schicht: [Name]



# Kapitel 3: SOLID

## Analyse Single-Responsibility-Principle (SRP)
[jeweils eine Klasse als positives und negatives Beispiel für SRP;  jeweils UML der Klasse und Beschreibung der Aufgabe bzw. der Aufgaben und möglicher Lösungsweg des Negativ-Beispiels (inkl. UML)]
### Positiv-Beispiel
Session: einzige Aufgabe ist den aktuell angemeldeten Benutzer zu tracken. Es gibt keine weiteren Aufgaben zum speichern, Zugriffe verifizieren oder ähnliches.
### Negativ-Beispiel
Herkunft. Sehr überladene Klasse. Ist gleichzeitig Anzeigepunkt für den Besitzer von Events als auch Implementationsstelle im Plugin Layer für die Nutzung von Use Cases.

## Analyse Open-Closed-Principle (OCP)
[jeweils eine Klasse als positives und negatives Beispiel für OCP;  jeweils UML der Klasse und Analyse mit Begründung, warum das OCP erfüllt/nicht erfüllt wurde – falls erfüllt: warum hier sinnvoll/welches Problem gab es? Falls nicht erfüllt: wie könnte man es lösen (inkl. UML)?]
### Positiv-Beispiel

### Negativ-Beispiel


## Analyse Liskov-Substitution- (LSP), Interface-Segreggation- (ISP), Dependency-Inversion-Principle (DIP)
[jeweils eine Klasse als positives und negatives Beispiel für entweder LSP oder ISP oder DIP);  jeweils UML der Klasse und Begründung, warum man hier das Prinzip erfüllt/nicht erfüllt wird]
[Anm.: es darf nur ein Prinzip ausgewählt werden; es darf NICHT z.B. ein positives Beispiel für LSP und ein negatives Beispiel für ISP genommen werden]
### Positiv-Beispiel
Wiederholung
### Negativ-Beispiel
EventRepository: Create, Update in einem
wird nicht erfüllt um einen einziges Interface zum implementieren zu haben, an dass die Datenbanken angebunden werden können ohne verschiedenste Implementationen bieten zu müssen


# Kapitel 4: Weitere Prinzipien

## Analyse GRASP: Geringe Kopplung
[jeweils eine bis jetzt noch nicht behandelte Klasse als positives und negatives Beispiel geringer Kopplung; jeweils UML Diagramm mit zusammenspielenden Klassen, Aufgabenbeschreibung und Begründung für die Umsetzung der geringen Kopplung bzw. Beschreibung, wie die Kopplung aufgelöst werden kann]
### Positiv-Beispiel
### Negativ-Beispiel

## Analyse GRASP: Hohe Kohäsion
[eine Klasse als positives Beispiel hoher Kohäsion; UML Diagramm und Begründung, warum die Kohäsion hoch ist]
## Don’t Repeat Yourself (DRY)
[ein Commit angeben, bei dem duplizierter Code/duplizierte Logik aufgelöst wurde; Code-Beispiele (vorher/nachher); begründen und Auswirkung beschreiben]



# Kapitel 5: Unit Tests
## 10 Unit Tests
[Nennung von 10 Unit-Tests und Beschreibung, was getestet wird]

| Unit Test      | Beschreibung |
|----------------|--------------|
| Klasse#Methode |              |
|                |              |
|                |              |
|                |              |
|                |              |
|                |              |
|                |              |
|                |              |
|                |              |
|                |              |

## ATRIP: Automatic
[Begründung/Erläuterung, wie ‘Automatic’ realisiert wurde]

## ATRIP: Thorough
[jeweils 1 positives und negatives Beispiel zu ‘Thorough’; jeweils Code-Beispiel, Analyse und Begründung, was professionell/nicht professionell ist]

## ATRIP: Professional
[jeweils 1 positives und negatives Beispiel zu ‘Professional’; jeweils Code-Beispiel, Analyse und Begründung, was professionell/nicht professionell ist]

## Code Coverage
[Code Coverage im Projekt analysieren und begründen]

## Fakes und Mocks
[Analyse und Begründung des Einsatzes von 2 Fake/Mock-Objekten; zusätzlich jeweils UML Diagramm der Klasse]



# Kapitel 6: Domain Driven Design

## Ubiquitous Language
[4 Beispiele für die Ubiquitous Language; jeweils Bezeichung, Bedeutung und kurze Begründung, warum es zur Ubiquitous Language gehört]

| Bezeichnung | Bedeutung                                                               | Begründung                                                                                                                                       |
|-------------|-------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------|
| Event       | Überbegriff für im Kalendar anzeigbare Items                            | Event hier anders Benutzt als typischerweise im Programmierkontext, hier wird Begriff stattdessen aus Domainkontext genommen                     |
| Serie       | Menge an Events, die alle durch Wiederholung desselben Events entstehen | Wiederholbare Events, wie z.B. Geburtstage jedes Jahr, sollen so von normalen Events unterschieden werden                                        |
| Geplant     | Solche Events, die in einem bestimmten (geplanten) Zeitraum stattfinden | Unterscheidung von Events mit festem Start und Endzeitpunkt von anderen                                                                          |
| Besitzer    | Derjenige Benutzer, der ein Event erstellt hat, ist dessen Besitzer     | Klare Definition um zu sehen, wem ein Event gehört                                                                                               |
| Herkunft    | Ort/Programm/quelle an dem ein Event oder eine Serie erstellt wurde     | Das Programm soll Events aus mehreren Quellen zusammenführen. Damit klar ist wie ein Event entstanden ist, wird die Herkunft des Events benötigt |

## Entities
[UML, Beschreibung und Begründung des Einsatzes einer Entity; falls keine Entity vorhanden: ausführliche Begründung, warum es keines geben kann/hier nicht sinnvoll ist]
![EntitiesOverview.svg](EntitiesOverview.svg)

## Value Objects
[UML, Beschreibung und Begründung des Einsatzes eines Value Objects; falls kein Value Object vorhanden: ausführliche Begründung, warum es keines geben kann/hier nicht sinnvoll ist]

Nur Zeitraum, der einen Zeitraum zwischen einem Date Start und einem Date Ende beschreibt. 
Wird benötigt um feste Regeln einfacher zu machen, die sonst an vielen Stellen kontrolliert werden müssten (Start muss vor Ende sein)
Zeitraum ist ein Wert in der Domäne, so Semantik definierbar.

## Repositories
[UML, Beschreibung und Begründung des Einsatzes eines Repositories; falls kein Repository vorhanden: ausführliche Begründung, warum es keines geben kann/hier nicht sinnvoll ist]

## Aggregates
[UML, Beschreibung und Begründung des Einsatzes eines Aggregates; falls kein Aggregate vorhanden: ausführliche Begründung, warum es keines geben kann/hier nicht sinnvoll ist]
Event zum Zugriff auf Sichbarkeit

Serie zum Zugriff auf Wiederholung


# Kapitel 7: Refactoring

## Code Smells
[jeweils 1 Code-Beispiel zu 2 Code Smells aus der Vorlesung; jeweils Code-Beispiel und einen möglichen Lösungsweg bzw. den genommen Lösungsweg beschreiben (inkl. (Pseudo-)Code)]

## 2 Refactorings
[2 unterschiedliche Refactorings aus der Vorlesung anwenden, begründen, sowie UML vorher/nachher liefern; jeweils auf die Commits verweisen]



# Kapitel 8: Entwurfsmuster
[2 unterschiedliche Entwurfsmuster aus der Vorlesung (oder nach Absprache auch andere) jeweils sinnvoll einsetzen, begründen und UML-Diagramm]
## Entwurfsmuster: [Name]
## Entwurfsmuster: [Name]