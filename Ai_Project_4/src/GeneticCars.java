

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class GeneticCars implements MouseListener
{
	//GENETIC PARAMETERS

	//how many frames the car is raced for
	public static final int ITERATIONS=2000;

	//number of breed/race/kill/mutate rounds
	public static final int GENERATIONS=50;

	//after each kill round, this many cars are left
	public static final int KILLTOPOPULATION=10;

	//the probability that any two cars will mate in a breed round
	public static final double BREED_RATE=0.5;

	//the probability that any car will produce a baby mutant in a mutate round
	public static final double MUTATE_SELECTION_RATE=0.5;

	//if the mutant is made, the probability that any single ball position or link is altered
	public static final double MUTATE_RATE=0.1;


	//This arraylist holds the population of cars
	public ArrayList<Car> population;

	//Program starts here:
	// creates an initial population
	// does the genetic simulation
	// shows the winning car

	public GeneticCars()
	{
		population=new ArrayList<Car>();

		generateInitialPopulation(KILLTOPOPULATION);
		doGenetic(GENERATIONS);

		show(population.get(0));
	}

	// does the genetic simulation
	public void doGenetic(int generations)
	{
		// runs for generations cycles
		for(int g=0; g<generations; g++)
		{
			//calls the breed, race, kill, mutate functions and prints the winner
			breed();
			raceAll();
			kill();
			mutate();
			System.out.println("Generation "+(g+1)+": best car has distance "+population.get(0).score_position+"/500, Iterations "+population.get(0).score_iterations+"/2000");
		}
	}


	//creates n new cars, each with 10 balls and random links, puts them in the population arraylist
	public void generateInitialPopulation(int n)
	{
		for(int i=0; i<n; i++)
			population.add(new Car(10));
	}

	public void breed()
	{
		//Make an arraylist of new cars
		 ArrayList<Car> newCars = new ArrayList<Car>();
		// Go through every pair of cars in population
		for(int i = 0; i < population.size(); i++)
		{
			for(int j = 0; j < population.size(); j++)
			{
				if( Math.random() < BREED_RATE)
				{
					Car breedingCar = population.get(i).breed(population.get(j));
					newCars.add(breedingCar);
				}
			}
		}
		population.addAll(newCars);
		//  with probability BREED_RATE, mate them by calling the "breed" method in class Car, and add the child to the new car arraylist
		//finally copy the cars in new car over to the population
	}


	public void mutate()
	{
		//Make an arraylist of new cars
		ArrayList<Car> newCars = new ArrayList<Car>();
		// Go through every car in the population
		for(int i = 0; i < population.size(); i++)
		{
			if(Math.random() < MUTATE_SELECTION_RATE)
			{
				Car e = population.get(i);
				newCars.add(e.mutate(.5));
			}
			
		}
		population.addAll(newCars);
		//   with probability MUTATE_SELECTION_RATE, call the "mutate" method in class Car and add the child to the new car arraylist
		//finally copy the cars in new car over to the population
	}

	
	public void kill()
	{
		//make a "keep" arraylist of cars
		ArrayList<Car> keepers = new ArrayList<Car>();
		//Do this KILLTOPOPULATION times:
			// go through your population and find the best car.  Use the compare function (below).
			// remove the best car from population and put it in the keep list
		for(int i = 0; i < KILLTOPOPULATION; i++)
		{
			Car a = population.get(i);
			for(int j=1; j <population.size(); j++)
			{
				
				
				if (compare(a, population.get(j)) == true)
				{
					a = population.get(j);
				}
			}
			keepers.add(a);
			population.remove(a);
		}
		//set population=keep to make the keep list your population
		population = keepers;
	}

	//false if a is better, true if b is better
	//Use this in your kill function to select the best cars
	private boolean compare(Car a, Car b)
	{
		if(a.score_position>=500 && b.score_position>=500)
			return b.score_iterations<a.score_iterations;
		else
			return b.score_position>a.score_position;
	}

	//go through every car and race it
	public void raceAll()
	{
		for(Car car: population)
			race(car);
	}

	//make a World object containing a racetrack of walls
	// if you do the optional step, you should make several of these and return one of them at random
	public World makeRaceCourse()
	{
		World world=new World();
		world.WIDTH=500;
		world.HEIGHT=500;
		world.makeWall(1,500,499,500);
		world.makeWall(-20,132,123,285);
		world.makeWall(104,285,203,277);
		world.makeWall(202,275,271,344);
		world.makeWall(271,344,320,344);
		world.makeWall(321,345,354,318);
		world.makeWall(354,318,394,324);
		world.makeWall(394,324,429,390);
		world.makeWall(429,391,498,401);
		return world;
	}

	//take an individual car, make a racetrack for it and simulate it
		//at the end of the function the car will have a score
	public void race(Car car)
	{
		World w=makeRaceCourse();
		car.constructCar(w);
		int i=0;
		for(i=0; i<ITERATIONS; i++)
		{
			w.doFrame();
			if(car.getPosition()>=500)
				break;
		}
		car.setScore(i);
	}

	//show every car in population racing, one at a time
	public void showAll()
	{
		for(Car car: population)
		{
			World w=makeRaceCourse();
			car.constructCar(w);
			show(w);
		}
	}

	//show a single car racing
	public void show(Car car)
	{
		World w=makeRaceCourse();
		car.constructCar(w);
		show(w);
	}

	//pop up a window and show a car falling down its track
	private void show(World world)
	{
		JFrame window=new JFrame("World");
		window.setSize(600,600);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.add(world);
		world.addMouseListener(this);
		world.graphics=true;

		window.setVisible(true);

		for(int i=0; i<ITERATIONS; i++)
		{
			world.doFrame();
			try{ Thread.sleep((int)(world.DT*1000/30)); } catch(InterruptedException e){};
		}
	}

	//these methods don't do anything currently
	// they're here only if you want to make the "show" window interactive
	public void mouseClicked(MouseEvent e)
	{
		int px=e.getX();
		int py=e.getY();
	}
	int px,py;
	public void mousePressed(MouseEvent e)
	{
		px=e.getX();
		py=e.getY();
	}
	int rx,ry;
	public void mouseReleased(MouseEvent e)
	{
		rx=e.getX();
		ry=e.getY();
	}
	public void mouseExited(MouseEvent e){}
	public void mouseEntered(MouseEvent e){}


	//main just calls GeneticCars
	public static void main(String[] args)
	{
		new GeneticCars();
	}
}
