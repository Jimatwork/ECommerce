package test;

import java.util.Date;

public class Student {
	private String name;
	private int age;
	private Date date = new Date();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "Student [name=" + name + ", age=" + age + ", date=" + date + "]";
	}

}
