package com.github.koshamo.puri.setup;

public enum Buildings {

	KL_INDIGO ("kleine Indigoküperei", GameSet.BASIS, "kleine Indigoküperei", 
			"kl. Indigoküperei", 1, 1, 1, 1),
	GR_INDIGO ("große Indigoküperei", GameSet.BASIS, "große Indigoküperei", 
			"gr. Indigoküperei", 3, 2, 3, 1),
	KL_ZUCKER ("kleine Zuckermühle", GameSet.BASIS, "kleine Zuckermühle", 
			"kl. Zuckermühle", 2, 1, 1, 1),
	GR_ZUCKER ("große Zuckermühle", GameSet.BASIS, "große Zuckermühle", 
			"gr. Zuckermühle", 4, 2, 3, 1),
	TABAK ("Tabakspeicher", GameSet.BASIS, "Tabakspeicher", 
			"Tabakspeicher", 5, 3, 3, 1),
	KAFFEE ("Kaffeerösterei", GameSet.BASIS, "Kaffeerösterei", 
			"Kaffeerösterei", 6, 3, 2, 1),
	KL_MARKT ("kleine Markthalle", GameSet.BASIS, "+1 Dublone beim Verkaufen (Händlerphase)",
			"Verkauf: +1 Dubl.", 1, 1, 1, 1),
	HAZIENDA ("Hazienda", GameSet.BASIS, "+1 Plantage vom Vorrat (Siedlerphase)",
			"Sied.: +1 Plantage", 2, 1, 1, 1),
	BAUHUETTE ("Bauhütte", GameSet.BASIS, "Steinbruch statt Plantage? (Siedlerphase)",
			"Sied.: Steinbruch?", 2, 1, 1, 1),
	KL_LAGER ("kleines Lagerhaus", GameSet.BASIS, "+1 Warensorte lagern (Kapitänphase)",
			"Lagern: +1 W.sorte", 3, 1, 1, 1),
	HOSPIZ ("Hospiz", GameSet.BASIS, "+1 Kolonist beim Siedeln (Siedlerphase)",
			"Sied.: +1 Kolonist", 4, 2, 1, 1),
	KONTOR ("Kontor", GameSet.BASIS, "Erlaubt Verkauf gleicher Waren (Händlerphase)",
			"Verk.: gleiche Ware", 5, 2, 1, 1),
	GR_MARKT ("große Markthalle", GameSet.BASIS, "+2 Dublonen beim Verkaufen (Händlerphase)",
			"Verkauf: +2 Dubl.", 5, 2, 1, 1),
	GR_LAGER ("großes Lagerhaus", GameSet.BASIS, "+2 Warensorten lagern (Kapitänphase)",
			"Lagern: +2 W.sorten", 6, 2, 1, 1),
	MANUFAKTUR ("Manufaktur", GameSet.BASIS, "+0/1/2/3/5 Dublonen beim Produzieren (Aufseherphase)",
			"Aufs.: +0/1/2/3/5 D.", 7, 3, 1, 1),
	UNIVERSITAET ("Universität", GameSet.BASIS, "+1 Kolonist beim Bauen (Baumeisterphase)",
			"Bau: +1 Kolonist", 8, 3, 1, 1),
	HAFEN ("Hafen", GameSet.BASIS, "+1 Siegpunkt pro Lieferung (Kapitänphase)",
			"+1 SP / Lieferung", 8, 3, 1, 1),
	WERFT ("Werft", GameSet.BASIS, "= ein eigenes Transportschiff (Kapitänphase)",
			"1 eigenes Schiff", 9, 3, 1, 1),
	ZUNFTHALLE ("Zunfthalle", GameSet.BASIS, "2 Siegpunkte für jedes große Produktionsgebäude\n1 Siegpunkt für jedes kleine Produktionsgebäude\n(Spielende)",
			"Ende: +1/+2 SP für jedes kl./gr. Prod.gebäude", 10, 4, 1, 2),
	RESIDENZ ("Residenz", GameSet.BASIS, "4 SP für bis zu 9\n5 SP für 10\n6 SP für 11\n7 SP für 12\nausliegende Inselplättchen\n(Spielende)",
			"Ende: +4-7 SP für 9-12 ausl. Insel-Plättchen", 10, 4, 1, 2),
	FESTUNG ("Festung", GameSet.BASIS, "1 Siegpunkt für je 3 Kolonisten\n(Spielende)",
			"Ende: +1 SP für jeweils 3 Kolonisten", 10, 4, 1, 2),
	ZOLLHAUS ("Zollhaus", GameSet.BASIS, "1 Siegpunkt für je vier Siegpunkte (nur Chips!)\n(Spielende)",
			"Ende: +1 SP für je 4 SP (nur Chips!)", 10, 4, 1, 2),
	RATHAUS ("Rathaus", GameSet.BASIS, "1 Siegpunkt für jedes beigefarbene Gebäude",
			"Ende: +1 SP für jedes beigefarb. Gebäude", 10, 4, 1, 2);
	
	
	private final String name;
	private final GameSet set;
	private final String description;
	private final String shortDescription;
	private final int cost;
	private final int victoryPoints;
	private final int places;
	private final int size;
	
	private Buildings(String name, GameSet set, String description, 
			String shortDescription, int cost, int victoryPoints, int places, 
			int size) {
		this.name = name;
		this.set = set;
		this.description = description;
		this.shortDescription = shortDescription;
		this.cost = cost;
		this.victoryPoints = victoryPoints;
		this.places = places;
		this.size = size;
	}

	public String getName() {
		return name;
	}
	
	public GameSet getSet() {
		return set;
	}

	public String getDescription() {
		return description;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public int getCost() {
		return cost;
	}

	public int getVictoryPoints() {
		return victoryPoints;
	}

	public int getSize() {
		return size;
	}

	public int getPlaces() {
		return places;
	}

	
}
