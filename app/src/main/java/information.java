public class information {
    String location;
    String description;
    String fName;
    String lName;
    String phone;

    public information(String location, String description, String fName, String lName, String phone) {
        this.location = location;
        this.description = description;
        this.fName = fName;
        this.lName = lName;
        this.phone = phone;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public String getfName() {
        return fName;
    }

    public String getlName() {
        return lName;
    }

    public String getPhone() {
        return phone;
    }
}
