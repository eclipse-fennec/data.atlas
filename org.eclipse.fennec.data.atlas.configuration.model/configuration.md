# Config Model

* Exports und DataSources extra
* Exports CSV will man Separator konfigurieren
* DataSources wiederverwenden

# Stefan:

Ich würde nochmal an dem Konfigurationsmodell so ein bisschen erzählen oder was ich mir vorstelle, wie so eine Konfiguration laufen kann oder welche Rahmenbedingungen von außen auf das Konfigurationsmodell wirken. Das eine ist halt, es wäre schön, wenn wir die Data Sources von dem Rest trennen könnten. So, der Rest ist halt, ich mache, ich sage hier die Klasse, dieses Modell und das Mapping und mache die ganzen Konfigurationen, an welchem Endpunkt welcher Adapter mit welchem Basisendpunkt und welchen Einstellungen hinkommt. Und dann sage ich, naja, und das bitte mit der Datenquelle. Und ich will aber gegebenenfalls dasselbe mit einer anderen Datenquelle machen. Dass ich entweder das mit dem Testsystem mache oder für zwei Kommunen oder für, dass man einfach vorsieht, dass es die ganze Einstellung gelten kann, dann vielleicht unter einem anderen Pfad, unter einer anderen Basispfad, aber dieselbe Einstellung mit einer anderen Datenquelle gelten kann. Das ist nach unten so das eine. Und dann gibt es nach oben bei den Exportern was Ähnliches nochmal, weil das das Ding, die CSVs per Komma separiert und wie er dann welche Quotes macht. Und das bei allen Schnittstellen. Das sollte man einmal templateartig definieren und dann geht man hin und sagt, naja, jetzt gibt es den Baum-Exporter, jetzt gibt es den Bank-Exporter, die sich an die Modelle kümmern, aber die wenden sozusagen diese Einstellungen nur an, dass man einen, das ist unsicherheit erreicht, dass man ein Template hat, wo man sozusagen sagt, wie das Ganze aussieht und dann nur immer wieder sagt, ja, jetzt ist wieder der derselbe CSV-Exporter, jetzt ist festgelegt wieder die Quotes, wenn wir die etwas sind und so weiter. Und vielleicht noch welcher Subpfad oder welche Application und den wendet man dann sozusagen nur noch an auf Baum und auf Bank und auf Auto und auf Feuerwehrauto und auf Wasser und so weiter, damit man das nicht zu sehr doppelt.machen wir es, wie ich das nicht gemacht hätte, wie ich auch vorgeschlagen habe, dass man für die Datenquellen, also die Data Sources, eine Registry und Container baut, genauso für die Exports, wo die dann alle beieinander drin liegen und wo wir dann sozusagen in der Konfiguration darauf referenzieren können, sodass du, ich sage mal, pro Mandant deine Datensourcen anlegen kannst. Gegebenenfalls kann man das halt dann auch mit irgendwelchen Mappings machen, die man dann noch hat, für irgendwelche Kubernetes-Mappings, die dazwischen kommen, dass man das alles nur referenziert. Das wäre so das Ende oben und unten. 



* Alle Texte im DCAT generieren sich vollständig aus den Modellen
* Gfs. sollten texte auch aus anderen quellen ggfs refernziert werden können, falls sie nicht in die jeweiligen modelle passen
* DCAT Endpunkt
* OData als Backend für DCAT + SPARQL
* DCAT UI Markus

# QGis

* Eine Eclass ist ein Layer
* QGis Client hat eine Konfigufile für diesen Layer
* QGis Server Konsumiert das und erstellt OGC Features / Maps
* Layer hat folgende Tabelle mit SELECT + Umbennenung der Tabellen in anzeigbarer Name
* Wir generieren das QGis File