package com.github.koshamo.puri.setup;

public enum RoleType {
	BAUMEISTER ("Baumeister", "Gebäudebau", "Baukosten minus 1 Dublone"),
	SIEDLER ("Siedler", "neue Plantagen", "Steinbruch statt Plantage"),
	BUERGERMEISTER ("Bürgermeister", "neue Kolonisten", "1 Kolonist mehr (vom Vorrat)"),
	AUFSEHER ("Aufseher", "Produktion", "1 Warenstein mehr (vom Vorrat)"),
	KAPITAEN ("Kapitän", "Warenlieferung", "1 Siegpunkt mehr (gesamt)"),
	HAENDLER ("Händler", "Warenverkauf", "1 Dublone mehr von der Bank"),
	GOLDSUCHER ("Goldsucher", "...", "1 Dublone von der Bank");
	
	private String title;
	private String shortDescription;
	private String description;
	
	private RoleType(String title, String shortDescription, String description) {
		this.title = title;
		this.shortDescription = shortDescription;
		this.description = description;
	}
	
	public String title() {
		return title;
	}
	
	public String description() {
		return description;
	}
	
	public String shorDescription() {
		return shortDescription;
	}
}
