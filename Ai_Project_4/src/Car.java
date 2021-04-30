import java.util.Random;
import java.util.stream.IntStream;

/*
MODIFY THIS CLASS TO MATE TWO CARS AND MUTATE CARS

Your code will go in methods BREED() and MUTATE().  Find the TODO lines.
  you will call these methods from your code in GeneticCars

A "Car" is a collection of balls and links
*/

public class Car
{
	//how many balls in the car
	int nodes;
	//position of balls
	int[] balls_x;
	int[] balls_y;
	//for every ball i,j  true if there's a link between them
	boolean[][] linkmatrix;

	//these are set by the setScore function after a simulated race
	double score_position;		//how far did the car get
	double score_iterations;	//how long did it take the car to reach the end

	//the simulated world the car is running in.  null until the car is raced.
	World world;

	//construct a car with nodes balls and random links
	//every ball is placed between (5,5) and (50,50)

	public Car(int nodes)
	{
		this.world=null;
		this.nodes=nodes;

		balls_x=new int[nodes];
		balls_y=new int[nodes];
		linkmatrix=new boolean[nodes][nodes];

		//randomly place balls between (5,5 and 50,50)
		for(int i=0; i<nodes; i++)
		{
			balls_x[i]=randint(5,50);
			balls_y[i]=randint(5,50);
		}

		//assign a link between two balls with probability 1/3
		for(int i=0; i<nodes; i++)
		{
			for(int j=0; j<nodes; j++)
			{
				if(randint(1,3)==1)
					linkmatrix[i][j]=true;
			}
		}
	}

	//return the average x position of the nodes
	//this is called only after the car has been raced
	public double getPosition()
	{
		int sum=0;
		for(int i=0; i<nodes; i++)
			sum+=world.getBall(i).position.x;
		return sum/nodes;
	}

	//set the car's score
	//this is called once the race simulation is done
		//don't call it before then or you'll get a nullpointerexception
	public void setScore(int iterations)
	{
		score_position=getPosition();
		if(score_position>world.WIDTH)
			score_position=world.WIDTH;
		score_iterations=iterations;
	}

	//build the car into the world: create its balls and links
	//call this when you're ready to start racing
	public void constructCar(World world)
	{
		this.world=world;
		for(int i=0; i<nodes; i++)
		{
			world.makeBall(balls_x[i],balls_y[i]);
		}
		for(int i=0; i<nodes; i++)
			for(int j=0; j<nodes; j++)
				if(linkmatrix[i][j])
					world.makeLink(i,j);
	}

	//returns a random integer between [a,b]
	private int randint(int a, int b)
	{
		return (int)(Math.random()*(b-a+1)+a);
	}

	//TODO
	//YOU WRITE THIS FUNCTION
	//It should return a "child" car that is the crossover between this car and parameter car c
	public Car breed(Car c)
	{
		Car child=new Car(10);
		Random rand = new Random();
		
		
		


		//YOUR WORK HERE

		//Choose a random crossover point.  Also choose a car to go first
		if(Math.random() > .5)
		{
			for(int i = 0; i<c.balls_x.length; i++)
			{
				child.balls_x[i] = c.balls_x[i];
				
			}
			for(int i = 0; i<c.balls_y.length; i++)
			{
				child.balls_y[i] = c.balls_y[i];
			}
		}
		// copy the balls from the first car's balls_x and balls_y to the child
		
		// after the crossover, copy the balls_x and balls_y from the second car to the child
		int randomNum = rand.nextInt(c.nodes);
		if(randomNum > c.nodes)
		{
			for (int i = 0; i < c.nodes ; i ++)
			{
				child.balls_x[i] = child.balls_x[i];
				child.balls_y[i] = child.balls_y[i];
			}
		}

		//pick a new crossover point, then do the same with the linkmatrix
		randomNum = rand.nextInt(c.nodes);
		for (int i = 0; i < c.nodes; i++)
		{
			for(int j = 0; j < c.nodes; j++)
			{
				if(Math.random() <= .35)
					{
						child.linkmatrix[i][j] = c.linkmatrix[i][j];
					}
				else
				{
					child.linkmatrix[i][j] = this.linkmatrix[i][j];
				}
			}
		}


		return child;
	}

	//TODO
	//YOU WRITE THIS FUNCTION
	//It should return a car "newcar" that is identical to the current car, except with mutations
	public Car mutate(double probability)
	{
		Car newcar=new Car(nodes);
		Random rand = (Random) new Random();
		IntStream randomNum = rand.ints(1,5,50);
		
		//YOUR WORK HERE
		//  You should copy over the car's balls_x and balls_y to newcar
		//newcar.balls_x = 
			for (int i = 0; i < newcar.nodes; i ++)
			{
				if(Math.random() < probability)
				{
					newcar.balls_x[i] = rand.nextInt(45)+5;
					newcar.balls_y[i] = rand.nextInt(45)+5;
				}
				else
				{
					newcar.balls_x[i] = balls_x[i];
					newcar.balls_y[i] = balls_y[i];
				}
			}
			//with probability "probability", change the balls_x and balls_y to a random number from 5 to 50
		//  Then copy over the links
		
		//	//with probability "probability", set the link to true/false (50/50 chance)

			for (int i = 0; i < newcar.nodes; i ++)
			{
				for (int j =0; j < newcar.nodes; j++)
				{
					if(Math.random() > probability)
					{
						newcar.linkmatrix[i][j] = linkmatrix[i][j];
					}
					else
					{
						newcar.linkmatrix[i][j] =! linkmatrix[i][j];
					}
				}
				
			}
		return newcar;
	}
}
