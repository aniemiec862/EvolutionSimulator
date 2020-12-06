package agh.cs.lab1;

import java.util.HashMap;
import java.util.Map;

/* Dodanie interfejsu IMapElement na pewno byłoby pomocne, choć nie jest w tym projekcie aż tak wymagane. Natomiast w
przypadku większej ilości klas mogących pojawić się na mapie, wprowadzenie takiego interfejsu byłoby wręcz wskazane.
Pomogło by to ustandaryzować obiekty mogące  wystąpić na mapie, a także usprawniłoby to dodawanie kolejnych takich klas.

Dodanie klasy AbstractWorldMapElement wydaje się być bezsensowny, ponieważ każda następna nowa klasa obiektu
występującego na mapie różniłaby się zawartością metod. Dziedziczenie z klasy abstrakcyjnej niewiele by dało, ponieważ
metody (np. toString) i tak w większości musiałyby być implementowane oddzielnie klasach dziedziczących.
*/

abstract class AbstractWorldMap implements IWorldMap, IPositionChangeObserver {
    protected final Map<Vector2d,Animal> mapOfAnimals = new HashMap<>();
    private final MapVisualizer visualize = new MapVisualizer(this);
    protected final MapBoundary mBoundary = new MapBoundary();

    public String toString(){
        System.out.println(this.mBoundary.getLowerLeft().toString() + this.mBoundary.getUpperRight().toString());
        return visualize.draw(this.mBoundary.getLowerLeft(), this.mBoundary.getUpperRight());
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        //zwierze może się poruszyć tylko jeśli nie przekroczy zera lub na nowej pozycji nie ma innego zwierzęcia,
        //dlatego nie korzystam z isOccupied, ponieważ muszę dać możliwość wejścia w przypadku trawy
        return !(objectAt(position) instanceof Animal);
    }

    @Override
    public boolean place(Animal animal) throws IllegalArgumentException {
        Vector2d position = animal.getPosition();
            if (canMoveTo(position)) {
                this.mapOfAnimals.put(position, animal);
                animal.addObserver(this);
                animal.addObserver(this.mBoundary);
                this.mBoundary.addObject(animal);
            }
            else throw new IllegalArgumentException("Animal couldn't be placed at position " + position.toString());
        return true;
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return !(objectAt(position) == null);
    }

    @Override
    public Object objectAt(Vector2d position) {
        return this.mapOfAnimals.get(position);
    }

    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition, Animal animal) {
        Animal a = this.mapOfAnimals.get(oldPosition);
        this.mapOfAnimals.replace(oldPosition, null);
        this.mapOfAnimals.put(newPosition, a);
    }
}