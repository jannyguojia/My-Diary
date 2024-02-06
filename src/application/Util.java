package application;

public class Util {

	// Use regular expressions to verify Email format on create account page
	public static boolean isValidEmailFormat(String email) {
		if (email == null) {
			return false;
		}
		String emailPattern = "^[A-Za-z0-9+_.-]+@(.+)$";
		return email.matches(emailPattern);
	}

	// check if the input information correct on create account page
	public static String createAccountValidateData(String householdname, String name, String email, String password,
			String confirmPassword) {
		if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()
				|| householdname.isEmpty()) {
			return "Please fill in all the fields!";
		} else if (householdname.contains(" ") || name.contains(" ") || email.contains(" ") || password.contains(" ")
				|| confirmPassword.contains(" ")) {
			return "No spaces allowed!";
		} else if (!password.equals(confirmPassword)) {
			return "Password does not match! ";

		} else if (!isValidEmailFormat(email)) {
			return "Email format is invalid!";
		}
		return "Create successful!";
	}

	// check if the input information correct on login page
	public static String loginValidateData(String householdname, String email, String password) {
		if (email.isEmpty() || password.isEmpty() || householdname.isEmpty()) {
			return "Login failed! Please check the household name, your email and password!";
		}
		return "";
	}

	// check if all the items are selected on add a chore page
	public static String addAChoreValidateData(String choreName, String frequency, String startTime, boolean isTeamTask,
			boolean notTeamTask, String teamMember) {
		if (choreName == null || frequency == null || startTime == null || (isTeamTask && teamMember == null)
				|| (!isTeamTask && !notTeamTask)) {
			return "Please select all items!";
		}
		return "";
	}

}
