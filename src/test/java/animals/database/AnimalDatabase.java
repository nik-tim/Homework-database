package animals.database;

import animals.bird.Duck;
import animals.pets.Animal;
import animals.pets.Cat;
import animals.pets.Dog;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AnimalDatabase {
    private Connection connection;

    public AnimalDatabase(Connection connection) {
        this.connection = connection;
    }

    public int addAnimal(Animal animal) throws SQLException {
        String query = "INSERT INTO animals (name, age, weight, color, type) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, animal.getName());
            statement.setInt(2, animal.getAge());
            statement.setDouble(3, animal.getWeight());
            statement.setString(4, animal.getColor());
            statement.setString(5, animal.getClass().getSimpleName());
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Ошибка при получении сгенерированного идентификатора.");
                }
            }
        }
    }

    public List<Animal> getAllAnimals() throws SQLException {
        List<Animal> animals = new ArrayList<>();
        String query = "SELECT * FROM animals";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Animal animal = createAnimalFromResultSet(resultSet);
                animals.add(animal);
            }
        }
        return animals;
    }

    public void updateAnimal(Animal animal) throws SQLException {
        String query = "UPDATE animals SET name=?, age=?, weight=?, color=? WHERE id=?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, animal.getName());
            statement.setInt(2, animal.getAge());
            statement.setDouble(3, animal.getWeight());
            statement.setString(4, animal.getColor());
            statement.setInt(5, animal.getId());
            statement.executeUpdate();
        }
    }

    public List<Animal> getAnimalsByType(String type) throws SQLException {
        List<Animal> animals = new ArrayList<>();
        String query = "SELECT * FROM animals WHERE type=?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, type);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Animal animal = createAnimalFromResultSet(resultSet);
                animals.add(animal);
            }
        }
        return animals;
    }

    private Animal createAnimalFromResultSet(ResultSet resultSet) throws SQLException {
        Animal animal;
        String type = resultSet.getString("type");
        switch (type) {
            case "Cat":
                animal = new Cat();
                break;
            case "Dog":
                animal = new Dog();
                break;
            case "Duck":
                animal = new Duck();
                break;
            default:
                throw new IllegalArgumentException("Unknown animal type");
        }
        animal.setId(resultSet.getInt("id"));
        animal.setName(resultSet.getString("name"));
        animal.setAge(resultSet.getInt("age"));
        animal.setWeight(resultSet.getDouble("weight"));
        animal.setColor(resultSet.getString("color"));
        return animal;
    }
}
