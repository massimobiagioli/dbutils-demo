package it.demo.dbutils.model;

public class Person {
	
	private int id;
	private String name;
	
	public Person() {
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		return this.getId() + " - " + this.getName();
	}
	
}
