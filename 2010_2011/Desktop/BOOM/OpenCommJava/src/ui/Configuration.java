package ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import processing.core.PApplet;

public class Configuration {
	ArrayList<Person> people;

	public Configuration(String file, PApplet parent)
			throws FileNotFoundException {
		people = new ArrayList<Person>();
		Scanner sc = new Scanner(new File(file));
		int i = 0;
		while (sc.hasNextLine()) {
			String line = sc.nextLine();
			String[] data = line.split(":");
			System.out.println (i + " " + data[2] + " " + data[3] + " " +data[0] + " " + data[1]);
			int x = Integer.parseInt(data[4]);
			int y = Integer.parseInt(data[5]);
			String ip = data[1];
			people.add(new Person(parent, i, data[2], data[3], data[0], ip)
					.setInitialXY(x, y));
			i++;
		}
	}

	public ArrayList<Person> getConfig() {
		return people;
	}
}
