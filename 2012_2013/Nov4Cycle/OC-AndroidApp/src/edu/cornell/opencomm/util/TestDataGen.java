package edu.cornell.opencomm.util;

import java.util.ArrayList;

import edu.cornell.opencomm.R;
import edu.cornell.opencomm.model.User;

/**This class can be used as test data generator for front end development
 * 
 * @author Ankit Singh:as2536
 *
 */
public class TestDataGen {

	public static ArrayList<User> createExampleUsers(){
		ArrayList<User> users = new ArrayList<User>();
		users.add(new User("naka_shaka_laka", "Risa Naka", R.drawable.example_picture_1));
		users.add(new User("noratheexplora", "Nora Ng-Quinn", R.drawable.example_picture_2));
		users.add(new User("makomania", "Makoto Bentz", R.drawable.example_picture_3));
		users.add(new User("graeme_craka", "Graeme Bailey", R.drawable.example_picture_1));
		users.add(new User("naj_hodge", "Najla Elmachtoub", R.drawable.example_picture_2));
		users.add(new User("xu_mu_moo", "Jason Xu", R.drawable.example_picture_3));
		users.add(new User("naj_hodge", "Najla Elmachtoub", R.drawable.example_picture_2));
		users.add(new User("xu_mu_moo", "Jason Xu", R.drawable.example_picture_3));
		users.add(new User("naj_hodge", "Najla Elmachtoub", R.drawable.example_picture_2));
		users.add(new User("xu_mu_moo", "Jason Xu", R.drawable.example_picture_3));
		return users;
	}
}
