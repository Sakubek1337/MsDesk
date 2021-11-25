package sample;

import java.util.ArrayList;

public class Admin extends Extra{

    ArrayList<String> admins = new ArrayList<>();

    public void addAdmin(String adminName){
        admins.add(adminName);
    }

    public Boolean adminCheck(String name){
        return admins.contains(name);
    }


    void adminAddPointsC(String name, int n) {
        sql("Update", name, n);
        System.out.println("Points added");
    }

    void adminAddUserC(String name) {

    }

    void adminDeleteUserC(String name) {

    }

    void adminGiftC(String name, int n) {
        sql("Update", name, n);
    }

    void adminGiveAdminC(String name) {

    }

    void adminTakeAdminC(String name) {

    }

}
