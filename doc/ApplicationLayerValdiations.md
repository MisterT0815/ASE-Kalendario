- Alle:
  - Schreibzugriff bei Schreiben:
    - angemeldeter muss Besitzer sein (über Herkunft)
  - Lesezugriff beim Lesen;
    - angemeldeter muss Besitzer sein, oder über Sichtbarkeit Lesezugriff haben
- 
- Creation:
  - Benutzer:
    - Name darf noch nicht existieren
  - Herkunft:
    - Es darf nur eine CLI Herkunft per Benutzer existieren
  - Event:
    - Herkunft muss existieren und Benutzer schreibzugriff darauf haben
  -Machbar:
    - setzeGetan muss gesetzt werde (nicht per konstruktor, wegen weiteren checks in domain layer)
  - Serie: 
    - DefaultEvent muss auf Serie zeigen

- Update:
  - Benutzer
    - nur für angemeldeten Benutzer
    - nur mit Passwortcheck
  - Event:
    - Herkunft Änderung
      - nicht möglich
    - Titel Änderung
      - keine Validierung
    - Sichtbarkeit Änderung
      - Benutzer muss validiert werden
    - Beschreibung Änderung
      - keine Validierung
    - Serie Änderung
      - nicht möglich
  - Serie:
    - defaultEventÄnderung:
      - nicht möglich
    - StartÄnderung:
      - keine Validierung
    - Wiederholung Änderung:
      - Löschen aller angepassten Events (Da ursprüngliche Zeitpunkte nicht mehr passen)
    - Event an Stelle anpassen (durch SerienEventAnpassung):
      - Originalzeitpunkt in Wiederholung überprüfen (changeEventAnZeitpunkt macht das im Domain)
      - Event muss auf Serie zeigen
      - Herkünfte müssen gleich sein
      - Serie muss auf Event zeigen

- Delete:
  - Benutzer:
    - alle Herkünfte mit Löschen
  - Herkunft:
    - alle Events Löschen
  - Event:
    - Serie Löschen (wenn defaultEvent)
    - angepasstes Event zu normales zurücksetzen (wenn angepasstes Event in Serie)
  - Serie:
    - Events der Serie löschen (default und angepasste)