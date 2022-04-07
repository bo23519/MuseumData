import java.sql.*;
import java.util.Scanner;

public class p3 {
    private static String USERID = null;
    private static String PASSWORD = null;
    private static String OPTION = null;
    private static Connection connection = null;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("You need to include your UserID and Password parameters on the command line");
            return;
        } else if (args.length < 3) {
            System.out.println("Include the number of the following menu item as the third parameter on the command line.\n");
            System.out.println("1 – Report Participant Information");
            System.out.println("2 – Report Pottery Information");
            System.out.println("3 – Report Building Galleries Information");
            System.out.println("4 – Update Member ID");
            return;
        } else if (args.length == 3) {
            USERID = args[0];
            PASSWORD = args[1];
            OPTION = args[2];
        }

        DBConnect db = new DBConnect();
        connection = db.connect(USERID, PASSWORD);
        if (connection == null) {
            System.out.println("connection fail");
            return;
        }

        switch (OPTION) {
            case "1":
                ReportParticipantInformation();
                break;
            case "2":
                ReportPotteryInformation();
                break;
            case "3":
                ReportBuildingGalleriesInformation();
                break;
            case "4":
                UpdateMemberID();
                break;
            default:
                System.out.println("Invalid option number entered in the argument");
                break;
        }

        scanner.close();
        return;
    }

    private static void ReportParticipantInformation() {
        System.out.println("Enter Participant’s email address:");
        String email = scanner.next();
        try {
            Statement stmt = connection.createStatement();
            String str = "SELECT * FROM Participant WHERE email = '" + email + "'";
            ResultSet rset = stmt.executeQuery(str);

            if (rset.next()) {
                System.out.println("Participant Information");
                System.out.println("Email: " + rset.getString("email"));
                System.out.println("Name: " + rset.getString("firstName") + " " + rset.getString("lastName"));
                System.out.println("Phone: " + rset.getString("phone"));
                System.out.println("City/State: " + rset.getString("city") + ", " + rset.getString("state"));
                int memberID = rset.getInt("memberID");
                if (memberID != 0){
                    System.out.println("MemberID: " + memberID);
                }
            }
            else {
                System.out.println("no such record has been found");
            }

            rset.close();
            stmt.close();
            connection.close();
        } catch (
                SQLException e) {
            System.out.println("Get Data Failed! Check output console");
            e.printStackTrace();
            return;
        }
    }

    private static void ReportPotteryInformation() {
        System.out.println("Enter Pottery ID:");
        String potteryID = scanner.next();
        try {
            Statement stmt = connection.createStatement();
            String str =
                    "SELECT p.artworkID, pa.firstName, pa.lastName, a.title, p.clayBody, a.price " +
                    "FROM Pottery p JOIN Artwork a ON p.artworkID = a.artworkID " +
                            "JOIN Participant pa ON a.creatorEmail = pa.email " +
                    "WHERE p.artworkID = " + potteryID;
            ResultSet rset = stmt.executeQuery(str);

            if (rset.next()) {
                System.out.println("Pottery Information");
                System.out.println("Artwork ID: " + rset.getInt("artworkID"));
                System.out.println("Artist Name: " + rset.getString("firstName") + " " + rset.getString("lastName"));
                System.out.println("Title: " + rset.getString("title"));
                System.out.println("Clay Body: " + rset.getString("clayBody"));
                System.out.println("Price: " + rset.getInt("price"));
            }
            else {
                System.out.println("no such record has been found");
            }

            rset.close();
            stmt.close();
            connection.close();
        } catch (
                SQLException e) {
            System.out.println("Get Data Failed! Check output console");
            e.printStackTrace();
            return;
        }
    }

    private static void ReportBuildingGalleriesInformation() {
        System.out.println("Enter Building Name:");
        String buildingName = scanner.next();
        System.out.println(buildingName);
        try {
            Statement stmt = connection.createStatement();
            String str1 =
                    "SELECT * FROM Building WHERE buildingName = '" + buildingName + "'";
            ResultSet rset1 = stmt.executeQuery(str1);

            if (rset1.next()) {
                System.out.println("Building Gallery Information");
                System.out.println("Building Name: " + rset1.getString("buildingName"));
                System.out.println("Building Address: " + rset1.getString("street"));
                System.out.println("\t\t\t\t " +  rset1.getString("city") + ", " + rset1.getString("state")
                    + " " + rset1.getString("zipcode"));
                rset1.close();

                String str2 =
                        "SELECT * FROM Gallery WHERE buildingName = '" + buildingName + "' "
                        + "ORDER BY galleryName";
                ResultSet rset2 = stmt.executeQuery(str2);
                int count = 1;
                while (rset2.next()){
                    System.out.println("Gallery " + count + ": " + rset2.getString("galleryName"));
                    count++;
                }

                rset2.close();
            }
            else {
                System.out.println("no such record has been found");
            }

            stmt.close();
            connection.close();
        } catch (
                SQLException e) {
            System.out.println("Get Data Failed! Check output console");
            e.printStackTrace();
            return;
        }
    }

    private static void UpdateMemberID() {
        System.out.println("Enter the Participant’s Email Address:");
        String email = scanner.next();
        System.out.println("Enter the updated Member ID:");
        String updatedID = scanner.next();
        try {
            Statement stmt = connection.createStatement();
            String str =
                    "UPDATE Participant SET memberID = " + updatedID +
                            " WHERE email = '" + email + "'";
            stmt.executeQuery(str);
        } catch (
                SQLException e) {
            System.out.println("Get Data Failed! Check output console");
            e.printStackTrace();
            return;
        }
    }
}
