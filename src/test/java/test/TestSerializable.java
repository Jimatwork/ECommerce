package test;

import java.io.*;

/**
 * @author HD
 * @Title: TestSerializable
 * @ProjectName Idea
 * @Description: TODO
 * @date 2018/9/14/01414:12
 */
public class TestSerializable implements Serializable {
    private String name;
    private int age;

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
    @Override
    public String toString() {
        return "Person [name=" + name + ", age=" + age + "]";
    }
    public TestSerializable(String name, int age) {
        super();
        this.name = name;
        this.age = age;
    }
    public static void main(String [] args) {
        try {
            OutputStream os = new FileOutputStream("E:/io"+File.separator + "a.text");
            ObjectOutputStream ojs = new ObjectOutputStream(os);
            ojs.writeObject(new TestSerializable("张三",18));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
