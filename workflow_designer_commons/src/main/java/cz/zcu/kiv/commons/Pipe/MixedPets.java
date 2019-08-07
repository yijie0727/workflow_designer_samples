package cz.zcu.kiv.commons.Pipe;

import cz.zcu.kiv.WorkflowDesigner.Annotations.BlockExecute;
import cz.zcu.kiv.WorkflowDesigner.Annotations.BlockOutput;
import cz.zcu.kiv.WorkflowDesigner.Annotations.BlockProperty;
import cz.zcu.kiv.WorkflowDesigner.Annotations.BlockType;

import java.io.ObjectOutputStream;
import java.io.PipedOutputStream;
import java.util.HashMap;
import java.util.Map;

import static cz.zcu.kiv.WorkflowDesigner.Type.STREAM;
import static cz.zcu.kiv.WorkflowDesigner.Type.STRING;

@BlockType(type ="MixedPets", family = "PIPE")
public class MixedPets {

    @BlockProperty(name = "petsNames", type = STRING, description = "Enter all the pets name, separate input pets Name with ',' (example: apple, meow, melody, marine, kiwi, bigPie, lucky)")
    String petsName;

    @BlockProperty(name = "petsTypes", type = STRING, description = "Enter corresponding type, separate input pets Type with ','(example: dog, cat, bird, fish, bird, dog, cat)")
    String petsType;

    @BlockOutput(name = "mixedPets", type = STREAM)
    PipedOutputStream pipedOut = new PipedOutputStream();


    @BlockOutput(name = "maxPetsType", type = STRING)
    String maxPetType;


    @BlockExecute
    public void process() throws Exception {


        int max = Integer.MIN_VALUE;

        Map<String, Integer> pets = new HashMap<>();

        petsName = petsName.replaceAll("\\s*", "");
        petsType = petsType.replaceAll("\\s*", "");

        String[] names = petsName.split(",");
        String[] types = petsType.split(",");

        if ( names.length == 0 || names.length != types.length ) {
            throw new Exception("Lengths of Pets Names and Pets Types do not match.");
        }

        ObjectOutputStream objectOut = new ObjectOutputStream( pipedOut );

        for(int i = 0; i < names.length; i++){

            if(pets.containsKey(types[i]))
                pets.put(types[i], pets.get(types[i])+1);

            else
                pets.put(types[i],1);


            if(max < pets.get(types[i])){
                max = pets.get(types[i]);
                maxPetType = types[i];
                System.out.println("maxPetType = types[i] = "+ maxPetType);
            }

            Pet pet = new Pet(names[i], types[i]);
            objectOut.writeObject(pet);
            objectOut.flush();
            Thread.sleep(500);
        }


        objectOut.writeObject(null);
        objectOut.flush();

        objectOut.close();
        pipedOut.close();

    }


}
