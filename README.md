nPuzzle10447768
===============
![My image](https://raw.githubusercontent.com/MarcSelles/nPuzzle10447768/master/nPuzzle%20readme/Kies%20afbeelding.png)

Wanneer het spel voor de eerste keer opgestart wordt, moet er gekozen worden met welke afbeelding gespeeld wordt.
De gebruiker kan, door middel van omlaag en omhoog swipen, een afbeelding kiezen. Wanneer hij een afbeelding uitgekozen heeft hoeft hij hier alleen maar op te tikken, waardoor hij verder gaat naar het volgende scherm.

Activity:
Een intent wordt gebruikt om naar het volgende scherm te gaan

![My image](https://raw.githubusercontent.com/MarcSelles/nPuzzle10447768/master/nPuzzle%20readme/Begin%20scherm%20spel.png)

De gebruiker krijgt het hele plaatje te zien, verdeeld in n vakjes. Deze afbeelding moet de gebruiker zien te maken. Na 3 seconden verdwijnt deze afbeelding

Activity:
Er komt geen nieuw scherm, maar de class wordt veranderd

![My image](https://raw.githubusercontent.com/MarcSelles/nPuzzle10447768/master/nPuzzle%20readme/spel%20random.png)

De vakjes worden willekeurig geplaatst in het scherm. Hiermee wordt het spel gestart. De gebruiker kan een vakje opschuiven wanneer 1 van de vakjes om het vakje heen het lege vakje is. Er moet dan getikt worden op het vakje om hem te verplaatsen naar het lege vakje.
Rechtsboven is het 'orginele' goede plaatje erg klein te zien. Indien een gebruiker zijn vinger hierop zet, is het plaatje te zien waar eerst het spel zelf zat. Dit wordt weer ongedaan gemaakt door middel van het weghalen van de vinger.

![My image](https://raw.githubusercontent.com/MarcSelles/nPuzzle10447768/master/nPuzzle%20readme/Menu.png)

Wanneer de gebruiker tijdens het spelen op de knop MENU klikt, komt hij in bovenstaand scherm. Hier kan hij de puzzel resetten, waardoor de vakjes weer op een willekeurige plaats worden gezet, om de moeilijkheid te veranderen, wat standaard op medium staat, en om het spel te stoppen, waardoor de gebruiker weer terug naar het scherm gaat om een afbeelding te kiezen. Indien er gereset wordt, is eerst het gehele plaatje zoals in de tweede afbeelding eerst opnieuw drie seconden te zien. 

![My image](https://raw.githubusercontent.com/MarcSelles/nPuzzle10447768/master/nPuzzle%20readme/Uitgespeeld.png)

Als de gebruiker het spel heeft uitgespeeld, dit gebeurt wanneer de puzzel er net zo uitziet als in de tweede afbeelding, krijgt hij een berichtje waarin informatie staat over zijn spel. Nu kan hij door middel van START EEN NIEUW SPEL terug naar een afbeelding uitkiezen

Classes:

Er wordt gebruik gemaakt van een class Field. Hierin wordt opgeslagen waar alle vakjes gepositioneerd zijn in het spel. Wanneer er een spel gestart wordt, wordt er een Field aangemaakt. Deze krijgt de parameters moeilijkheid en afbeelding mee. Hiermee kan vervolgens worden bepaald welk afbeelding gemaakt moet worden en in hoeveel vakjes het veld gemaakt moet worden. In de class worden het aantal stappen ook bijgehouden.
Een nieuwe zet wordt doormiddel van een functie setTiles gedaan. Deze geeft de aparte vakjes een positie.
Ook moet er worden bijgehouden welk vakje leeg is. Dan wordt er namelijk duidelijk welke vakjes kunnen bewegen en welke niet. Dit vakje kan worden gevonden door een functie in de class.

Bitmap:

De afbeeldingen waar de puzzel uit gaat bestaan worden door de bitmap verwerkt. Daarom moeten ze in de drawable map komen. Hierdoor kunnen we vakjes maken van de afbeelding. Hier wordt gebruik gemaakt van imageView.

Een overzicht van hoe bitmap gebruikt wordt:
- decodeResource
- createScaledBitmap
- createBitmap

Het is belangrijk dat de instance van class Field opgeslagen wordt, indien de app afgesloten wordt. Wanneer dit niet gedaan wordt, moet de gebruiker altijd een nieuw spel beginnen wanneer hij de app opstart in plaats van dat hij zijn spel kan vervolgen.
