package wander.wise.application.repository.user.deleted;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import wander.wise.application.model.User;

@Repository
@RequiredArgsConstructor
public class DeletedRepositoryImpl implements DeletedRepository {
    public static final String IS_DELETED_QUERY
            = "SELECT deleted FROM users WHERE email = :email";
    public static final String RESTORE_ACCOUNT_QUERY
            = "UPDATE users SET deleted = false WHERE email = :email";
    public static final String FIND_USER_BY_EMAIL_QUERY
            = "SELECT * FROM users WHERE email = :email";
    public static final String IS_EXISTS_QUERY
            = "SELECT EXISTS(SELECT * FROM users WHERE email = :email)";
    public static final String SET_USER_ROLES_QUERY
            = "INSERT INTO user_role (user_id, role_id) VALUES (:user_id, :role_id)";
    public static final int USER_ROLE_ID = 3;
    private final EntityManager entityManager;

    @Override
    public boolean isExistsAndDeleted(String email) {
        Long isExists = (Long) entityManager
                .createNativeQuery(IS_EXISTS_QUERY)
                .setParameter("email", email)
                .getSingleResult();
        if (isExists != null && isExists == 1) {
            return (boolean) entityManager
                    .createNativeQuery(IS_DELETED_QUERY)
                    .setParameter("email", email)
                    .getSingleResult();
        } else {
            return false;
        }
    }

    @Override
    @Transactional
    public User restoreAccount(String email) {
        entityManager.createNativeQuery(RESTORE_ACCOUNT_QUERY)
                .setParameter("email", email)
                .executeUpdate();
        User restoredUser = (User) entityManager
                .createNativeQuery(FIND_USER_BY_EMAIL_QUERY, User.class)
                .setParameter("email", email)
                .getSingleResult();
        entityManager.createNativeQuery(SET_USER_ROLES_QUERY)
                .setParameter("user_id", restoredUser.getId())
                .setParameter("role_id", USER_ROLE_ID)
                .executeUpdate();
        return restoredUser;
    }
}
