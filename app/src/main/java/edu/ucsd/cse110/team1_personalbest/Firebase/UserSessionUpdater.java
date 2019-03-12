package edu.ucsd.cse110.team1_personalbest.Firebase;

import java.util.Map;

public class UserSessionUpdater implements IDatabaseObserver {
    @Override
    public void update(User user) {
        UserSession.setCurrentUser(user);
    }

    @Override
    public void update(Map<String, User> allUsers) {
        UserSession.setUsers(allUsers);
    }
}