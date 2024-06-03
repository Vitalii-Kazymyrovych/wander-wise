package wander.wise.application.repository.user.deleted;

import wander.wise.application.model.User;

public interface DeletedRepository {
    boolean isExistsAndDeleted(String email);

    User restoreAccount(String email);
}
