package test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TestListSort {
	public static void main(String[] args) {
		Student stu = new Student();
		stu.setName("张三");
		stu.setAge(18);
		Student stu1 = new Student();
		stu1.setName("李四");
		stu1.setAge(15);
		Student stu2 = new Student();
		stu2.setName("王二");
		stu2.setAge(20);
		Student stu3 = new Student();
		stu3.setName("麻子");
		stu3.setAge(19);
		Student stu4 = new Student();
		stu4.setName("张六");
		stu4.setAge(30);
		
		List<Student> list = new ArrayList<Student>();
		list.add(stu);
		list.add(stu1);
		list.add(stu2);
		list.add(stu3);
		list.add(stu4);
		
		System.out.println(list);
		list.sort(new Comparator<Student>(
				
				) {

					@Override
					public int compare(Student o1, Student o2) {
						// TODO Auto-generated method stub
						return -o1.getName().compareTo(o2.getName());
					}
		});
		
		System.out.println(list);
		
	}
}
