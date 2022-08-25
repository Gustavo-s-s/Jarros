package br.com.application;

import java.util.Objects;

public class Jug {

	private int capacity;
	private int liters;

	public Jug(int capacity, int liters) {
		this.capacity = Math.abs(Math.min(capacity, 40));
		this.addLiters(liters);
	}
	
	public int addLiters(int liters) {
		if (this.capacity == this.liters)
			return liters;

		final int SAVE = this.liters + liters;		
		this.liters = Math.min(SAVE, this.capacity);
		
		return SAVE - this.liters;
	}

	public void transferWatter(Jug otherJug) {
		this.liters = otherJug.addLiters(this.liters);
	}
	
	public int getLiters() {
		return liters;
	}

	public int getCapacity() {
		return capacity;
	}

	public Jug copy() {
		return new Jug(this.capacity, this.liters);
	}

	@Override
	public String toString() {
		return String.format("{Capacity: %d, liters: %d}", this.capacity, this.liters);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.capacity, this.liters);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Jug other) {
			return this.capacity == other.capacity && this.liters == other.liters;
		}
		return false;
	}

}
