package org.example.UserState;

import org.example.JsonPars.JsonProducts.Item;

import java.util.List;
public class UserState {
    private List<List<Item>> userData;
    private int userCurrentPage;
    private int userCurrentStore;

    public UserState(List<List<Item>> userData, int userCurrentPage, int userCurrentStore) {
        this.userData = userData;
        this.userCurrentPage = userCurrentPage;
        this.userCurrentStore = userCurrentStore;
    }

    public List<List<Item>> getUserData() {
        return userData;
    }



    public void setUserData(List<List<Item>> userData) {
        this.userData = userData;
    }

    public int getUserCurrentPage() {
        return userCurrentPage;
    }

    public void setUserCurrentPage(int userCurrentPage) {
        this.userCurrentPage = userCurrentPage;
    }

    public int getUserCurrentStore() {
        return userCurrentStore;
    }

    public void setUserCurrentStore(int userCurrentStore) {
        this.userCurrentStore = userCurrentStore;
    }
}
