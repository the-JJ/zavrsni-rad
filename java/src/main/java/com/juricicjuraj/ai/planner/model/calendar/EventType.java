package com.juricicjuraj.ai.planner.model.calendar;

public enum EventType {
	HARD_CONSTRAINT(10, "#757575"),
	SOFT_CONSTRAINT(3, "#BDBDBD"),
	EXISTING(8, "#9C27B0"),
	GENERATED(4, "#2196F3");

	private int collisionCost;
	private String color;

	EventType(int collisionCost, String color){
		this.collisionCost = collisionCost;
		this.color = color;
	}

	EventType(int collisionCost){
		this(collisionCost, null);
	}

	public int getCollisionCost(){
		return collisionCost;
	}

	public String getColor() {
	    return color;
    }
}
