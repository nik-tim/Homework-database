package animals.pets;

public abstract class Animal {

    private String name;
    private int age;
    private double weight;
    private String color;
    private int id;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void say() {
        System.out.println("Я говорю!");
    }

    public void go() {
        System.out.println("Я иду!");
    }

    public void drink() {
        System.out.println("Я пью!");
    }

    public void eat() {
        System.out.println("Я ем!");
    }

    @Override
    public String toString() {
        String ageStr;
        if (age % 10 == 1 && age % 100 != 11) {
            ageStr = age + " год";
        } else if (age % 10 >= 2 && age % 10 <= 4 && (age % 100 < 10 || age % 100 >= 20)) {
            ageStr = age + " года";
        } else {
            ageStr = age + " лет";
        }
        return "Привет! меня зовут " + name + ",мне " + ageStr + ",я вешу - " + weight + " " + "кг," + " мой цвет - " + color;
    }
}
