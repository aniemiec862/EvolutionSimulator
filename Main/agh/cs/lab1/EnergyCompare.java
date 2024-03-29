package agh.cs.lab1;

import java.util.Comparator;

public class EnergyCompare implements Comparator<Animal> {
    //komparator pozwalający sortować zwierzęta na tym samym polu mapy po ich energii
    @Override
    public int compare(Animal o1, Animal o2) {
        int firstEnergy = o1.getEnergy();
        int secondEnergy = o2.getEnergy();
        return Integer.compare(secondEnergy, firstEnergy);
    }
}
