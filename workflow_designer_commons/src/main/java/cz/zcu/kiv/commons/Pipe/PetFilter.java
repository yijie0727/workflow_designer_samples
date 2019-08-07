package cz.zcu.kiv.commons.Pipe;

import cz.zcu.kiv.WorkflowDesigner.Annotations.*;

import java.io.*;

import static cz.zcu.kiv.WorkflowDesigner.Type.STREAM;
import static cz.zcu.kiv.WorkflowDesigner.Type.STRING;

@BlockType(type ="PetFilter", family = "PIPE")
public class PetFilter {


//    @BlockProperty(name = "targetPetType", type = STRING, description = "Enter the pet type you want to get(example: cat)")
//    String maxPetType;


    @BlockInput(name = "targetPetType", type = STRING)
    String petType;


    @BlockInput(name = "pets1", type = STREAM)
    PipedInputStream pipedIn1   = new PipedInputStream();

    @BlockInput(name = "pets2", type = STREAM)
    PipedInputStream pipedIn2   = new PipedInputStream();

    @BlockOutput(name = "targetPets", type = STREAM)
    PipedOutputStream pipedOut = new PipedOutputStream();



    @BlockExecute
    public void process() throws Exception {
 System.out.println(" String maxPetType = "+ petType );

        ObjectInputStream objectInStream1 = new ObjectInputStream(pipedIn1);
        ObjectInputStream objectInStream2 = new ObjectInputStream(pipedIn2);
        ObjectOutputStream objectOutStream = new ObjectOutputStream(pipedOut);

        Pet pet1;
        Pet pet2;
        while ((pet1 = (Pet) objectInStream1.readObject())!= null) {
            System.out.println(pet1.getType()+": "+pet1.getName());

            if (petType == null || petType.length() == 0 || petType.equals(pet1.getType())) {
                objectOutStream.writeObject(pet1);
                objectOutStream.flush();
            }
        }

        while ((pet2 = (Pet) objectInStream2.readObject())!= null) {

            if (petType == null || petType.length() == 0 || petType.equals(pet2.getType())) {
                objectOutStream.writeObject(pet2);
                objectOutStream.flush();
            }

            System.out.println(pet2.getType()+": "+pet2.getName());
        }


        objectOutStream.writeObject(null);
        objectOutStream.flush();

        objectInStream1.close();
        objectInStream2.close();
        objectOutStream.close();
        pipedIn1.close();
        pipedIn2.close();
        pipedOut.close();
    }



}
