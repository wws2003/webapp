/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vienna.mendel.hpg.biz.service.auth.abstr;

import java.util.List;
import java.util.Optional;
import vienna.mendel.hpg.model.constants.MendelPrivilege;
import vienna.mendel.hpg.model.constants.MendelRole;
import vienna.mendel.hpg.model.dto.user.MendelUser;
import vienna.mendel.hpg.model.exception.MendelRuntimeException;

/**
 * Interface for user service
 *
 * @author wws2003
 */
public interface IUserService {

    /**
     * Find user by user name and role
     *
     * @param userName
     * @param role
     * @return
     * @throws MendelRuntimeException When find operation failed
     */
    Optional<MendelUser> findUserByName(String userName, MendelRole role) throws MendelRuntimeException;

    /**
     * Create new user
     *
     * @param user
     * @return Number of created user
     * @throws MendelRuntimeException When create operation failed
     */
    int createUser(MendelUser user) throws MendelRuntimeException;

    /**
     * Delete list of users by id
     *
     * @param userIds
     * @return Number of deleted users
     * @throws MendelRuntimeException When delete operation failed
     */
    int deleteUsers(List<Long> userIds) throws MendelRuntimeException;

    /**
     * Find privileges assigned to the given role
     *
     * @param role
     * @return List of privileges assigned to the given role
     * @throws MendelRuntimeException When find operation failed
     */
    List<MendelPrivilege> findPrivilegesForRole(MendelRole role) throws MendelRuntimeException;
}
