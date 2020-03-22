package com.github.koshamo.puri.ai;

public class Pair<S,T> {

	private S first;
	private T second;
	
	public Pair(S first, T second) {
		this.first = first;
		this.second = second;
	}
	
	public S first() {
		return first;
	}
	
	public T second() {
		return second;
	}
}
