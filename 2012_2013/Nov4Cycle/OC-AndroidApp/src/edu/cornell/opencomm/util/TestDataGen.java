package edu.cornell.opencomm.util;

import java.util.ArrayList;

import edu.cornell.opencomm.R;
import edu.cornell.opencomm.manager.UserManager;
import edu.cornell.opencomm.model.User;

/**This class can be used as test data generator for front end development
 * 
 * @author Ankit Singh:as2536
 *
 */
public class TestDataGen {

	public static ArrayList<User> createExampleUsers(){
		ArrayList<User> users = new ArrayList<User>();
		users.add(UserManager.PRIMARY_USER);
		users.add(new User("noratheexplora", "Nora Ng-Quinn", R.drawable.example_picture_2));
		users.add(new User("batman", "Bruce Wayne", R.drawable.example_picture_3));
		users.add(new User("batman", "Bruce Wayne", R.drawable.example_picture_3));
		users.add(new User("batman", "Bruce Wayne", R.drawable.example_picture_3));
		return users;
	}
}
