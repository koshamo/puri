package com.github.koshamo.puri.setup;

public enum RoleType {
	BAUMEISTER ("Gebäudebau", "Baukosten minus 1 Dublone"),
	SIEDLER ("neue Plantagen", "Steinbruch statt Plantage"),
	BUERGERMEISTER ("neue Kolonisten", "1 Kolonist mehr (vom Vorrat)"),
	AUFSEHER ("Produktion", "1 Warenstein mehr (vom Vorrat)"),
	KAPITAEN ("Warenlieferung", "1 Siegpunkt merhr (gesamt)"),
	HAENDLER ("Warenverkauf", "1 Dublone mehr von der Bank"),
	GOLDSUCHER ("...", "1 Dublone von der Bank");
	
	private String shortDescription;
	private String description;
	
	private RoleType(String shortDescription, String description) {
		this.shortDescription = shortDescription;
		this.description = description;
	}
	
	public String description() {
		return description;
	}
	
	public String shorDescription() {
		return shortDescription;
	}
}
