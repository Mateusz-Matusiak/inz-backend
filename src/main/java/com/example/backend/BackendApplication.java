package com.example.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

    /*@Bean
    CommandLineRunner commandLineRunner(RoleRepository roleRepository,
                                        AnimalService animalService,
                                        AnimalTypeRepository animalTypeRepository,
                                        UserService userService) {
        return args -> {
            RoleEntity roleUser = new RoleEntity("USER");
            if(!roleRepository.exists(Example.of(roleUser))) {
                roleRepository.save(roleUser);
            }
            if (animalTypeRepository.findByTypeName("dog").isEmpty()) {
                animalTypeRepository.save(new AnimalTypeEntity("dog"));
            }
            if (animalService.getAllAnimals().isEmpty()) {
                animalService.addAnimal(new NewAnimalDTO("Minnie", 2, "dog",
                        "ginger", "Calm and friendly",
                        "Beautiful dog which loves to play with her dog",
                        AnimalSex.FEMALE, 35));
            }
            if (userService.getAllUsers().isEmpty()) {
                userService.addUser(new RegisterUserDTO("test@gmail.com", "password"));
            }
        };
    }*/

}
