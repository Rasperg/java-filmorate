package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserStorage {
    Collection<User> getAllUsers();

    User createUser(User user);

    User updateUser(User user);

    Optional<User> getUserById(int id);

    Optional<User> deleteUserById(int id);

    List<Integer> followUser(int followingId, int followerId);

    List<Integer> unfollowUser(int followingId, int followerId);

    List<User> getFriendsListById(int id);

    List<User> getCommonFriendsList(int firstId, int secondId);
}
