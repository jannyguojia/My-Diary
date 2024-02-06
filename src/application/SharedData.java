package application;

public class SharedData {
	// 静态变量用于保存数据
    private static String userEmail;

    public static String getuserEmail() {
        return userEmail;
    }

    public static void setuserEmail(String userEmail) {
        SharedData.userEmail = userEmail;
    }
}
