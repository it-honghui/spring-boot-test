package cn.gathub.test.model;

import java.util.function.Predicate;

public class Employee {
    private Integer id;
    private Integer age;
    private String gender;
    private String firstName;
    private String lastName;

    public static Predicate<Employee> ageGreaterThan70 = x -> x.getAge() >70;
    public static Predicate<Employee> genderM = x -> x.getGender().equals("M");

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Employee() {
    }

    public Employee(Integer id, Integer age, String gender, String firstName, String lastName) {
        this.id = id;
        this.age = age;
        this.gender = gender;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}