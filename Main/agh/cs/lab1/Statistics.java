package agh.cs.lab1;

import java.util.*;
import java.util.stream.Collectors;

public class Statistics {
    private int age = 0 ;
    private float totalSumOfAverageEnergy = 0;
    private int totalAverageNumberOfAnimals = 0;
    private int totalAverageNumberOfGrass = 0;
    private final Map<Genotype, Integer> strongestGenesOfAllTime = new HashMap<>();
    private int totalAverageLifeLength = 0;
    private float totalAverageChildrenAmount = 0;
    protected int numberOfGrass = 0;
    protected int numberOfAnimals = 0;
    protected int numberOfDeadAnimals = 0;
    protected float sumOfLifeLengths;
    private float averageEnergy;
    private float averageLifeLength;
    private float averageNumberOfChildren;
    //hashmapa genotypów - klucz-genotyp, wartości - ilość występowania takich samych genów
    private final Map<Genotype, Integer> currentGenesMap = new HashMap<>();
    private Genotype currentStrongestGenotype;
    private int strongestGenotypeAmount = 0;

    @Override
    public String toString() {
        return "Current Statistics:" +
                "\nAge= " + age +
                "\nAlive Animals= " + numberOfAnimals +
                "\nPlants= " + numberOfGrass +
                "\nCurrent Strongest Genotype= " + currentStrongestGenotype +
                "\nDead Animals= " + numberOfDeadAnimals +
                "\nAverage Energy= \n" + averageEnergy +
                "\nAverage Life Length= \n" + averageLifeLength +
                "\nAverage Children Number= " + averageNumberOfChildren;
    }

    public int getAge() {
        return age;
    }

    public Genotype getCurrentStrongestGenotype() {
        return currentStrongestGenotype;
    }

    public void countAverages(ArrayList<Animal> list) {
        this.age += 1;
        float averageEnergy = 0;
        float averageNumberOfChildren = 0;

        for (Animal animal : list) {
            averageEnergy += animal.getEnergy();
            averageNumberOfChildren += animal.getAliveChildren();
        }
        if (this.numberOfAnimals > 0) {
            this.averageEnergy = averageEnergy / this.numberOfAnimals;
            this.averageNumberOfChildren = averageNumberOfChildren / this.numberOfAnimals;
            this.totalSumOfAverageEnergy += this.averageEnergy;
            this.totalAverageChildrenAmount += this.averageNumberOfChildren;
        } else {
            this.averageEnergy = 0;
            this.averageNumberOfChildren = 0;
        }
        if (currentGenesMap.size() > 0) {
            //sortuję hashmapę genotypów po ilości ich występowania
            Map.Entry<Genotype, Integer> entry = getFirstHashMapElement(this.currentGenesMap);
            this.currentStrongestGenotype = entry.getKey();
            this.strongestGenotypeAmount = entry.getValue();

            if(this.strongestGenesOfAllTime.get(this.currentStrongestGenotype) == null){
                this.strongestGenesOfAllTime.put(this.currentStrongestGenotype, 1);
            }
            else{
                this.strongestGenesOfAllTime.replace(this.currentStrongestGenotype, this.strongestGenesOfAllTime.get(this.currentStrongestGenotype) +1);
            }
        }
        this.totalAverageNumberOfAnimals += this.numberOfAnimals;
        this.totalAverageNumberOfGrass += this.numberOfGrass;

        countAverageLifeLength();
    }

    public void addToHashmap(Animal animal){
        Genotype animalGenes = animal.getGenes();
        this.numberOfAnimals += 1;
        if (this.currentGenesMap.get(animalGenes) == null) this.currentGenesMap.put(animalGenes, 1);
        else this.currentGenesMap.replace(animalGenes, this.currentGenesMap.get(animalGenes) + 1);
    }

    public void removeFromHashmap(Animal animal){
        Genotype animalGenes = animal.getGenes();
        this.numberOfAnimals -= 1;
        this.numberOfDeadAnimals += 1;
        this.sumOfLifeLengths += animal.getLifeLength();
        this.currentGenesMap.replace(animalGenes, this.currentGenesMap.get(animalGenes) - 1);
        if(this.currentGenesMap.get(animalGenes) == 0) this.currentGenesMap.remove(animalGenes);
    }

    public Map.Entry<Genotype, Integer> getFirstHashMapElement(Map<Genotype, Integer> hashMap){
        LinkedHashMap<Genotype, Integer> sortedGenes = hashMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
        return sortedGenes.entrySet().iterator().next();
    }

    public void countAverageLifeLength() {
        if (this.numberOfDeadAnimals > 0)
            this.averageLifeLength = this.sumOfLifeLengths / this.numberOfDeadAnimals;
        this.totalAverageLifeLength += this.averageLifeLength;
    }

    public String getStatisticsOfAllTime(){
        Map.Entry<Genotype, Integer> entry = getFirstHashMapElement(this.strongestGenesOfAllTime);
        return "General Statistics: "+
                "\nAverage Animals Number= " + this.totalAverageNumberOfAnimals/this.age +
                "\nAverage Grass Number= " + this.totalAverageNumberOfGrass/this.age +
                "\nStrongest Genotype= " + entry.getKey() +
                "\nAverage Animal Energy= " + this.totalSumOfAverageEnergy/this.age +
                "\nAverage Animal Life Length= " + this.totalAverageLifeLength/this.age +
                "\nAverage Children Number= " + this.totalAverageChildrenAmount/this.age;
    }
}