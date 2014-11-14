nPuzzle10447768
===============
![My image](https://raw.githubusercontent.com/MarcSelles/nPuzzle10447768/master/nPuzzle%20readme/Kies%20afbeelding.png)

Wanneer het spel voor de eerste keer opgestart wordt, moet er gekozen worden met welke afbeelding gespeeld wordt.
De gebruiker kan, door middel van omlaag en omhoog swipen, een afbeelding kiezen. Wanneer hij een afbeelding uitgekozen heeft hoeft hij hier alleen maar op te tikken, waardoor hij verder gaat naar het volgende scherm.

![My image](https://raw.githubusercontent.com/MarcSelles/nPuzzle10447768/master/nPuzzle%20readme/Begin%20scherm%20spel.png)

De gebruiker krijgt het hele plaatje te zien, verdeeld in n vakjes. Deze afbeelding moet de gebruiker zien te maken. Na 3 seconden verdwijnt deze afbeelding

![My image](https://raw.githubusercontent.com/MarcSelles/nPuzzle10447768/master/nPuzzle%20readme/spel%20random.png)

De vakjes worden willekeurig geplaatst in het scherm. Hiermee wordt het spel gestart. De gebruiker kan een vakje opschuiven wanneer 1 van de vakjes om het vakje heen het lege vakje is. Er moet dan getikt worden op het vakje om hem te verplaatsen naar het lege vakje.

![My image](https://raw.githubusercontent.com/MarcSelles/nPuzzle10447768/master/nPuzzle%20readme/Menu.png)

Wanneer de gebruiker tijdens het spelen op de knop MENU klikt, komt hij in bovenstaand scherm. Hier kan hij de puzzel resetten, waardoor de vakjes weer op een willekeurige plaats worden gezet, om de moeilijkheid te veranderen, wat standaard op medium staat, en om het spel te stoppen, waardoor de gebruiker weer terug naar het scherm gaat om een afbeelding te kiezen.

![My image](https://raw.githubusercontent.com/MarcSelles/nPuzzle10447768/master/nPuzzle%20readme/Uitgespeeld.png)

Als de gebruiker het spel heeft uitgespeeld, dit gebeurt wanneer de puzzel er net zo uitziet als in de tweede afbeelding, krijgt hij een berichtje waarin informatie staat over zijn spel. Nu kan hij door middel van START EEN NIEUW SPEL terug naar een afbeelding uitkiezen

Classes:

Er wordt gebruik gemaakt van een class Field. Hierin wordt opgeslagen waar alle vakjes gepositioneerd zijn in het spel. Wanneer er een spel gestart wordt, wordt er een Field aangemaakt. Deze krijgt de parameters moeilijkheid en afbeelding mee. Hiermee kan vervolgens worden bepaald welk afbeelding gemaakt moet worden en in hoeveel vakjes het veld gemaakt moet worden. In de class worden het aantal stappen ook bijgehouden.
Een nieuwe zet wordt doormiddel van een functie setTiles gedaan. Deze geeft de aparte vakjes een positie.
