import animals.database.AnimalDatabase;
import animals.data.Command;
import animals.pets.Animal;
import animals.pets.Cat;
import animals.pets.Dog;
import animals.bird.Duck;

import java.sql.*;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/animals", "root", "371245n");
            AnimalDatabase animalDAO = new AnimalDatabase(connection);

            Scanner scanner = new Scanner(System.in);
            List<Animal> animals = animalDAO.getAllAnimals();

            while (true) {
                System.out.println("Введите команду (add/list/edit/filter/exit):");
                String command = scanner.nextLine().trim().toLowerCase();

                Command commandEnum = null;
                try {
                    commandEnum = Command.valueOf(command.toUpperCase());
                } catch (IllegalArgumentException e) {
                    System.out.println("Неизвестная команда");
                    continue;
                }

                switch (commandEnum) {
                    case ADD:
                        System.out.println("Какое животное вы хотите добавить (cat/dog/duck):");
                        String animalType = scanner.nextLine().trim().toLowerCase();
                        Animal animal = null;
                        while (animal == null) {
                            switch (animalType) {
                                case "cat":
                                    animal = new Cat();
                                    break;
                                case "dog":
                                    animal = new Dog();
                                    break;
                                case "duck":
                                    animal = new Duck();
                                    break;
                                default:
                                    System.out.println("Неизвестное животное. Повторите ввод:");
                                    animalType = scanner.nextLine().trim().toLowerCase();
                            }
                        }
                        System.out.println("Введите имя животного:");
                        String name = scanner.nextLine().trim();
                        animal.setName(name);
                        System.out.println("Введите цвет животного:");
                        String color = scanner.nextLine().trim();
                        animal.setColor(color);

                        int age = 0;
                        boolean validAge = false;
                        while (!validAge) {
                            System.out.println("Введите возраст животного:");
                            try {
                                age = Integer.parseInt(scanner.nextLine().trim());
                                if (age < 0) {
                                    throw new IllegalArgumentException("Возраст животного не может быть отрицательным!");
                                }
                                validAge = true;
                            } catch (NumberFormatException e) {
                                System.out.println("Неверный ввод возраста,повторите попытку!");
                            } catch (IllegalArgumentException e) {
                                System.out.println(e.getMessage());
                            }
                        }
                        animal.setAge(age);

                        double weight = 0;
                        boolean validWeight = false;
                        while (!validWeight) {
                            System.out.println("Введите вес животного:");
                            try {
                                weight = Double.parseDouble(scanner.nextLine().trim());
                                if (weight < 0) {
                                    throw new IllegalArgumentException("Вес животного не может быть отрицательным!");
                                }
                                validWeight = true;
                            } catch (NumberFormatException e) {
                                System.out.println("Неверный ввод веса,повторите попытку!");
                            } catch (IllegalArgumentException e) {
                                System.out.println(e.getMessage());
                            }
                        }
                        animal.setWeight(weight);
                        int id = animalDAO.addAnimal(animal);
                        animal.setId(id);
                        animals.add(animal);
                        animal.say();
                        break;
                    case LIST:
                        if (animals.isEmpty()) {
                            System.out.println("Список животных пуст");
                            break;
                        }
                        for (Animal animal1 : animals) {
                            System.out.println(animal1.toString());
                        }
                        break;
                    case EDIT:
                        System.out.println("Введите id животного для редактирования:");
                        id = Integer.parseInt(scanner.nextLine().trim());
                        Animal animalToUpdate = null;
                        for (Animal existingAnimal : animals) {
                            if (existingAnimal.getId() == id) {
                                animalToUpdate = existingAnimal;
                                break;
                            }
                        }
                        if (animalToUpdate == null) {
                            System.out.println("Животное с указанным id не найдено");
                        } else {
                            System.out.println("Введите новое имя животного:");
                            name = scanner.nextLine().trim();
                            animalToUpdate.setName(name);
                            System.out.println("Введите новый цвет животного:");
                            color = scanner.nextLine().trim();
                            animalToUpdate.setColor(color);
                            System.out.println("Введите новый возраст животного:");
                            age = Integer.parseInt(scanner.nextLine().trim());
                            animalToUpdate.setAge(age);
                            System.out.println("Введите новый вес животного:");
                            weight = Double.parseDouble(scanner.nextLine().trim());
                            animalToUpdate.setWeight(weight);
                            animalDAO.updateAnimal(animalToUpdate);
                        }
                        break;
                    case FILTER:
                        System.out.println("Введите тип животного для фильтрации (cat/dog/duck):");
                        String filterType = scanner.nextLine().trim().toLowerCase();
                        List<Animal> filteredAnimals = animalDAO.getAnimalsByType(filterType);
                        if (filteredAnimals.isEmpty()) {
                            System.out.println("Животные такого типа не найдены");
                        } else {
                            for (Animal filteredAnimal : filteredAnimals) {
                                System.out.println(filteredAnimal.toString());
                            }
                        }
                        break;
                    case EXIT:
                        System.out.println("До свидания!");
                        return;
                }
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при работе с базой данных: " + e.getMessage());
        }
    }
}
